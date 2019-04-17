package org.prebid.server.health;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.prebid.server.health.model.Status;
import org.prebid.server.health.model.StatusResponse;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;

public class DatabaseChecker extends AbstractHealthCheck {

    private static final String NAME = "database";

    private final JDBCClient jdbcClient;

    private StatusResponse status;

    public DatabaseChecker(Vertx vertx, JDBCClient jdbcClient, long refreshPeriod) {
        super(vertx, refreshPeriod);
        this.jdbcClient = Objects.requireNonNull(jdbcClient);
    }

    @Override
    public StatusResponse status() {
        return status;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    void checkStatus() {
        final Future<SQLConnection> connectionFuture = Future.future();
        jdbcClient.getConnection(connectionFuture.completer());
        connectionFuture.setHandler(result ->
                status = StatusResponse.of(
                        result.succeeded() ? Status.UP.name() : Status.DOWN.name(),
                        ZonedDateTime.now(Clock.systemUTC())));
    }
}
