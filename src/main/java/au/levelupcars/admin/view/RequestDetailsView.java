package au.levelupcars.admin.view;

import au.levelupcars.admin.controller.DataController;
import au.levelupcars.admin.model.Request;

import javax.swing.*;
import java.awt.*;

public class RequestDetailsView extends JPanel {

    private final DataController dataController;

    private final JTextField clientNameField;
    private final JTextField clientEmailField;
    private final JTextField clientPhoneField;
    private final JTextField carModelField;
    private final JTextField postCodeField;
    private final JTextField requestedDateField;
    private final JTextArea detailsField;
    private final JComboBox<String> serviceDetailsField;
    private final JButton toggleRespondedButton;

    public RequestDetailsView(DataController dataController) {
        this.dataController = dataController;

        dataController.setOnCurrentRequestChangedCallback(this::setFields);

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        clientNameField = createField();
        clientEmailField = createField();
        clientPhoneField = createField();
        carModelField = createField();
        postCodeField = createField();
        requestedDateField = createField();
        detailsField = new JTextArea(5, 0);
        detailsField.setEnabled(false);
        detailsField.setBorder(clientNameField.getBorder());
        serviceDetailsField = new JComboBox<>();

        toggleRespondedButton = new JButton("Toggle Responded");
        toggleRespondedButton.setEnabled(false);
        toggleRespondedButton.addActionListener((event) -> dataController.toggleCurrentRequestResponded());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(toggleRespondedButton);

        JSeparator topSeparator = new JSeparator();
        topSeparator.setBackground(Color.GRAY);

        add(createFormRowPanel(new JComponent[]{topSeparator}, new double[]{1.0}));
        add(createFormRowPanel(
            new JComponent[]{
                createLabelledPanel(clientNameField, "Client Name"),
                createLabelledPanel(clientEmailField, "Client Email"),
                createLabelledPanel(clientPhoneField, "Client Phone")
            },
            new double[]{0.33, 0.33, 0.33}
        ));
        add(createFormRowPanel(
            new JComponent[]{
                createLabelledPanel(carModelField, "Car Model"),
                createLabelledPanel(postCodeField, "Postcode"),
                createLabelledPanel(requestedDateField, "Requested Date")
            },
            new double[]{0.6, 0.15, 0.25}
        ));
        add(createFormRowPanel(
            new JComponent[]{
                createLabelledPanel(serviceDetailsField, "Requested Services"),
                buttonPanel
            },
            new double[]{0.5, 0.5}
        ));
        add(createFormRowPanel(
            new JComponent[]{createLabelledPanel(detailsField, "Additional Details", BorderLayout.PAGE_START)},
            new double[]{1.0}
        ));
    }

    private JTextField createField() {
        JTextField result = new JTextField();
        result.setEnabled(false);
        result.setBackground(Color.WHITE);
        return result;
    }

    private JPanel createLabelledPanel(JComponent component, String labelText) {
        return createLabelledPanel(component, labelText, BorderLayout.WEST);
    }

    private JPanel createLabelledPanel(
        JComponent component,
        String labelText,
        String labelPosition
    ) {
        JPanel result = new JPanel(new BorderLayout());
        result.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel label = new JLabel(String.format("%s: ", labelText));
        label.setLabelFor(component);
        result.add(label, labelPosition);
        result.add(component, BorderLayout.CENTER);
        return result;
    }

    private JPanel createFormRowPanel(JComponent[] content, double[] weights) {
        JPanel result = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        for (int i = 0; i < content.length; i++) {
            gbc.gridx = i;
            gbc.weightx = weights[i];
            result.add(content[i], gbc);
        }
        return result;
    }

    private void clearFields() {
        clientNameField.setText("");
        clientEmailField.setText("");
        clientPhoneField.setText("");
        carModelField.setText("");
        postCodeField.setText("");
        requestedDateField.setText("");
        detailsField.setText("");
        serviceDetailsField.removeAllItems();
        toggleRespondedButton.setEnabled(false);
    }

    private void setFields() {
        Request request = dataController.getCurrentRequest();
        if (request == null) {
            clearFields();
            return;
        }
        clientNameField.setText(request.getClientName());
        clientEmailField.setText(request.getClientEmail());
        clientPhoneField.setText(request.getClientPhone());
        carModelField.setText(request.getCarModel());
        postCodeField.setText(request.getPostCode());
        requestedDateField.setText(request.getRequestedDate());
        detailsField.setText(request.getDetails());
        serviceDetailsField.removeAllItems();
        for (String serviceDetail : request.getServiceDetails()) {
            serviceDetailsField.addItem(serviceDetail);
        }
        toggleRespondedButton.setEnabled(true);
    }
}
