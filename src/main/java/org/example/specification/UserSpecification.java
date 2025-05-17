package org.example.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.example.model.entity.EmailData;
import org.example.model.entity.PhoneData;
import org.example.model.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {

    public static Specification<User> nameLike(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(root.get("name"), name + "%");
    }

    public static Specification<User> emailEquals(String email) {
        return (root, query, cb) -> {
            if (email == null) return null;
            Join<User, EmailData> emailJoin = root.join("emails", JoinType.LEFT);
            return cb.equal(emailJoin.get("email"), email);
        };
    }

    public static Specification<User> phoneEquals(String phone) {
        return (root, query, cb) -> {
            if (phone == null) return null;
            Join<User, PhoneData> phoneJoin = root.join("phones", JoinType.LEFT);
            return cb.equal(phoneJoin.get("phone"), phone);
        };
    }

    public static Specification<User> dateOfBirthAfter(LocalDate date) {
        return (root, query, cb) -> date == null ? null : cb.greaterThan(root.get("dateOfBirth"), date);
    }
}
