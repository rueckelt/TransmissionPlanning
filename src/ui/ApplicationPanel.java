package ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import schedulingIOModel.Flow.FlowType;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;

import schedulingIOModel.Flow;

/**
 * 
 * @author Jens Balze
 *
 */
public class ApplicationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSpinner startTimeSpinner;
	private JSpinner durationSpinner;
	private JComboBox<FlowType> applicationTypeComboBox;

	/**
	 * Create the panel.
	 */
	public ApplicationPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 20, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel applicationLabel = new JLabel("Application");
		applicationLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblApplication = new GridBagConstraints();
		gbc_lblApplication.insets = new Insets(0, 0, 5, 5);
		gbc_lblApplication.gridx = 0;
		gbc_lblApplication.gridy = 0;
		add(applicationLabel, gbc_lblApplication);

		JLabel applicationTypeLabel = new JLabel("Application type:");
		GridBagConstraints gbc_lblApplicationType = new GridBagConstraints();
		gbc_lblApplicationType.insets = new Insets(0, 0, 5, 5);
		gbc_lblApplicationType.gridx = 0;
		gbc_lblApplicationType.gridy = 1;
		add(applicationTypeLabel, gbc_lblApplicationType);

		applicationTypeComboBox = new JComboBox<FlowType>();
		applicationTypeComboBox.setModel(new DefaultComboBoxModel<FlowType>(FlowType.values()));
		GridBagConstraints gbc_comboBoxApplicationType = new GridBagConstraints();
		gbc_comboBoxApplicationType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxApplicationType.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxApplicationType.gridx = 1;
		gbc_comboBoxApplicationType.gridy = 1;
		add(applicationTypeComboBox, gbc_comboBoxApplicationType);

		JLabel durationLabel = new JLabel("Duration");
		GridBagConstraints gbc_labelDuration = new GridBagConstraints();
		gbc_labelDuration.insets = new Insets(0, 0, 5, 5);
		gbc_labelDuration.gridx = 0;
		gbc_labelDuration.gridy = 2;
		add(durationLabel, gbc_labelDuration);

		durationSpinner = new JSpinner();
		durationSpinner.setModel(new SpinnerNumberModel(new Integer(30), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spinnerDuration = new GridBagConstraints();
		gbc_spinnerDuration.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerDuration.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerDuration.gridx = 1;
		gbc_spinnerDuration.gridy = 2;
		add(durationSpinner, gbc_spinnerDuration);
		
		JLabel durationUnitLabel = new JLabel("sec");
		GridBagConstraints gbc_durationUnitLabel = new GridBagConstraints();
		gbc_durationUnitLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_durationUnitLabel.insets = new Insets(0, 0, 5, 0);
		gbc_durationUnitLabel.gridx = 2;
		gbc_durationUnitLabel.gridy = 2;
		add(durationUnitLabel, gbc_durationUnitLabel);

		JLabel startTimeLabel = new JLabel("Start time");
		GridBagConstraints gbc_labelStartTime = new GridBagConstraints();
		gbc_labelStartTime.insets = new Insets(0, 0, 5, 5);
		gbc_labelStartTime.gridx = 0;
		gbc_labelStartTime.gridy = 3;
		add(startTimeLabel, gbc_labelStartTime);

		startTimeSpinner = new JSpinner();
		startTimeSpinner.setModel(new SpinnerNumberModel(new Integer(25), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spinnerStartTime = new GridBagConstraints();
		gbc_spinnerStartTime.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerStartTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerStartTime.gridx = 1;
		gbc_spinnerStartTime.gridy = 3;
		add(startTimeSpinner, gbc_spinnerStartTime);
		
		JLabel startTimeUnitLabel = new JLabel("sec");
		GridBagConstraints gbc_startTimeUnitLabel = new GridBagConstraints();
		gbc_startTimeUnitLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_startTimeUnitLabel.insets = new Insets(0, 0, 5, 0);
		gbc_startTimeUnitLabel.gridx = 2;
		gbc_startTimeUnitLabel.gridy = 3;
		add(startTimeUnitLabel, gbc_startTimeUnitLabel);
	}

	/**
	 * 
	 * @return The start time in time slots
	 */
	public int getStartTime() {
		return (int) startTimeSpinner.getValue() * 10;
	}

	/**
	 * 
	 * @return The duration in time slots
	 */
	public int getDuration() {
		return (int) durationSpinner.getValue() * 10;
	}

	/**
	 * 
	 * @return The enum type of flow
	 */
	public FlowType getFlowType() {
		return applicationTypeComboBox.getModel().getElementAt(applicationTypeComboBox.getSelectedIndex());
	}

	/**
	 * 
	 * @return The flow object created from user entries
	 */
	public Flow getFlow() {
		FlowType type = getFlowType();

		switch (type) {
		case IPCALL:
			return Flow.IPCall(getStartTime(), getStartTime() + getDuration());
		case BUFFERABLESTREAM:
			return Flow.BufferableStream(getStartTime(), getDuration());
		case USERREQUEST:
			return Flow.UserRequest(getStartTime(), getDuration());
		case UPDATE:
			// TODO set chunks = 15 * duration in slots?
			return Flow.Update(getStartTime(), getDuration());
		}

		return null;
	}
}
