package com.example.tssruntime.service;

import com.example.tssruntime.model.User;
import com.example.tssruntime.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void testCreateUser() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890");

        // When
        User savedUser = userService.createUser(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("1234567890", savedUser.getPhone());

        // Verify it's actually saved in the database
        User foundUser = entityManager.find(User.class, savedUser.getId());
        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getName());
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        // Given
        User user1 = new User("John Doe", "john@example.com", "1234567890");
        userService.createUser(user1);

        User user2 = new User("Jane Smith", "john@example.com", "0987654321");

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.createUser(user2));
    }

    @Test
    void testGetAllUsers() {
        // Given
        User user1 = new User("John Doe", "john@example.com", "1234567890");
        User user2 = new User("Jane Smith", "jane@example.com", "0987654321");
        
        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("John Doe")));
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Jane Smith")));
    }

    @Test
    void testGetUserById() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890");
        User savedUser = entityManager.persistAndFlush(user);

        // When
        Optional<User> foundUser = userService.getUserById(savedUser.getId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    void testGetUserByIdNotFound() {
        // When
        Optional<User> foundUser = userService.getUserById(999L);

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testGetUserByEmail() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890");
        entityManager.persistAndFlush(user);

        // When
        Optional<User> foundUser = userService.getUserByEmail("john@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    void testUpdateUser() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890");
        User savedUser = entityManager.persistAndFlush(user);

        User updateData = new User("John Updated", "john.updated@example.com", "1234567890");

        // When
        User updatedUser = userService.updateUser(savedUser.getId(), updateData);

        // Then
        assertEquals("John Updated", updatedUser.getName());
        assertEquals("john.updated@example.com", updatedUser.getEmail());

        // Verify it's actually updated in the database
        User foundUser = entityManager.find(User.class, savedUser.getId());
        assertEquals("John Updated", foundUser.getName());
        assertEquals("john.updated@example.com", foundUser.getEmail());
    }

    @Test
    void testUpdateUserNotFound() {
        // Given
        User updateData = new User("John Updated", "john.updated@example.com", "1234567890");

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.updateUser(999L, updateData));
    }

    @Test
    void testDeleteUser() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890");
        User savedUser = entityManager.persistAndFlush(user);

        // When
        userService.deleteUser(savedUser.getId());

        // Then
        User foundUser = entityManager.find(User.class, savedUser.getId());
        assertNull(foundUser);
    }

    @Test
    void testDeleteUserNotFound() {
        // When & Then
        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void testUserExists() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890");
        User savedUser = entityManager.persistAndFlush(user);

        // When & Then
        assertTrue(userService.userExists(savedUser.getId()));
        assertFalse(userService.userExists(999L));
    }
} 