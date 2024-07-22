package com.wipro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.entity.ConfigVersion;
import com.wipro.entity.Configuration;

import java.util.List;

public interface ConfigVersionRepository extends JpaRepository<ConfigVersion, Long> {
    List<ConfigVersion> findByDeviceId(String deviceId);
}
