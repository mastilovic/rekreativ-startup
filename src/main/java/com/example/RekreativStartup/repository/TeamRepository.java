package com.example.RekreativStartup.repository;

import com.example.RekreativStartup.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);
    Optional<Team> findByTeammate(String teammate);
    Optional<Team> deleteTeammate(String teammate);
}
