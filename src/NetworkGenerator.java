import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

/**
 * Firstly creates Network objects defining capacity to each time slot
 * Secondly converts it to network part of the dat-file 
 * @author QZ61P8
 *
 */


public class NetworkGenerator {
	

	private Vector<Network> networks = new Vector<Network>();
	private String content;
	
	public NetworkGenerator(){
		addNetworks();
	}
	
	public void addNetworks(){
		
		//initialize networks (now manually, should later be done from methods to iterate over variable )
		networks.add(Network.getWiFi(42, 30, 5));
		networks.add(Network.getWiFi(64, 25, 24));
		networks.add(Network.getWiFi(35, 45, 65));
		networks.add(Network.getWiFi(75, 35, 100));
		networks.add(Network.getWiFi(45, 25, 155));
		
		networks.add(Network.getCellular(140, 20));
		networks.add(Network.getCellular(200, 25));
	}
	
	
	public void writeOutput(String source, String dest){
		//read source
		try {
			content = new Scanner(new File(source)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//get max size
		int size =0;
		int interfaceTypes = 0;
		for (Network network : networks) {
			if(network.getSlots()>size){
				size = network.getSlots();
			}
			if(network.getType()>interfaceTypes){
				interfaceTypes = network.getType();
			}
		}
		
		content=content.replace("[nChannels]", ""+networks.size());
		content=content.replace("[nTime]", ""+size);
		content=content.replace("[interfaceTypes]", ""+interfaceTypes);
		
		
		//one interface of each type
		boolean first=true;
		String clientInterfaces = "";
		for(int i=0;i<interfaceTypes;i++){
			if(first){
				first = false;
			}else{
				clientInterfaces+=", ";
			}
			//add list item
			clientInterfaces+="1";
		}
		
		content=content.replace("[clientInterfaces]", clientInterfaces);
		
		//set equal size
		//and print networks
		String availBW="";
		String type = "";
		String cost = "";
		String latency = "";
		String jitter = "";
		
		first = true;
		for (Network network : networks) {
			//set size
			network.setSlots(size);
			//list separators: omit before first list element
			if(first){
				first = false;
			}else{
				availBW+=",\n";
				type+=", ";
				cost+=", ";
				latency+=", ";
				jitter+=", ";
			}
			//add list items
			
			availBW+="\t"+network.toString();
			type+=network.getType();
			cost+=network.getCost();
			latency+=network.getLatency();
			jitter+=network.getJitter();
		}
		
		content=content.replace("[availBW]", availBW);
		content=content.replace("[type]", type);
		content=content.replace("[cost]", cost);
		content=content.replace("[latency]", latency);
		content=content.replace("[jitter]", jitter);
		
		content= content.replace("[networkGenTime]", new Date().toString());
		
		//write file
		try {
			PrintWriter pw = new PrintWriter(dest);
			pw.println(content);
			pw.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
