package com.example.managing_users_Develhope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 1. CREATE a new User
    public User createUser(User user) {
        User userSaved = userRepository.save(user);
        return userSaved;
    }

    // 2. GET all Users
    public List<User> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    // 3. GET a User by ID
    public Optional<User> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser;
    }

    // 4. UPDATE User (data, not primary key)
    public Optional<User> updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            user.setName(updatedUser.getName());
            user.setSurname(updatedUser.getSurname());
            user.setEmail(updatedUser.getEmail());

            User savedUser = userRepository.save(user);
            return Optional.of(savedUser);
        }

        return Optional.empty();
    }

    // 5. UPDATE Email only
    public Optional<User> updateEmail(Long id, String email) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(email);  // aggiorniamo solo l'email
            User updatedUser = userRepository.save(user);
            return Optional.of(updatedUser);
        }

        return Optional.empty();
    }

    // 6. DELETE a User
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
