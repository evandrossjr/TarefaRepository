package com.evtechsolution.gerenciador_tarefas.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testBuscarUsuarioPorEmail() {
        User user = new User(1L, "João", "joao@email.com", "senha123",userRepository "ADMIN");
        Mockito.when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));

        User resultado = UserService.findByEmail("joao@email.com");

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("João", resultado.getName());
    }
}

