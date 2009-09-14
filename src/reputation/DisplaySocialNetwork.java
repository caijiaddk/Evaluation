package reputation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import displaySWT.DisplaySWT;
import figures.AgentConnectionAnchor;
import figures.BadPeopleFigure;
import figures.GoodPeopleFigure;


public class DisplaySocialNetwork {
	
	private DisplaySWT socialNetwork;
	public SocialNetworkNode[] vertixlist ;
	private int nodeCount;
	public HashMap<SocialNetworkNode,NodeInfoView> nodeMap = new HashMap<SocialNetworkNode,NodeInfoView>();
	private HashMap<Figure, Label> promptLabelMap = new HashMap<Figure, Label>();
	private double region =2*Math.PI;
	private int total_levels;
	private int radius;			//test
	private Point circle_center;
	private SocialNetworkNode child;
	
	public ArrayList<ArrayList<SocialNetworkNode>> everylevelArrayList = new ArrayList<ArrayList<SocialNetworkNode>>();
	private int viewPort;
	
	public int getViewPort() {
		return viewPort;
	}

	public void setViewPort(int viewPort) {
		this.viewPort = viewPort;
	}
	
	public DisplaySocialNetwork(DisplaySWT socialNetwork){
		this.socialNetwork = socialNetwork;
	}
	
	public void initial(String filename) throws IOException{
		BufferedReader in =new BufferedReader(new FileReader(filename));
		String s;
		s = in.readLine();
		if(s!=null){
			String[] result = s.split(" ");
			if(result.length==2){
				nodeCount = Integer.parseInt(result[1]);
			}
		}
		vertixlist = new SocialNetworkNode[nodeCount];
		for(int i=0;i<nodeCount;i++){
			SocialNetworkNode newNode = new SocialNetworkNode(null,i+1);
			vertixlist[i]= newNode;
		}
	}
	
	public void generateSocialNetwork(String filename) throws IOException{
		BufferedReader in =new BufferedReader(new FileReader(filename));
		String s;
		while((s=in.readLine())!=null){
			String[] result = s.split(" ");
			if(result.length==3){
				int ID1 = Integer.parseInt(result[0]);
				int ID2 = Integer.parseInt(result[1]);
				SocialNetworkNode node1 = getNode(ID1);
				SocialNetworkNode currentNode = node1;
				while(currentNode.getNextNode()!=null)
					currentNode = currentNode.getNextNode();
				SocialNetworkNode node2 = new SocialNetworkNode(null,ID2);
				currentNode.setNextNode(node2);
			}
			if(result.length==4&&result[2].equals("ic")){
				int id = Integer.parseInt(result[1]);
				if(result[3].equals("Green")){
					vertixlist[id-1].setMalicious(true);
				}
				else{
					vertixlist[id-1].setMalicious(false);
				}
			}
		}
	}
	
	public SocialNetworkNode getNode(int id){
		return vertixlist[id-1];
	}
	
