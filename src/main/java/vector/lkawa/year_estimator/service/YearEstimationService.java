package vector.lkawa.year_estimator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vector.lkawa.year_estimator.dto.YearEstimationDto;
import vector.lkawa.year_estimator.model.YearEstimation;
import vector.lkawa.year_estimator.repository.YearEstimationRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class YearEstimationService {

    private final YearEstimationRepository repository;

    private final ModelMapper modelMapper;

    public Page<YearEstimationDto> findAll(Pageable pageable) {
        log.info("Fetching all estimations for pagination={}", pageable);

        Page<YearEstimationDto> result = repository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, YearEstimationDto.class));

        log.info("Returned {} estimations", result.getContent().size());
        return result;
    }

    public YearEstimationDto save(YearEstimationDto dto) {
        log.info("Saving estimation {} to database", dto);

        YearEstimation estimation = modelMapper.map(dto, YearEstimation.class);
        repository.save(estimation);
        YearEstimationDto savedDto = modelMapper.map(estimation, YearEstimationDto.class);

        log.info("Saved estimation: {}", savedDto);

        return savedDto;
    }

    public List<YearEstimationDto> saveAll(List<YearEstimationDto> dtos) {
        log.info("Saving {} estimations to database...", dtos.size());

        List<YearEstimation> estimations = dtos.stream()
                .map(dto -> modelMapper.map(dto, YearEstimation.class))
                .toList();

        List<YearEstimation> savedEntities = repository.saveAll(estimations);

        log.info("Successfully saved {} estimations.", savedEntities.size());

        return savedEntities.stream()
                .map(entity -> modelMapper.map(entity, YearEstimationDto.class))
                .toList();
    }
}
