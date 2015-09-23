package ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import schedulingIOModel.Network.NetworkType;

public class NetworkPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public NetworkPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNetwork = new JLabel("Network");
		GridBagConstraints gbc_lblNetwork = new GridBagConstraints();
		gbc_lblNetwork.insets = new Insets(0, 0, 5, 5);
		gbc_lblNetwork.gridx = 0;
		gbc_lblNetwork.gridy = 0;
		add(lblNetwork, gbc_lblNetwork);
		
		JLabel lblNetworkType = new JLabel("Network type");
		GridBagConstraints gbc_lblNetworkType = new GridBagConstraints();
		gbc_lblNetworkType.insets = new Insets(0, 0, 0, 5);
		gbc_lblNetworkType.anchor = GridBagConstraints.EAST;
		gbc_lblNetworkType.gridx = 0;
		gbc_lblNetworkType.gridy = 1;
		add(lblNetworkType, gbc_lblNetworkType);
		
		JComboBox comboBoxNetworkType = new JComboBox();
		comboBoxNetworkType.setModel(new DefaultComboBoxModel(NetworkType.values()));
		GridBagConstraints gbc_comboBoxNetworkType = new GridBagConstraints();
		gbc_comboBoxNetworkType.gridx = 1;
		gbc_comboBoxNetworkType.gridy = 1;
		add(comboBoxNetworkType, gbc_comboBoxNetworkType);

	}

}
