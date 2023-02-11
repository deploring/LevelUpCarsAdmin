package au.levelupcars.admin.view;

import au.levelupcars.admin.controller.DataController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class MainView extends JFrame {

    public MainView(DataController dataController) {
        super("Level Up! Cars - Admin Console");
        AffineTransform at = getGraphicsConfiguration().getDefaultTransform();
        setMinimumSize(new Dimension((int) (800 * at.getScaleX()), (int) (600 * at.getScaleY())));
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        // Move to center of screen.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
            dim.width / 2 - getSize().width / 2,
            dim.height / 2 - getSize().height / 2
        );
        setLayout(new BorderLayout());
        add(new RequestTableView(dataController), BorderLayout.CENTER);
        add(new RequestDetailsView(dataController), BorderLayout.SOUTH);
    }
}
