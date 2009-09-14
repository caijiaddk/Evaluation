package workflow;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.DeferredUpdateManager;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.ics.tcg.web.workflow.client.examples.Client_Choice;
import com.ics.tcg.web.workflow.client.examples.Client_ConditionalOutputPort;
import com.ics.tcg.web.workflow.client.examples.Client_Loop;
import com.ics.tcg.web.workflow.client.examples.Client_Node;
import com.ics.tcg.web.workflow.client.examples.Client_OutputPort;
import com.ics.tcg.web.workflow.client.examples.Client_ServiceTask;
import com.ics.tcg.web.workflow.client.examples.Client_Workflow;

import figures.ChoiceFigure;
import figures.InputPortFigure;
import figures.LoopTaskFigure;
import figures.NodeFigure;
import figures.OutputPortFigure;
import figures.TaskFigure;

public class DisplayWorkflow {

	protected Shell shell = null;

	private Client_Workflow client_Workflow;
	private HashMap<Client_Node, NodeFigure> data2figure = new HashMap<Client_Node, NodeFigure>();

	private Composite totalComposite;
	private Composite topComposite;
	private Canvas bottomcanvas;
	private Figure parentFigure;

	protected static final String SIMPLENODE = "Simple";
	protected static final String LOOPNODE = "Loop";
	protected static final String CHOICENODE = "Choice";
	protected static final String TIMERNODE = "Timer";
	protected static final String FIXEDFORNODE = "FLoop";
	protected static final String NONFIXEDNUMFOR = "TLoop";

	protected static final String LOOPNODETITLE = "loop";
	protected static final String FIXEDFORNODETITLE = "fixednumloop";
	protected static final String NONFIXEDNUMFORTITLE = "timeintervalloop";

	private Button startButton;// “执行”按钮

