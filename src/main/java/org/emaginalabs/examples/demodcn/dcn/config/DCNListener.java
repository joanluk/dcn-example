package org.emaginalabs.examples.demodcn.dcn.config;

import org.emaginalabs.examples.demodcn.events.NotificationEvent;
import org.emaginalabs.examples.demodcn.model.Notification;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.dcn.QueryChangeDescription;
import oracle.jdbc.dcn.RowChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class DCNListener implements DatabaseChangeListener {

    @Setter
    @NonNull
    private DatabaseChangeRegistration databaseChangeRegistration;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onDatabaseChangeNotification(DatabaseChangeEvent databaseChangeEvent) {
        if (databaseChangeEvent.getRegId() == databaseChangeRegistration.getRegId()) {
            log.info("Database change event received for table {}", databaseChangeEvent.getDatabaseName());
            QueryChangeDescription[] queryChanges = databaseChangeEvent.getQueryChangeDescription();
            if (queryChanges != null) {

                for (QueryChangeDescription queryChange : queryChanges) {

                    TableChangeDescription[] tcds = queryChange.getTableChangeDescription();
                    for (TableChangeDescription tableChange: tcds) {
                        RowChangeDescription[] rcds = tableChange.getRowChangeDescription();

                        for (RowChangeDescription rcd : rcds) {
                            log.info("Registration information changed with rowid {} and type operation {}", rcd.getRowid(), rcd.getRowOperation().name());
                            Notification notification = Notification.builder().table(tableChange.getTableName())
                                    .operation(rcd.getRowOperation().name())
                                    .rowId(rcd.getRowid().stringValue()).build();
                            applicationEventPublisher.publishEvent(new NotificationEvent(this, notification));
                        }

                    }
                }
            }
        }
    }
}