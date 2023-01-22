package com.example.rekreativ.service;

import com.example.rekreativ.model.Teammate;

public interface TeammateService {

    Teammate findTeammateByName(String name);

    Teammate findById(Long id);

    Teammate save(Teammate teammate);

    Teammate initSave(Teammate teammate);

    Iterable<Teammate> findAll();

    void delete(Long id);
}
