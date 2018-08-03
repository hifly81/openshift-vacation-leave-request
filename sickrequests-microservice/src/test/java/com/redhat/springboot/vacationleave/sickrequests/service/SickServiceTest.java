package com.redhat.springboot.vacationleave.sickrequests.service;

import com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequestStatus;
import com.redhat.springboot.vacationleave.sickrequests.service.SickService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SickServiceTest {

    @Autowired
    private SickService sickService;

    @Before
    public void beforeTest() { }

    @Test
    public void save() {
        SickRequestDto sickRequestDto = new SickRequestDto();
        sickRequestDto.setEmployeeId("AB121231");
        sickRequestDto.setDateRequested(LocalDate.of(2018, 10, 21));
        sickRequestDto = sickService.save(sickRequestDto);
        assertNotNull(sickRequestDto);
        assertEquals(sickRequestDto.getSickRequestStatus(), SickRequestStatus.PENDING);
    }

    @Test
    public void getRequestsByEmployee() {
        SickRequestDto sickRequestDto = new SickRequestDto();
        sickRequestDto.setEmployeeId("AB121231");
        sickRequestDto.setDateRequested(LocalDate.of(2018, 10, 21));
        sickRequestDto = sickService.save(sickRequestDto);
        assertNotNull(sickRequestDto);
        assertEquals(sickRequestDto.getSickRequestStatus(), SickRequestStatus.PENDING);

        List<SickRequestDto> result = sickService.getRequestsByEmployee("AB121231", new PageRequest(0, 100));
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }


}