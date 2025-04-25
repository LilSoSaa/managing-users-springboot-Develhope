package com.example.managing_users_Develhope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. CREATE a new User
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // 2. GET all Users
    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 3. GET a User by ID
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    // 4. UPDATE User (data, not primary key)
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> user = userService.updateUser(id, updatedUser);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.notFound().build();
    }

    // 5. UPDATE Email only
    @PatchMapping("/update-email/{id}")
    public ResponseEntity<User> updateEmail(@PathVariable Long id, @RequestParam String email) {
        Optional<User> updatedUser = userService.updateEmail(id, email);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    // 6. DELETE a User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