	public void displayLikeSnowflake(final int viewport){
		if(viewport>vertixlist.length){
			System.out.println("over the upper index,please try it later");
			return ;
		}
		else{
			this.viewPort = viewport;
			
			final int circle_level = calculateTreeLevels(viewport);
			total_levels = circle_level;
			System.out.println("the tree level is "+circle_level);
			
			socialNetwork.button.getDisplay().syncExec(new Runnable(){

				//@Override
				public void run() {
					// TODO Auto-generated method stub
					
					//reset the node display state
					reset();
					
					vertixlist[viewport-1].setChecked(true);
					
					Point point = new Point(socialNetwork.canvas.getSize().x,socialNetwork.canvas.getSize().y);
					radius = Math.min(socialNetwork.canvas.getSize().x, socialNetwork.canvas.getSize().y)/2/circle_level;
					circle_center = new Point(point.x/2,point.y/2); 
					Point displayLocation = new Point(point.x/2,point.y/2);
					NodeInfoView nodeInfoView = new NodeInfoView(displayLocation,0,Math.PI*2);
					nodeMap.put(vertixlist[viewport-1], nodeInfoView);
					Ellipse ellipse = new Ellipse();
					ellipse.addMouseMotionListener(new MouseMotionListener(){

					//	@Override
						public void mouseDragged(MouseEvent me) {
							// TODO Auto-generated method stub
							
						}

					//	@Override
						public void mouseEntered(MouseEvent me) {
							// TODO Auto-generated method stub
							Figure pirimitiveFigure = (Figure)me.getSource();
							SocialNetworkNode node = findNode(pirimitiveFigure);
							if(node!=null){
								Rectangle bounds = pirimitiveFigure.getBounds();
								Label promptLabel = new Label(Integer.toString(node.getID()));
								promptLabelMap.put(pirimitiveFigure, promptLabel);
								promptLabel.setBorder(new LineBorder());
								socialNetwork.pFigure.add(promptLabel,new Rectangle(bounds.x,bounds.y+30,20,20));
							}
						}

						//@Override
						public void mouseExited(MouseEvent me) {
							// TODO Auto-generated method stub
							Figure pirimitiveFigure = (Figure)me.getSource();
							Label promptLabel = promptLabelMap.get(pirimitiveFigure);
							if(promptLabel!=null){
								socialNetwork.pFigure.remove(promptLabel);
							}
						}

						//@Override
						public void mouseHover(MouseEvent me) {
							// TODO Auto-generated method stub
							
						}

						//@Override
						public void mouseMoved(MouseEvent me) {
							// TODO Auto-generated method stub
							
						}
						
					});
					int ellipse_radius = circle_level+1;
					ellipse.setBounds(new Rectangle(point.x/2-ellipse_radius,point.y/2-ellipse_radius,2*ellipse_radius,2*ellipse_radius));
					nodeInfoView.setFigure(ellipse);
					socialNetwork.pFigure.add(ellipse);
					
					Queue<SocialNetworkNode> queue1 = new LinkedList<SocialNetworkNode>();
					queue1.add(vertixlist[viewport-1]);
					Queue<SocialNetworkNode> queue2 = new LinkedList<SocialNetworkNode>();
					for(int i=1;i<=circle_level;i++){
						if(i%2==1){
							while(queue1.size()>0){
								SocialNetworkNode node = queue1.poll();
								drawNeighbor(node);
								ArrayList<SocialNetworkNode> children = drawChildren(node,i);
								everylevelArrayList.add(children);
								queue2.addAll(children);
							}
						}
						else{
							while(queue2.size()>0){
								SocialNetworkNode node = queue2.poll();
								drawNeighbor(node);
								ArrayList<SocialNetworkNode> children = drawChildren(node,i);
								everylevelArrayList.add(children);
								queue1.addAll(children);
							}
						}
					}
//					drawChildren(vertixlist[viewport-1],1);
				}
				
			});

		}
	}
	
	public int calculateTreeLevels(int viewport){
		if(viewport>vertixlist.length){
			System.out.println("over the upper index,please try it later");
			throw new IllegalArgumentException("over the upper index,please try it later");
		}
		else{
			int circle_level = 0;
			Queue<SocialNetworkNode> queue1 = new LinkedList<SocialNetworkNode>();
			queue1.add(vertixlist[viewport-1]);
			vertixlist[viewport-1].setChecked(true);
			
			Queue<SocialNetworkNode> queue2 = new LinkedList<SocialNetworkNode>();
			while(transferFromTwoQueues(queue1,queue2)){
				Queue<SocialNetworkNode> temp = queue1;
				queue1 = queue2;
				queue2 = temp;
				circle_level++;
			}
			return circle_level;
		}
	}
	
	public void reset(){
		for(int i=0;i<vertixlist.length;i++){
			vertixlist[i].setChecked(false);
		}
	}
	
	public boolean transferFromTwoQueues(Queue<SocialNetworkNode> queue1,Queue<SocialNetworkNode> queue2){
		queue2.clear();
		while(queue1.size()!=0){
			SocialNetworkNode node = queue1.poll();
			SocialNetworkNode currentNode = vertixlist[node.getID()-1];
			currentNode.setChecked(true);
			while(currentNode.getNextNode()!=null){
				currentNode = currentNode.getNextNode();
				SocialNetworkNode nextNode = vertixlist[currentNode.getID()-1];
				if(!nextNode.isChecked()){
					queue2.add(nextNode);
				}
			}
		}
		if(queue2.size()>0){
			return true	;
		}
		else{
			return false;
		}
		
	}
	
