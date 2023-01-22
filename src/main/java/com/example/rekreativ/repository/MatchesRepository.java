package com.example.rekreativ.repository;

import com.example.rekreativ.model.Matches;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MatchesRepository extends PagingAndSortingRepository<Matches, Long> {
}
