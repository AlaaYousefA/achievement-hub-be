package com.achivhub.backend.domain.services.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    public String generateOtp() {
        Random random = new SecureRandom();
        int realPart = random.nextInt(9000) + 1000;
        int imaginaryPart = random.nextInt(9000) + 1000;

        char randomChar;
        int charType = random.nextInt(3);

        if (charType == 0) {
            randomChar = (char) (random.nextInt(26) + 'a');
        } else if (charType == 1) {
            randomChar = (char) (random.nextInt(26) + 'A');
        } else {
            char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+'};
            randomChar = symbols[random.nextInt(symbols.length)];
        }

        return Integer.toString(realPart) + randomChar + imaginaryPart;
    }

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hashOtp(String otp, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedOtp = otp + salt;
            byte[] hashedBytes = digest.digest(saltedOtp.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing OTP", e);
        }
    }
}
