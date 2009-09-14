package trustChainDiscovery;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;

import displaySWT.DisplaySWT;


/**
 * 
 * @author andy
 * 
 */
public class ShowView extends Canvas {

	// drawKind is 1 then draw message and node, if draw is 2 then draw agent
	// and nodes
	int drawKind = 1;

	int agentKind = 1;

//	RequirementTable result = null;

	FriendDataList friendList = null;

	FriendDataList allFriendList = null;

	Image hostImage = null;

	Image friendImage = null;

	Image agentImage = null;

	Image msgRequestImage = null;

	Image msgResultImage = null;

	Image resultImage = null;

	public Composite composite = null;

	NodeViewList nodeViewList = new NodeViewList();

	LinkedList<int[]> agentViewList = new LinkedList<int[]>();

	LinkedList<int[]> requestMsgViewList = new LinkedList<int[]>();

	LinkedList<int[]> resultMsgViewList = new LinkedList<int[]>();

	LinkedList<int[]> msgLineViewList = new LinkedList<int[]>();

	LinkedList<int[]> agentLineViewList = new LinkedList<int[]>();

	LinkedList<int[]> resultViewList = new LinkedList<int[]>();

	Rectangle bounds;

	int sectNo = 10;
	int gap = 10;
	int nodeGap = 10;
	// sign==false draw msg, sign==true draw agent
	boolean sign = false;

	public DisplaySWT gui; 
	
	public ShowView(final Composite composite, FriendDataList list,DisplaySWT gui) {

		super(composite, SWT.NONE | SWT.DOUBLE_BUFFERED);

		this.composite = composite;

		allFriendList = list;
		this.gui = gui;
//		this.result = result;

		friendList = new FriendDataList();

		for (int i = 0; i < allFriendList.size(); i++) {
			if (allFriendList.get(i).getLayerNo() != -1
					&& allFriendList.get(i).getLayerNo() <= 1)
				friendList.add(allFriendList.get(i));
		}

		hostImage = new Image(composite.getDisplay(),
				"resource\\icons\\pc3.ico");

		friendImage = new Image(composite.getDisplay(),
				"resource\\icons\\pc.ico");

		agentImage = new Image(composite.getDisplay(),
				"resource\\icons\\agent112.png");

		msgRequestImage = new Image(composite.getDisplay(),
				"resource\\icons\\help.gif");

		msgResultImage = new Image(composite.getDisplay(),
				"resource\\icons\\file_obj(1).gif");

		resultImage = new Image(composite.getDisplay(),
				"resource\\icons\\result90.png");
//		resultImage = new Image(composite.getDisplay(),
//		"resource\\icons\\user.png");

		addPaintListener(new ShowViewPaintListener());

	}

	private void initialize() {
		agentViewList.clear();
		requestMsgViewList.clear();
		resultMsgViewList.clear();
		msgLineViewList.clear();
		friendList.clear();
		resultViewList.clear();

		for (int i = 0; i < allFriendList.size(); i++) {
			if (allFriendList.get(i).getLayerNo() != -1
					&& allFriendList.get(i).getLayerNo() <= 1)
				friendList.add(allFriendList.get(i));
		}
	}

