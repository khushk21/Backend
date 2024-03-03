package com.backend.Wasteless.repository;

import com.backend.Wasteless.constants.WasteCategory;
import com.backend.Wasteless.model.WastePOI;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WastePOIRepository extends  MongoRepository<WastePOI, String>{
    public List<WastePOI> findAllByWasteCategory(WasteCategory wasteCategory);
}
