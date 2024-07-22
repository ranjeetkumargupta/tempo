package com.wipro.service;

import java.util.List;

import com.wipro.entity.Configuration;

public interface ConfigurationService {

    Configuration addConfig(Configuration configuration);

    List<Configuration> getAll();

    Configuration updateConfiguration(Configuration configuration, Long id);

    List<Configuration> getConfigurations(String deviceId);

    void deleteConfig(String deviceId);
}
