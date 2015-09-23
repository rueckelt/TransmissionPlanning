package ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import schedulingIOModel.Flow.FlowType;

public class ApplicationPanel extends JPanel {
	private JTextField txtTest;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the panel.
	 */
	public ApplicationPanel() {
		setLayout(new GridLayout(0, 4, 0, 0));
		
		JLabel lblApplication = new JLabel("Application");
		add(lblApplication);
		
		JLabel label_4 = new JLabel("");
		add(label_4);
		
		JLabel label_5 = new JLabel("");
		add(label_5);
		
		JLabel label_6 = new JLabel("");
		add(label_6);
		
		JLabel lblApplicationType = new JLabel("Application type:");
		add(lblApplicationType);
		
		JComboBox comboBoxApplicationType = new JComboBox();
		comboBoxApplicationType.setModel(new DefaultComboBoxModel(FlowType.values()));
		add(comboBoxApplicationType);
		
		JLabel lblSomeProperties = new JLabel("Some properties");
		add(lblSomeProperties);
		
		txtTest = new JTextField();
		txtTest.setText("Test");
		add(txtTest);
		txtTest.setColumns(10);
		
		JLabel label = new JLabel("Some values");
		add(label);
		
		JSpinner spinner_1 = new JSpinner();
		add(spinner_1);
		
		JLabel label_2 = new JLabel("Some properties");
		add(label_2);
		
		textField = new JTextField();
		textField.setText("Test");
		textField.setColumns(10);
		add(textField);
		
		JLabel label_1 = new JLabel("Some values");
		add(label_1);
		
		JSpinner spinner_2 = new JSpinner();
		add(spinner_2);
		
		JLabel label_3 = new JLabel("Some properties");
		add(label_3);
		
		textField_1 = new JTextField();
		textField_1.setText("Test");
		textField_1.setColumns(10);
		add(textField_1);

	}

}
