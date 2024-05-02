package com.backend.Wasteless.model;

public class UserFactory {
    public User createUser(String userType, String username, String name, String email, String password) {
        if (userType == null) {
            return new User(username, name, email, password);
        }
        //if (userType.equalsIgnoreCase("REGULAR")) {
       //     return new RegularUser();
        //} else if (userType.equalsIgnoreCase("PREMIUM")) {
        //    return new PremiumUser();
        //}
        return null;
    }
}