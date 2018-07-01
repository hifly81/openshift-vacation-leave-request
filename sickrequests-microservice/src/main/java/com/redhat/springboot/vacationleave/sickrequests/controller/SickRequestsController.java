package com.redhat.springboot.vacationleave.sickrequests.controller;

import com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.sickrequests.service.SickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/sickrequests")
public class SickRequestsController {

    @Autowired
    SickService sickService;


    @ResponseBody
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SickRequestDto> send(@RequestBody SickRequestDto input) {
        SickRequestDto sickRequestDto = sickService.save(input);
        return new ResponseEntity<>(sickRequestDto, HttpStatus.OK);

    }

}
