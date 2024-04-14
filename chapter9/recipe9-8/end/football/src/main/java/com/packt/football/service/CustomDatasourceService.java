package com.packt.football.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.stereotype.Service;

@Service
public class CustomDatasourceService {

    private DataSourceInitializationMode dataSourceInitializationMode;

    public CustomDatasourceService(@Value("${spring.sql.init.mode}") DataSourceInitializationMode dataSourceInitializationMode) {
        this.dataSourceInitializationMode = dataSourceInitializationMode;
    }

    public DataSourceInitializationMode getInitializationMode() {
        return dataSourceInitializationMode;
    }
}
