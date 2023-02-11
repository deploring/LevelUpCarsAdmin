package au.levelupcars.admin.controller;

import au.levelupcars.admin.model.Request;
import au.levelupcars.admin.model.Settings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataController {

    private final DbController dbController;

    @Setter
    private Runnable onCurrentRequestChangedCallback;
    @Setter
    private Runnable onCurrentRequestUpdatedCallback;

    @Getter
    private final List<Request> requests;

    private int currentRequestIndex;

    public DataController(Settings settings) throws SQLException {
        dbController = new DbController(settings);
        requests = new ArrayList<>();
        currentRequestIndex = -1;
    }

    public void refreshRequests(boolean includeResponded) {
        try {
            requests.clear();
            onCurrentRequestChangedCallback.run();
            requests.addAll(dbController.getRequests(includeResponded));
            setCurrentRequestIndex(-1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Unable to refresh requests: " + e.getMessage());
        }
    }

    public Request getCurrentRequest() {
        if (currentRequestIndex == -1) return null;
        else return requests.get(currentRequestIndex);
    }

    public void setCurrentRequestIndex(int currentRequestIndex) {
        this.currentRequestIndex = currentRequestIndex;
        onCurrentRequestChangedCallback.run();
    }

    public void toggleCurrentRequestResponded() {
        Request request = getCurrentRequest();
        request.toggleResponded();
        try {
            dbController.updateRequest(request);
        } catch (SQLException e) {
            request.toggleResponded();
            JOptionPane.showMessageDialog(null, "Unable to update request: " + e.getMessage());
        }
        onCurrentRequestUpdatedCallback.run();
    }
}
