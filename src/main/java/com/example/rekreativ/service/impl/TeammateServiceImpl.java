package com.example.rekreativ.service.impl;

import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.repository.TeammateRepository;
import com.example.rekreativ.service.TeammateService;
import com.example.rekreativ.util.ValidatorUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TeammateServiceImpl implements TeammateService {

    private final TeammateRepository teammateRepository;
    private final ValidatorUtil validatorUtil;

    public TeammateServiceImpl(TeammateRepository teammateRepository,
                               ValidatorUtil validatorUtil) {
        this.teammateRepository = teammateRepository;
        this.validatorUtil = validatorUtil;
    }

    public Teammate findTeammateByName(String name){
        Optional<Teammate> teammate = teammateRepository.findTeammateByName(name);

        if (teammate.isEmpty()){

            throw new ObjectNotFoundException(Teammate.class, name);
        }

        return teammate.get();
    }

    @Override
    public Teammate findById(Long id) {
        Optional<Teammate> teammate = teammateRepository.findById(id);

        if(teammate.isEmpty()){

            throw new ObjectNotFoundException(Teammate.class, id);
        }

        return teammate.get();
    }


    public Teammate save(Teammate teammate) {
        if(Objects.isNull(teammate)) return null;

        Teammate newTeammate = new Teammate();

        newTeammate.setName(teammate.getName());
        newTeammate.setTotalGamesPlayed(0);
        newTeammate.setWins(0);
        validatorUtil.validate(newTeammate);

        return teammateRepository.save(newTeammate);
    }

    public Teammate initSave(Teammate teammate) {

        return teammateRepository.save(teammate);
    }

    public Iterable<Teammate> findAll(){

        return teammateRepository.findAll();
    }

    public void delete(Long id) {
        Teammate teammate = findById(id);

        teammateRepository.deleteById(teammate.getId());
    }
}
