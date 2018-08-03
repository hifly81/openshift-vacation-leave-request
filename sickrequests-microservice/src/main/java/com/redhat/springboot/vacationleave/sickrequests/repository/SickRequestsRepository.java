package com.redhat.springboot.vacationleave.sickrequests.repository;

import com.redhat.springboot.vacationleave.sickrequests.model.SickRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface SickRequestsRepository extends PagingAndSortingRepository<SickRequest, Integer> {

    @Query("select sr from SickRequest sr where sr.employeeId = :employeeId")
    Page<SickRequest> findByEmployeeId(@Param("employeeId") String employeeId, Pageable p);

}
