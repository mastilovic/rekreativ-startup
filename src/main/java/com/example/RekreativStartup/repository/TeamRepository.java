package com.example.RekreativStartup.repository;

import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);

    @Query(value = "SELECT teammate FROM Team", nativeQuery = true)
    Optional<Team> findByTeammate(String teammate);

    @Query(value = "DELETE FROM team WHERE teammate={teammate};", nativeQuery = true)
    Optional<Team> deleteTeammate(String teammate);
}
