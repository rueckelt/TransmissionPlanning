package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import io.SimulationInputGenerator;
import schedulers.OptimizationScheduler;
import schedulers.PriorityMatchScheduler;
import schedulers.PriorityScheduler;
import schedulers.Scheduler;
import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;

import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import toolSet.EvaluationScenarioCreator;

import ui.ApplicationPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;

/**
 * 
 * @author Jens Balze
 *
 */
public class Mainwindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogPath;
	private Vector<ApplicationPanel> applicationPanels;
	private Vector<NetworkPanel> networkPanels;
	private JScrollPane networksScrollPane;
	private JPanel networksPanel;
	private JPanel applicationsPanel;
	private JScrollPane applicationsScrollPane;

	/**
	 * Create the frame.
	 */
	public Mainwindow() {
		applicationPanels = new Vector<ApplicationPanel>();
		networkPanels = new Vector<NetworkPanel>();

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
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 23, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblLogPath = new JLabel("Log path");
		GridBagConstraints gbc_lblLogPath = new GridBagConstraints();
		gbc_lblLogPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogPath.gridx = 0;
		gbc_lblLogPath.gridy = 0;
		contentPane.add(lblLogPath, gbc_lblLogPath);

		txtLogPath = new JTextField();
		txtLogPath.setText("my_logs");
		GridBagConstraints gbc_txtLogPath = new GridBagConstraints();
		gbc_txtLogPath.insets = new Insets(0, 0, 5, 5);
		gbc_txtLogPath.gridx = 1;
		gbc_txtLogPath.gridy = 0;
		contentPane.add(txtLogPath, gbc_txtLogPath);
		txtLogPath.setColumns(10);

		JButton btnAddNetwork = new JButton("Add network");
		GridBagConstraints gbc_btnAddNetwork = new GridBagConstraints();
		gbc_btnAddNetwork.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddNetwork.gridx = 2;
		gbc_btnAddNetwork.gridy = 0;
		contentPane.add(btnAddNetwork, gbc_btnAddNetwork);
		btnAddNetwork.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				NetworkPanel network = new NetworkPanel();
				networkPanels.addElement(network);
				networksPanel.add(network);
				networksPanel.validate();
				networksPanel.repaint();
				networksScrollPane.updateUI();
			}

		});

		JButton btnAddApplication = new JButton("Add application");
		GridBagConstraints gbc_btnAddApplication = new GridBagConstraints();
		gbc_btnAddApplication.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddApplication.gridx = 3;
		gbc_btnAddApplication.gridy = 0;
		contentPane.add(btnAddApplication, gbc_btnAddApplication);
		btnAddApplication.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				ApplicationPanel app = new ApplicationPanel();
				applicationPanels.addElement(app);
				applicationsPanel.add(app);
				applicationsPanel.validate();
				applicationsPanel.repaint();
				applicationsPanel.updateUI();
			}

		});

		JButton generateUnscheduledModelButton = new JButton("Generate unscheduled Model");
		GridBagConstraints gbc_generateUnscheduledModelButton = new GridBagConstraints();
		gbc_generateUnscheduledModelButton.insets = new Insets(0, 0, 5, 5);
		gbc_generateUnscheduledModelButton.gridx = 4;
		gbc_generateUnscheduledModelButton.gridy = 0;
		contentPane.add(generateUnscheduledModelButton, gbc_generateUnscheduledModelButton);
		generateUnscheduledModelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				NetworkGenerator ng = getNetworkGenerator();
				FlowGenerator fg = getFlowGenerator();
				Vector<Network> networks = ng.getNetworks();
				Vector<Flow> flows = fg.getFlows();

				int model_f_t_n[][][] = new int[flows.size()][ng.getNofTimeSlots()][networks.size()];

				for (int n = 0; n < networks.size(); ++n) {
					Network network = networks.elementAt(n);
					boolean appFinished[] = new boolean[flows.size()];

					for (int f = 0; f < flows.size(); ++f) {
						if (appFinished[f] == true)
							continue;
						Flow flow = flows.elementAt(f);
						int chunksToSend = flow.getChunks();
						int sendChunks = 0;
						int chunksPerSlot = flow.getChunksPerSlot();
						System.out.println("Chunks: " + chunksToSend);
						// application will send chunks after there is no more
						// network open, than continue
						if (flow.getStartTime() > ng.getNofTimeSlots())
							continue;

						for (int t = flow.getStartTime(); t < ng.getNofTimeSlots(); ++t) {
							if (t > flow.getDeadline() || appFinished[f] == true)
								continue;

							if (sendChunks < chunksToSend) {
								model_f_t_n[f][t][n] = chunksPerSlot;
								sendChunks += chunksPerSlot;
							}
							if (sendChunks >= chunksToSend) {
								appFinished[f] = true;
							}

						}
						System.out.println("Send Chunks: " + sendChunks);
					}
				}

				SimulationInputGenerator sim = new SimulationInputGenerator(model_f_t_n, ng.getNetworks(),
						fg.getFlows(), "model\\generatedUnscheduledTcpApps.dat");
				sim.writeSimulationTcpApps();
				
				CostFunction cf = new CostFunction(ng, fg);
				System.out.println("Total cost unscheduled model: " + cf.costTotal(model_f_t_n));
			}
		});

		JButton btnGenerateModels = new JButton("Generate Model");
		GridBagConstraints gbc_btnGenerateModels = new GridBagConstraints();
		gbc_btnGenerateModels.insets = new Insets(0, 0, 5, 0);
		gbc_btnGenerateModels.gridx = 5;
		gbc_btnGenerateModels.gridy = 0;
		contentPane.add(btnGenerateModels, gbc_btnGenerateModels);
		btnGenerateModels.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				NetworkGenerator ng = getNetworkGenerator();
				FlowGenerator fg = getFlowGenerator();

				Vector<Scheduler> schedulers = initSchedulers(ng, fg);
				for (Scheduler scheduler : schedulers) {
					scheduler.calculateInstance(txtLogPath.getText() + File.separator);
					System.out.println("Start simulation input generator.");
					SimulationInputGenerator sim = new SimulationInputGenerator(scheduler.getSchedule(),
							ng.getNetworks(), fg.getFlows(), "model\\generatedScheduledTcpApps.dat");
					sim.writeSimulationTcpApps();
					CostFunction cf = new CostFunction(ng, fg);
					System.out.println("Total cost scheduled model: " + cf.costTotal(scheduler.getSchedule()));
				}
			}
		});

		networksScrollPane = new JScrollPane();
		GridBagConstraints gbc_networksScrollPane = new GridBagConstraints();
		gbc_networksScrollPane.gridwidth = 3;
		gbc_networksScrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_networksScrollPane.fill = GridBagConstraints.BOTH;
		gbc_networksScrollPane.gridx = 0;
		gbc_networksScrollPane.gridy = 1;
		contentPane.add(networksScrollPane, gbc_networksScrollPane);

		networksPanel = new JPanel();
		networksPanel.setLayout(new BoxLayout(networksPanel, BoxLayout.Y_AXIS));
		networksScrollPane.setViewportView(networksPanel);

		applicationsScrollPane = new JScrollPane();
		GridBagConstraints gbc_applicationsScrollPane = new GridBagConstraints();
		gbc_applicationsScrollPane.gridwidth = 3;
		gbc_applicationsScrollPane.fill = GridBagConstraints.BOTH;
		gbc_applicationsScrollPane.gridx = 3;
		gbc_applicationsScrollPane.gridy = 1;
		contentPane.add(applicationsScrollPane, gbc_applicationsScrollPane);

		applicationsPanel = new JPanel();
		applicationsScrollPane.setViewportView(applicationsPanel);
		applicationsPanel.setLayout(new BoxLayout(applicationsPanel, BoxLayout.Y_AXIS));

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmReset = new JMenuItem("Reset");
		mnEdit.add(mntmReset);
		mntmReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txtLogPath.setText("my_logs");
				networksPanel.removeAll();
				networksPanel.updateUI();
				applicationsPanel.removeAll();
				applicationsPanel.updateUI();
			}
		});
	}

	/**
	 * 
	 * @return The networkgenerator represented by the ui
	 */
	public NetworkGenerator getNetworkGenerator() {
		NetworkGenerator networkGenerator = new NetworkGenerator();
		for (NetworkPanel networkPanel : networkPanels) {
			Network network = networkPanel.getNetwork();
			if (network != null) {
				networkGenerator.addNetwork(network);
			} else {
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return networkGenerator;
	}

	/**
	 * 
	 * @return The flowgenerator represented by the ui
	 */
	public FlowGenerator getFlowGenerator() {
		FlowGenerator flowGenerator = new FlowGenerator();
		for (ApplicationPanel applicationPanel : applicationPanels) {
			Flow flow = applicationPanel.getFlow();
			if (flow != null) {
				flowGenerator.addFlow(flow);
			} else {
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return flowGenerator;
	}

	/**
	 * get list of all schedulers which shall calculate a schedule
	 * 
	 * @param ng
	 * @param fg
	 * @return list of schedulers
	 */
	private Vector<Scheduler> initSchedulers(NetworkGenerator ng, FlowGenerator fg) {
		Vector<Scheduler> schedulers = new Vector<Scheduler>();
		// schedulers.add(new RandomScheduler(ng, tg, 500)); //500 random runs of this scheduler. Returns average duration and cost
		//schedulers.add(new PriorityScheduler(ng, fg));
		schedulers.add(new PriorityMatchScheduler(ng, fg));
		// schedulers.add(new OptimizationScheduler(ng, tg));
		return schedulers;
	}
}
