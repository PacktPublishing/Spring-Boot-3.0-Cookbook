package com.packt.football.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.stereotype.Service;

@Service
public class CustomDatasourceService {

    private DatabaseInitializationMode dataSourceInitializationMode;

    public CustomDatasourceService(@Value("${spring.sql.init.mode}") DatabaseInitializationMode dataSourceInitializationMode) {
        this.dataSourceInitializationMode = dataSourceInitializationMode;
    }

    public DatabaseInitializationMode getInitializationMode() {
        return dataSourceInitializationMode;
    }
}
