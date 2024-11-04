package za.co.oldmutual.asisa;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "za.co.oldmutual.asisa")
public class DatasourceConfig {

  HikariDataSource dataSource;

  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("spring.datasource")
  public HikariDataSource dataSource() {
    this.dataSource =
        dataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    return this.dataSource;
  }

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
    return new NamedParameterJdbcTemplate(dataSource());
  }

}
