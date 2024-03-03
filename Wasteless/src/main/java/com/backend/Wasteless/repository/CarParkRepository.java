package com.backend.Wasteless.repository;

import com.backend.Wasteless.model.CarPark;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarParkRepository extends MongoRepository<CarPark, String> {
}
