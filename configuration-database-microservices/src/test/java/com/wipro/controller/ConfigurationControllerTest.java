package com.wipro.controller;

import com.wipro.entity.Configuration;
import com.wipro.service.impl.ConfigurationServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigurationControllerTest {

    @InjectMocks
    private ConfigurationController configurationController;

    @Mock
    private ConfigurationServiceImpl configurationServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addConfigurationTest() {
        Configuration response = new Configuration();
        when(configurationServiceImpl.addConfig(any())).thenReturn(response);
        ResponseEntity<Configuration> apiResponse = configurationController.addConfiguration(new Configuration());
        Assertions.assertEquals(HttpStatus.CREATED, apiResponse.getStatusCode());
    }

    @Test
    void getAllConfigurationTest() {
        Configuration response = new Configuration();
        when(configurationServiceImpl.getAll()).thenReturn(List.of(response));
        ResponseEntity<List<Configuration>> apiResponse = configurationController.getAllConfiguration();
        Assertions.assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
    }

    @Test
    void updateConfigurationTest() {
        Configuration response = new Configuration();
        when(configurationServiceImpl.updateConfiguration(any(),anyLong())).thenReturn(response);
        ResponseEntity<Configuration> apiResponse = configurationController.updateConfiguration(new Configuration(),1L);
        Assertions.assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
    }

    @Test
    void getConfigurationTest() {
        Configuration response = new Configuration();
        when(configurationServiceImpl.getConfigurations(anyString())).thenReturn(List.of(response));
        ResponseEntity<List<Configuration>> apiResponse = configurationController.getConfiguration("device1");
        Assertions.assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
    }

    @Test
    void deleteConfigurationTest() {
        Configuration response = new Configuration();
        ResponseEntity<Void> apiResponse = configurationController.deleteConfiguration("device1");
        Assertions.assertEquals(HttpStatus.NO_CONTENT, apiResponse.getStatusCode());
    }
}