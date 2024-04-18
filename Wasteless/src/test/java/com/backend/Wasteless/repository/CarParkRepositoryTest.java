package com.backend.Wasteless.repository;

import com.backend.Wasteless.model.CarPark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarParkRepositoryTest {

    @Mock
    private CarParkRepository carParkRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        // Arrange
        String carParkId = "ACB";
        CarPark expectedCarPark = new CarPark(carParkId, "Test Address", 1.0, 1.0, "Test Type", "Test Parking", "Free Parking");

        when(mongoTemplate.findById(carParkId, CarPark.class)).thenReturn(expectedCarPark);

        // Act
        Optional<CarPark> result = carParkRepository.findById(carParkId);

        // Assert
        assertEquals(expectedCarPark, result.orElse(null));
    }

    @Test
    void testSave() {
        // Arrange
        CarPark carPark = new CarPark("1", "Test Address", 1.0, 1.0, "Test Type", "Test Parking", "Free Parking");

        when(mongoTemplate.save(carPark)).thenReturn(carPark);

        // Act
        CarPark result = carParkRepository.save(carPark);

        // Assert
        assertEquals(carPark, result);
    }

    @Test
    void testDeleteById() {
        // Arrange
        String carParkId = "1";

        // Act
        carParkRepository.deleteById(carParkId);

        // Assert
        verify(mongoTemplate, times(1)).remove(any(Query.class), eq(CarPark.class));
    }
}
