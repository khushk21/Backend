//package com.backend.Wasteless.repository;
//
//import com.backend.Wasteless.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class UserRepositoryTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private MongoTemplate mongoTemplate;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testFindById() {
//        // Arrange
//        User user = new User("test_pass", "Success", "test@gmail.com", "password");
//        user.setPoints(0);
//        when(userRepository.findById(user.getUserName())).thenReturn(Optional.empty());
//
//        when(mongoTemplate.findById(userId, User.class)).thenReturn(expectedUser);
//
//        // Act
//        Optional<User> result = userRepository.findById(userId);
//
//        // Assert
//        assertEquals(expectedUser, result.orElse(null));
//    }
//
//    @Test
//    void testSave() {
//        // Arrange
//        User user = new User("1", "John", "Doe", "john.doe@example.com");
//
//        when(mongoTemplate.save(user)).thenReturn(user);
//
//        // Act
//        User result = userRepository.save(user);
//
//        // Assert
//        assertEquals(user, result);
//    }
//
//    @Test
//    void testDeleteById() {
//        // Arrange
//        String userId = "1";
//
//        // Act
//        userRepository.deleteById(userId);
//
//        // Assert
//        verify(mongoTemplate, times(1)).remove(any(Query.class), eq(User.class));
//    }
//}
