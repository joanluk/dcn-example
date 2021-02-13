package org.emaginalabs.examples.demodcn.dcn.config;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeRegistration;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DcnRegister {

    public static final String SELECT_FROM_NOTIFICATIONS = "select * from notifications";
    private DataSource dataSource;

    private DCNListener listener;

    private OracleConnection oracleConnection = null;
    @Getter
    private DatabaseChangeRegistration dcr = null;
    private Statement statement = null;


    @Autowired
    public DcnRegister(DataSource dataSource, DCNListener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }


    @PostConstruct
    public void init() {
        this.register();
    }


    private void register() {

        Properties props = new Properties();
        props.put(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
        props.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true");
        props.setProperty(OracleConnection.DCN_BEST_EFFORT,"true");
        try {

            oracleConnection = (OracleConnection) dataSource.getConnection();

            dcr = oracleConnection.registerDatabaseChangeNotification(props);
            statement = oracleConnection.createStatement();
            ((OracleStatement) statement).setDatabaseChangeRegistration(dcr);

            statement.executeQuery(SELECT_FROM_NOTIFICATIONS).close();

            dcr.addListener(listener);

            String[] tableNames = dcr.getTables();
            Arrays.stream(tableNames)
                    .forEach(i -> log.info("Table {}" + " registered.", i));

            listener.setDatabaseChangeRegistration(dcr);

        } catch (SQLException e) {
            log.error("Error registering dcn", e);
        }


    }

    @PreDestroy
    public void unregister() throws SQLException {
        log.info("Unregistering dcn with regID {}", dcr.getRegId());
        oracleConnection = (OracleConnection) dataSource.getConnection();

        try {
            oracleConnection.unregisterDatabaseChangeNotification(dcr);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}