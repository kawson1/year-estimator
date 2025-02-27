package vector.lkawa.year_estimator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import vector.lkawa.year_estimator.dto.YearEstimationDto;
import vector.lkawa.year_estimator.service.YearEstimationService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(YearEstimationController.class)
class YearEstimationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private YearEstimationService service;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void getEstimation_ShouldReturnEstimationWhenValidNameProvided() throws Exception {
        // Given
        String name = "John";
        YearEstimationDto estimation = new YearEstimationDto("John", 30, 100);
        when(restTemplate.getForObject(any(String.class), any(), any(Object.class))).thenReturn(estimation);
        when(service.save(any())).thenReturn(estimation);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/estimations/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.count").value(100));
    }

    @Test
    void getEstimation_shouldReturnNoContentWhenApiReturnsNull() throws Exception {
        // Given
        String name = "Unknown";
        when(restTemplate.getForObject(any(String.class), any(), any(Object.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/estimations/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getHistory_shouldReturnHistoryPage() throws Exception {
        // Given
        List<YearEstimationDto> estimations = List.of(
                new YearEstimationDto("Alice", 25, 200),
                new YearEstimationDto("Bob", 32, 150)
        );
        when(service.findAll(any())).thenReturn(new PageImpl<>(estimations));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/estimations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Alice"))
                .andExpect(jsonPath("$.content[1].name").value("Bob"));
    }

}