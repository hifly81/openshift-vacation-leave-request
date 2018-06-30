package com.redhat.springboot.vacationleave.dto;

import com.github.jmnarloch.spring.boot.modelmapper.ModelMapperConfigurer;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NamingConventions;
import org.springframework.stereotype.Component;

@Component
public class CustomConfigurer implements ModelMapperConfigurer {

       public void configure(ModelMapper modelMapper) {
           modelMapper.getConfiguration()
               .setSourceNamingConvention(NamingConventions.NONE)
               .setDestinationNamingConvention(NamingConventions.NONE);
       }
  }