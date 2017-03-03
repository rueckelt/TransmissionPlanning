package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;

import schedulers.Scheduler;
import schedulingIOModel.CostFunction;
import schedulingIOModel.Network;

/**
 * 
 * @author DennisHanslik@web.de
 * 
 * Create a window, put it in a JScrollPane (to use scrollbars) and set some initial values.
 * Automatically calls DrawingPanel, which starts printing the screen.
 *
 */
public class Plot extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4512141223520070006L;

	public Plot(VisualizationPack vis) {
		super("Connection problem prediction");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		DrawingPanel drawingPanel = new DrawingPanel(screenSize.width, screenSize.height, vis);
		JScrollPane scrollPane = new JScrollPane(drawingPanel);
		add(scrollPane);
		
		setSize(1024, 768);			// Window size when window is not maximized. Otherwise it would only be a Titlebar. 
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
    }
}


/**
 * 
 * @author DennisHanslik@web.de
 * 
 * DrawingPanel is the main window. It defines objects for all schedulers (PlotGraph-Objects) which are printed inside
 * PlotGraph.  
 * DrawingPanel just decides where each PlotGraph is printed on the screen, how many PlotGraphs are needed and
 * prints global informations (not uses) and for example labels for each PlotGraph. 
 *
 */
class DrawingPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2188487228621530045L;
	VisualizationPack vis;
	int screenW;				// maximum width of frame
	int screenH;				// maximum height of frame
	int width;					// actual width of panel
	int height;					// actual height of panel
	int heightHead = 100;		// height of head area
	int [] widthController;		// actual width for every graph to control width of panel

	// Head
	//

	
	//Body
	//
	int offsetHGraph = 20;		// horizontal offset for all graphs
	int offsetVGraph = 50;		// vertical offset for all graphs
	
	// Label for each scheduler
	final JLabel labelGraph0;
	final JLabel labelGraph1;
	final JLabel labelGraph2;
	final JLabel labelGraph3;
	
	// PlotGraph for each scheduler to print them on the screen
	final PlotGraph gL0;
	final PlotGraph gL1;
	final PlotGraph gL2;
	final PlotGraph gL3;
	
	
	public DrawingPanel(int screenW, int screenH, VisualizationPack v) {
		super(null);			// No layout
		vis = v;
		this.screenW = screenW;
		this.screenH = screenH;
		widthController = new int[4];

		// Definitions
		width = screenW;
		height = screenH;
		
		// Head
		//
		

		// Body
		//
		int factor = 0;		// count schedulers
		if (vis.getScheduler0() != null)
			factor++;
		if (vis.getScheduler1() != null)
			factor++;
		if (vis.getScheduler2() != null)
			factor++;
		if (vis.getScheduler3() != null)
			factor++;
		if (vis.getSp() != null) 
			factor = 0;
		
		// factor is reused! Until now it just counts the needed schedulers. From now on, it is the factor to multiply
		// the height of each printed scheduler.
		switch (factor) {
			case 1: factor = 4; break;
			case 2: factor = 2; break;
			default: factor = 1;
		}
		
		int nextStartY = heightHead;
		
		if (vis.getNets() != null) {
			if (vis.getSp() != null) {

			labelGraph0 = null;
			gL0 = new PlotGraph(vis, vis.getSp(), factor);
				gL0.setLocation(offsetHGraph, nextStartY);
				gL0.setVisible(true);
			add(gL0);
			
			nextStartY += gL0.getHeight() +offsetVGraph;
			
			// correction for screen width
			int tempwidth = (int)gL0.getPreferredSize().getWidth() +50;
			if (tempwidth > width)
				width = tempwidth;
			} else
			// Line 0
			if (vis.getScheduler0() != null) {
				labelGraph0 = new JLabel(vis.getScheduler0().getType());
					labelGraph0.setLocation(offsetHGraph, nextStartY -30);
					labelGraph0.setSize(100, 20);
					labelGraph0.setVisible(true);
				add(labelGraph0);

				gL0 = new PlotGraph(vis, vis.getScheduler0(), factor);
					gL0.setLocation(offsetHGraph, nextStartY);
					gL0.setVisible(true);
				add(gL0);
				
				nextStartY += gL0.getHeight() +offsetVGraph;
				
				// correction for screen width
				int tempwidth = (int)gL0.getPreferredSize().getWidth() +50;
				if (tempwidth > width)
					width = tempwidth;
				
			} else {
				labelGraph0 = null;
				gL0 = null;
			}
			
			// Line 1
			if (vis.getScheduler1() != null) {
				labelGraph1 = new JLabel(vis.getScheduler1().getType());
					labelGraph1.setLocation(offsetHGraph, nextStartY);
					labelGraph1.setSize(100, 20);
					labelGraph1.setVisible(true);
				add(labelGraph1);

				gL1 = new PlotGraph(vis, vis.getScheduler1(), factor);
					gL1.setLocation(offsetHGraph, nextStartY +labelGraph1.getHeight() +10);
					gL1.setVisible(true);
				add(gL1);

				nextStartY += labelGraph1.getHeight() +10 +gL1.getHeight() +offsetVGraph;

				// correction for screen width
				int tempwidth = (int)gL1.getPreferredSize().getWidth() +50;
				if (tempwidth > width)
					width = tempwidth;
				
			} else {
				labelGraph1 = null;
				gL1 = null;
			}
			
			// Line 2
			if (vis.getScheduler2() != null) {
				labelGraph2 = new JLabel(vis.getScheduler2().getType());
					labelGraph2.setLocation(offsetHGraph, nextStartY);
					labelGraph2.setSize(100, 20);
					labelGraph2.setVisible(true);
				add(labelGraph2);

				gL2 = new PlotGraph(vis, vis.getScheduler2(), factor);
					gL2.setLocation(offsetHGraph, nextStartY +labelGraph2.getHeight() +10);
					gL2.setVisible(true);
				add(gL2);

				nextStartY += labelGraph2.getHeight() +10 +gL2.getHeight() +offsetVGraph;

				// correction for screen width
				int tempwidth = (int)gL2.getPreferredSize().getWidth() +50;
				if (tempwidth > width)
					width = tempwidth;
				
			} else {
				labelGraph2 = null;
				gL2 = null;
			}
			
			// Line 3
			if (vis.getScheduler3() != null) {
				labelGraph3 = new JLabel(vis.getScheduler3().getType());
					labelGraph3.setLocation(offsetHGraph, nextStartY);
					labelGraph3.setSize(100, 20);
					labelGraph3.setVisible(true);
				add(labelGraph3);

				gL3= new PlotGraph(vis, vis.getScheduler3(), factor);
					gL3.setLocation(offsetHGraph, nextStartY +labelGraph3.getHeight() +10);
					gL3.setVisible(true);
				add(gL3);
				
				nextStartY += labelGraph3.getHeight() +10 +gL3.getHeight();

				// correction for screen width
				int tempwidth = (int)gL3.getPreferredSize().getWidth() +50;
				if (tempwidth > width)
					width = tempwidth;
				
			} else {
				labelGraph3 = null;
				gL3 = null;
			}
			
				
			height = nextStartY;
				
		} else {
			labelGraph0 = null;
			labelGraph1 = null;
			labelGraph2 = null;
			labelGraph3 = null;
			
			gL0 = null;
			gL1 = null;
			gL2 = null;
			gL3 = null;
		}
		
		height += 20;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
}


