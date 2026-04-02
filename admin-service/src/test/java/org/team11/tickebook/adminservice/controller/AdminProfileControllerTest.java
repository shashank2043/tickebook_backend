package org.team11.tickebook.adminservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.service.AdminProfileService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(AdminProfileController.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
class AdminProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminProfileService adminProfileService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------- CREATE ADMIN --------

    @Test
    void createAdmin_shouldReturn200_whenSuccess() throws Exception {

        AdminProfile admin = new AdminProfile();
        admin.setId(UUID.randomUUID());

        when(adminProfileService.createAdmin(any()))
                .thenReturn(admin);

        mockMvc.perform(post("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk());

        verify(adminProfileService).createAdmin(any());
    }

    @Test
    void createAdmin_shouldReturn500_whenServiceFails() throws Exception {

        when(adminProfileService.createAdmin(any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isInternalServerError());
    }

    // -------- UPDATE ADMIN --------

    @Test
    void updateAdmin_shouldReturn200_whenSuccess() throws Exception {

        UUID id = UUID.randomUUID();

        AdminProfile admin = new AdminProfile();
        admin.setId(id);

        when(adminProfileService.updateAdmin(any(), any()))
                .thenReturn(admin);

        mockMvc.perform(put("/api/admins/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk());

        verify(adminProfileService).updateAdmin(any(), any());
    }

    // -------- DELETE ADMIN --------

    @Test
    void deleteAdmin_shouldReturn204_whenSuccess() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing().when(adminProfileService).deleteAdmin(id);

        mockMvc.perform(delete("/api/admins/{id}", id))
                .andExpect(status().isNoContent());

        verify(adminProfileService).deleteAdmin(id);
    }
}