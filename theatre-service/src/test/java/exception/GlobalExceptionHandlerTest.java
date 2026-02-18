package exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.team11.tickebook.theatreservice.TheatreServiceApplication;
import org.team11.tickebook.theatreservice.controller.ScreenController;
import org.team11.tickebook.theatreservice.dto.request.CreateScreenRequest;
import org.team11.tickebook.theatreservice.exception.GlobalExceptionHandler;
import org.team11.tickebook.theatreservice.service.ScreenService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScreenController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = TheatreServiceApplication.class)
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ScreenService service;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createScreen_shouldReturn409_whenDuplicateKey() throws Exception {

        when(service.create(any(), any()))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        CreateScreenRequest req = new CreateScreenRequest(
                "Screen 1", 1, 100, UUID.randomUUID()
        );

        mockMvc.perform(post("/api/screens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Duplicate entry"));
    }
    @Test
    void createScreen_shouldReturnSpecificMessage_whenScreenNumberConstraint() throws Exception {

        when(service.create(any(), any()))
                .thenThrow(new DataIntegrityViolationException(
                        "constraint uk_theatre_screen_number violated"
                ));

        CreateScreenRequest req = new CreateScreenRequest(
                "Screen 1", 1, 100, UUID.randomUUID()
        );

        mockMvc.perform(post("/api/screens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(content()
                        .string("Screen number already exists for this theatre"));
    }
    @Test
    void createScreen_shouldReturnDuplicateEntry_whenMessageNull() throws Exception {

        when(service.create(any(), any()))
                .thenThrow(new DataIntegrityViolationException(null));

        CreateScreenRequest req = new CreateScreenRequest(
                "Screen 1", 1, 100, UUID.randomUUID()
        );

        mockMvc.perform(post("/api/screens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Duplicate entry"));
    }
    @Test
    void createScreen_shouldReturn400_whenIllegalArgument() throws Exception {

        when(service.create(any(), any()))
                .thenThrow(new IllegalArgumentException("Access denied"));

        CreateScreenRequest req = new CreateScreenRequest(
                "Screen 1", 1, 100, UUID.randomUUID()
        );

        mockMvc.perform(post("/api/screens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Access denied"));
    }

}
