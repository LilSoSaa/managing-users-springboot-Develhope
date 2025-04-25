package com.example.managing_users_Develhope;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService; // Mocka UserService

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Mario");
        testUser.setSurname("Rossi");
        testUser.setEmail("mario.rossi@example.com");
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mario"))
                .andExpect(jsonPath("$.surname").value("Rossi"))
                .andExpect(jsonPath("$.email").value("mario.rossi@example.com"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(testUser));

        mockMvc.perform(get("/users/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mario"))
                .andExpect(jsonPath("$[0].surname").value("Rossi"));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/users/find-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mario"))
                .andExpect(jsonPath("$.surname").value("Rossi"));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(Optional.of(testUser));

        testUser.setName("Luigi");
        testUser.setSurname("Verdi");

        mockMvc.perform(put("/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luigi"))
                .andExpect(jsonPath("$.surname").value("Verdi"));
    }

    @Test
    void testUpdateEmail() throws Exception {
        // Prepara l'utente con la nuova email
        User updatedUser = new User(1L, "Mario", "Rossi", "luigi.verdi@example.com");

        // Mocka la risposta del servizio
        when(userService.updateEmail(anyLong(), any(String.class))).thenReturn(Optional.of(updatedUser));

        // Esegui il test della PATCH request con il parametro email
        mockMvc.perform(patch("/users/update-email/1")
                        .param("email", "luigi.verdi@example.com"))  // parametro email come query parameter
                .andExpect(status().isOk())  // Status 200 OK
                .andExpect(jsonPath("$.email").value("luigi.verdi@example.com"));  // Verifica che l'email sia stata aggiornata
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}