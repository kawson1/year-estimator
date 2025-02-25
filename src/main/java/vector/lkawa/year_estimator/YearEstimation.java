package vector.lkawa.year_estimator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class YearEstimation {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;

    private String name;

    private Integer count;

    @JsonIgnore
    private LocalDateTime date = LocalDateTime.now();

    @PrePersist
    void setDate() {
        date = LocalDateTime.now();
    }
}
