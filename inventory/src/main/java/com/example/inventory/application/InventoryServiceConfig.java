package com.example.inventory.application;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryServiceConfig {

    // Add configuration beans or properties here if needed

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
