package vector.lkawa.year_estimator.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import vector.lkawa.year_estimator.dto.YearEstimationDto;
import vector.lkawa.year_estimator.service.YearEstimationService;
import vector.lkawa.year_estimator.util.Message;
import vector.lkawa.year_estimator.util.RandomStringGenerator;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RequestMapping("/estimations")
@RestController
public class YearEstimationController {

    private final YearEstimationService service;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${api.agify.url}")
    private String estimationApiURL;

    @GetMapping("/{name}")
    public YearEstimationDto getEstimation(@PathVariable String name) {
        logger.info("Called method getEstimation for name={}", name);

        YearEstimationDto response = getEstimationFromApi(name);

        if (Objects.isNull(response)) {
            logger.error("Returned object from external API for name={} is empty.", name);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, Message.API.Exception.noContentException());
        }

        logger.info("Saving object {} to database", response);
        service.save(response);

        return response;
    }

    @GetMapping
    public Page<YearEstimationDto> getHistory(@PageableDefault(size = 5) Pageable pageable) {
        logger.info("Called method getHistory for pageable={}", pageable);
        Page<YearEstimationDto> history = service.findAll(pageable);
        logger.info("Returned {} records from history", history.getContent().size());
        return history;
    }

    @GetMapping("/generate/{count}")
    public List<YearEstimationDto> generateRandomEstimations(@PathVariable int count) {
        logger.info("Starting generating {} random values", count);
        List<YearEstimationDto> estimations = IntStream.range(0, count)
                .mapToObj(x -> {
                    String randomName = RandomStringGenerator.generateRandomString();
                    return getEstimationFromApi(randomName);
                })
                .filter(Objects::nonNull)
                .toList();

        logger.info("Finished generating process. Collected {} estimations.", estimations.size());

        List<YearEstimationDto> savedEstimations = service.saveAll(estimations);
        logger.info("Saved {} estimations to database.", estimations.size());

        return savedEstimations;
    }

    private YearEstimationDto getEstimationFromApi(String name) {
        logger.info("Fetching data from external API for name={}", name);
        YearEstimationDto response = restTemplate.getForObject(estimationApiURL + "?name={name}", YearEstimationDto.class, name);
        logger.info("Fetched data from API: {}", response);
        return response;
    }

}
