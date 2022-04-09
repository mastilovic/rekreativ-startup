package com.example.RekreativStartup.controller;

import com.example.RekreativStartup.Service.TeammateService;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.repository.TeammateRepository;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/teammate/v1")
public class TeammateController {

    private final TeammateRepository teammateRepository;
    private final TeammateService teammateService;

    @Autowired
    public TeammateController(TeammateRepository teammateRepository,
                              TeammateService teammateService) {
        this.teammateRepository = teammateRepository;
        this.teammateService = teammateService;
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Teammate teammate) {
        if(ValidatorUtil.teammateValidator(teammate)){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        teammateRepository.save(teammate);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {

        Iterable<Teammate> obj = teammateService.findAll();
        ArrayList myList = new ArrayList();
        for (Teammate t: obj){
            myList.add(t);
        }
        if (obj == null) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
//        Object[] objArray = myList.toArray();
//        ArrayList dtoList = new ArrayList();
//        ModelMapper modelMapper = new ModelMapper();
//        for(int i=0; i < objArray.length ; i++) {
//            UserDTO userDto = modelMapper.map(objArray[i], UserDTO.class);
//            dtoList.add(userDto);
//        }

        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeammate(@PathVariable Long id) {
        teammateService.delete(id);
        return new ResponseEntity<Object>("Teammate deleted successfully!", HttpStatus.OK);
    }
}
