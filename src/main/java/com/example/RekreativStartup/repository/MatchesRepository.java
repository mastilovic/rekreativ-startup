package com.example.RekreativStartup.repository;

import com.example.RekreativStartup.model.Matches;
import com.example.RekreativStartup.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchesRepository extends PagingAndSortingRepository<Matches, Long> {

    @Query(value = "SELECT * FROM matches WHERE teama={teamname} OR teamb = {teamname};", nativeQuery = true)
    Optional<Matches> findAllMatchesByTeamName(String teamname);
}
