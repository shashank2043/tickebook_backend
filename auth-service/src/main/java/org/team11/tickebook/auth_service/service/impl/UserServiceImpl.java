package org.team11.tickebook.auth_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team11.tickebook.auth_service.client.EmailClient;
import org.team11.tickebook.auth_service.client.Mail;
import org.team11.tickebook.auth_service.exception.UserNotFoundException;
import org.team11.tickebook.auth_service.kafka.MailProducer;
import org.team11.tickebook.auth_service.model.entity.Otp;
import org.team11.tickebook.auth_service.model.entity.User;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.repository.OtpRepository;
import org.team11.tickebook.auth_service.repository.UserRepository;
import org.team11.tickebook.auth_service.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private MailProducer mailProducer;

    @Override
    public User getUser(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(UUID id) {
        var user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        userRepository.delete(user);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean generateOtp(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not found with email id " + email));
        if(user.isEmailVerified()){
            throw new RuntimeException("User is already verified!");
        }
        String otpValue = String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 999999)
        );
        Otp otp = new Otp(email, otpValue, LocalDateTime.now().plusMinutes(5));
        Otp save = otpRepository.save(otp);

        String subject = "TickEBook - Email Verification OTP";
        String body =
                "Hello,\n\n" +
                        "Your OTP for TickEBook email verification is: " + otpValue + "\n\n" +
                        "This OTP is valid for 5 minutes.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Regards,\n" +
                        "TickEBook Team";
        Mail mail = new Mail(email,subject,body);
        ResponseEntity<String> response = emailClient.sendMail(mail);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send OTP email");
        }
        return true;
    }

    @Override
    public Boolean validateOtp(String email, String enteredOtp) {
        Optional<Otp> otpOpt =
                otpRepository.findTopByEmailOrderByExpiresAtDesc(email);

        if (otpOpt.isEmpty()) return false;

        Otp otp = otpOpt.get();

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            return false;
        }

        if (!otp.getOtp().equals(enteredOtp)) return false;
        else {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email id " + email));
            user.setEmailVerified(true);
            userRepository.save(user);
        }
        otpRepository.delete(otp); // delete after success
        return true;
    }
    @Override
    @Transactional
    public void updateUserRole(UUID userId, Role role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Role> beforeRoles = List.copyOf(user.getRoles());

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
        }

        userRepository.save(user);

        List<Role> afterRoles = user.getRoles();

        // ---- MAIL ----
        String subject = "Your Roles Have Been Updated";

        String body =
                "Hello,\n\n" +
                        "Your account roles have been updated.\n\n" +
                        "Before: " + beforeRoles + "\n" +
                        "After: " + afterRoles + "\n\n" +
                        "If you did not expect this change, contact support.\n\n" +
                        "Regards,\nTickEBook Team";

        Mail mail = new Mail(user.getEmail(), subject, body);

        mailProducer.sendRoleUpdateMail(mail);
    }

//
//	@Override
//	public RegistrationResponse createUser(RegistrationRequest dto) {
//
//		User user = new User();
//		//From dto
//		user.setId(UUID.randomUUID());
//		user.setEmail(dto.getEmail());
//		user.setFirstName(dto.getFirstName());
//		user.setLastName(dto.getLastName());
//		user.setPassword(passwordEncoder.encode(dto.getPassword()));
//		//defaults
//		user.setRole(Role.USER);
//		user.setEmailVerified(false);
//		user.setAccountLocked(false);
//		user.setFailedLoginAttempts(0);
//		user.setTokenVersion(0);
//		user.setActive(true);
//		user.setDeleted(false);
//
//		user.setCreatedAt(LocalDateTime.now());
//		user.setUpdatedAt(LocalDateTime.now());
//
//		User save = userRepository.save(user);
//		return new RegistrationResponse(
//				save.getId(),
//				save.getEmail(),
//				save.getFirstName(),
//				save.getLastName(),
//				save.getRole().name(),
//				save.isEmailVerified(),);
//	}

}
