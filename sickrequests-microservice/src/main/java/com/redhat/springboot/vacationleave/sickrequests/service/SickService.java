package com.redhat.springboot.vacationleave.sickrequests.service;

import com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto;

public interface SickService {

    SickRequestDto save(SickRequestDto sickRequestDto);
}
