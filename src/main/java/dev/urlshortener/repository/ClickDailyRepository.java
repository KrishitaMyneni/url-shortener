package dev.urlshortener.repository;

import dev.urlshortener.entity.ClickDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClickDailyRepository extends JpaRepository<ClickDaily, Long> {

    Optional<ClickDaily> findByLinkIdAndClickDate(Long linkId, LocalDate clickDate);

    List<ClickDaily> findByLinkIdOrderByClickDateAsc(Long linkId);
}