package com.example.RekreativStartup.repository;

import com.example.RekreativStartup.model.Matches;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MatchesRepository extends PagingAndSortingRepository<Matches, Long> {
}
