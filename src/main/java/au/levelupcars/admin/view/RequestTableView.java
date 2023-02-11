package au.levelupcars.admin.view;

import au.levelupcars.admin.controller.DataController;
import au.levelupcars.admin.model.Request;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class RequestTableView extends JPanel {

    private final DataController dataController;

    public RequestTableView(DataController dataController) {
        this.dataController = dataController;
        setLayout(new BorderLayout());

        JTable table = new JTable();
        table.setModel(new RequestTableModel());
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(210);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(210);
        table.getSelectionModel().addListSelectionListener(event -> dataController.setCurrentRequestIndex(table.getSelectedRow()));

        dataController.setOnCurrentRequestUpdatedCallback(table::repaint);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(800, 200));

        JPanel controlsPanel = new JPanel();
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        BoxLayout controlsBoxLayout = new BoxLayout(controlsPanel, BoxLayout.X_AXIS);
        controlsPanel.setLayout(controlsBoxLayout);

        JPanel includeRespondedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton includeRespondedYesButton = new JRadioButton("Yes");
        includeRespondedYesButton.setSelected(true);
        JRadioButton includeRespondedNoButton = new JRadioButton("No");
        ButtonGroup includeRespondedButtonGroup = new ButtonGroup();
        includeRespondedButtonGroup.add(includeRespondedYesButton);
        includeRespondedButtonGroup.add(includeRespondedNoButton);
        includeRespondedPanel.add(includeRespondedYesButton);
        includeRespondedPanel.add(includeRespondedNoButton);
        includeRespondedPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Include responded requests?"
        ));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener((event) -> {
            dataController.refreshRequests(includeRespondedYesButton.isSelected());
            table.repaint();
            table.getSelectionModel().clearSelection();
        });

        controlsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlsPanel.add(includeRespondedPanel);
        controlsPanel.add(Box.createHorizontalGlue());
        controlsPanel.add(refreshButton);
        controlsPanel.add(Box.createHorizontalGlue());

        add(tableScrollPane, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.SOUTH);
    }

    private class RequestTableModel extends AbstractTableModel {

        public RequestTableModel() {
        }

        @Override
        public int getRowCount() {
            return dataController.getRequests().size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int column) {
            return switch (column) {
                case 0 -> "Request ID";
                case 1 -> "Request Time";
                case 2 -> "Responded";
                case 3 -> "Requested Date";
                default -> null;
            };
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Request request = dataController.getRequests().get(rowIndex);
            return switch (columnIndex) {
                case 0 -> request.getRequestID();
                case 1 -> request.getRequestTime();
                case 2 -> request.isResponded() ? "Yes" : "No";
                case 3 -> request.getRequestedDate();
                default -> throw new IllegalStateException(String.format("Unknown columnIndex %d", columnIndex));
            };
        }
    }
}
