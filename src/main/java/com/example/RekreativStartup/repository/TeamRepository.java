package com.example.RekreativStartup.repository;

import com.example.RekreativStartup.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);

    @Query(value = "SELECT teammate FROM Team", nativeQuery = true)
    Optional<Team> findByTeammate(String teammate);

    @Query(value = "DELETE d FROM team as d WHERE d.teammate={teammate};", nativeQuery = true)
    Optional<Team> deleteTeammate(String teammate);

//    @Query(value = "SELECT t.teammate FROM Team t WHERE t.teammates.team_name IN (:teamName)")
//    Optional<Team> findAllTeammatesByTeamName(String teamName);

//    @Query(value = "SELECT COUNT()")
//    Optional<Team> findTeamScore(String teamName);
}
