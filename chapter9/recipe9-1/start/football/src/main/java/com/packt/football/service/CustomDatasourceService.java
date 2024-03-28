package com.packt.football.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class CustomDatasourceService {

    private DataSourceInitializationMode dataSourceInitializationMode;

    public CustomDatasourceService(@Value("${spring.datasource.initialization-mode}") DataSourceInitializationMode dataSourceInitializationMode) {
        this.dataSourceInitializationMode = dataSourceInitializationMode;
    }

    public DataSourceInitializationMode getInitializationMode() {
        return dataSourceInitializationMode;
    }
}
