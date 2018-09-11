package com.redhat.springboot.vacationleave.employee.caching;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoteCacheContainer {

    @Value("${jdg.serverList}")
    private String serverList;


    @Bean
    public RemoteCacheManager remoteCacheManager() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServers(serverList);
        return new RemoteCacheManager(builder.build());
    }

}