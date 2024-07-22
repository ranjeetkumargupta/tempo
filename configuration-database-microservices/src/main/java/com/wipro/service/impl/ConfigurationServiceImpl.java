package com.wipro.service.impl;

import com.wipro.entity.ConfigVersion;
import com.wipro.entity.Configuration;
import com.wipro.repository.ConfigVersionRepository;
import com.wipro.repository.ConfigurationRepository;
import com.wipro.service.ConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {


    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ConfigVersionRepository configVersionRepository;

    @Override
    public Configuration addConfig(Configuration configuration) {
        return configurationRepository.save(configuration);

    }

    @Override
    public List<Configuration> getAll() {
        return configurationRepository.findAll();
    }

    @Override
    public Configuration updateConfiguration(Configuration configuration, Long id) {
        //  finding old configuration using id
        Optional<Configuration> configurationOptional = configurationRepository.findById(id);

        if (configurationOptional.isPresent()) {
            Configuration currentConfiguration = configurationOptional.get();
            //  Storing old configuration into ConfigVersion table
            ConfigVersion configVersion = new ConfigVersion();
            configVersion.setConfiguration(currentConfiguration);
            configVersion.setConfigData(currentConfiguration.getConfigData());
            configVersion.setDeviceId(currentConfiguration.getDeviceId());
            configVersion.setVersion(currentConfiguration.getVersion());
            configVersion.setCreatedAt(currentConfiguration.getCreatedAt());
            configVersionRepository.save(configVersion);

            // storing new configuration coming from controller into configuration table
            Configuration newConfiguration = new Configuration();
            newConfiguration.setId(currentConfiguration.getId());
            newConfiguration.setConfigData(configuration.getConfigData());
            newConfiguration.setVersion(currentConfiguration.getVersion() + 1);
            newConfiguration.setDeviceId(configuration.getDeviceId());
            newConfiguration.setCreatedAt(LocalDateTime.now());
            return configurationRepository.save(newConfiguration);


        } else {
            throw new RuntimeException("Configuration not found for this id : " + id);
        }
    }

    @Override
    public List<Configuration> getConfigurations(String deviceId) {
        return configurationRepository.findByDeviceId(deviceId);
    }

    @Override
    public void deleteConfig(String deviceId) {
        final List<ConfigVersion> configVersionList = configVersionRepository.findByDeviceId(deviceId);
        final List<Configuration> configurationList = configurationRepository.findByDeviceId(deviceId);
        try {
            configVersionRepository.deleteAll(configVersionList);
            configurationRepository.deleteAll(configurationList);
        } catch (Exception ex) {
            System.out.println("Error Occurred while deletion :: " + ex.getMessage());
        }

    }

}
