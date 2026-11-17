package dev.urlshortener.repository;

import dev.urlshortener.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByCode(String code);

    boolean existsByCode(String code);

    List<Link> findAllByOrderByClickCountDesc(Pageable pageable);
}