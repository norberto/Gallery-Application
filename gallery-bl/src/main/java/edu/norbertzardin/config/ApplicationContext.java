package edu.norbertzardin.config;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class ApplicationContext {

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        try {
            OracleDataSource dataSource = new OracleDataSource();

            dataSource.setURL(env.getProperty("database.url"));
            dataSource.setUser(env.getProperty("database.user"));
            dataSource.setPassword(env.getProperty("database.pass"));
            dataSource.setDatabaseName(env.getProperty("database.name"));
            return dataSource;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }


    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceUnitName("imagePersistence");

        return entityManagerFactoryBean;
    }
}
