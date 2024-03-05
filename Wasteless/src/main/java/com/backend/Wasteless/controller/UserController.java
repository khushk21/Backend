package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;


@RestController
@CrossOrigin(origins="*")
public class UserController {

    @Autowired
    private UserRepository userRepo;

//    @RequestMapping(value="/")
//    public void redirect(HttpServletResponse response) throws IOException {
//        response.sendRedirect("/swagger-ui.html");
//    }

    // Save method is predefine method in Mongo Repository
    // with this method we will save user in our database

    // Method to generate a random salt
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // Method to hash a password using SHA-512 with salt
    public static String hashPassword(String password) {
        System.out.println("REAL user input : " + password);
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
            // Convert salt bytes to hexadecimal representation
            StringBuilder saltString = new StringBuilder();
            for (byte b : salt) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) saltString.append('0');
                saltString.append(hex);
            }
            // Concatenate hashed password with salt using a delimiter
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
    // Method to verify a password
    public static boolean verifyPassword(String inputPassword, String storedHashedPassword) {
         System.out.println("Input password: " + inputPassword);
        String[] parts = storedHashedPassword.split(":");
//        System.out.println("String");
//        System.out.println(Arrays.toString(parts));
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid storedHashedPassword format");
        }
        String storedHash = parts[0];
        // Convert the salt string to a byte array
        byte[] salt = hexStringToByteArray(parts[1]);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // Update the message digest with the salt
            md.update(salt);
            // Compute the hash of the input password with the salt
            byte[] hashedInputPassword = md.digest(inputPassword.getBytes(StandardCharsets.UTF_8));
            // Convert the hashed input password bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedInputPassword) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            // Compare the hashed input password with the stored hashed password
            String hashedInputPasswordString = hexString.toString();
//            System.out.println("Below the same? ");
//            System.out.println(hashedInputPasswordString);
//            System.out.println(storedHash);
            return storedHash.equals(hashedInputPasswordString);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/registerUser")
    public String createUser(@RequestBody User user) {
        //TODO: Add hashing password logic
//        user.setPassword(user.getPassword()+"1");
        String hashedPassword = hashPassword(user.getPassword());
        System.out.println("hashedPassword inside createUser method " + hashedPassword);
        Optional<User> validateUser = userRepo.findById(user.getUserName());
        if(validateUser.isEmpty()){
            user.setPoints(0);
            user.setPassword(hashedPassword);
            User savedUser = userRepo.save(user);
            return "Successfully saved " + savedUser.getUserName();
        }
        else{
            return "Username already ex5678  ists. Please try again.";
        }

    }

    @PostMapping("/userLogin")
    public HashMap<String, Object> verifyCredentials(@RequestBody User credentials){
        Optional<User> user = userRepo.findById(credentials.getUserName());
        System.out.println("1 credentials.getUsername " + credentials.getUserName());
        System.out.println("2 credentials.getPassword()" + credentials.getPassword());
        HashMap<String, Object> result = new HashMap<String, Object>();
        if(user.isPresent()) {
            User existingUser = user.get();
            //TODO: Add hashing for the password before checking
            System.out.println("User input password: "  + credentials.getPassword());
            boolean passwordMatch = verifyPassword(credentials.getPassword(),existingUser.getPassword());
            System.out.println("Printing credentials.getPassword (user's input): " + credentials.getPassword());
            System.out.println("Printing of existing user password (from db): " + existingUser.getPassword());
//            System.out.println(hashPassword(credentials.getPassword()));
//            System.out.println("Above is user input password");
//            System.out.println(existingUser.getPassword());
//            System.out.println("Above is existing user password");
            if(passwordMatch){
                result.put("user",existingUser);
                result.put("error", null);
            }
//            if(credentials.getPassword().equals(existingUser.getPassword())){
//                result.put("user",existingUser);
//                result.put("error", null);
//            }
            else{
                result.put("user", null);
                Error errorMsg = new Error("Incorrect Password.");
                result.put("error", errorMsg);
            }
        }
        else{
            result.put("user", null);
            Error errorMsg = new Error("User account does not exist.");
            result.put("error", errorMsg);
        }
        return result;
    }

    // findAll method is predefine method in Mongo Repository
    // with this method we will all user that is save in our database
    @GetMapping("/getAllUser")
    public List<User> getAllUser() {
        return userRepo.findAll();
    }
}