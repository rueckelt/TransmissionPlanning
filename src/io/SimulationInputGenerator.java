package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.text.AbstractDocument.LeafElement;

import schedulingIOModel.Flow;
import schedulingIOModel.Network;

/**
 * 
 * @author Jens Balze
 *
 */
public class SimulationInputGenerator {

	private int[][][] model_f_t_n;
	private Vector<Flow> flows;
	private String source = "model\\tcpAppString.dat";
	private String dest;

	/**
	 * 
	 * @param output_f_t_n Model contains the number of chunks send from an application [f] over a network [n] to a time [t]
	 * @param networks Vector contains all the networks for the simulation
	 * @param flows Vector contains all the flows for the simulation
	 */
	public SimulationInputGenerator(int[][][] output_f_t_n, Vector<Network> networks, Vector<Flow> flows, String dest) {
		this.setModel_f_t_n(output_f_t_n);
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

	public String getSource() {
		return source;
	}

	/**
	 * 
	 * @param src Set the source string where the template for the generated tcp apps is stored
	 */
	public void setSource(String src) {
		this.source = src;
	}

	public String getDest() {
		return dest;
	}

	/**
	 * 
	 * @param dest Set the destination string where the generated tcp apps should be stored
	 */
	public void setDest(String dest) {
		this.dest = dest;
	}

	/**
	 * For each application generate an output string for the simulation tool
	 */
	public void writeSimulationTcpApps() {
		System.out.print("Write tcp apps");
		String content = "**.cli[*].numTcpApps = " + model_f_t_n.length + "\n\n";

		for (int f = 0; f < model_f_t_n.length; ++f) {
			Flow flow = flows.elementAt(f);
			System.out.println("<<<<< Flow deadline: " + flows.elementAt(f).getDeadline() + "; Model time length: " + model_f_t_n[f].length);
			String tcpApp = "";
			// read tcp app string
			try {
				Scanner scanner = new Scanner(new File(source));
				tcpApp = scanner.useDelimiter("\\Z").next();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tcpApp += "\n\n";
			tcpApp = tcpApp.replace("[tcpAppIndex]", String.valueOf(f));
			tcpApp = tcpApp.replace("[tcpAppName]", f + "_" + flow.getFlowType().toString());

			// TODO: Networkadress??
			// content.replace("[tcpAppConnectAddress]", "Networkadress");
			tcpApp = tcpApp.replace("[tcpAppStartTime]", String.valueOf( (float) flow.getStartTime() / 10));
			
			// Chunk size can be about 10kb --> 1MiB ~ 100 Chunks
			System.out.println( (float) flow.getChunks() / 100);
			tcpApp = tcpApp.replace("[tcpAppSendBytes]", String.valueOf( (float) flow.getChunks() / 100));

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
			tcpApp = tcpApp.replace("[tcpAppDeadline]", String.valueOf( (float) flow.getDeadline() / 10));
			content += tcpApp;
		}

		System.out.println("Destination: " + dest);
		
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
