package com.redhat.springboot.vacationleave.sickrequests.service;

import com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequest;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequestStatus;
import com.redhat.springboot.vacationleave.sickrequests.repository.SickRequestsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SickServiceImpl implements SickService {

    @Autowired
    SickRequestsRepository sickRequestsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public SickRequestDto save(SickRequestDto sickRequestDto) {
        SickRequest sickRequest = convertToEntity(sickRequestDto);
        sickRequest.setSickRequestStatus(SickRequestStatus.PENDING);
        sickRequest = sickRequestsRepository.save(sickRequest);
        sickRequestDto.setSickRequestStatus(sickRequest.getSickRequestStatus());
        sickRequestDto.setId(sickRequest.getId());
        return sickRequestDto;
    }

    private SickRequest convertToEntity(SickRequestDto sickRequestDto) {
        SickRequest sickRequest = modelMapper.map(sickRequestDto, SickRequest.class);
        return sickRequest;
    }
}
