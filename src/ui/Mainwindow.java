package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import schedulingIOModel.Network;

import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import toolSet.EvaluationScenarioCreator;
import javax.swing.AbstractAction;
import javax.swing.Action;

import ui.ApplicationPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

public class Mainwindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogPath;
	private Vector<ApplicationPanel> applications;
	private Vector<NetworkPanel> networks;

	/**
	 * Create the frame.
	 */
	public Mainwindow() {
		applications = new Vector<ApplicationPanel>();
		networks = new Vector<NetworkPanel>();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1002, 606);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		JMenuItem mntmCloseApplication = new JMenuItem("Close Application");
		mnMenu.add(mntmCloseApplication);
		mntmCloseApplication.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}

		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblTime = new JLabel("Time");
		contentPane.add(lblTime);

		final JSpinner spinnerTime = new JSpinner();
		spinnerTime.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		contentPane.add(spinnerTime);

		JLabel lblNetworks = new JLabel("Number of networks");
		contentPane.add(lblNetworks);

		final JSpinner spinnerNetworks = new JSpinner();
		spinnerNetworks.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		contentPane.add(spinnerNetworks);

		JLabel lblApplications = new JLabel("Applications");
		contentPane.add(lblApplications);

		final JSpinner spinnerApplications = new JSpinner();
		spinnerApplications.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		contentPane.add(spinnerApplications);

		JLabel lblRepetitions = new JLabel("Repetitions");
		contentPane.add(lblRepetitions);

		final JSpinner spinnerRepetitions = new JSpinner();
		spinnerRepetitions.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		contentPane.add(spinnerRepetitions);

		JLabel lblLogPath = new JLabel("Log path");
		contentPane.add(lblLogPath);

		txtLogPath = new JTextField();
		txtLogPath.setText("my_logs");
		contentPane.add(txtLogPath);
		txtLogPath.setColumns(10);

		JButton btnGenerateModels = new JButton("Generate Models");
		contentPane.add(btnGenerateModels);
		btnGenerateModels.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				int time = (int) spinnerTime.getValue();
				int nets = (int) spinnerNetworks.getValue();
				int apps = (int) spinnerApplications.getValue();
				int rep = (int) spinnerRepetitions.getValue();
				String logpath = txtLogPath.getText();

				EvaluationScenarioCreator eval = new EvaluationScenarioCreator(time, nets, apps, rep, logpath);
				eval.evaluateAll();
			}

		});

		JButton btnAddApplication = new JButton("Add application");
		contentPane.add(btnAddApplication);
		btnAddApplication.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				ApplicationPanel app = new ApplicationPanel();
				applications.addElement(app);
				contentPane.add(app);
				contentPane.validate();
				contentPane.repaint();
			}

		});
		
		JButton btnAddNetwork = new JButton("Add network");
		contentPane.add(btnAddNetwork);
		btnAddNetwork.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				NetworkPanel network = new NetworkPanel();
				networks.addElement(network);
				contentPane.add(network);
				contentPane.validate();
				contentPane.repaint();
			}
			
		});

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmReset = new JMenuItem("Reset");
		mnEdit.add(mntmReset);
		mntmReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				spinnerTime.setValue(1);
				spinnerNetworks.setValue(1);
				spinnerApplications.setValue(1);
				spinnerRepetitions.setValue(1);
				txtLogPath.setText("my_logs" + File.separator + "t0");
				applications.clear();
			}
		});
	}
}
