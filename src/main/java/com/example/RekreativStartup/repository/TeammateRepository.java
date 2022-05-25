package com.example.RekreativStartup.repository;

import com.example.RekreativStartup.model.Teammate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeammateRepository extends CrudRepository<Teammate, Long> {
    Optional<Teammate> findTeammateByName(String name);
}