/**
 * 
 * @author DennisHanslik@web.de
 * 
 * This is where the magic happens! 
 * PlotGraph prints ONE graph (schedule) including all networks, flows, descriptions, tooltips, etc.
 * Every schedule has his own PlotGraph-Object.
 *
 */
class PlotGraph extends JPanel{
	
	private Vector<Network> nets;
	private Scheduler sched;
	private int[][][] sp;
	private Vector <FlowRect> vFlowRect = new Vector <FlowRect>();
	private ViolationRect vViolationRect = new ViolationRect();
	private CostFunction costs;
	private boolean firstTime = true;		// Don't count violations, once screen is updated
	private int[][] violations;				// for every flow: vioTp, vioJit, vioLcy, vioSt, vioDl -> !=0, if violation happens. This is for the TextArea!
	
	final JTextArea textArea;
	final JScrollPane scrollPane;
	
	private static final long serialVersionUID = -8176671097532436132L;

	int width;					// self defined by analyzing data
	int height;					// defined external
	int factor;					// if there are only one or two graphs, they can be higher
	int widthTimeslot = 10;		// width per timeslot
	int vOffset = 20;			// space between two networks
	int textAreaWidth = 165;

	
	public PlotGraph(VisualizationPack vis, Scheduler s, int factor) {
		super(null);			// No layout
		
		sched = s;
		nets = vis.getNets().getNetworks();
		this.factor = factor;
		costs = new CostFunction(vis.getNets(), vis.getTraffic());
		
		textArea = new JTextArea("Costs: "+costs.costTotal(sched.getSchedule())+"\n\n");//costs.costMon(sched.getSchedule())+"\n\n");
		textArea.setEditable(false);
		textArea.setVisible(true);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setLocation(0, 0);
		add(scrollPane);

		width = nets.get(0).getSlots() *widthTimeslot +(textAreaWidth +10);
		
		// calculate height to set size
		for(Network net : nets) {
			int heightMax = 0;
			for (int c : net.getCapacity()) {
				if (c > heightMax)
					heightMax = c;
			}
			
			height += heightMax *factor +vOffset;
		}
		
		violations = new int[sched.getFlowCounter()][5];
		
		for (int i=0; i<sched.getFlowCounter(); i++) {
			vFlowRect.add(new FlowRect());
		}
		
		scrollPane.setSize(textAreaWidth, height);
		setSize(width, height);
	}
	
