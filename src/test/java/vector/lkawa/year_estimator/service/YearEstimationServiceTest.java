package vector.lkawa.year_estimator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import vector.lkawa.year_estimator.dto.YearEstimationDto;
import vector.lkawa.year_estimator.model.YearEstimation;
import vector.lkawa.year_estimator.repository.YearEstimationRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class YearEstimationServiceTest {

    @Mock
    private YearEstimationRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private YearEstimationService service;

    private YearEstimation entity;
    private YearEstimationDto dto;

    @BeforeEach
    void setUp() {
        entity = new YearEstimation(1L, 25, "John", 100, LocalDateTime.now());
        dto = new YearEstimationDto(entity.getName(), entity.getAge(), entity.getCount());
    }

    @Test
    void findAll_ShouldReturnPagedResult() {
        // Given
        PageRequest pageable = PageRequest.of(0, 5);
        Page<YearEstimation> entityPage = new PageImpl<>(List.of(entity));
        when(repository.findAll(pageable)).thenReturn(entityPage);
        when(modelMapper.map(entity, YearEstimationDto.class)).thenReturn(dto);

        // When
        Page<YearEstimationDto> result = service.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(dto.getName(), result.getContent().get(0).getName());

        verify(repository, times(1)).findAll(pageable);
        verify(modelMapper, times(1)).map(entity, YearEstimationDto.class);
    }

    @Test
    void save_ShouldSaveAndReturnDto() {
        // Given
        when(modelMapper.map(dto, YearEstimation.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, YearEstimationDto.class)).thenReturn(dto);

        // When
        YearEstimationDto result = service.save(dto);

        // Then
        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());

        verify(repository, times(1)).save(entity);
        verify(modelMapper, times(1)).map(dto, YearEstimation.class);
        verify(modelMapper, times(1)).map(entity, YearEstimationDto.class);
    }

    @Test
    void saveAll_ShouldSaveListAndReturnDtos() {
        // Given
        List<YearEstimationDto> dtoList = List.of(dto);
        List<YearEstimation> entityList = List.of(entity);

        when(modelMapper.map(dto, YearEstimation.class)).thenReturn(entity);
        when(repository.saveAll(entityList)).thenReturn(entityList);
        when(modelMapper.map(entity, YearEstimationDto.class)).thenReturn(dto);

        // When
        List<YearEstimationDto> result = service.saveAll(dtoList);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto.getName(), result.get(0).getName());

        verify(repository, times(1)).saveAll(entityList);
        verify(modelMapper, times(1)).map(dto, YearEstimation.class);
        verify(modelMapper, times(1)).map(entity, YearEstimationDto.class);
    }
}