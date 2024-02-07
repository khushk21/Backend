package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @PostMapping("/registerUser")
    public String createUser(@RequestBody User user) {
        //TODO: Add hashing password logic
//        user.setPassword(user.getPassword()+"1");
        Optional<User> validateUser = userRepo.findById(user.getUserName());
        if(validateUser.isEmpty()){
            user.setPoints(0);
            User savedUser = userRepo.save(user);
            return "Successfully saved " + savedUser.getUserName();
        }
        else{
            return "Username already exists. Please try again.";
        }

    }

    @PostMapping("/userLogin")
    public HashMap<String, Object> verifyCredentials(@RequestBody User credentials){
        Optional<User> user = userRepo.findById(credentials.getUserName());
        HashMap<String, Object> result = new HashMap<String, Object>();
        if(user.isPresent()) {
            User existingUser = user.get();
            //TODO: Add hashing for the password before checking
            if(credentials.getPassword().equals(existingUser.getPassword())){
                result.put("user",existingUser);
                result.put("error", null);
            }
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