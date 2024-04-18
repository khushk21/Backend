package com.backend.Wasteless.service;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public String hashPassword(String password) {
        byte[] salt = generateSalt();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedPassword) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            StringBuilder saltString = new StringBuilder();
            for (byte b : salt) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) saltString.append('0');
                saltString.append(hex);
            }
            return hexString.toString() + ":" + saltString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public boolean verifyPassword(String inputPassword, String storedHashedPassword) {
        String[] parts = storedHashedPassword.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid storedHashedPassword format");
        }
        String storedHash = parts[0];
        byte[] salt = hexStringToByteArray(parts[1]);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedInputPassword = md.digest(inputPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedInputPassword) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String hashedInputPasswordString = hexString.toString();

            return storedHash.equals(hashedInputPasswordString);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public User createUser(User user) {
        String hashedPassword = hashPassword(user.getPassword());
        Optional<User> validateUser = userRepo.findById(user.getUserName());
        if (validateUser.isEmpty()) {
            user.setPoints(0);
            user.setPassword(hashedPassword);
            return userRepo.save(user);
        } else {
            throw new RuntimeException("Username already exists. Please try again.");
        }
    }

    public Optional<User> verifyCredentials(User credentials) {
        Optional<User> user = userRepo.findById(credentials.getUserName());
        if (user.isPresent()) {
            User existingUser = user.get();
            boolean passwordMatch = verifyPassword(credentials.getPassword(), existingUser.getPassword());
            if (passwordMatch) {
                return Optional.of(existingUser);
            } else {
                throw new RuntimeException("Incorrect Password.");
            }
        } else {
            throw new RuntimeException("User account does not exist.");
        }
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserByUsername(String userName) {
        return userRepo.findById(userName);
    }
}
