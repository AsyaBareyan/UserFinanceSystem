package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.PhoneMapper;
import org.example.model.entity.PhoneData;
import org.example.model.entity.User;
import org.example.repository.PhoneDataRepository;
import org.example.repository.UserRepository;
import org.example.service.PhoneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneDataRepository phoneRepository;
    private final UserRepository userRepository;
    private final PhoneMapper phoneMapper;

    @Override
    public void addPhone(Long userId, String phone) {
        if (phoneRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("phone is already in use");
        }
        var user = userRepository.findById(userId).orElseThrow();
        var phoneData = phoneMapper.toEntity(phone, user);
        phoneRepository.save(phoneData);
    }

    @Override
    public void removePhone(Long userId, String phone) {
        var user = userRepository.findById(userId).orElseThrow();

        var userPhones = user.getPhones();
        if (userPhones.size() <= 1) {
            throw new IllegalStateException("Cannot delete the last remaining phone");
        }

        var phoneData = phoneRepository.findByPhone(phone)
                .filter(e -> e.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("phone not found or not owned by user"));

        phoneRepository.delete(phoneData);
    }

    @Override
    public PhoneData replacePhone(Long userId, String oldPhone, String newPhone) {
        if (phoneRepository.existsByPhone(newPhone)) {
            throw new IllegalArgumentException("New phone is already in use");
        }
        var phoneData = phoneRepository.findByPhone(oldPhone)
                .filter(e -> e.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Old phone not found or not owned by user"));
        phoneData.setPhone(newPhone);
        return phoneRepository.save(phoneData);
    }
}
