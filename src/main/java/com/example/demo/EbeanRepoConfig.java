/*
 *
 * Copyright © 2018 Webvalto Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo;

import com.example.demo.entities.TestPosition;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.spring.txn.SpringJdbcTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class EbeanRepoConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Primary
    public ServerConfig defaultEbeanServerConfig(DataSource dataSource) {
        ServerConfig config = new ServerConfig();

        config.setDataSource(dataSource);
        config.setExternalTransactionManager(new SpringJdbcTransactionManager());

        config.loadFromProperties();
        config.addPackage("com.example");
        config.setDefaultServer(true);
        config.setRegister(true);
        config.setAutoCommitMode(false);
        config.setExpressionNativeIlike(true);
        config.setDatabaseBooleanFalse("0");
        config.setDatabaseBooleanTrue("1");

        return config;
    }

    @Bean
    @Primary
    public EbeanServer defaultEbeanServer(ServerConfig defaultEbeanServerConfig) {
        return EbeanServerFactory.create(defaultEbeanServerConfig);
    }


}
