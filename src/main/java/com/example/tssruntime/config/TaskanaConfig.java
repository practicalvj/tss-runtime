package com.example.tssruntime.config;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pro.taskana.TaskanaConfiguration;
import pro.taskana.classification.api.ClassificationService;
import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.internal.SpringTaskanaEngine;
import pro.taskana.task.api.TaskService;
import pro.taskana.workbasket.api.WorkbasketService;

/** Class to set /load configuration for Taskana Library. */
@Configuration
@EnableTransactionManagement()
public class TaskanaConfig {

  @Value("${taskana.schema-name:TASKANA}")
  private String schemaName;

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  public DataSource dataSource(DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder().build();
  }

  @Bean
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
    transactionManager.setDataSource(dataSource);
    return transactionManager;
  }

  @Bean
  public TaskanaConfiguration taskanaConfiguration(DataSource dataSource) {
    return new TaskanaConfiguration.Builder(dataSource, true, schemaName, false)
        .initTaskanaProperties("/taskana.properties", "|")
        .build();
  }

  @Bean
  public TaskanaEngine taskanaEngine(TaskanaConfiguration taskanaConfiguration)
      throws SQLException {
    return SpringTaskanaEngine.buildTaskanaEngine(taskanaConfiguration);
  }

  @Bean
  public WorkbasketService workbasketService(TaskanaEngine taskanaEngine) {
    return taskanaEngine.getWorkbasketService();
  }

  @Bean
  public TaskService taskService(TaskanaEngine taskanaEngine) {
    return taskanaEngine.getTaskService();
  }

  @Bean
  public ClassificationService classificationService(TaskanaEngine taskanaEngine) {
    return taskanaEngine.getClassificationService();
  }
} 