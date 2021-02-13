package org.emaginalabs.examples.demodcn;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.emaginalabs.examples.demodcn.dcn.config.DCNListener;
import org.emaginalabs.examples.demodcn.dcn.config.DcnRegister;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.pool.OracleDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class DemoDcnApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoDcnApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Executing app with listener to Oracle databse ...");
    }


    @Bean(destroyMethod = "close")
    public OracleDataSource dataSource(@Value("${dcn.datasource.url}") String url,
                                       @Value("${dcn.datasource.user}") String user,
                                       @Value("${dcn.datasource.password}") String password) {
        OracleDataSource dataSource = null;
        try {
            dataSource = new OracleDataSource();
            dataSource.setURL(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);

        } catch (SQLException e) {
            log.error("There was an error while creating the datasource " + e.getMessage());
        }
        return dataSource;
    }

    @Bean
    public DCNListener dcnListener() {
        return new DCNListener();
    }

    @Bean(destroyMethod = "unregister")
    public DcnRegister register(DataSource dataSource, DCNListener listener) {
        return new DcnRegister(dataSource, listener);
    }

}
