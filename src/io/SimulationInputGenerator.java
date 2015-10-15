package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.text.AbstractDocument.LeafElement;

import schedulingIOModel.Flow;
import schedulingIOModel.Flow.FlowType;
import schedulingIOModel.Network;

/**
 * 
 * @author Jens Balze
 *
 */
public class SimulationInputGenerator {

	private int[][][] model_f_t_n;
	private Vector<Network> networks;
	private Vector<Flow> flows;
	private String tcpEchoServerName = "\"TCPEchoApp\"";
	private String tcpAppSource = "model\\tcpAppString.dat";
	private String tcpEchoAppSource = "model\\tcpEchoAppString.dat";
	private String dest;

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
	public SimulationInputGenerator(int[][][] output_f_t_n, Vector<Network> networks, Vector<Flow> flows, String dest) {
		this.setModel_f_t_n(output_f_t_n);
		this.networks = networks;
		this.flows = flows;
		this.dest = dest;
		this.writeSimulationTcpApps();
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

	public String getDest() {
		return dest;
	}

	/**
	 * 
	 * @param dest
	 *            Set the destination string where the generated tcp apps should
	 *            be stored
	 */
	public void setDest(String dest) {
		this.dest = dest;
	}

	/**
	 * For each application generate an output string for the simulation tool
	 */
	public void writeSimulationTcpApps() {
		System.out.print("Write tcp apps");
		// numTcpApps = 1 for one server
		String content = "# TCP apps \n";
		content += "**.cli[*].numTcpApps = 1\n\n";

		for (int f = 0; f < model_f_t_n.length; ++f) {
			Flow flow = flows.elementAt(f);
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
			tcpApp = tcpApp.replace("[tcpAppIndex]", String.valueOf(f));
			tcpApp = tcpApp.replace("[tcpAppName]", f + "_" + flow.getFlowType().toString());

			tcpApp = tcpApp.replace("[tcpAppConnectAddress]", tcpEchoServerName);
			tcpApp = tcpApp.replace("[tcpAppStartTime]", String.valueOf((float) flow.getStartTime() / 10));

			// Chunk size can be about 10kb --> 1MiB ~ 100 Chunks
			System.out.println((float) flow.getChunks() / 100);
			tcpApp = tcpApp.replace("[tcpAppSendBytes]", String.valueOf((float) flow.getChunks() / 100));

			String sendScript = "\" ";
			for (int t = 0; t < model_f_t_n[f].length; ++t) {
				int chunks = 0;
				for (int n = 0; n < model_f_t_n[f][t].length; ++n) {
					chunks += model_f_t_n[f][t][n];
				}
				if (chunks > 0) {
					sendScript += String.valueOf((float) t / 10) + " " + chunks + "; ";
				}
			}
			sendScript += "\" ";
			tcpApp = tcpApp.replace("[tcpAppSendScript]", sendScript);
			tcpApp = tcpApp.replace("[tcpAppDeadline]", String.valueOf((float) flow.getDeadline() / 10));
			content += tcpApp;
		}
		
		content += "# Echo server \n";
		content += "**.server.tcpApp[*].typename = " + tcpEchoServerName + " \n";
		int i = 0;
		for (FlowType type : FlowType.values()) {
			int echoFactor = 1;
			int echoDelay = 0; 
			
			switch(type){
			case IPCALL:
				echoFactor = 1;
				echoDelay = 0;
				break;
			case BUFFERABLESTREAM:
				echoFactor = 3;
				echoDelay = 1;
				break;
			case USERREQUEST:
				echoFactor = 10;
				echoDelay = 2;
				break;
			case UPDATE:
				echoFactor = 100;
				echoDelay = 5;
				break;
			}
			
			String tcpEchoApp = "";
			// read tcp app string
			try {
				Scanner scanner = new Scanner(new File(tcpEchoAppSource));
				tcpEchoApp = scanner.useDelimiter("\\Z").next();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tcpEchoApp += "\n\n";
			tcpEchoApp = tcpEchoApp.replace("[echoIndex]", String.valueOf(i));
			tcpEchoApp = tcpEchoApp.replace("[echoPort]", String.valueOf(1000 + i));
			tcpEchoApp = tcpEchoApp.replace("[echoFactor]", String.valueOf(echoFactor));
			tcpEchoApp = tcpEchoApp.replace("[echoDelay]", String.valueOf(echoDelay));
			
			content += tcpEchoApp;
			++i;
		}

		// write file
		try {
			PrintWriter pw = new PrintWriter(dest);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished writing tcp apps");
	}
}
