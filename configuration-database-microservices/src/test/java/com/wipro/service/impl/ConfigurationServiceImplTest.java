package com.wipro.service.impl;

import com.wipro.entity.ConfigVersion;
import com.wipro.entity.Configuration;
import com.wipro.repository.ConfigVersionRepository;
import com.wipro.repository.ConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigurationServiceImplTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private ConfigVersionRepository configVersionRepository;

    @InjectMocks
    private ConfigurationServiceImpl configurationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addConfig() {
        Configuration configuration = new Configuration();
        configuration.setConfigData("Sample Data");
        when(configurationRepository.save(configuration)).thenReturn(configuration);

        Configuration result = configurationService.addConfig(configuration);

        assertNotNull(result);
        assertEquals("Sample Data", result.getConfigData());
        verify(configurationRepository, times(1)).save(configuration);
    }

    @Test
    void getAll() {
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        when(configurationRepository.findAll()).thenReturn(Arrays.asList(config1, config2));

        List<Configuration> result = configurationService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(configurationRepository, times(1)).findAll();
    }

    @Test
    void updateConfiguration() {
        Long id = 1L;
        Configuration existingConfiguration = new Configuration();
        existingConfiguration.setId(id);
        existingConfiguration.setConfigData("Old Data");
        existingConfiguration.setDeviceId("Device123");
        existingConfiguration.setVersion(1);
        existingConfiguration.setCreatedAt(LocalDateTime.now());

        Configuration newConfiguration = new Configuration();
        newConfiguration.setConfigData("New Data");
        newConfiguration.setDeviceId("Device123");

        when(configurationRepository.findById(id)).thenReturn(Optional.of(existingConfiguration));
        when(configurationRepository.save(any(Configuration.class))).thenReturn(newConfiguration);
        when(configVersionRepository.save(any(ConfigVersion.class))).thenReturn(new ConfigVersion());

        Configuration result = configurationService.updateConfiguration(newConfiguration, id);

        assertNotNull(result);
        assertEquals("New Data", result.getConfigData());
        verify(configurationRepository, times(1)).findById(id);
        verify(configVersionRepository, times(1)).save(any(ConfigVersion.class));
        verify(configurationRepository, times(1)).save(any(Configuration.class));
    }

    @Test
    void updateConfiguration_NotFound() {
        Long id = 1L;
        Configuration newConfiguration = new Configuration();
        newConfiguration.setConfigData("New Data");
        newConfiguration.setDeviceId("Device123");

        when(configurationRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            configurationService.updateConfiguration(newConfiguration, id);
        });

        assertEquals("Configuration not found for this id : " + id, exception.getMessage());
        verify(configurationRepository, times(1)).findById(id);
        verify(configVersionRepository, never()).save(any(ConfigVersion.class));
        verify(configurationRepository, never()).save(any(Configuration.class));
    }


    @Test
    void getConfigurations() {
        String deviceId = "Device123";
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        when(configurationRepository.findByDeviceId(deviceId)).thenReturn(Arrays.asList(config1, config2));

        List<Configuration> result = configurationService.getConfigurations(deviceId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(configurationRepository, times(1)).findByDeviceId(deviceId);
    }

    @Test
    void deleteConfig() {
        String deviceId = "Device123";
        ConfigVersion version1 = new ConfigVersion();
        ConfigVersion version2 = new ConfigVersion();
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();

        when(configVersionRepository.findByDeviceId(deviceId)).thenReturn(Arrays.asList(version1, version2));
        when(configurationRepository.findByDeviceId(deviceId)).thenReturn(Arrays.asList(config1, config2));

        configurationService.deleteConfig(deviceId);

        verify(configVersionRepository, times(1)).deleteAll(Arrays.asList(version1, version2));
        verify(configurationRepository, times(1)).deleteAll(Arrays.asList(config1, config2));
    }

    @Test
    void deleteConfig_ErrorTest() {
        String deviceId = "Device123";
        ConfigVersion version1 = new ConfigVersion();
        ConfigVersion version2 = new ConfigVersion();
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();

        when(configVersionRepository.findByDeviceId(deviceId)).thenReturn(Arrays.asList(version1, version2));
        when(configurationRepository.findByDeviceId(deviceId)).thenReturn(Arrays.asList(config1, config2));
        doThrow(RuntimeException.class).when(configurationRepository).deleteAll(anyList());

        configurationService.deleteConfig(deviceId);

        verify(configVersionRepository, times(1)).deleteAll(Arrays.asList(version1, version2));
        verify(configurationRepository, times(1)).deleteAll(Arrays.asList(config1, config2));
    }
}
