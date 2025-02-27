package vector.lkawa.year_estimator.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class YearEstimationDto {
    private String name;
    private Integer age;
    private Integer count;
}
