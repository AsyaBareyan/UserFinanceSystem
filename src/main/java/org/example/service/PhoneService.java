package org.example.service;

import org.example.model.entity.PhoneData;

public interface PhoneService {
    void addPhone(Long userId, String phone);

    void removePhone(Long userId, String phone);

    PhoneData replacePhone(Long userId, String oldPhone, String newPhone);
}