	public void drawNeighbor(SocialNetworkNode node){
		
		final NodeInfoView nodeInfoView = nodeMap.get(node);
		ArrayList<SocialNetworkNode> childrenList = new ArrayList<SocialNetworkNode>();
		childrenList = getNeighbor(node);
		int i = 1;
		for(Iterator<SocialNetworkNode> iterator = childrenList.iterator();iterator.hasNext();){
			i++;
			SocialNetworkNode neighbor = iterator.next();			//get the child
			final NodeInfoView neighborNodeInfoView = nodeMap.get(neighbor);
			
			
			
			//draw this child
			socialNetwork.button.getDisplay().syncExec(new Runnable(){

			//	@Override
				public void run() {
					// TODO Auto-generated method stub


					PolylineConnection connection = new PolylineConnection();
					PolygonDecoration arrow = new PolygonDecoration();
					arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
					arrow.setScale(5, 5);
					connection.setTargetDecoration(arrow);

					connection.setLineWidth(2);
					connection.setForegroundColor(ColorConstants.blue);
//					connection.setStart(nodeInfoView.getLocation());
//					connection.setEnd(neighborNodeInfoView.getLocation());
					connection.setSourceAnchor(new AgentConnectionAnchor(nodeInfoView.getFigure()));
					connection.setTargetAnchor(new AgentConnectionAnchor(neighborNodeInfoView.getFigure()));
					socialNetwork.pFigure.add(connection);
				}
				
			});

		}
	}
	
public ArrayList<SocialNetworkNode> drawChildren(SocialNetworkNode parent,final int level){
		
		ArrayList<SocialNetworkNode> childrenList = new ArrayList<SocialNetworkNode>();
		childrenList = getChildren(parent);
		int childrenNum = childrenNumber(parent);
		final NodeInfoView nodeInfoView = nodeMap.get(parent);
		double start = nodeInfoView.getChildrenstartdirection();
		double end = nodeInfoView.getChildrenenddirection();
		if(end<start)
			end = end + Math.PI*2;
		region = (end - start)/(childrenNum+1);
		int i = 1;
		for(Iterator<SocialNetworkNode> iterator = childrenList.iterator();iterator.hasNext();){
			i++;
			child = iterator.next();			//get the child
			child.setChecked(true);
			double childAngle = start + region*i;
			int relative_coordinate_x = (int)(radius*level*Math.cos(childAngle));
			int relative_coordinate_y = (int)(radius*level*Math.sin(childAngle));
			final Point childLocation = new Point(circle_center.x+relative_coordinate_x,
					circle_center.y-relative_coordinate_y);
			
			
			double startdirection = start + region*i-region/2;
			double enddirection = start + region*i+region/2;
			final NodeInfoView newNodeInfoView = new NodeInfoView(childLocation,startdirection,enddirection);
			
			nodeMap.put(child, newNodeInfoView);
			
			//draw this child
			socialNetwork.button.getDisplay().syncExec(new Runnable(){

				//@Override
				public void run() {
					// TODO Auto-generated method stub
//					Ellipse ellipse = new Ellipse();
//					int ellipse_radius = total_levels + 1 - level;
//					ellipse.setBounds(new Rectangle(childLocation.x-ellipse_radius,childLocation.y-ellipse_radius,2*ellipse_radius,2*ellipse_radius));
//					socialNetwork.parentFigure.add(ellipse);
					
					Figure agentFigure;
					if(child.isMalicious()){
						agentFigure = new BadPeopleFigure(new Point(childLocation.x-10,childLocation.y-10));
						newNodeInfoView.setFigure(agentFigure);
						socialNetwork.pFigure.add(agentFigure,new Rectangle(childLocation.x-10,childLocation.y-10,20,20));
		
					}
					else{
						agentFigure = new GoodPeopleFigure(new Point(childLocation.x-10,childLocation.y-10));
						newNodeInfoView.setFigure(agentFigure);
						socialNetwork.pFigure.add(agentFigure,new Rectangle(childLocation.x-10,childLocation.y-10,20,20));
			
					}
					agentFigure.addMouseMotionListener(new MouseMotionListener(){

						//@Override
						public void mouseDragged(MouseEvent me) {
							// TODO Auto-generated method stub

						}

					//	@Override
						public void mouseEntered(MouseEvent me) {
							// TODO Auto-generated method stub
							Figure pirimitiveFigure = (Figure)me.getSource();
							SocialNetworkNode node = findNode(pirimitiveFigure);
							if(node!=null){
								Rectangle bounds = pirimitiveFigure.getBounds();
								Label promptLabel = new Label(Integer.toString(node.getID()));
								promptLabelMap.put(pirimitiveFigure, promptLabel);
								promptLabel.setBorder(new LineBorder());
								socialNetwork.pFigure.add(promptLabel,new Rectangle(bounds.x,bounds.y+30,20,20));
							}

						}

					//	@Override
						public void mouseExited(MouseEvent me) {
							// TODO Auto-generated method stub
							Figure pirimitiveFigure = (Figure)me.getSource();
							Label promptLabel = promptLabelMap.get(pirimitiveFigure);
							if(promptLabel!=null){
								socialNetwork.pFigure.remove(promptLabel);
							}
						}

					//	@Override
						public void mouseHover(MouseEvent me) {
							// TODO Auto-generated method stub
							
						}

					//	@Override
						public void mouseMoved(MouseEvent me) {
							// TODO Auto-generated method stub
							
						}
						
					});
//					BadAgentFigure agentFigure = new BadAgentFigure(new Point(childLocation.x-10,childLocation.y-10));
//					socialNetwork.parentFigure.add(agentFigure,new Rectangle(childLocation.x-10,childLocation.y-10,20,20));
					
					PolylineConnection connection = new PolylineConnection();
					PolygonDecoration arrow = new PolygonDecoration();
					arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
					arrow.setScale(5, 5);
					connection.setTargetDecoration(arrow);

					connection.setLineWidth(2);
					connection.setForegroundColor(ColorConstants.blue);
//					connection.setStart(nodeInfoView.getLocation());
//					connection.setEnd(childLocation);
					connection.setSourceAnchor(new AgentConnectionAnchor(nodeInfoView.getFigure()));
					connection.setTargetAnchor(new AgentConnectionAnchor(newNodeInfoView.getFigure()));
					socialNetwork.pFigure.add(connection);
				}
				
			});

		}
		
		return childrenList;
	}
	
