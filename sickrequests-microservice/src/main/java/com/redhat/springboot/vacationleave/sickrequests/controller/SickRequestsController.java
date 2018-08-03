package com.redhat.springboot.vacationleave.sickrequests.controller;

import com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequest;
import com.redhat.springboot.vacationleave.sickrequests.service.SickService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/sickrequests")
public class SickRequestsController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SickService sickService;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SickRequestDto>> list(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Page<SickRequest> sickRequestPage = sickService.list(new PageRequest(page, pageSize));
        if(sickRequestPage.getContent() != null) {
            List<SickRequestDto> result = sickRequestPage.getContent().stream()
                    .map(post -> convertToDto(post))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SickRequestDto> send(@RequestBody SickRequestDto input) {
        SickRequestDto sickRequestDto = sickService.save(input);
        return new ResponseEntity<>(sickRequestDto, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/{employeeId}")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SickRequestDto>> listByEmployeeId(@PathVariable String employeeId, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        List<SickRequestDto> result = sickService.getRequestsByEmployee(employeeId, new PageRequest(page, pageSize));
        if(result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private SickRequestDto convertToDto(SickRequest sickRequest) {
        SickRequestDto sickRequestDto = modelMapper.map(sickRequest, SickRequestDto.class);
        return sickRequestDto;
    }

}
