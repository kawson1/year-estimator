package vector.lkawa.year_estimator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(indexes = @Index(columnList = "name"))
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
    private LocalDateTime date;

    @PrePersist
    void setDate() {
        date = LocalDateTime.now();
    }
}
