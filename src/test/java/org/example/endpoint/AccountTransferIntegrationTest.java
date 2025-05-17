package org.example.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.request.TransferRequest;
import org.example.model.entity.Account;
import org.example.model.entity.User;
import org.example.repository.AccountRepository;
import org.example.repository.UserRepository;
import org.example.util.JwtTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AccountTransferIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private String jwtToken;
    private User sender;
    private User recipient;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();

        sender = User.builder()
                .name("Sender")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .password("$2a$10$Li2wJU8k6NIanBCk6Fw6yOUYT6LsZ8Hysi1uaBqOQ86ZVqzH3mVMi")
                .build();
        sender = userRepository.save(sender);

        recipient = User.builder()
                .name("Recipient")
                .dateOfBirth(LocalDate.of(1991, 1, 1))
                .password("$2a$10$Li2wJU8k6NIanBCk6Fw6yOUYT6LsZ8Hysi1uaBqOQ86ZVqzH3mVMi")
                .build();
        recipient = userRepository.save(recipient);

        accountRepository.save(Account.builder()
                .user(sender)
                .balance(new BigDecimal("100.00"))
                .initialBalance(new BigDecimal("100.00"))
                .build());

        accountRepository.save(Account.builder()
                .user(recipient)
                .balance(new BigDecimal("50.00"))
                .initialBalance(new BigDecimal("50.00"))
                .build());

        jwtToken = JwtTestUtil.generateToken(sender.getId());
    }

    @Test
    void testTransferMoneySuccess() throws Exception {
        TransferRequest request = new TransferRequest(recipient.getId(), new BigDecimal("30.00"));

        mockMvc.perform(post("/account/transfer")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Account updatedSender = accountRepository.findByUserId(sender.getId()).orElseThrow();
        Account updatedRecipient = accountRepository.findByUserId(recipient.getId()).orElseThrow();

        assertEquals(new BigDecimal("70.00"), updatedSender.getBalance());
        assertEquals(new BigDecimal("80.00"), updatedRecipient.getBalance());
    }

    @Test
    void testTransferFailsWhenInsufficientBalance() throws Exception {
        TransferRequest request = new TransferRequest(recipient.getId(), new BigDecimal("1000.00"));

        mockMvc.perform(post("/account/transfer")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void testTransferFailsWhenSelfTransfer() throws Exception {
        TransferRequest request = new TransferRequest(sender.getId(), new BigDecimal("10.00"));

        mockMvc.perform(post("/account/transfer")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testTransferFailsWhenRecipientNotFound() throws Exception {
        TransferRequest request = new TransferRequest(99999L, new BigDecimal("10.00"));

        mockMvc.perform(post("/account/transfer")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
