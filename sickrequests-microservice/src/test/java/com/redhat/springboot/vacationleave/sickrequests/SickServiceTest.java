package com.redhat.springboot.vacationleave.sickrequests;

import com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequestStatus;
import com.redhat.springboot.vacationleave.sickrequests.service.SickService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

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
        sickRequestDto.setEmployeeId(1);
        sickRequestDto.setDateRequested(LocalDate.of(2018, 10, 21));
        sickRequestDto = sickService.save(sickRequestDto);
        assertNotNull(sickRequestDto);
        assertEquals(sickRequestDto.getSickRequestStatus(), SickRequestStatus.PENDING);
    }


}