	public int neighborNumber(SocialNetworkNode node){
		
		int count=0;
		SocialNetworkNode current = vertixlist[node.getID()];
		while(current.getNextNode()!=null){
			current = current.getNextNode();
			count++;
		}
		
		return count;
	}
	
	public int childrenNumber(SocialNetworkNode node){
		
		int count=0;
		SocialNetworkNode current = vertixlist[node.getID()-1];
		while(current.getNextNode()!=null){
			current = current.getNextNode();
			if(!current.isChecked())
				count++;
		}
		
		return count;
	}
	
	public ArrayList<SocialNetworkNode> getChildren(SocialNetworkNode parent){
		ArrayList<SocialNetworkNode> children = new ArrayList<SocialNetworkNode>();
		SocialNetworkNode current = vertixlist[parent.getID()-1];
		while(current.getNextNode()!=null){
			current = current.getNextNode();
			if(!vertixlist[current.getID()-1].isChecked())
				children.add(vertixlist[current.getID()-1]);
		}
		
		return children;
	}
	
	public ArrayList<SocialNetworkNode> getNeighbor(SocialNetworkNode node){
		ArrayList<SocialNetworkNode> neighbor = new ArrayList<SocialNetworkNode>();
		SocialNetworkNode current = vertixlist[node.getID()-1];
		while(current.getNextNode()!=null){
			current = current.getNextNode();
			if(vertixlist[current.getID()-1].isChecked())
				neighbor.add(vertixlist[current.getID()-1]);
		}
		
		return neighbor;
	}
	
	public SocialNetworkNode findNode(Figure figure){
		SocialNetworkNode node=null;
		for(int i=0;i<vertixlist.length;i++){
			node = vertixlist[i];
			NodeInfoView nodeInfoView = nodeMap.get(node);
			if(nodeInfoView!=null){
				if(nodeInfoView.getFigure()==figure)
					return node;
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException{
//		DisplaySocialNetwork displaySocialNetwork = new DisplaySocialNetwork(null);
//		String filename = "g_trust1.net";
//		displaySocialNetwork.initial(filename);
//		displaySocialNetwork.generateSocialNetwork(filename);
//		displaySocialNetwork.displayLikeSnowflake(5);
//		System.out.println("init finish");
	}
}
