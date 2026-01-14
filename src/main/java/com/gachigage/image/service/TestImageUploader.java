package com.gachigage.image.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("test")
public class TestImageUploader implements ImageUploader{
    @Override
    public String upload(MultipartFile file) {
        return "/img/test-fridge.jpg";
    }
}