	/**
	 * 
	 * @param event
	 */
	public void runDraw(Event event) {
		initialize();

		final LinkedList<int[]> startResultMsgList = new LinkedList<int[]>();

		final int maxLayer = allFriendList.getLast().getLayerNo();

		final Counter counter = new Counter(maxLayer, sectNo);

		final Counter counterAgent = new Counter(maxLayer, sectNo);

		final Event finalEvent = event;

		event.display.timerExec(10, new Runnable() {
			//@Override
			public void run() {

				switch (drawKind) {
				// step1: draw nodes and messages
				case 1:

					// the request messages arrive the destination nodes, then
					// the result messages will back the start node
					if ((counter.j == counter.maxJ) && (sign == false)) {
						sign = true;
						startResultMsgList.clear();
						resultMsgViewList.clear();
						int length = requestMsgViewList.size();
						for (int i = 0; i < length; i++) {
							startResultMsgList.add(requestMsgViewList
									.removeLast());
							resultMsgViewList.add(startResultMsgList.get(i));
						}
						counter.j = 1;
						redraw();
						if (counter.i < maxLayer)
							finalEvent.display.timerExec(nodeGap, this);
						else {
							drawKind = 2;
							sign = false;
							finalEvent.display.timerExec(nodeGap, this);
						}

					}
					// the result messages back to the start node, then go on
					// the next hop to send request messages.
					else if ((counter.j == counter.maxJ) && (sign == true)) {
						sign = false;
						resultMsgViewList.clear();
						msgLineViewList.clear();
						counter.inc();

						// get the next layer friend list, then grow the tree.
						for (int i = 0; i < allFriendList.size(); i++) {
							if (allFriendList.get(i).getLayerNo() == counter.i) {
								friendList.add(allFriendList.get(i));
							}
						}
						redraw();
						if (counter.i < maxLayer)
							finalEvent.display.timerExec(nodeGap, this);
						else {
							drawKind = 2;
							sign = false;
							finalEvent.display.timerExec(nodeGap, this);
						}

					}
					// the sign is true, should draw the result messages. So
					// calculate the result message view list.
					else if (sign == true) {
						int[] start = { nodeViewList.get(0).getX(),
								nodeViewList.get(0).getY() };
						resultMsgViewList.clear();
						for (int k = 0; k < startResultMsgList.size(); k++) {
							int[] temp = new int[2];
							temp[0] = (int) (startResultMsgList.get(k)[0] + (float) (start[0] - startResultMsgList
									.get(k)[0])
									/ (counter.i * sectNo) * counter.j);
							temp[1] = (int) (startResultMsgList.get(k)[1] + (float) (start[1] - startResultMsgList
									.get(k)[1])
									/ (counter.i * sectNo) * counter.j);
							resultMsgViewList.add(temp);

						}
						redraw();
						counter.inc();
						if (counter.i < maxLayer)
							finalEvent.display.timerExec(gap, this);
						else {
							drawKind = 2;
							sign = false;
							finalEvent.display.timerExec(nodeGap, this);
						}

					}
					// the sign is false, should draw the request messages. So
					// calculate the request message view list.
					else if (sign == false) {
						int[] start = { nodeViewList.get(0).getX(),
								nodeViewList.get(0).getY() };
						NodeViewList desNodeList = getNodeViewList(counter.i);
						requestMsgViewList.clear();
						msgLineViewList.clear();
						for (int k = 0; k < desNodeList.size(); k++) {
							int[] end = { desNodeList.get(k).getX(),
									desNodeList.get(k).getY() };
							int[] temp = new int[2];
							temp[0] = (int) (start[0] + (float) (end[0] - start[0])
									/ (counter.i * sectNo) * counter.j);
							temp[1] = (int) (start[1] + (float) (end[1] - start[1])
									/ (counter.i * sectNo) * counter.j);
							requestMsgViewList.add(temp);

							int[] lineTemp = new int[4];
							lineTemp[0] = start[0];
							lineTemp[1] = start[1];
							lineTemp[2] = end[0];
							lineTemp[3] = end[1];
							msgLineViewList.add(lineTemp);
						}
						redraw();
						counter.inc();
						if (counter.i < maxLayer)
							finalEvent.display.timerExec(gap, this);
						else {
							drawKind = 2;
							sign = false;
							finalEvent.display.timerExec(nodeGap, this);
						}

					}
					break;

				// step2: draw nodes and agents
				case 2:
					requestMsgViewList.clear();
					resultMsgViewList.clear();
					msgLineViewList.clear();

					// drawAgent2(finalEvent,this,counterAgent,startResultMsgList);
					drawAgent(finalEvent, this, counterAgent);
					break;
				}

			}
		});
	}



