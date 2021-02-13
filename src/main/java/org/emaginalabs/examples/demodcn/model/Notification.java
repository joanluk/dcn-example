package org.emaginalabs.examples.demodcn.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.ToString;

/**
 * @author Arquitectura
 */
@Builder
@ToString
public final class Notification implements Serializable {

    private final String table;
    private final String rowId;
    private final String operation;
}
