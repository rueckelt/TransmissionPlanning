package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import toolSet.EvaluationScenarioCreator;

public class Mainwindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogPath;

	/**
	 * Create the frame.
	 */
	public Mainwindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 600);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		JMenuItem mntmCloseApplication = new JMenuItem("Close Application");
		mnMenu.add(mntmCloseApplication);
		mntmCloseApplication.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}

		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblTime = new JLabel("Time");
		contentPane.add(lblTime);

		final JSpinner spinnerTime = new JSpinner();
		spinnerTime.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		contentPane.add(spinnerTime);

		JLabel lblNetworks = new JLabel("Networks");
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
				int rep  = (int) spinnerRepetitions.getValue();
				String logpath = txtLogPath.getText();

				EvaluationScenarioCreator eval = new EvaluationScenarioCreator(time, nets, apps, rep, logpath);
				eval.evaluateAll();
			}

		});

		JButton btnReset = new JButton("Reset");
		contentPane.add(btnReset);
		btnReset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				spinnerTime.setValue(1);
				spinnerNetworks.setValue(1);
				spinnerApplications.setValue(1);
				spinnerRepetitions.setValue(1);
				txtLogPath.setText("my_logs"+File.separator+"t0");
			}

		});
	}

}
