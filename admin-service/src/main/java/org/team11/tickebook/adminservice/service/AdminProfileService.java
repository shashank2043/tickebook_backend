package org.team11.tickebook.adminservice.service;

import org.team11.tickebook.adminservice.model.AdminProfile;

import java.util.UUID;

public interface AdminProfileService {

    AdminProfile createAdmin(AdminProfile adminProfile);

    AdminProfile updateAdmin(UUID id, AdminProfile adminProfile);

    void deleteAdmin(UUID id);
}