	/**
	 * Draw agent route like the messages' route
	 * 
	 * @param event
	 * @param runAble
	 * @param counter
	 */
	public void drawAgent(Event event, Runnable runAble, Counter counter) {
		NodeViewList desNodeList = new NodeViewList();

		int[] temp;
		switch (agentKind) {
		case 1:
			agentViewList.clear();
			resultViewList.clear();
			desNodeList = getNodeViewList(counter.i);

			// System.out.println("##########"+desNodeList.toString());
			counter.maxK = desNodeList.size() - 1;
			counter.k = 0;

			int[] start = { nodeViewList.get(0).getX(),
					nodeViewList.get(0).getY() };
			int[] end = { desNodeList.get(0).getX(), desNodeList.get(0).getY() };

			temp = new int[2];
			temp[0] = (int) (start[0] + (float) (end[0] - start[0])
					/ (counter.i * sectNo) * counter.j);
			temp[1] = (int) (start[1] + (float) (end[1] - start[1])
					/ (counter.i * sectNo) * counter.j);
			agentViewList.add(temp);

			if (counter.j == counter.maxJ) {
				int[] rTemp = new int[2];
				rTemp[0] = desNodeList.get(0).getX() - 50;
				rTemp[1] = desNodeList.get(0).getY() - 100;
				resultViewList.add(rTemp);

				drawResult2Image(desNodeList.get(0));
			}

			redraw();
			if (counter.j == counter.maxJ) {
				agentKind = 2;
				counter.j = 1;
				event.display.timerExec(nodeGap, runAble);
			} else {
				counter.inc();
				event.display.timerExec(gap, runAble);
			}
			break;
		case 2:
			agentViewList.clear();
			resultViewList.clear();
			desNodeList = getNodeViewList(counter.i);
			// System.out.println("##########"+desNodeList.toString());
			counter.maxK = desNodeList.size() - 1;
			counter.maxJ = sectNo;
			if (counter.k + 1 <= counter.maxK) {
				int[] start2 = { desNodeList.get(counter.k).getX(),
						desNodeList.get(counter.k).getY() };
				int[] end2 = { desNodeList.get(counter.k + 1).getX(),
						desNodeList.get(counter.k + 1).getY() };
				temp = new int[2];
				temp[0] = (int) (start2[0] + (float) (end2[0] - start2[0])
						/ (sectNo) * counter.j);
				temp[1] = (int) (start2[1] + (float) (end2[1] - start2[1])
						/ (sectNo) * counter.j);
				agentViewList.add(temp);

				if (counter.j == counter.maxJ) {
					int[] rTemp = new int[2];
					rTemp[0] = temp[0] - 50;
					rTemp[1] = temp[1] - 100;
					resultViewList.add(rTemp);

					drawResult2Image(desNodeList.get(counter.k + 1));
				}

				redraw();

				if (counter.j == counter.maxJ) {
					counter.k++;
					counter.j = 1;
					event.display.timerExec(nodeGap, runAble);
				} else {
					counter.inc();
					event.display.timerExec(gap, runAble);
				}
			} else {
				agentKind = 3;
				counter.j = 1;
				event.display.timerExec(gap, runAble);
			}

			break;
		case 3:
			agentViewList.clear();
			resultViewList.clear();
			counter.maxJ = counter.i * sectNo;
			desNodeList = getNodeViewList(counter.i);
			int[] start2 = { desNodeList.get(counter.k).getX(),
					desNodeList.get(counter.k).getY() };
			int[] end2 = { nodeViewList.get(0).getX(),
					nodeViewList.get(0).getY() };

			temp = new int[2];
			temp[0] = (int) (start2[0] + (float) (end2[0] - start2[0])
					/ (counter.i * sectNo) * counter.j);
			temp[1] = (int) (start2[1] + (float) (end2[1] - start2[1])
					/ (counter.i * sectNo) * counter.j);
			agentViewList.add(temp);

			redraw();

			if (counter.j == counter.maxJ) {
				counter.i++;
				counter.j = 1;

				if (counter.i <= counter.maxI) {
					agentKind = 1;
					counter.maxJ = counter.i * sectNo;
					event.display.timerExec(50, runAble);
				} else {
					drawKind = 1;
					agentKind = 1;

					int[] rTemp = new int[2];
					rTemp[0] = temp[0] - 50;
					rTemp[1] = temp[1] - 100;
					resultViewList.add(rTemp);
					drawResult2Image(nodeViewList.get(0));

					redraw();
					
					new Thread(){
						public void run(){
							updateResult();
						}
					}.start();
					
					break;
				}
			} else {
				counter.inc();
				event.display.timerExec(gap, runAble);
			}
			break;

		}
		
	}
	