	private Button stopButton;// “停止”按钮
	
	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DisplayWorkflow window = new DisplayWorkflow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {

		shell = new Shell();
		shell.setText("Display Workflow");
		shell.setSize(1000, 480);

		createControlButton();
		createWorkflow();

		Display display = Display.getDefault();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public void createControlButton() {
		topComposite = new Composite(shell, SWT.BORDER);
		topComposite.setBounds(0, 0, 1000, 30);

		ToolBar toolBar = new ToolBar(topComposite, SWT.NONE);
		toolBar.setBounds(0, 0, 100, 30);

		ToolItem playButton = new ToolItem(toolBar, SWT.RADIO);
		playButton.setText("Play");
		playButton.addSelectionListener(new ButtonListener(playButton));

		// ToolItem p = new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem pauseButton = new ToolItem(toolBar, SWT.RADIO);
		pauseButton.setText("Pause");
		pauseButton.addSelectionListener(new ButtonListener(pauseButton));

	}

	public void createWorkflow() {

		bottomcanvas = new Canvas(shell, SWT.None);
		bottomcanvas.setBounds(0, 20, 1000, 450);
		LightweightSystem lws = new LightweightSystem(bottomcanvas);
		parentFigure= new Figure();
		parentFigure.setLayoutManager(new XYLayout());
		lws.setContents(parentFigure);

		client_Workflow = parseWorkflow();
		displayWorkflow(client_Workflow, parentFigure);
	}

	public Client_Workflow parseWorkflow() {
		Client_Workflow cWorkflow = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"workflow.out"));
			cWorkflow = (Client_Workflow) in.readObject();
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return cWorkflow;
	}

	public void displayWorkflow(Client_Workflow cWorkflow, Figure parent) {
		Client_Workflow client_Workflow = cWorkflow;
		// clear primitive nodes
		// parent.removeAll();
		// clear the current timerNode
		// for(Iterator<TimerNode> iterator =
		// timernodeList.iterator();iterator.hasNext();){
		// TimerNode timerNode = iterator.next();
		// timerNode.removeFromParent();
		// }
		// timernodeList.clear();

		if (client_Workflow != null) {
			Client_Workflow workflow = client_Workflow;
			int node_count = client_Workflow.getHasNodes().size();
			for (int index = 0; index < node_count; index++) {
				Client_Node client_Node = workflow.getHasNodes().get(index);
				if (client_Node.getName().equals(LOOPNODE)) {
					Client_Loop client_Loop = (Client_Loop) client_Node;
					LoopTaskFigure loop = new LoopTaskFigure();

					// set the loop location
					parent.add(loop, new Rectangle(zoomoutRelative(client_Loop
							.getX() - 183), client_Loop.getY(), 120, 120));

					// 设置是否为终止点
					if (client_Loop.isIsfinished()) {
						loop.setFinishTask(true);
					}
					// 设置是否为起始点
					if (client_Loop.isIsstart()) {
						loop.setStartTask(true);
					}

					// 设置基本属性
					NodeFigure nodeFigure = loop;
					data2figure.put(client_Node, nodeFigure); // 为接下来寻找nextnode服务
				} else if (client_Node.getName().equals(CHOICENODE)) {
					Client_Choice client_Choice = (Client_Choice) client_Node;
					ChoiceFigure choice = new ChoiceFigure();

					// set the loop location
					parent.add(choice, new Rectangle(
							zoomoutRelative(client_Choice.getX() - 183),
							client_Choice.getY(), 95, 85));

					// choice节点的出口设置
					for (Iterator<Client_ConditionalOutputPort> iterator = client_Choice
							.getConditionalOutputPort().iterator(); iterator
							.hasNext();) {
						Client_ConditionalOutputPort client_ConditionalOutputPort = iterator
								.next();
						String name = client_ConditionalOutputPort.getId();
						String ifcondition = client_ConditionalOutputPort
								.getClient_IfCondition().getCondition_value();
						choice.addConditionalOutput(name, ifcondition);
					}

					// 设置是否为终止点
					if (client_Choice.isIsfinished()) {
						choice.setFinishTask(true);
					}
					// 设置是否为起始点
					if (client_Choice.isIsstart()) {
						choice.setStartTask(true);
					}

					NodeFigure nodeFigure = choice;
					data2figure.put(client_Node, nodeFigure); // 为接下来寻找nextnode服务
				} else {
					Client_ServiceTask client_ServiceTask = (Client_ServiceTask) client_Node;
					TaskFigure service = new TaskFigure();

					// set the loop location
					parent.add(service, new Rectangle(
							zoomoutRelative(client_ServiceTask.getX() - 183),
							client_ServiceTask.getY(), 135, 60));

					// //判断是否有时钟
					// if(client_ServiceTask.getTimer()!=null){
					// AddTimerCommand addTimerCommand = new
					// AddTimerCommand(service,this);
					// addTimerCommand.addTimer();
					// //设置时间节点的start_time
					// service.getTimerNode().setStart_time(client_ServiceTask.
					// getTimer().getTimeString());
					// }

					// 设置是否为终止点
					if (client_ServiceTask.isIsfinished()) {
						service.setFinishTask(true);
					}
					// 设置是否为起始点
					if (client_ServiceTask.isIsstart()) {
						service.setStartTask(true);
					}

					NodeFigure nodeFigure = service;
					data2figure.put(client_Node, nodeFigure); // 为接下来寻找nextnode服务
				}
			}

			/* 添加边，并且设置nextnode列表 */
			for (int index = 0; index < node_count; index++) {
				Client_Node client_Node = workflow.getHasNodes().get(index);

				if (client_Node.getName().equals(LOOPNODE)) { // 节点是loopnode时
					Client_Loop client_Loop = (Client_Loop) client_Node;
					LoopTaskFigure loop = (LoopTaskFigure) data2figure
							.get(client_Loop);

					Client_OutputPort client_OutputPort = client_Loop
							.getOutputPort();
					for (Iterator<Client_Node> iterator2 = client_OutputPort
							.getHasNextNodes().iterator(); iterator2.hasNext();) {
						// 找出outputport的下一个节点
						Client_Node nextnode = (Client_Node) iterator2.next();
						NodeFigure loop2 = data2figure.get(nextnode);

						PolylineConnection connection = new PolylineConnection();
						PolygonDecoration arrow = new PolygonDecoration();
						arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
						arrow.setScale(5, 2.5);
						connection.setTargetDecoration(arrow);

						connection.setSourceAnchor((ConnectionAnchor) loop
								.getSourceConnectionAnchors().get(1));
						connection.setTargetAnchor((ConnectionAnchor) loop2
								.getTargetConnectionAnchors().get(0));
						parent.add(connection);

						// Rectangle start_rectangle =
						// loop.getDefaultOutputPort().getClientArea();
						// Rectangle end_rectangle =
						// loop2.getDefaultInputPort().getClientArea();
						// Point start_point = new
						// Point(start_rectangle.getCenter());
						// Point end_point = new
						// Point(end_rectangle.getCenter());
						// connection.setStart(start_point);
						// connection.setEnd(end_point);

					}

				} else if (client_Node.getName().equals(CHOICENODE)) { // 节点是选择几点
					Client_Choice client_Choice = (Client_Choice) client_Node;
					ChoiceFigure choice = (ChoiceFigure) data2figure
							.get(client_Choice);
					int outputport_count = 0;
					for (Iterator<Client_ConditionalOutputPort> iterator2 = client_Choice
							.getConditionalOutputPort().iterator(); iterator2
							.hasNext();) {
						Client_ConditionalOutputPort client_ConditionalOutputPort = (Client_ConditionalOutputPort) iterator2
								.next();

						for (Iterator<Client_Node> iterator4 = client_ConditionalOutputPort
								.getHasNextNodes().iterator(); iterator4
								.hasNext();) {
							Client_Node nextnode = (Client_Node) iterator4
									.next();
							NodeFigure choice2 = (NodeFigure) data2figure
									.get(nextnode);
							// suppose there is only one input port

							PolylineConnection connection = new PolylineConnection();
							PolygonDecoration arrow = new PolygonDecoration();
							arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
							arrow.setScale(5, 2.5);
							connection.setTargetDecoration(arrow);

							connection
									.setSourceAnchor((ConnectionAnchor) choice
											.getSourceConnectionAnchors().get(
													outputport_count + 1));
							connection
									.setTargetAnchor((ConnectionAnchor) choice2
											.getTargetConnectionAnchors()
											.get(0));
							parent.add(connection);
						}
					}
				}

				else { // 节点是服务节点
					Client_ServiceTask client_ServiceTask = (Client_ServiceTask) client_Node;
					TaskFigure service = (TaskFigure) data2figure
							.get(client_ServiceTask);
					Client_OutputPort client_OutputPort = client_ServiceTask
							.getOutputPort();
					for (Iterator<Client_Node> iterator2 = client_OutputPort
							.getHasNextNodes().iterator(); iterator2.hasNext();) {
						// 找出outputport的下一个节点
						Client_Node nextnode = (Client_Node) iterator2.next();
						NodeFigure service2 = data2figure.get(nextnode);

						PolylineConnection connection = new PolylineConnection();
						PolygonDecoration arrow = new PolygonDecoration();
						arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
						arrow.setScale(5, 2.5);
						connection.setTargetDecoration(arrow);

						connection.setSourceAnchor((ConnectionAnchor) service
								.getSourceConnectionAnchors().get(1));
						connection.setTargetAnchor((ConnectionAnchor) service2
								.getTargetConnectionAnchors().get(0));
						parent.add(connection);

						// Rectangle start_rectangle =
						// service.getDefaultOutputPort().getClientArea();
						// Rectangle end_rectangle =
						// service2.getDefaultInputPort().getClientArea();
						// Point start_point = new
						// Point(start_rectangle.getCenter());
						// Point end_point = new
						// Point(end_rectangle.getCenter());
						// connection.setStart(start_point);
						// connection.setEnd(end_point);
					}
				}
			}

			System.out.println(node_count);
		}
	}

	public int zoomoutRelative(int primitive) {
		int newInteger = (int) (primitive * 1.5);
		return newInteger;
	}

	class ButtonListener extends SelectionAdapter {
		private ToolItem button;
		
		Point currentStart;
		Point currentEndPoint;

		ButtonListener(ToolItem button) {
			super();
			this.button = button;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			super.widgetSelected(e);
			new Thread() {
				public void run() {
					button.getDisplay().syncExec(new Runnable() {
						public void run() {
							button.setEnabled(false);

						}
					});
//					
//					button.getDisplay().syncExec(new Runnable() {
//						public void run() {
							
							Client_Node firstNode = client_Workflow.getHasFirstNode();
							NodeFigure currentNode = data2figure.get(firstNode);
							if(firstNode.getName().equals("Choice")){
								Client_Choice choice = (Client_Choice)firstNode;
								ChoiceFigure choiceFigure = (ChoiceFigure)currentNode;
								int outputNum=0;
								for(Iterator<Client_ConditionalOutputPort> iterator = choice.getConditionalOutputPort().iterator();iterator.hasNext();){
									Client_ConditionalOutputPort client_ConditionalOutputPort = iterator.next();
									OutputPortFigure outputPortFigure = choiceFigure.getOutputportList().get(outputNum);
									Point startPoint = outputPortFigure.getBounds().getRight();
									for(Iterator<Client_Node> iterator2 = client_ConditionalOutputPort.getHasNextNodes().iterator();iterator2.hasNext();){
										Client_Node client_Node = iterator2.next();
										NodeFigure nextFigure = data2figure.get(client_Node);
										InputPortFigure inputPortFigure = nextFigure.getDefaultInputPort();
										Point endPoint = inputPortFigure.getBounds().getLeft();
										display_data_flow(startPoint,endPoint);
										
										display_flitter_circle(nextFigure);
									}
								}
							}
							else if(firstNode.getName().equals("Loop")){
								Client_Loop loop = (Client_Loop)firstNode;
								LoopTaskFigure loopTaskFigure = (LoopTaskFigure)currentNode;
								OutputPortFigure outputPortFigure = loopTaskFigure.getDefaultOutputPort();
								Point startPoint = outputPortFigure.getBounds().getRight();
								for(Iterator<Client_Node> iterator = loop.getOutputPort().getHasNextNodes().iterator();iterator.hasNext();){
									Client_Node client_Node = iterator.next();
									NodeFigure nextFigure = data2figure.get(client_Node);
									InputPortFigure inputPortFigure = nextFigure.getDefaultInputPort();
									Point endPoint = inputPortFigure.getBounds().getLeft();
									display_data_flow(startPoint,endPoint);
									
									display_flitter_circle(nextFigure);
								}
							}
							else {
								Client_ServiceTask client_ServiceTask = (Client_ServiceTask)firstNode;
								TaskFigure taskFigure = (TaskFigure)currentNode;
								OutputPortFigure outputPortFigure = taskFigure.getDefaultOutputPort();
								Point parentPoint = taskFigure.getLocation();
								Point startPoint = new Point(outputPortFigure.getBounds().getRight().x+parentPoint.x,outputPortFigure.getBounds().getRight().y+parentPoint.y);
								for(Iterator<Client_Node> iterator = client_ServiceTask.getOutputPort().getHasNextNodes().iterator();iterator.hasNext();){
									Client_Node client_Node = iterator.next();
									NodeFigure nextFigure = data2figure.get(client_Node);
									InputPortFigure inputPortFigure = nextFigure.getDefaultInputPort();
									Point parentPoint2 = nextFigure.getLocation();
									Point endPoint = new Point(inputPortFigure.getBounds().getLeft().x+parentPoint2.x,inputPortFigure.getBounds().getLeft().y+parentPoint2.y);
									display_data_flow(startPoint,endPoint);
									
									display_flitter_circle(nextFigure);
								}
							}
							
							
//							display_flitter_circle(currentNode);

//						}
//					});

				}
			}.start();
		}

		
		public void display_flitter_circle(Figure figure){
			final Rectangle area = figure.getBounds();
			final Point flitter_circle_location = new Point(area.getLeft().x+15,area.getTop().y+5);
			
						button.getDisplay().syncExec(new Runnable() {
				public void run() {
					final Ellipse ellipse = new Ellipse();
					ellipse.setBackgroundColor(ColorConstants.red);
					parentFigure.add(ellipse,new Rectangle(flitter_circle_location.x,flitter_circle_location.y,20,20));
				}
			});
		}
		
		public void display_data_flow(Point start,Point end){
			final PolylineConnection connection = new PolylineConnection();
			connection.setForegroundColor(ColorConstants.red);
			connection.setLineWidth(4);
			
			final Point startPoint = start;
			final float stepX = (end.x-start.x)/10;
			final float stepY = (end.y-start.y)/10;
			
			currentStart = start;
			currentEndPoint = new Point(currentStart.x+(int)stepX,currentStart.y+(int)stepY);
			

			button.getDisplay().syncExec(new Runnable() {
				public void run() {
					connection.setStart(currentStart);
					connection.setEnd(currentEndPoint);
					parentFigure.add(connection);
				}
			});
			
//			button.getDisplay().syncExec(new Runnable() {
//				public void run() {
					int timeStep = 10;
					while(timeStep>0){
						timeStep--;
//						parentFigure.remove(connection);


						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						currentStart = new Point(startPoint.x+(int)(stepX*(10-timeStep)),startPoint.y+(int)(stepY*(10-timeStep)));;
						currentEndPoint = new Point(startPoint.x+(int)(stepX*(11-timeStep)),startPoint.y+(int)(stepY*(11-timeStep)));

						
						button.getDisplay().syncExec(new Runnable() {
							public void run() {
								connection.setStart(currentStart);
								connection.setEnd(currentEndPoint);
								
								parentFigure.add(connection);
		
								DeferredUpdateManager deferredUpdateManager = (DeferredUpdateManager) parentFigure.getUpdateManager();
								deferredUpdateManager.performUpdate();
							}
						});
					}
					
					button.getDisplay().syncExec(new Runnable() {
						public void run() {
							parentFigure.remove(connection);
							DeferredUpdateManager deferredUpdateManager = (DeferredUpdateManager) parentFigure.getUpdateManager();
							deferredUpdateManager.performUpdate();
						}
					});
					
//					parentFigure.remove(connection);
//					DeferredUpdateManager deferredUpdateManager = (DeferredUpdateManager) parentFigure.getUpdateManager();
//					deferredUpdateManager.performUpdate();
//				}
//			});
		}
		
	}

}
