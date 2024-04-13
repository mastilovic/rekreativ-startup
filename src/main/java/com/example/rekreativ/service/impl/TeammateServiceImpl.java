package com.example.rekreativ.service.impl;

import com.example.rekreativ.dto.TeammateUpdateDTO;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.repository.TeammateRepository;
import com.example.rekreativ.service.TeammateService;
import com.example.rekreativ.commons.CustomValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class TeammateServiceImpl implements TeammateService {

    private final TeammateRepository teammateRepository;
    private final CustomValidator customValidator;

    public TeammateServiceImpl(TeammateRepository teammateRepository,
                               CustomValidator customValidator) {
        this.teammateRepository = teammateRepository;
        this.customValidator = customValidator;
    }

    public Teammate findTeammateByName(String name) {
        log.debug("calling findTeammateByName method");

        return teammateRepository.findTeammateByName(name)
                .orElseThrow(() -> new ObjectNotFoundException(Teammate.class, name));
    }

    @Override
    public Teammate findById(Long id) {
        log.debug("calling findById method");

        return teammateRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Teammate.class, id));
    }

    public Teammate save(Teammate teammate) {
        log.debug("calling save method");

        if (Objects.isNull(teammate)) return null;

        Teammate newTeammate = new Teammate();

        newTeammate.setName(teammate.getName());
        newTeammate.setTotalGamesPlayed(0);
        newTeammate.setWins(0);
        customValidator.validate(newTeammate);

        return teammateRepository.save(newTeammate);
    }

    public Teammate initSave(Teammate teammate) {
        log.debug("calling initSave method");

        return teammateRepository.save(teammate);
    }

    public Iterable<Teammate> findAll() {
        log.debug("calling findAll method");

        return teammateRepository.findAll();
    }

    public void delete(Long id) {
        log.debug("calling delete method");

        Teammate teammate = findById(id);

        teammateRepository.deleteById(teammate.getId());
    }

    public Teammate update(TeammateUpdateDTO teammateUpdateDTO) {
        log.debug("calling save method");
        Teammate teammate = this.findById(teammateUpdateDTO.getId());

        teammate.setName(teammateUpdateDTO.getName());

        return teammateRepository.save(teammate);
    }
}
