package com.redhat.springboot.vacationleave.sickrequests.service;

import com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SickService {

    SickRequestDto save(SickRequestDto sickRequestDto);

    Page<SickRequest> list(PageRequest pageRequest);

    List<SickRequestDto> getRequestsByEmployee(String employeeId, PageRequest pageRequest);
}
