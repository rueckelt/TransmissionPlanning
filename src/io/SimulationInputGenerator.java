package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.text.AbstractDocument.LeafElement;

import schedulingModel.Flow;
import schedulingModel.Network;
import schedulingModel.Flow.FlowType;

/**
 * 
 * @author Jens Balze
 *
 */
public class SimulationInputGenerator {

	private int[][][] model_f_t_n;
	private boolean scheduled;
	private Vector<Network> networks;	// Can maybe removed because not used
	private Vector<Flow> flows;
	private String tcpAppSource = "model\\tcpAppString.dat";
	private String tcpEchoAppSource = "model\\tcpEchoAppString.dat";
	private String omnetppSource = "model\\omnetpp.ini";
	private String omnetppDest = "model\\generatedOmnetpp.ini";

	/**
	 * 
	 * @param output_f_t_n
	 *            Model contains the number of chunks send from an application
	 *            [f] over a network [n] to a time [t]
	 * @param networks
	 *            Vector contains all the networks for the simulation
	 * @param flows
	 *            Vector contains all the flows for the simulation
	 */
	public SimulationInputGenerator(int[][][] output_f_t_n, Vector<Network> networks, Vector<Flow> flows, boolean scheduled) {
		this.setModel_f_t_n(output_f_t_n);
		this.scheduled = scheduled;
		this.networks = networks;
		this.flows = flows;
	}

	public int[][][] getModel_f_t_n() {
		return model_f_t_n;
	}

	public void setModel_f_t_n(int[][][] model_f_t_n) {
		this.model_f_t_n = model_f_t_n;
	}

	public String getTcpAppSource() {
		return tcpAppSource;
	}

	/**
	 * 
	 * @param src
	 *            Set the source string where the template for the generated tcp
	 *            apps is stored
	 */
	public void setTcpAppSource(String src) {
		this.tcpAppSource = src;
	}

	/**
	 * Writes the omnetpp.ini file which you can load in omnetpp for simulation
	 */
	public void writeSimulationInputFile() {
		
		/**
		 * Generate for each flow one representing TCPSessionApp
		 */
		String apps = "**.numTcpApps = " + flows.size() + "\n";
		apps += "**.MA[*].tcpApp[*].typename = \"TCPSessionApp\" \n\n";
		for (int f = 0; f < model_f_t_n.length; ++f) {
			Flow flow = flows.elementAt(f);
			
			// Set the name, divisor and port depending on flowtype
			String name = flow.getFlowType().name();
			int divisor = 1;
			int port = 0;
			switch(flow.getFlowType()){
			case IPCALL:
				// Only upstream
				divisor = flow.getChunksMax() / flow.getChunksPerSlot();
				port = 1000;
				break;
			case BUFFERABLESTREAM:
				// Only upstream
				divisor = flow.getChunksMax() / flow.getChunksPerSlot();
				port = 1001;
				break;
			case USERREQUEST:
				// ~10% from downstream
				// 50.000 / 10 = 5.000 ~ 50kB/s
				divisor = 10;
				port = 1002;
				break;
			case UPDATE:
				// ~5 % from downstream
				// 1000 / 20 = 50
				divisor = 20;
				port = 1003;
				break;
			}
			
			apps += "#-----" + name + "-----\n";
			
			// Read string which contains the basic structure for a TCPSessionApp
			String tcpApp = "";
			// read tcp app string
			try {
				Scanner scanner = new Scanner(new File(tcpAppSource));
				tcpApp = scanner.useDelimiter("\\Z").next();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tcpApp += "\n\n";
			
			// replace all placeholder with the values from the flow
			tcpApp = tcpApp.replace("[tcpAppIndex]", String.valueOf(f));
			tcpApp = tcpApp.replace("[tcpAppConnectAddress]", "\"CN1[*]\"");
			tcpApp = tcpApp.replace("[tcpAppConnectPort]", String.valueOf(port));
			tcpApp = tcpApp.replace("[tcpAppStartTime]", String.valueOf((float) flow.getStartTime() / 10));

			// generate the sendScript: " <time> <sendBytes>; ... "
			String sendScript = "\" ";
			for (int t = 0; t < model_f_t_n[f].length; ++t) {
				int chunks = 0;
				for (int n = 0; n < model_f_t_n[f][t].length; ++n) {
					chunks += model_f_t_n[f][t][n];
				}
				if (chunks > 0) {
					sendScript += String.valueOf((float) t / 10) + " " + chunks / divisor + "; ";
				}
			}
			sendScript += "\" ";
			tcpApp = tcpApp.replace("[tcpAppSendScript]", sendScript);
			tcpApp = tcpApp.replace("[tcpAppDeadline]", String.valueOf((float) flow.getDeadline() / 10));
			apps += tcpApp;
		}
		
		/**
		 * Generate for each flowtype one server TCPEchoApp
		 */
		String server = "**.CN1[*].tcpApp[*].typename = \"TCPEchoApp\" \n";
		
		// set for each flowtype the server settings
		int i = 0;
		for (FlowType type : FlowType.values()) {
			int echoFactor = 1;
			String echoDelay = "0s"; 
			
			switch(type){
			case IPCALL:
				echoFactor = 1;
				echoDelay = "0s";
				break;
			case BUFFERABLESTREAM:
				//Only Downstream
				echoFactor = 51;
				echoDelay = "1s";
				break;
			case USERREQUEST:
				// 5 * 100kB/s ~ 500kB/s
				echoFactor = 5;
				echoDelay = "2s";
				break;
			case UPDATE:
				// 20 * 50kB/s ~ 1MB/s
				echoFactor = 20;
				echoDelay = "5s";
				break;
			}
			
			String tcpEchoApp = "";
			// read basic structure for a TCPEchoApp
			try {
				Scanner scanner = new Scanner(new File(tcpEchoAppSource));
				tcpEchoApp = scanner.useDelimiter("\\Z").next();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tcpEchoApp += "\n\n";
			
			// replace all placeholder with the flowtype values
			tcpEchoApp = tcpEchoApp.replace("[echoIndex]", String.valueOf(i));
			tcpEchoApp = tcpEchoApp.replace("[echoPort]", String.valueOf(1000 + i));
			tcpEchoApp = tcpEchoApp.replace("[echoFactor]", String.valueOf(echoFactor));
			tcpEchoApp = tcpEchoApp.replace("[echoDelay]", String.valueOf(echoDelay));
			
			server += tcpEchoApp;
			++i;
		}
		
		// read the basic simulation scenario structure
		String omnetpp ="";
		try {
			Scanner scanner = new Scanner(new File(omnetppSource));
			omnetpp = scanner.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// replace the configuration of tcp apps
		if(scheduled) omnetpp = omnetpp.replace("#[Insert_scheduled_tcp_apps_here]", apps);
		else omnetpp = omnetpp.replace("#[Insert_generated_tcp_apps_here]", apps);
		
		// insert the server
		omnetpp = omnetpp.replace("#[Insert_server_here]", server);
		
		// write generatedOmnetpp.ini file
		try {
			PrintWriter pw = new PrintWriter(omnetppDest);
			pw.println(omnetpp);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
