package org.team11.tickebook.auth_service.service;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.team11.tickebook.auth_service.model.entity.User;
import org.team11.tickebook.auth_service.model.enums.Role;

@Service
public interface UserService {

    //	public RegistrationResponse createUser(RegistrationRequest dto);
    public User getUser(UUID id);

    public List<User> getAllUsers();

    public boolean deleteUser(UUID id);

    public Boolean generateOtp(String email);

    public Boolean validateOtp(String email,String otp);

    void updateUserRole(UUID userId, Role role);
}