	public PlotGraph(VisualizationPack vis, int[][][] sp, int factor) {
		super(null);			// No layout
		this.sp = sp;
		nets = vis.getNets().getNetworks();
		this.factor = factor;
		costs = new CostFunction(vis.getNets(), vis.getTraffic());
		
		textArea = new JTextArea("Costs: "+costs.costTotal(sp)+"\n\n");//costs.costMon(sched.getSchedule())+"\n\n");
		textArea.setEditable(false);
		textArea.setVisible(true);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setLocation(0, 0);
		add(scrollPane);

		width = nets.get(0).getSlots() *widthTimeslot +(textAreaWidth +10);
		
		// calculate height to set size
		for(Network net : nets) {
			int heightMax = 0;
			for (int c : net.getCapacity()) {
				if (c > heightMax)
					heightMax = c;
			}
			
			height += heightMax *factor +vOffset;
		}
		
		violations = new int[sp.length][5];
		
		for (int i=0; i<sp.length; i++) {
			vFlowRect.add(new FlowRect());
		}
		
		scrollPane.setSize(textAreaWidth, height);
		setSize(width, height);
	}

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int[][][] s;
		if (sp != null) {
			s = sp;
		} else {
			s = sched.getSchedule();
		}

		int netCounter = 0;
		int offset = textAreaWidth +10;
		height = 0;
		
