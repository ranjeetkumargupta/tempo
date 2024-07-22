package com.wipro.controller;

import com.wipro.entity.ConfigVersion;
import com.wipro.entity.Configuration;
import com.wipro.service.ConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    
    @PostMapping(value = "/add")
    public ResponseEntity<Configuration> addConfiguration(@RequestBody Configuration configuration) {
        final Configuration savedConfiguration = configurationService.addConfig(configuration);
        return new ResponseEntity<>(savedConfiguration, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Configuration>> getAllConfiguration() {
        final List<Configuration> configurations = configurationService.getAll();
        return new ResponseEntity<>(configurations, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Configuration> updateConfiguration(@RequestBody Configuration configuration,
                                                             @PathVariable Long id) {
        final Configuration updatedConfiguration = configurationService.updateConfiguration(configuration, id);
        return new ResponseEntity<>(updatedConfiguration, HttpStatus.OK);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<List<Configuration>> getConfiguration(@PathVariable String deviceId) {
        List<Configuration> configurations = configurationService.getConfigurations(deviceId);
        return new ResponseEntity<>(configurations, HttpStatus.OK);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable String deviceId) {
        configurationService.deleteConfig(deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
