package au.levelupcars.admin.controller;

import au.levelupcars.admin.model.Request;
import au.levelupcars.admin.model.Settings;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DbController {

    private final Connection db;

    public DbController(Settings settings) throws SQLException {
        db = DriverManager.getConnection(
            String.format("jdbc:postgresql://%s:%s/%s", settings.getHost(), settings.getPort(), settings.getDatabase()),
            settings.getUsername(),
            settings.getPassword()
        );
    }

    private ResultSet runQuery(String sql) throws SQLException {
        Statement statement = db.createStatement();
        return statement.executeQuery(sql);
    }

    public List<Request> getRequests(boolean includeResponded) throws SQLException {
        String whereClause = includeResponded ? "" : " WHERE NOT Request.Responded ";
        String sql = " SELECT " +
            "   Request.*, " +
            "   array_agg(Service.ServiceName || coalesce(' for ' || RequestedServices.CarType, '')) AS ServiceDetails " +
            " FROM " +
            "   Request " +
            "   INNER JOIN RequestedServices USING (RequestID) " +
            "   INNER JOIN Service USING (ServiceID) " +
            whereClause +
            " GROUP BY " +
            "   Request.RequestID " +
            " ORDER BY " +
            "   Request.RequestedDate ASC ";
        List<Request> result = new ArrayList<>();
        try (ResultSet requestsSet = runQuery(sql)) {
            while (requestsSet.next()) {
                result.add(new Request(
                    requestsSet.getObject("RequestID", UUID.class),
                    new Date(requestsSet.getTimestamp("RequestTime").getTime()),
                    requestsSet.getBoolean("Responded"),
                    requestsSet.getString("ClientName"),
                    requestsSet.getString("ClientEmail"),
                    requestsSet.getString("ClientPhone"),
                    requestsSet.getString("PostCode"),
                    requestsSet.getDate("RequestedDate"),
                    requestsSet.getString("CarModel"),
                    requestsSet.getString("Details"),
                    (String[]) requestsSet.getArray("ServiceDetails").getArray()
                ));
            }
        }
        return result;
    }

    public void updateRequest(Request request) throws SQLException {
        String sql = " UPDATE " +
            "   Request " +
            " SET " +
            "   Responded = ? " +
            " WHERE " +
            "   RequestID = ? ";
        PreparedStatement statement = db.prepareStatement(sql);
        statement.setBoolean(1, request.isResponded());
        statement.setObject(2, request.getRequestID());
        statement.execute();
    }
}
