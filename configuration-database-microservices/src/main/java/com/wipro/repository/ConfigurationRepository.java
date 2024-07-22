package com.wipro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.entity.Configuration;

import java.util.List;

public interface ConfigurationRepository extends JpaRepository<Configuration,Long> {
    List<Configuration> findByDeviceId(String deviceId);
}
