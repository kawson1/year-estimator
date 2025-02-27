package vector.lkawa.year_estimator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vector.lkawa.year_estimator.model.YearEstimation;

@Repository
public interface YearEstimationRepository extends JpaRepository<YearEstimation, Long> {
}