		for(Network n : nets) {
			int heightMax = 0;
			for (int c : n.getCapacity()) {
				if (c > heightMax)
					heightMax = c;
			}
			
			heightMax *= factor;
			
			int t = 0;		// Current timeslot
			if (netCounter >= s[0][0].length) {
				break;
			}
			for (int c : n.getCapacity()) {
				g.setColor(Color.GRAY);
				
				c *= factor;
				
				// Network-capacity
				g.fillRect(offset+ t *widthTimeslot, height +heightMax -c, widthTimeslot, c);
				
				// Markers for timeslots
				g.drawLine(offset+ t *widthTimeslot, height +heightMax, offset+ t *widthTimeslot, height +heightMax +1);
				g.drawLine(offset+ (t+1) *widthTimeslot -1, height +heightMax, offset+ (t+1) *widthTimeslot -1, height +heightMax +1);

				if (t%10 == 0 && t != 0 && t < n.getCapacity().size() -1) {
					g.drawChars(Integer.toString(t).toCharArray(), 0, Integer.toString(t).length(), offset+ t *widthTimeslot -Integer.toString(t).length()*4, height +heightMax +12);
				}
				
				int newlevel = 0;		// for adding multiple flows per timeslot
				FlowRect temp;

				boolean violationFound = false;
				String sViolation = new String ("<html>Slot "+t);
				
				int flowNum = s.length;
				for (int f = 0; f < s.length; f++) {//sched.getFlowCounter(); f++) {
					if (s[f][t][netCounter] != 0) {
						
						// Flows
						temp = vFlowRect.get(f);
						
						// different color for each flow .... until there are more then 17.
						switch (f) {
							case 0: g.setColor(Color.GREEN.darker()); break;
							case 1: g.setColor(Color.BLUE); break;
							case 2: g.setColor(Color.PINK); break;
							case 3: g.setColor(Color.LIGHT_GRAY); break;
							case 4: g.setColor(Color.MAGENTA); break;
							case 5: g.setColor(Color.ORANGE); break;
							case 6: g.setColor(Color.YELLOW); break;
							case 7: g.setColor(Color.CYAN); break;
							case 8: g.setColor(Color.GREEN); break;
							case 9: g.setColor(Color.BLUE.brighter()); break;
							case 10: g.setColor(Color.PINK.brighter()); break;
							case 11: g.setColor(Color.DARK_GRAY); break;
							case 12: g.setColor(Color.MAGENTA.brighter()); break;
							case 13: g.setColor(Color.ORANGE.brighter()); break;
							case 14: g.setColor(Color.YELLOW.brighter()); break;
							case 15: g.setColor(Color.CYAN.brighter()); break;
							default: g.setColor(Color.GREEN.brighter());
						}

/*						if (newlevel != 0) {
							// one pixel between two flows
							newlevel += s[f][t][netCounter] *factor;
							g.fillRect(offset+ t *widthTimeslot, height +heightMax -newlevel, widthTimeslot, s[f][t][netCounter] *factor -1);
						} else {
*/							newlevel += s[f][t][netCounter] *factor;
							g.fillRect(offset+ t *widthTimeslot, height +heightMax -newlevel, widthTimeslot, s[f][t][netCounter] *factor);
//						}
						
						temp.add(new Rectangle(offset+ t *widthTimeslot, height +heightMax -newlevel, widthTimeslot, s[f][t][netCounter] *factor), s[f][t][netCounter], t);
						vFlowRect.remove(f);
						vFlowRect.add(f, temp);
						
						if (sp != null) {
							
						} else {
						// Violations (including target-actual-comparison)
						if (sched.getFlow(f).getDeadline() < t) {
							// Deadline violation
							sViolation += "<br>"+"Deadline("+f+") T:"+sched.getFlow(f).getDeadline()+" vs A:"+t+" | Cost: "+vioDl(f, t, netCounter);
							violationFound = true;
							if (firstTime) violations[f][4] += 1;
						}
						
						if (sched.getFlow(f).getStartTime() > t) {
							// Starttime violation
							sViolation += "<br>"+"Starttime("+f+") T:"+sched.getFlow(f).getStartTime()+" vs A:"+t+" | Cost: "+vioSt(f, t, netCounter);
							violationFound = true;
							if (firstTime) violations[f][3] += 1;
						}

						if (sched.getFlow(f).getTokensMin() > s[f][t][netCounter]) {
							// Throughput violation
							sViolation += "<br>"+"Throughput("+f+") T:"+sched.getFlow(f).getTokensMin()+" vs A:"+s[f][t][netCounter]+" | Cost: "+vioTp(f, t);
							violationFound = true;
							if (firstTime) violations[f][0] += 1;
						}
						
						if (sched.getFlow(f).getReqLatency() < n.getLatency()) {
							// Latency violation
							sViolation += "<br>"+"Latency("+f+") T:"+sched.getFlow(f).getReqLatency()+" vs A:"+n.getLatency()+" | Cost: "+vioLcy(f, t, netCounter);
							violationFound = true;
							if (firstTime) violations[f][2] += 1;
						}
						
						if (sched.getFlow(f).getReqJitter() < n.getJitter()) {
							// Jitter violation
							sViolation += "<br>"+"Jitter("+f+") T:"+sched.getFlow(f).getReqJitter()+" vs A:"+n.getJitter()+" | Cost: "+vioJit(f, t, netCounter);
							violationFound = true;
							if (firstTime) violations[f][1] += 1;
						}
						}
					}
				}

				if (violationFound) {
					// Print violation-marker
					sViolation += "</html>";
					g.setColor(Color.RED);
					g.fillRect(offset+ t *widthTimeslot, height +heightMax, widthTimeslot, 5);
					vViolationRect.add(new Rectangle(offset+ t *widthTimeslot, height +heightMax, widthTimeslot, 5), sViolation);
				}

				t++;
			}
			
			height += heightMax +vOffset;
			netCounter++;
		}

		
		/**
		 * 
		 * Check if current mouse position is on a element with tooltip, then print tooltip.
		 * 
		 */
		addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseMoved(MouseEvent e) {
            	boolean found = false;
            	String s = null;
            	int frCounter = 0;

            	for (int pos = vViolationRect.size() -1; pos >= 0; pos--) {
                	if (vViolationRect.getRect(pos).contains(e.getPoint())) {
            			s = vViolationRect.getViolation(pos);
            			found = true;
            			break;
            		}
            	}
    			
    			if (!found) {
        			for(FlowRect fr : vFlowRect) {
                    	for (int pos = fr.size() -1; pos >= 0; pos--) {
    	                	if (fr.getRect(pos).contains(e.getPoint())) {
    	            			s = new String("Slot: "+fr.getTimeslot(pos)+"; Flow: "+frCounter+"; Capacity: "+fr.getCapacity(pos));
    	            			found = true;
    	            			break;
    	            		}
                    	}
                    	if (found)
                    		break;
                    	
                    	frCounter++;
                	}
    			}
    			
    			setToolTipText(s);
                ToolTipManager.sharedInstance().mouseMoved(e);
                ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);		// stay on screen until mouse moves
            }

			@Override
			public void mouseDragged(MouseEvent e) {
				// nothing to do
			}
        });
		
		
		// Print summary in a text area.
		if (sp != null) {
			
		} else {
		if (firstTime) {
			textArea.append("Violations:");
			for (int f = 0; f < sched.getFlowCounter(); f++) {
				textArea.append("\nFlow " +f +":\n");
				if (violations[f][0] != 0)
					textArea.append("Throughput: " +violations[f][0] +"\n");
		
				if (violations[f][1] != 0)
					textArea.append("Jitter: " +violations[f][1] +"\n");
		
				if (violations[f][2] != 0)
					textArea.append("Latency: " +violations[f][2] +"\n");
		
				if (violations[f][3] != 0)
					textArea.append("Starttime: " +violations[f][3] +"\n");
		
				if (violations[f][4] != 0)
					textArea.append("Deadline: " +violations[f][4] +"\n");
			}
		}
		}
		
		firstTime = false;			// don't count any more violations, because they are already calculated 
	}
	
	
	public int getWidth() {
		return width;
	}
	
	private int vioDl (int flow, int timeslot, int network) {
		return costs.vioDl_f_t_n(sched.getSchedule(), flow, timeslot, network)
				*sched.getFlow(flow).getImpDeadline()*sched.getFlow(flow).getImpUser();
	}
	
	private int vioSt (int flow, int timeslot, int network) {
		return costs.vioSt_f_t_n(sched.getSchedule(), flow, timeslot, network)
				*sched.getFlow(flow).getImpStartTime()*sched.getFlow(flow).getImpUser();
	}
	
	private int vioLcy (int flow, int timeslot, int network) {
		return costs.vioLcy_f_t_n(sched.getSchedule(), flow, timeslot, network)
				*sched.getFlow(flow).getImpLatency()*sched.getFlow(flow).getImpUser();
	}
	
	private int vioJit (int flow, int timeslot, int network) {
		return costs.vioJit_f_t_n(sched.getSchedule(), flow, timeslot, network)
				*sched.getFlow(flow).getImpJitter()*sched.getFlow(flow).getImpUser();
	}
	
	private int vioTp (int flow, int timeslot) {
		int[][] cummulated_f_t = costs.cummulated_f_t(sched.getSchedule());
		return costs.vioTp_f_t(cummulated_f_t, flow, timeslot)
				*sched.getFlow(flow).getImpThroughputMin()*sched.getFlow(flow).getImpUser();
	}	
}