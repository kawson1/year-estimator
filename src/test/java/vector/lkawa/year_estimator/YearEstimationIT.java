package vector.lkawa.year_estimator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import vector.lkawa.year_estimator.dto.YearEstimationDto;
import vector.lkawa.year_estimator.model.YearEstimation;
import vector.lkawa.year_estimator.repository.YearEstimationRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class YearEstimationIT {

    private static final String YEAR_ESTIMATION_CONTROLLER_PATH = "/estimations";

    @Autowired
    private YearEstimationRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private RestTemplate restTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    void getEstimation_shouldStoreSearchHistory() throws Exception {
        // Given
        YearEstimationDto dto = YearEstimationDto.builder()
                .name("Anna")
                .age(20)
                .count(1000)
                .build();
        when(restTemplate.getForObject(any(String.class), any(), any(Object.class))).thenReturn(dto);

        // When
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get(
                        YEAR_ESTIMATION_CONTROLLER_PATH + "/" + dto.getName()
                ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        YearEstimationDto responseDto = getDtoFromResponse(response);
        YearEstimation entity = repository.findAll().get(0);

        // Then
        assertThat(entity).isNotNull();
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getAge()).isEqualTo(entity.getAge());
        assertThat(responseDto.getName()).isEqualTo(entity.getName());
        assertThat(responseDto.getCount()).isEqualTo(entity.getCount());
    }

    @Test
    void shouldReturnSortedHistoryByNameAsc() throws Exception {
        // Given
        YearEstimation johnEstimation = YearEstimation.builder()
                .name("John")
                .build();
        YearEstimation annaEstimation = YearEstimation.builder()
                .name("Anna")
                .build();
        YearEstimation markEstimation = YearEstimation.builder()
                .name("Mark")
                .build();
        repository.saveAll(List.of(johnEstimation, annaEstimation, markEstimation));

        // When
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get(
                        YEAR_ESTIMATION_CONTROLLER_PATH + "?sort=name,asc"
                ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andReturn();

        List<YearEstimationDto> content = getDtoListFromResponse(response);

        // Then
        assertThat(content).hasSize(3);
        assertThat(content.get(0).getName()).isEqualTo(annaEstimation.getName());
        assertThat(content.get(1).getName()).isEqualTo(johnEstimation.getName());
        assertThat(content.get(2).getName()).isEqualTo(markEstimation.getName());
    }

    @Test
    void shouldReturnSortedHistoryByAge() throws Exception {
        // Given
        YearEstimation oldest = YearEstimation.builder()
                .age(3)
                .build();
        YearEstimation youngest = YearEstimation.builder()
                .age(1)
                .build();
        YearEstimation middle = YearEstimation.builder()
                .age(2)
                .build();
        repository.saveAll(List.of(oldest, youngest, middle));

        // When
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get(
                        YEAR_ESTIMATION_CONTROLLER_PATH + "?sort=age,asc"
                ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andReturn();

        List<YearEstimationDto> content = getDtoListFromResponse(response);

        // Then
        assertThat(content).hasSize(3);
        assertThat(content.get(0).getAge()).isEqualTo(youngest.getAge());
        assertThat(content.get(1).getAge()).isEqualTo(middle.getAge());
        assertThat(content.get(2).getAge()).isEqualTo(oldest.getAge());
    }

    private List<YearEstimationDto> getDtoListFromResponse(MvcResult response) throws UnsupportedEncodingException, JsonProcessingException {
        String responseBody = response.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode contentNode = rootNode.get("content");
        return objectMapper.readValue(contentNode.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, YearEstimationDto.class));
    }

    private YearEstimationDto getDtoFromResponse(MvcResult response) throws UnsupportedEncodingException, JsonProcessingException {
        String responseBody = response.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, YearEstimationDto.class);
    }
}