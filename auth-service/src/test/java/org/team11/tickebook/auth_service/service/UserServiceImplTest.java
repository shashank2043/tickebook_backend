package org.team11.tickebook.auth_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.auth_service.client.EmailClient;
import org.team11.tickebook.auth_service.client.Mail;
import org.team11.tickebook.auth_service.exception.UserNotFoundException;
import org.team11.tickebook.auth_service.kafka.MailProducer;
import org.team11.tickebook.auth_service.model.entity.Otp;
import org.team11.tickebook.auth_service.model.entity.User;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.repository.OtpRepository;
import org.team11.tickebook.auth_service.repository.UserRepository;
import org.team11.tickebook.auth_service.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailClient emailClient;

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private MailProducer mailProducer;

    @InjectMocks
    private UserServiceImpl service;

    private UUID userId;
    private User user;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setEmail("test@mail.com");
        user.setPassword("password");
        user.setEmailVerified(false);
        user.setRoles(new ArrayList<>());
    }

    // ---------- GET USER ----------

    @Test
    void getUser_shouldReturnUserWithoutPassword_whenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = service.getUser(userId);

        assertEquals("", result.getPassword());
        verify(userRepository).findById(userId);
    }

    @Test
    void getUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getUser(userId));
    }

    // ---------- GET ALL USERS ----------

    @Test
    void getAllUsers_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = service.getAllUsers();

        assertEquals(1, result.size());
    }

    // ---------- DELETE USER ----------

    @Test
    void deleteUser_shouldDeleteAndReturnTrue_whenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Boolean result = service.deleteUser(userId);

        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.deleteUser(userId));
    }

    // ---------- GENERATE OTP ----------

    @Test
    void generateOtp_shouldGenerateAndSendOtp_whenUserValid() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(emailClient.sendMail(any()))
                .thenReturn(ResponseEntity.ok("sent"));

        Boolean result = service.generateOtp(user.getEmail());

        assertTrue(result);
        verify(otpRepository).save(any(Otp.class));
        verify(emailClient).sendMail(any(Mail.class));
    }

    @Test
    void generateOtp_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.generateOtp(user.getEmail()));
    }

    @Test
    void generateOtp_shouldThrowException_whenAlreadyVerified() {
        user.setEmailVerified(true);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> service.generateOtp(user.getEmail()));
    }

    @Test
    void generateOtp_shouldThrowException_whenEmailFails() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(emailClient.sendMail(any()))
                .thenReturn(ResponseEntity.status(500).build());

        assertThrows(RuntimeException.class,
                () -> service.generateOtp(user.getEmail()));
    }

    // ---------- VALIDATE OTP ----------

    @Test
    void validateOtp_shouldReturnTrue_andVerifyUser_whenOtpValid() {
        Otp otp = new Otp(user.getEmail(), "123456",
                LocalDateTime.now().plusMinutes(5));

        when(otpRepository.findTopByEmailOrderByExpiresAtDesc(user.getEmail()))
                .thenReturn(Optional.of(otp));

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Boolean result = service.validateOtp(user.getEmail(), "123456");

        assertTrue(result);
        assertTrue(user.isEmailVerified());
        verify(userRepository).save(user);
        verify(otpRepository).delete(otp);
    }

    @Test
    void validateOtp_shouldReturnFalse_whenOtpNotFound() {
        when(otpRepository.findTopByEmailOrderByExpiresAtDesc(user.getEmail()))
                .thenReturn(Optional.empty());

        assertFalse(service.validateOtp(user.getEmail(), "123456"));
    }

    @Test
    void validateOtp_shouldReturnFalse_whenOtpExpired() {
        Otp otp = new Otp(user.getEmail(), "123456",
                LocalDateTime.now().minusMinutes(1));

        when(otpRepository.findTopByEmailOrderByExpiresAtDesc(user.getEmail()))
                .thenReturn(Optional.of(otp));

        Boolean result = service.validateOtp(user.getEmail(), "123456");

        assertFalse(result);
        verify(otpRepository).delete(otp);
    }

    @Test
    void validateOtp_shouldReturnFalse_whenOtpIncorrect() {
        Otp otp = new Otp(user.getEmail(), "123456",
                LocalDateTime.now().plusMinutes(5));

        when(otpRepository.findTopByEmailOrderByExpiresAtDesc(user.getEmail()))
                .thenReturn(Optional.of(otp));

        Boolean result = service.validateOtp(user.getEmail(), "000000");

        assertFalse(result);
    }

    // ---------- UPDATE ROLE ----------

    @Test
    void updateUserRole_shouldAddRole_andSendMail_whenRoleNotPresent() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        service.updateUserRole(userId, Role.ADMIN);

        assertTrue(user.getRoles().contains(Role.ADMIN));
        verify(userRepository).save(user);
        verify(mailProducer).sendRoleUpdateMail(any(Mail.class));
    }

    @Test
    void updateUserRole_shouldNotDuplicateRole_whenAlreadyExists() {
        user.getRoles().add(Role.ADMIN);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        service.updateUserRole(userId, Role.ADMIN);

        assertEquals(1, user.getRoles().size());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserRole_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.updateUserRole(userId, Role.ADMIN));
    }
}
