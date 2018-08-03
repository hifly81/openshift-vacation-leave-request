package com.redhat.springboot.vacationleave.sickrequests.service;

import com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequest;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequestStatus;
import com.redhat.springboot.vacationleave.sickrequests.repository.SickRequestsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SickServiceImpl implements SickService {

    @Autowired
    SickRequestsRepository sickRequestsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<SickRequest> list(PageRequest pageRequest) {
        Page<SickRequest> page = sickRequestsRepository.findAll(pageRequest);
        return page;
    }

    @Override
    public SickRequestDto save(SickRequestDto sickRequestDto) {
        SickRequest sickRequest = convertToEntity(sickRequestDto);
        sickRequest.setSickRequestStatus(SickRequestStatus.PENDING);
        sickRequest = sickRequestsRepository.save(sickRequest);
        sickRequestDto.setSickRequestStatus(sickRequest.getSickRequestStatus());
        sickRequestDto.setId(sickRequest.getId());
        return sickRequestDto;
    }

    @Override
    public List<SickRequestDto> getRequestsByEmployee(String employeeId, PageRequest pageRequest) {
        Page<SickRequest> page = sickRequestsRepository.findByEmployeeId(employeeId, pageRequest);
        if(page.getContent() != null) {
            List<SickRequestDto> result = page.getContent().stream()
                    .map(post -> convertToDto(post))
                    .collect(Collectors.toList());
            return result;
        }
        return null;
    }

    private SickRequest convertToEntity(SickRequestDto sickRequestDto) {
        SickRequest sickRequest = modelMapper.map(sickRequestDto, SickRequest.class);
        return sickRequest;
    }

    private SickRequestDto convertToDto(SickRequest sickRequest) {
        SickRequestDto sickRequestDto = modelMapper.map(sickRequest, SickRequestDto.class);
        return sickRequestDto;
    }

}
