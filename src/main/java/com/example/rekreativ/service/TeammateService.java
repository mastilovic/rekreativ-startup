package com.example.rekreativ.service;

import com.example.rekreativ.model.Teammate;

import javax.xml.bind.ValidationException;

public interface TeammateService {

    public Teammate findTeammateByName(String name);

    public Teammate findById(Long id);

    public Teammate save(Teammate teammate) throws ValidationException;

    public Teammate initSave(Teammate teammate);

    public Iterable<Teammate> findAll();

    public void delete(Long id);
}
