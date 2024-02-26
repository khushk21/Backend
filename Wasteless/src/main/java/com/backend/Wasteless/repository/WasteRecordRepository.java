package com.backend.Wasteless.repository;

import com.backend.Wasteless.model.WasteRecord;
import com.backend.Wasteless.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WasteRecordRepository extends  MongoRepository<WasteRecord, String>{
}
