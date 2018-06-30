package com.redhat.springboot.vacationleave.sickrequests.repository;

import com.redhat.springboot.vacationleave.sickrequests.model.SickRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SickRequestsRepository extends PagingAndSortingRepository<SickRequest, Integer> {
}
