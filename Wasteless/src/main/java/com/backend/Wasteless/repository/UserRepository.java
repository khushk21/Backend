package com.backend.Wasteless.repository;



import com.backend.Wasteless.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String>{

}