	private void updateResult(){
		
		updateResult1();
		try {
               Thread.sleep(2000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        updateResult2();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateResult3();
	}

	 private void updateResult1(){
	    	Display.getDefault().syncExec(new Runnable() {
	            public void run() {
	            	gui.updateServiceCertified();
	            }
	        });
	    }
	
	 private void updateResult2(){
	    	Display.getDefault().syncExec(new Runnable() {
	            public void run() {
//	            	gui.updateCandidate(new int[]{10,9,8,7});
	            	System.out.println("trust chain finished");
	     			gui.moveProgress(2);
	     		
	            }
	        });
	    }
	 
	 private void updateResult3(){
		 Display.getDefault().syncExec(new Runnable() {
	            public void run() {
	            
	     			gui.getRecommenderEvaluationLabel().setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	     			gui.createRecommenderChart();
	     			TabFolder tabFolder = gui.getTabFolder();
	     			tabFolder.setSelection(1);
	            }
	        });
		 
	 }
	private void drawResult2Image(NodeView nodeView) {

//		RequirementTable requirementTable = nodeView.getFriend()
//				.getRequirementTable();
//
//		if (requirementTable == null) {
//			drawResult2Image2(nodeView);
//
//			return;
//		}
//		if (requirementTable.size() == 0) {
//
//			return;
//		}
		GC gc = new GC(this.resultImage);
		String str = "test";
//		String str = "Res(" + requirementTable.get(0).getGood() + ","
//				+ requirementTable.get(0).getTotal() + ")\n";
//
//		str += "Ava(" + requirementTable.get(1).getGood() + ","
//				+ requirementTable.get(1).getTotal() + ")\n";
//
//		str += "Dur(" + requirementTable.get(2).getGood() + ","
//				+ requirementTable.get(2).getTotal() + ")\n";
//
//		str += "Pri(" + requirementTable.get(3).getGood() + ","
//				+ requirementTable.get(3).getTotal() + ")\n";
//
//		str += "Ref(" + requirementTable.get(4).getGood() + ","
//				+ requirementTable.get(4).getTotal() + ")\n";

		gc.drawText(str, 30, 15, true);
		gc.dispose();
	}

	

	/**
	 * This class gets the user input and draws the requested arc
	 * 
	 * @author andy
	 * 
	 */
	private class ShowViewPaintListener implements PaintListener {

		synchronized public void paintControl(PaintEvent e) {
			// System.out.println("Draw View...");
			drawHits(e);
			drawNodes(e);
			drawMsg(e);
			drawAgent(e);
			drawResult(e);
		}

		private void drawHits(PaintEvent e) {
			e.gc.drawImage(hostImage, 25, 10);
			e.gc.drawText("Host Node", 12, 50);

			e.gc.drawImage(friendImage, 105, 10);
			e.gc.drawText("Host Node", 92, 50);

			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLUE));
			e.gc.setLineWidth(3);
			e.gc.drawLine(200, 20, 235, 20);
			e.gc.drawText("FriendShip Line", 250, 15);

			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
			e.gc.setLineWidth(1);
			e.gc.setLineStyle(SWT.LINE_DASH);
			e.gc.drawLine(200, 55, 235, 55);
			e.gc.drawText("Message Route Line", 250, 47);

		}

		private void drawResult(PaintEvent e) {
			for (int i = 0; i < resultViewList.size(); i++) {
				int startX = resultViewList.get(i)[0];
				int startY = resultViewList.get(i)[1];
				e.gc.drawImage(resultImage, startX + 5, startY + 5);
				resultImage.dispose();
				resultImage = new Image(composite.getDisplay(),
						"resource\\icons\\result90.png");
//				resultImage = new Image(composite.getDisplay(),
//				"resource\\icons\\user1.png");
			}

		}

		private void drawMsg(PaintEvent e) {
			if (sign == false) {// draw request message
				for (int i = 0; i < requestMsgViewList.size(); i++) {
					int startX = requestMsgViewList.get(i)[0];
					int startY = requestMsgViewList.get(i)[1];
					e.gc.drawImage(msgRequestImage, startX + 8, startY + 8);
				}

			} else { // draw result message
				for (int i = 0; i < resultMsgViewList.size(); i++) {
					int startX = resultMsgViewList.get(i)[0];
					int startY = resultMsgViewList.get(i)[1];
					e.gc.drawImage(msgResultImage, startX + 4, startY + 4);
				}

			}

		}

		private void drawAgent(PaintEvent e) {

			for (int i = 0; i < agentViewList.size(); i++) {
				int startX = agentViewList.get(i)[0];
				int startY = agentViewList.get(i)[1];
				e.gc.drawImage(agentImage, startX + 4, startY + 4);
			}
		}

		private void drawNodes(PaintEvent e) {

			// reinitialize the nodeViewList
			nodeViewList.clear();
			bounds = composite.getBounds();
			int x = bounds.width;
			int y = bounds.height;

			int layerNo = friendList.getLast().getLayerNo();

			// calculate the sum of nodes in every same layer.
			int[] layerSum = new int[layerNo + 1];
			for (int i = 0; i < friendList.size(); i++) {
				layerSum[friendList.get(i).getLayerNo()]++;

			}
			int pre = 0;
			int preLayer = 0;
			for (int i = 0; i < friendList.size(); i++) {

				int la = friendList.get(i).getLayerNo();

				if (preLayer != la) {
					preLayer = la;
					pre = 1;
				} else {
					pre++;
				}
				NodeView temp = new NodeView(pre * x / (layerSum[la] + 1) - 30,
						(la + 1) * y / (layerNo + 2) - 30, friendList.get(i));

				nodeViewList.add(temp);

			}

			// draw lines of the friendship.
			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLUE));
			e.gc.setLineWidth(3);
			e.gc.setLineStyle(SWT.LINE_SOLID);
			for (int i = nodeViewList.size() - 1; i >= 0; i--) {
				int startX = nodeViewList.get(i).getX() + 16;
				int startY = nodeViewList.get(i).getY() + 16;

				int endX = nodeViewList.get(
						nodeViewList.get(i).getFriend().getParent()).getX() + 16;
				int endY = nodeViewList.get(
						nodeViewList.get(i).getFriend().getParent()).getY() + 16;
				e.gc.drawLine(startX, startY, endX, endY);
			}

