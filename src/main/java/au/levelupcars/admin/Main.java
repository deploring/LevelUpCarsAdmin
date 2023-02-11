package au.levelupcars.admin;

import au.levelupcars.admin.controller.DataController;
import au.levelupcars.admin.model.Settings;
import au.levelupcars.admin.view.MainView;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        Settings settings;
        try {
            settings = new Settings();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, String.format("Could not load settings file: %s", e.getMessage()));
            return;
        }

        DataController dataController;
        try {
            dataController = new DataController(settings);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, String.format("Encountered database error: %s", e.getMessage()));
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.put("TextField.inactiveBackground", new ColorUIResource(new Color(0, 0, 0)));
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }

            new MainView(dataController);
        });
    }
}
