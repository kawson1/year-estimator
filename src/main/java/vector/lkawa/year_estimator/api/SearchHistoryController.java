package vector.lkawa.year_estimator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import vector.lkawa.year_estimator.SearchHistoryRepository;
import vector.lkawa.year_estimator.YearEstimation;
import vector.lkawa.year_estimator.utils.RandomStringGenerator;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RequestMapping("/estimations")
@RestController
public class SearchHistoryController {

    private final SearchHistoryRepository repository;

    private final RestTemplate restTemplate;

    @Value("${api.agify.url}")
    private String estimationApiURL;

    @GetMapping("/{name}")
    public YearEstimation getEstimation(@PathVariable String name) {
        YearEstimation ye = getEstimationFromApi(name);
        repository.save(ye);

        return ye;
    }

    @GetMapping
    public Page<YearEstimation> getHistory(@PageableDefault(size = 5) Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/generate/{count}")
    public List<YearEstimation> generateRandomEstimations(@PathVariable int count) {
        List<YearEstimation> estimations = IntStream.range(0, count)
                .mapToObj(x -> {
                    String randomName = RandomStringGenerator.generateRandomString();
                    return getEstimationFromApi(randomName);
                })
                .filter(Objects::nonNull)
                .toList();

        repository.saveAll(estimations);
        return estimations;
    }

    private YearEstimation getEstimationFromApi(String name) {
        return restTemplate.getForObject(estimationApiURL + "?name={name}", YearEstimation.class, name);
    }

}