			// draw lines of the message will route if has anyone
			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
			e.gc.setLineWidth(1);
			e.gc.setLineStyle(SWT.LINE_DASH);
			for (int i = msgLineViewList.size() - 1; i >= 0; i--) {
				int startX = msgLineViewList.get(i)[0] + 16;
				int startY = msgLineViewList.get(i)[1] + 16;

				int endX = msgLineViewList.get(i)[2] + 16;
				int endY = msgLineViewList.get(i)[3] + 16;
				e.gc.drawLine(startX, startY, endX, endY);
			}

			// draw lines of the agent will route if has anyone
			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
			e.gc.setLineWidth(1);
			e.gc.setLineStyle(SWT.LINE_DASH);
			for (int i = agentLineViewList.size() - 1; i >= 0; i--) {
				int startX = agentLineViewList.get(i)[0] + 16;
				int startY = agentLineViewList.get(i)[1] + 16;

				int endX = agentLineViewList.get(i)[2] + 16;
				int endY = agentLineViewList.get(i)[3] + 16;
				e.gc.drawLine(startX, startY, endX, endY);
			}

			// draw the host node
			e.gc.drawImage(hostImage, nodeViewList.get(0).getX(), nodeViewList
					.get(0).getY());

			// draw the node name of the host
			e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
			e.gc.drawString(nodeViewList.get(0).getFriend().getFriendAddress(),
					nodeViewList.get(0).getX() + 32,
					nodeViewList.get(0).getY() + 16);

			// draw every friend of the host and their names
			for (int i = 1; i < nodeViewList.size(); i++) {
				e.gc.drawString(nodeViewList.get(i).getFriend()
						.getFriendAddress(), nodeViewList.get(i).getX() + 35,
						nodeViewList.get(i).getY() + 16);
				e.gc.drawImage(friendImage, nodeViewList.get(i).getX(),
						nodeViewList.get(i).getY());

			}

		}
	}

	/**
	 * get the node list which layer number is all la.
	 * 
	 * @param la
	 * @return
	 */
	NodeViewList getNodeViewList(int la) {

		NodeViewList temp = new NodeViewList();
		for (int i = 0; i < nodeViewList.size(); i++) {
			if (nodeViewList.get(i).getFriend().getLayerNo() == la) {
				temp.add(nodeViewList.get(i));
			}
		}
		return temp;

	}

}

/**
 * control the timerExec function
 * 
 * @author andy
 * 
 */
class Counter {
	int i = 1;
	int j = 1;
	int k = 0;
	int maxK;
	int maxI;
	int maxJ;
	int sectNo;

	public Counter(int maxI, int sectNo) {
		this.sectNo = sectNo;
		this.maxI = maxI;
		this.maxJ = sectNo;
	}

	public void setMaxJ(int j) {
		maxJ = j;
	}

	public void inc() {
		j++;
		if (j == maxJ + 1) {
			i++;
			j = 1;
			maxJ = i * sectNo;
		}
	}
}
