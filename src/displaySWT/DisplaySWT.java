package displaySWT;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.DeferredUpdateManager;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import com.ics.tcg.web.workflow.client.examples.Client_Choice;
import com.ics.tcg.web.workflow.client.examples.Client_ConditionalOutputPort;
import com.ics.tcg.web.workflow.client.examples.Client_Loop;
import com.ics.tcg.web.workflow.client.examples.Client_Node;
import com.ics.tcg.web.workflow.client.examples.Client_OutputPort;
import com.ics.tcg.web.workflow.client.examples.Client_ServiceTask;
import com.ics.tcg.web.workflow.client.examples.Client_Workflow;
import com.ics.tcg.web.workflow.server.engine.util.CredentialChainResult;
import com.ics.tcg.web.workflow.server.engine.util.ServiceInfo;
import com.ics.tcg.web.workflow.server.service.reputation.EvaluateObject;

import feedbackLearning.HistoryChart;
import figures.AgentFigure;
import figures.BubblesFigure;
import figures.ChoiceFigure;
import figures.Circle;
import figures.CustomRectangle;
import figures.DatabaseFigure;
import figures.FriendsFigure;
import figures.HelpFigure;
import figures.HostFigure;
import figures.InputPortFigure;
import figures.LoopTaskFigure;
import figures.MsgFigure;
import figures.NodeFigure;
import figures.OutputPortFigure;
import figures.TaskFigure;
import figures.TransparentFigure;

import trustChainDiscovery.FriendData;
import trustChainDiscovery.FriendDataList;
import trustChainDiscovery.ShowView;
import util.ImageFactory;
import util.SWTResourceManager;
import workflow.AbstractViewModel;
import workflow.Parser;
import workflow.PetriView;
import workflow.TrustNode;


/**
 * This class is the user interface that displays data for a single game in
 * progress.
 * 
 * @author K. Fullam
 */
public class DisplaySWT {

	private Shell sShell = null;
	private SashForm sashForm = null;

	private SashForm topViewForm = null;

	private Composite workflowComposite = null;
	private TabFolder displayComposite = null;
	private SashForm bottomView;
	
	private boolean shellIsMaximun;

	// private DataPanel recChart;
	HistoryChart chart;
	TabItem recommender;
	ToolItem evaButton;
	Composite navigation;
	ProgressBar bar;
	Table table;
	Table serviceAspect;
	// private Composite costgram = null;
	private TrustNode root = null;
	// private Composite totalcostgram = null;
	// private Composite grams = null;
	private static PetriView petri = null;
	
	private PaintDaemon task = new PaintDaemon(this); // Task为后台处理类

	private Label[] monitorLabel = new Label[5];

	private Label searchingServicesLabel;

	private Label trustChainDiscoveryLabel;

	private Label recommenderEvaluationLabel;

	private Label reputationEvaluationLabel;

	private ToolItem startButton;// “执行”按钮

	FriendDataList fr;
	
	TableItem candidateServiceItem;
	
	public String firstServiceName;					//in order to display the first step ,save the first node name
	
	
	private Client_Workflow client_Workflow;
	private HashMap<Client_Node, NodeFigure> data2figure = new HashMap<Client_Node, NodeFigure>();
	private HashMap<Client_Node, Integer> hasfinishedPreNode = new HashMap<Client_Node, Integer>();

	private Canvas bottomcanvas;
	public Figure parentFigure;

	public Canvas canvas;
	public Figure pFigure;
	public ToolItem button;
	
//	private DisplaySocialNetwork displaySocialNetwork = new DisplaySocialNetwork(this);
	
	protected static final String SIMPLENODE = "Simple";
	protected static final String LOOPNODE = "Loop";
	protected static final String CHOICENODE = "Choice";
	protected static final String TIMERNODE = "Timer";
	protected static final String FIXEDFORNODE = "FLoop";
	protected static final String NONFIXEDNUMFOR = "TLoop";

	protected static final String LOOPNODETITLE = "loop";
	protected static final String FIXEDFORNODETITLE = "fixednumloop";
	protected static final String NONFIXEDNUMFORTITLE = "timeintervalloop";
	
	static ArrayList<GraphNode> graphnodeList = new ArrayList<GraphNode>();
	
	private boolean bindingServicefinished = false;

	public synchronized boolean isBindingServicefinished() {
		return bindingServicefinished;
	}

	public synchronized void setBindingServicefinished(
			boolean bindingServicefinished) {
		this.bindingServicefinished = bindingServicefinished;
	}
	
	//the shared data of the whole display module
	ArrayList<String> specialServiceNameList = new ArrayList<String>();
	
	//come from reputation model display module
	
	int id=1;

//	private ArrayList<GraphNode> graphnodeList = new ArrayList<GraphNode>();
	private ArrayList<SocialNetworkNode> socialnetworkList = new ArrayList<SocialNetworkNode>();
	private ArrayList<Point> destinationPointList = new ArrayList<Point>();
	private HashMap<SocialNetworkNode, NodeInfoView> node2figure = new HashMap<SocialNetworkNode, NodeInfoView>();
	private boolean hasAgent;
	private boolean hasfinishedRunnigAgent;

	public synchronized boolean isHasfinishedRunnigAgent() {
		return hasfinishedRunnigAgent;
	}

	public synchronized void setHasfinishedRunnigAgent(
			boolean hasfinishedRunnigAgent) {
		this.hasfinishedRunnigAgent = hasfinishedRunnigAgent;
	}

	private int displayAgentStep = 20;
	AgentInfo agentInfo1, agentInfo2;

	public synchronized boolean isHasAgent() {
		return hasAgent;
	}

	public synchronized void setHasAgent(boolean hasAgent) {
		this.hasAgent = hasAgent;
	}

	private ArrayList<Integer> agentList = new ArrayList<Integer>();
	private HashMap<Integer, AgentInfo> agent2figure = new HashMap<Integer, AgentInfo>();
	
	EvaluateObject evaluateObject;
	ArrayList<Integer> deleteNodeId;
	double[][] pageRankResult;
	int[] recommenderArray;
	double[] candidatesArray;
	int displayUserid;
	TableItem pageRankTableitem;
	Table rankTable;
	//...............reputation model variable over
	
	//come from trust chain discovery
	protected Canvas trustChainCanvas;
	protected Canvas trustRulesCanvas;
	protected Figure tcParentFigure,tcParentFigure2;
	HostFigure hostFigure;
	FriendsFigure friendFigure,friendFigure2,friendFigure3,friendFigure4;
	org.eclipse.draw2d.Label hostdomainName = new org.eclipse.draw2d.Label("IBM")
	,frienddomainName = new org.eclipse.draw2d.Label("ISO")
	,frienddomainName2 = new org.eclipse.draw2d.Label("A")
	,frienddomainName3 = new org.eclipse.draw2d.Label("XX")
	,frienddomainName4 = new org.eclipse.draw2d.Label("TrustedGroup");
	DatabaseFigure databaseFigure = new DatabaseFigure(new Point(0,0))
	,databaseFigure2 = new DatabaseFigure(new Point(0,0))
	,databaseFigure3 = new DatabaseFigure(new Point(0,0))
	,databaseFigure4 = new DatabaseFigure(new Point(0,0))
	,databaseFigure5 = new DatabaseFigure(new Point(0,0));
	
	BubblesFigure bubblesFigure,bubblesFigure2,bubblesFigure3,bubblesFigure4,bubblesFigure5;
	
	PolylineConnection secondpolylineConnection,secondpolylineConnection2;
	
	org.eclipse.draw2d.Label label = new org.eclipse.draw2d.Label("[Service->\nISO.rankA]A")
	,label2 = new org.eclipse.draw2d.Label("[Service->IBM.service]IBM")
	,label3 = new org.eclipse.draw2d.Label("[IBM.serivce->ISO.rankA]A")
	,label3forbubble = new org.eclipse.draw2d.Label("[IBM.serivce\n->ISO.rankA]A")
	,label4 = new org.eclipse.draw2d.Label("[A->TrustedGroup.member]TrustedGroup")
	,label5 = new org.eclipse.draw2d.Label("[TrusteGroup.member->ISO.certifier]XX")
	,label5forbubble = new org.eclipse.draw2d.Label("[TrusteGroup.member\n->ISO.certifier]XX")
	,label6 = new org.eclipse.draw2d.Label("[XX->ISO.administrator]ISO")
	,label7 = new org.eclipse.draw2d.Label("[ISO.administrator->ISO.certifier']ISO")
	,label8 = new org.eclipse.draw2d.Label("[ISO.certifier->ISO.rankA']ISO")
	,merge5and6 = new org.eclipse.draw2d.Label("[XX->ISO.certifier']ISO")
	,merge3and4and7 = new org.eclipse.draw2d.Label("[A->ISO.rankA']ISO");
	
	
	HelpFigure helpFigure,helpFigure3,helpFigure4;
	
	AgentFigure agentFigure = new AgentFigure(new Point(0,0))
	,agentFigure2 = new AgentFigure(new Point(0,0));
	
	Point currentPoint = new Point()
	,currentPoint2 = new Point()
	,msgCurrentPoint = new Point()
	,msgCurrentPoint2 = new Point()
	,currentLocation = new Point();
	
	MsgFigure msgFigure = new MsgFigure(new Point(0,0));
	MsgFigure msgFigure2 = new MsgFigure(new Point(0,0));
	
	Point startPoint,endPoint,endPoint2,msgStartPoint,msgEndPoint;
	double agentStepX,agentStepY,agentStepX2,agentStepY2,msgStepX,msgStepY,stepX,stepY;
	
	PolylineConnection virtualPolylineConnection,virtualPolylineConnection2,virtualPolylineConnection3;
	PolylineConnection deductionLine,deductionLine2;
	
	String rule,rule2,rule3,rule4,rule5,rule6,rule7,rule8; 
	//...............trust chain model variable over
	
	/**
	 * Creates a new instance of GameMonitorInterface.
	 * 
	 */
	public DisplaySWT() {

	}

	public void open() {
		final Display display = Display.getDefault();
		createSShell();
		
//		sShell.addListener(SWT.Resize, new Listener() {
//			public void handleEvent(Event e) {
//				org.eclipse.swt.graphics.Point point = bar.getSize();
//				System.out.println(point.x+" "+point.y);
//				bar.setSize(403, 30);
//				sShell.layout();
//				System.out.println("modify the height of the progressBar");
//			}
//		});
		
		sShell.open();
		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Service Evaluation and Selection Windows");
		sShell.setLayout(new GridLayout());
		// sShell.setSize(1100, 800);
		sShell.setLocation(70, 70);
		sShell.setImage(ImageFactory.loadImage(sShell.getDisplay(),	ImageFactory.MAIN));
		createSashForm();
		// createMenu();
		// sShell.setImage(ImageFactory.loadImage(sShell.getDisplay(),
		// ImageFactory.MAIN));
		// sShell.setSize(new org.eclipse.swt.graphics.Point(307, 218));

		sShell.pack();

		sShell.setSize(1000, 640);
	}

	private void createSashForm() {
		sashForm = new SashForm(sShell, SWT.VERTICAL);
		// sashForm.setLayout(new FillLayout());
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTopViewForm();
		// createTableViewForm1();
		createBottomViewForm();

		sashForm.setWeights(new int[] { 38, 62 });

	}

	private void createBottomViewForm() {

		bottomView = new SashForm(sashForm, SWT.BORDER);
		//bottomView.set
		createNavigation();
		createDisplay();

		bottomView.setWeights(new int[] { 25, 75 });
	}

	private void createNavigation() {
		navigation = new Composite(bottomView,SWT.NONE);
		navigation.setLayout(new FormLayout());
		
		createIntro();
		createCandidate();

		sashForm.setWeights(new int[] { 38, 62 });
		
	}
	private void createIntro() {
//		Text intro = new Text(bottomView, SWT.BORDER | SWT.WRAP);
//		intro
//				.setText("Evaluation steps:\n\n"
//						+ "Searching services:\n\n"
//						+ "Trust chain discovery:\n\nRecommender evaluation: \n\nReputation evaluation:");
		// intro.setSize(10, 10);
		
		ViewForm vf = new ViewForm(navigation,SWT.BORDER);
		final FormData fd_vf = new FormData();
		fd_vf.top = new FormAttachment(0, 0);
		fd_vf.left = new FormAttachment(0, 0);
		fd_vf.right = new FormAttachment(100, 0);
		fd_vf.bottom = new FormAttachment(0, 175);
		vf.setLayoutData(fd_vf);
		
		ToolBar toolBar = new ToolBar(vf, SWT.NONE);
		startButton = new ToolItem(toolBar, SWT.PUSH);
		startButton.setText("Start evaluation");
		startButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setButtonState(false);
				
				final int taskCount = 1;
				bar.setMaximum(4);
				// 为后台任务新开一个线程
				new Thread() {
					public void run() {
						task.start(taskCount);
					}
				}.start();
			}
		});
	
		vf.setTopLeft(toolBar);
		
		Composite composite = new Composite(vf, SWT.BORDER | SWT.WRAP);
		composite.setLayout(new FormLayout());
		
		bar = new ProgressBar(composite,SWT.NONE);
		final FormData fd_bar = new FormData();
		fd_bar.top = new FormAttachment(0, 0);
		fd_bar.left = new FormAttachment(0, 0);
		fd_bar.right = new FormAttachment(100, 0);
		fd_bar.bottom = new FormAttachment(0, 25);
		bar.setLayoutData(fd_bar);
//		bar.setBounds(0, 0, 230, 30);
		
		//bar.setLayoutData();	
//		evaluationStepsLabel = new Label(composite, SWT.NONE);
//		evaluationStepsLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	//	evaluationStepsLabel.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
//		evaluationStepsLabel.setText("Evaluation steps...");
	//	evaluationStepsLabel.setBounds(10, 17, 253, 23);

		Composite introductionComposite = new Composite(composite,SWT.NONE);
		
		final FormData fd_introductionComposite = new FormData();
		fd_introductionComposite.top = new FormAttachment(0, 25);
		fd_introductionComposite.left = new FormAttachment(0, 0);
		fd_introductionComposite.bottom = new FormAttachment(100, 0);
		introductionComposite.setLayoutData(fd_introductionComposite);
		
//		introductionComposite.setBounds(0, 30, 230, 115);
		introductionComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		searchingServicesLabel = new Label(introductionComposite, SWT.NONE);
//		searchingServicesLabel.setBounds(0, 35, 230, 30);
//		searchingServicesLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
//		searchingServicesLabel.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
	//	searchingServicesLabel.setBounds(10, 57, 253, 23);
		searchingServicesLabel.setText("Searching services...");

		trustChainDiscoveryLabel = new Label(introductionComposite, SWT.NONE);
//		trustChainDiscoveryLabel.setBounds(0, 65, 230, 30);
//		trustChainDiscoveryLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	//	trustChainDiscoveryLabel.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
	//	trustChainDiscoveryLabel.setBounds(10, 97, 253, 23);
		trustChainDiscoveryLabel.setText("Trust chain discovery...");

		recommenderEvaluationLabel = new Label(introductionComposite, SWT.NONE);
//		recommenderEvaluationLabel.setBounds(0, 95, 230, 30);
//		recommenderEvaluationLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	//	recommenderEvaluationLabel.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
//		recommenderEvaluationLabel.setBounds(10, 137, 253, 23);
		recommenderEvaluationLabel.setText("Recommender evaluation...");

		reputationEvaluationLabel = new Label(introductionComposite, SWT.NONE);
//		reputationEvaluationLabel.setBounds(0, 125, 230, 30);
		reputationEvaluationLabel.setText("Reputation evaluation...");
		
		vf.setContent(composite);

	}

	private void createCandidate(){
		table = new Table(navigation, SWT.MULTI | SWT.FULL_SELECTION);
		final FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(0, 175);
		fd_table.left = new FormAttachment(0, 0);
		fd_table.right = new FormAttachment(100, 0);
		fd_table.bottom = new FormAttachment(100, 0);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn co1 = new TableColumn(table, SWT.NONE);
		co1.setText("Name");
		
		co1.setWidth(150);
		TableColumn co2 = new TableColumn(table, SWT.NONE);
		co2.setText("Company");
		co2.setWidth(80);

	}
	
	private void createDisplay() {
		displayComposite = new TabFolder(bottomView, SWT.BORDER);

	}

	public void createSerchingService(){
		
		//search the database
		
		String stringName =null;
		String stringBusiness =null;
		
		java.sql.Connection conn;
		java.sql.Statement stmt;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(
			"jdbc:mysql://172.16.167.230:3306/project", "admin", "");
			stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("select serviceName,businessName from Service where abserviceName='"+firstServiceName+"'");
//			System.out.println("connetct db"+resultSet.first());
			while(resultSet.next()){
				stringName = resultSet.getString(1);
				stringBusiness = resultSet.getString(2);
				String specialServiceName = stringBusiness+"."+stringName;
				specialServiceNameList.add(specialServiceName);
				candidateServiceItem = new TableItem(table, 0);
				candidateServiceItem.setText(new String[] { stringName, stringBusiness });
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		TableItem cs2 = new TableItem(table, 0);
//		cs2.setText(new String[] { "weather forecast2", "ics2" });
		
	
//		TableItem cs1 = new TableItem(table, 0);
//		cs1.setText(new String[] { "weather forecast1", "ics1" });
//		TableItem cs2 = new TableItem(table, 0);
//		cs2.setText(new String[] { "weather forecast2", "ics2" });
//		TableItem cs3 = new TableItem(table, 0);
//		cs3.setText(new String[] { "weather forecast3", "ics3" });
//		TableItem cs4 = new TableItem(table, 0);
//		cs4.setText(new String[] { "weather forecast4", "ics4" });
//		TableItem cs5 = new TableItem(table, 0);
//		cs5.setText(new String[] { "weather forecast5", "ics5" });
//		TableItem cs6 = new TableItem(table, 0);
//		cs6.setText(new String[] { "weather forecast6", "ics6" });
//		TableItem cs7 = new TableItem(table, 0);
//		cs7.setText(new String[] { "weather forecast7", "ics7" });
//		TableItem cs8 = new TableItem(table, 0);
//		cs8.setText(new String[] { "weather forecast8", "ics8" });
//		TableItem cs9 = new TableItem(table, 0);
//		cs9.setText(new String[] { "weather forecast9", "ics9" });
//		TableItem cs10 = new TableItem(table, 0);
//		cs10.setText(new String[] { "weather forecast10", "ics10" });
//		TableItem cs11 = new TableItem(table, 0);
//		cs11.setText(new String[] { "weather forecast11", "ics11" });
		
	
	}
	
	public void createTrustChainDiscovery(){
		TabItem chain = new TabItem(displayComposite, SWT.NONE);
		chain.setText("Trust chain Discovery");

		Composite child1 = new Composite(displayComposite, SWT.V_SCROLL);
	//	child1.setBounds(-12, 0,366, 188);
		child1.setLayout(new GridLayout());

		fr = makeFriendList();
		System.out.println("build friend list");
		
		createChainSashForm(child1);
		System.out.println("display BootGui");
//		host = new ShowView(child1);
//		trustChain = new TrustChain();
//		trustChain.createContents(child1);

		chain.setControl(child1);
	}
	
	public void initialRules(){
		
		java.sql.Connection conn;
		java.sql.Statement stmt;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(
			"jdbc:mysql://172.16.167.230:3306/project", "admin", "");
			stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("select chain from display_chain where serviceName = 'BookAirTicketService' and businessName ='Microsoft'");
//			System.out.println("connetct db "+resultSet.first());
			while(resultSet.next()){
				ObjectInputStream trustChain;
				try {
					trustChain = new ObjectInputStream(resultSet.getBinaryStream(1));
					try {
						CredentialChainResult credentialChainResult = (CredentialChainResult) trustChain.readObject();
						rule = credentialChainResult.getAssignCerUserRoleRightResultEx()+"\n"
						+credentialChainResult.getAssignCerUserRoleRightRuleEx()+"\n"
						+credentialChainResult.getAssignCerUserRoleRightRuleIssuer();
						rule2 = credentialChainResult.getAssignSerRankRoleRightResultEx()+"\n"
						+credentialChainResult.getAssignSerRankRoleRightRuleEx()+"\n"
						+credentialChainResult.getAssignSerRankRoleRightRuleIssuer();
						rule3 = credentialChainResult.getCerGroup_AdminRoleResultEx()+"\n"
						+credentialChainResult.getCerGroup_AdminRoleRuleEx()+"\n"
						+credentialChainResult.getCerGroup_AdminRoleRuleIssuer();
						rule4 = credentialChainResult.getCertificationUserRoleResultEx()+"\n"
						+credentialChainResult.getCertificationUserRoleRuleEx()+"\n"
						+credentialChainResult.getCertificationUserRoleRuleIssuer();
						rule5 = credentialChainResult.getSerPro_SerRoleResultEx()+"\n"
						+credentialChainResult.getSerPro_SerRoleRuleEx()+"\n"
						+credentialChainResult.getSerPro_SerRoleRuleIssuer();
						rule6 = credentialChainResult.getServiceRankResultEx()+"\n"
						+credentialChainResult.getServiceRankRuleEx()+"\n"
						+credentialChainResult.getServiceRankRuleIssuer();
						rule7 = credentialChainResult.getThirdPG_MemRoleResultEx()+"\n"
						+credentialChainResult.getThirdPG_MemRoleRuleEx()+"\n"
						+credentialChainResult.getThirdPG_MemRoleRuleIssuer();
						rule8 = credentialChainResult.getValidateExpression();
//						System.out.println("the rule is \n"+rule);
//						System.out.println("the rule2 is \n"+rule2);
//						System.out.println("the rule3 is \n"+rule3);
//						System.out.println("the rule4 is \n"+rule4);
//						System.out.println("the rule5 is \n"+rule5);
//						System.out.println("the rule6 is \n"+rule6);
//						System.out.println("the rule7 is \n"+rule7);
//						System.out.println("the rule8 is \n"+rule8);
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	            	updateServiceCertified();
	            }
	        });
	    }
	 
	private void updateResult2(){
    	Display.getDefault().syncExec(new Runnable() {
            public void run() {
//            	gui.updateCandidate(new int[]{10,9,8,7});
            	System.out.println("trust chain finished");
     			moveProgress(2);
     		
            }
        });
    }
 
 private void updateResult3(){
	 Display.getDefault().syncExec(new Runnable() {
            public void run() {
            
            	getRecommenderEvaluationLabel().setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
     			createRecommenderChart();
     			TabFolder tabFolder = getTabFolder();
     			tabFolder.setSelection(1);
            }
        });
	 
 }
	
	public void createRecommenderChart() {
		
		recommender = new TabItem(displayComposite, SWT.NONE);
		recommender.setText("Recommender Evaluation");

		SashForm rSashForm = new SashForm(displayComposite,SWT.HORIZONTAL);

		SashForm rlSashForm = new SashForm(rSashForm,SWT.VERTICAL);

		ViewForm rec = new ViewForm(rlSashForm, SWT.BORDER);
		rec.setLayout(new FillLayout());

		ToolBar toolBar = new ToolBar(rec, SWT.BORDER);

		evaButton = new ToolItem(toolBar, SWT.PUSH);
		evaButton.setText("Evaluation");
		evaButton.setImage(ImageFactory.loadImage(toolBar.getDisplay(),ImageFactory.EVALUATION));
		evaButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		evaButton.setEnabled(false);
		new Thread() {
		public void run() {
		task.startEvaluation();
		}
		}.start();
		}
		});

		rec.setTopLeft(toolBar);

		Composite rLabel = new Composite(rec,SWT.BORDER|SWT.WRAP);
		rLabel.setLayout(new GridLayout(2,false));

		monitorLabel[0] = new Label(rLabel, SWT.NONE);
		monitorLabel[0].setText("Monitor1");
		monitorLabel[1] = new Label(rLabel, SWT.NONE);
		monitorLabel[1].setText("Monitor2");

		monitorLabel[2] = new Label(rLabel, SWT.NONE);
		monitorLabel[2].setText("Monitor3");

		monitorLabel[3] = new Label(rLabel, SWT.NONE);
		monitorLabel[3].setText("Monitor4");

		monitorLabel[4] = new Label(rLabel, SWT.NONE);
		monitorLabel[4].setText("Monitor5");

		rec.setContent(rLabel);

		serviceAspect = new Table(rlSashForm,SWT.MULTI | SWT.FULL_SELECTION);
		serviceAspect.setHeaderVisible(true);
		serviceAspect.setLinesVisible(true);
		TableColumn co1 = new TableColumn(serviceAspect, SWT.NONE);
		co1.setText("Service");
		co1.setWidth(150);
		TableColumn co2 = new TableColumn(serviceAspect, SWT.NONE);
		co2.setText("Execution");
		co2.setWidth(80);

		//8.7 add....................................................
		TableColumn co3 = new TableColumn(serviceAspect, SWT.NONE);
		co3.setText("Latency");
		co3.setWidth(80);

		TableColumn co4 = new TableColumn(serviceAspect, SWT.NONE);
		co4.setText("Response");
		co4.setWidth(80);

		TableColumn co5 = new TableColumn(serviceAspect, SWT.NONE);
		co5.setText("Throughput");
		co5.setWidth(80);

		TableColumn co6 = new TableColumn(serviceAspect, SWT.NONE);
		co6.setText("Scalability");
		co6.setWidth(80);

		TableColumn co7 = new TableColumn(serviceAspect, SWT.NONE);
		co7.setText("Availability");
		co7.setWidth(80);

		TableColumn co8 = new TableColumn(serviceAspect, SWT.NONE);
		co8.setText("Accuracy");
		co8.setWidth(80);

		TableColumn co9 = new TableColumn(serviceAspect, SWT.NONE);
		co9.setText("SucceedRate");
		co9.setWidth(80);

		rlSashForm.setWeights(new int[] {22,78});

		Composite table = new Composite(rSashForm, SWT.EMBEDDED);
		table.setLayout(new GridLayout());


		XYSeriesCollection coll = new XYSeriesCollection();
		XYSeries series = new XYSeries("Monitor1");
		series.add(0, 0);
		coll.addSeries(series);

		XYSeries series2 = new XYSeries("Monitor2");
		series2.add(0, 0);
		coll.addSeries(series2);
		//8.7 update.................................................................
		XYSeries series3 = new XYSeries("Monitor3");
		series3.add(0, 0);
		coll.addSeries(series3);

		XYSeries series4 = new XYSeries("Monitor4");
		series4.add(0, 0);
		coll.addSeries(series4);

		XYSeries series5 = new XYSeries("Monitor5");
		series5.add(0, 0);
		coll.addSeries(series5);

		NumberAxis xAxis = new NumberAxis("Timesteps");
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(100);

		NumberAxis yAxis = new NumberAxis("Trust Value");
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(1);

		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, true);

		XYPlot plot = new XYPlot(coll, xAxis, yAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);


		// XYLineAndShapeRenderer lineandshaperenderer = new XYLineAndShapeRenderer();
		// plot.setRenderer(lineandshaperenderer);
		// lineandshaperenderer.setShape(new Rectangle(-3, -3, 1, 1));
		// plot.setRangeCrosshairPaint(Color.CYAN);
		// plot.setRangeCrosshairVisible(true);
		// plot.setRangeCrosshairStroke(new BasicStroke(7));

		// plot1.setDomainCrosshairVisible(true);
		chart = new HistoryChart("Third-party Monitor Evaluation",
		JFreeChart.DEFAULT_TITLE_FONT, plot, true);

		chart.initializeSeriesCollectionHistory(1);
		chart.putMasterSeriesCollection(coll);

		Frame frame = SWT_AWT.new_Frame(table);
		frame.add(new ChartPanel(chart));

		//ChartComposite frame = new ChartComposite(table, SWT.NONE, chart, true);
		rSashForm.setWeights(new int[] {30,70});
		recommender.setControl(rSashForm);

	}

	public void createReputationEvaluation() throws IOException{
		
		initiateTheReputationService();
		
		TabItem reputationItem = new TabItem(displayComposite, SWT.NONE);
		reputationItem.setText("Reputation Evaluation");
		
		SashForm rep = new SashForm(displayComposite,SWT.HORIZONTAL);
	
		ViewForm repViewForm = new ViewForm(rep, SWT.BORDER | SWT.VERTICAL);
		repViewForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		ToolBar repToolBar = new ToolBar(repViewForm, SWT.FLAT);
						
		button = new ToolItem(repToolBar, SWT.PUSH);
		button.setText("Evaluation");
		button.setImage(ImageFactory.loadImage(repToolBar.getDisplay(),ImageFactory.EVALUATION));

		repViewForm.setTopLeft(repToolBar);
		
		serviceReputation = new Table(repViewForm,SWT.MULTI | SWT.FULL_SELECTION);
		serviceReputation.setHeaderVisible(true);
		serviceReputation.setLinesVisible(true);
		TableColumn co1 = new TableColumn(serviceReputation, SWT.NONE);
		co1.setText("Service");
		co1.setWidth(120);
		TableColumn co2 = new TableColumn(serviceReputation, SWT.NONE);
		co2.setText("Reputation");
		co2.setWidth(80);
		for(Iterator<ServiceInfo> iterator = evaluateObject.getInitServiceList().getServiceList().iterator();iterator.hasNext();){
			ServiceInfo serviceInfo = iterator.next();
			String serviceName = serviceInfo.getServiceName();
			String businessName = serviceInfo.getBusinessName();
			String actualServiceName = businessName+"."+serviceName;
			TableItem tableItem = new TableItem(serviceReputation,0);
			tableItem.setText(new String[]{actualServiceName,null});
		}
		
		repViewForm.setContent(serviceReputation);
		
//		final SashForm reputationView = new SashForm(rep,SWT.VERTICAL);
		
		Composite starViewAndRankTable = new Composite(rep,SWT.NONE);
		starViewAndRankTable.setLayout(new FormLayout());

//		canvas = new Canvas(starViewAndRankTable, SWT.NONE);
//		final FormData fd_canvas = new FormData();
//		fd_canvas.left = new FormAttachment(0, 0);
//		fd_canvas.top = new FormAttachment(0, 0);
//		fd_canvas.right = new FormAttachment(75, 0);
//		fd_canvas.bottom = new FormAttachment(100, 0);
//		canvas.setLayoutData(fd_canvas);
//		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	//		canvas.setBounds(10,10,1000,1000);
		Composite graphComposite = new Composite(starViewAndRankTable,SWT.BORDER);
		final FormData fd_graphComposite = new FormData();
		fd_graphComposite.left = new FormAttachment(0, 0);
		fd_graphComposite.top = new FormAttachment(0, 0);
		fd_graphComposite.right = new FormAttachment(75, 0);
		fd_graphComposite.bottom = new FormAttachment(100, 0);
		graphComposite.setLayoutData(fd_graphComposite);
		
		graphComposite.setLayout(new FillLayout());
		
		final Graph g = new Graph(graphComposite, SWT.NONE);
		g.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);

		BufferedReader in = new BufferedReader(new FileReader(
				"g_recommender49.net"));
		String s;
		s = in.readLine();
		while ((s = in.readLine()) != null) {
			String[] result = s.split(" ");
			if (result.length == 4 && result[2].equals("ic")) {
				int id = Integer.parseInt(result[1]);
				SocialNetworkNode socialNetworkNode = new SocialNetworkNode(id);
				socialnetworkList.add(socialNetworkNode);
				if (result[3].equals("Green")) {
					socialNetworkNode.setMalicious(false);
				} else {
					socialNetworkNode.setMalicious(true);
				}
				GraphNode n = new GraphNode(g, SWT.NONE, result[1]);
				NodeInfoView nodeInfoView = new NodeInfoView(n);
				if (socialNetworkNode.isMalicious()) {
					nodeInfoView.setColor(ColorConstants.orange);
				} else {
					nodeInfoView.setColor(ColorConstants.green);
				}
				node2figure.put(socialNetworkNode, nodeInfoView);
				n.setNodeStyle(ZestStyles.NODES_NO_LAYOUT_RESIZE);
				graphnodeList.add(n);
			}
			if (result.length == 3) {
				int ID1 = Integer.parseInt(result[0]);
				int ID2 = Integer.parseInt(result[1]);
				GraphNode node1 = graphnodeList.get(ID1 - 1);
				GraphNode node2 = graphnodeList.get(ID2 - 1);
				new GraphConnection(g, SWT.NONE, node1, node2);
			}
		}

		// GraphNode n1 = new GraphNode(g, SWT.NONE);
		// GraphNode n2 = new GraphNode(g, SWT.NONE);
		// GraphNode n3 = new GraphNode(g, SWT.NONE);
		//
		// new GraphConnection(g, SWT.NONE, n1, n2);
		// new GraphConnection(g, SWT.NONE, n2, n3);
		// new GraphConnection(g, SWT.NONE, n3, n3);

		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		g.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.setClipping((Region) null);
				e.gc.setForeground(ColorConstants.black);
				if (isHasAgent()) {
					for (Iterator<Integer> iterator = agentList.iterator(); iterator
							.hasNext();) {

						Integer integer = iterator.next();
						AgentInfo agentInfo = agent2figure.get(integer);
						e.gc.drawImage(agentInfo.getAgentImage(), agentInfo
								.getCurrentPoint().x, agentInfo
								.getCurrentPoint().y);
					}
					// e.gc.drawImage(agentInfo1.getAgentImage(),
					// agentInfo1.getCurrentPoint().x,
					// agentInfo1.getCurrentPoint().y);
					// e.gc.drawImage(agentInfo2.getAgentImage(),
					// agentInfo2.getCurrentPoint().x,
					// agentInfo2.getCurrentPoint().y);
				}
			}
		});
		
		rankTable = new Table(starViewAndRankTable,SWT.MULTI | SWT.FULL_SELECTION);
		final FormData fd_rankTable = new FormData();
		fd_rankTable.left = new FormAttachment(75, 1);
		fd_rankTable.top = new FormAttachment(0, 0);
		fd_rankTable.right = new FormAttachment(100, 0);
		fd_rankTable.bottom = new FormAttachment(100, 0);
		rankTable.setLayoutData(fd_rankTable);
		rankTable.setLinesVisible(true);
		rankTable.setHeaderVisible(true);
		TableColumn rankTableco1 = new TableColumn(rankTable, SWT.NONE);
		rankTableco1.setText("ID");
		rankTableco1.setWidth(70);
		TableColumn rankTableco2 = new TableColumn(rankTable, SWT.NONE);
		rankTableco2.setText("Rating");
		rankTableco2.setWidth(70);
		rankTable.setVisible(true);
			
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				
				
				
				//establish the relation between node and it's figure
				for (Iterator<SocialNetworkNode> iterator = socialnetworkList
						.iterator(); iterator.hasNext();) {
					SocialNetworkNode socialNetworkNode = iterator.next();
					NodeInfoView nodeInfoView = node2figure
							.get(socialNetworkNode);
					if (nodeInfoView != null) {
						nodeInfoView.getGraphNode().setBackgroundColor(
								nodeInfoView.getColor());
						nodeInfoView.setPosition(nodeInfoView.getGraphNode()
								.getLocation());
					}
				}

				//this location need modify,destination point is not the point of the node id
				Point rootPoint = new Point(
						graphnodeList.get(displayUserid-1).getLocation().x, graphnodeList
								.get(displayUserid-1).getLocation().y);
				graphnodeList.get(displayUserid-1).setBackgroundColor(ColorConstants.lightBlue);
				System.out.println("userid is "+ displayUserid);
				
				for(int i=0;i<recommenderArray.length;i++){
					Point des = new Point(graphnodeList.get(recommenderArray[i]).getLocation().x,
							graphnodeList.get(recommenderArray[i]).getLocation().y);
					destinationPointList.add(des);
					agentList.add(new Integer(recommenderArray[i]+1));
				}
				

//				agentList.add(new Integer(2));
//				agentList.add(new Integer(3));
//				agentList.add(new Integer(4));
				//				
				// Image image1 = new Image(null,"resource/icons/agent.png");
				// agentInfo1 = new AgentInfo(image1,rootPoint,des1);
				// agentInfo1.setInterval(displayAgentStep);
				// Image image2 = new Image(null,"resource/icons/agent.png");
				// agentInfo2 = new AgentInfo(image2,rootPoint,des2);
				// agentInfo2.setInterval(displayAgentStep);

				int i = 0;
				for (Iterator<Integer> iterator = agentList.iterator(); iterator
						.hasNext();) {

					Integer integer = iterator.next();
					Image image = new Image(null, "resource/icons/agent.png");
					AgentInfo agentInfo = new AgentInfo(image, rootPoint,
							destinationPointList.get(i++));
					agentInfo.setInterval(displayAgentStep);
					agent2figure.put(integer, agentInfo);

				}

				new Thread() {
					public void run() {
						
						//display the process of node disappearing
						Hashtable<Integer,Integer> remainEdge = evaluateObject.getGIDandNum();
						System.out.println("HashTable is "+evaluateObject.getGIDandNum());
						deleteNodeId = new ArrayList<Integer>();
						for(int i=0;i<49;i++){
							if(remainEdge.get(new Integer(i))==null){
								deleteNodeId.add(new Integer(i+1));
							}
						}
						
						for(Iterator<Integer> iterator = deleteNodeId.iterator();iterator.hasNext();){
//							System.out.println("delete node "+iterator.next());
							displayNodeDisapear(iterator.next());
						}
						
						
//						displayNodeDisapear(13);
//						displayNodeDisapear(14);
//						displayNodeDisapear(15);
						
						//display the process of page rank
						updateNodeTrustRank();
						
						setHasAgent(true);
						// make the node flickering ,until the agent's running finished
						while (!isHasfinishedRunnigAgent()) {
//							for (int i = 0; i < 10; i++) {
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										g.setSelection(null);
									}
								});
								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										for(int i=0;i<recommenderArray.length;i++){
											graphnodeList.get(recommenderArray[i]).setBackgroundColor(
													ColorConstants.red);
										}
									}
								});

								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										for(int i=0;i<recommenderArray.length;i++){
											graphnodeList.get(recommenderArray[i]).setBackgroundColor(
													ColorConstants.blue);
										}
									}
								});

//							}

						}

						//recovery the selected node color
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
//								
								for(int i=0;i<recommenderArray.length;i++){
									graphnodeList.get(recommenderArray[i]).setBackgroundColor(
											ColorConstants.red);
								}
							}
						});
						
						//refresh the reputation table
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								serviceReputation.removeAll();
								int i=0;
								for(Iterator<ServiceInfo> iterator = evaluateObject.getServiceList().getServiceList().iterator();iterator.hasNext();){
									ServiceInfo serviceInfo = iterator.next();
									String serviceName = serviceInfo.getServiceName();
									String businessName = serviceInfo.getBusinessName();
									String actualServiceName = businessName+"."+serviceName;
									TableItem tableItem = new TableItem(serviceReputation,0);
									tableItem.setText(new String[]{actualServiceName,Double.toString(candidatesArray[i++])});
								}
									
							}
						});
						
	
					}
				}.start();

				//display the process of running agent
				new Thread() {
					public void run() {

						 while(!isHasAgent()){
													
						 }
						int steps = displayAgentStep;

						while (steps > 0) {

							steps--;
							for (Iterator<Integer> iterator = agentList
									.iterator(); iterator.hasNext();) {

								Integer integer = iterator.next();
								AgentInfo agentInfo = agent2figure.get(integer);
								agentInfo.increase();
							}
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									g.redraw();
								}

							});
						}

						while (steps < displayAgentStep) {
							steps++;
							for (Iterator<Integer> iterator = agentList
									.iterator(); iterator.hasNext();) {

								Integer integer = iterator.next();
								AgentInfo agentInfo = agent2figure.get(integer);
								agentInfo.decrease();
							}
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									g.redraw();
									//		                        	
									// DeferredUpdateManager
									// deferredUpdateManager =
									// (DeferredUpdateManager) parentFigure
									// .getUpdateManager();
									// deferredUpdateManager.performUpdate();
								}

							});
						}

						setHasfinishedRunnigAgent(true);
						// Display.getDefault().syncExec(new Runnable() {
						// public void run() {
						// g.redraw();
						// }
						// });
					}
				}.start();
			}
		});
		
//		reputationView.setWeights(new int[] {2,98});
		
		rep.setWeights(new int[] {20,80});
		reputationItem.setControl(rep);
	}

	public void initiateTheReputationService(){
		//connect database to get the data
		java.sql.Connection conn;
		java.sql.Statement stmt;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(
			"jdbc:mysql://172.16.167.230:3306/project", "admin", "");
			stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("select evaluateObj from display_evaluate where serviceName = 'BookAirTicketService'");
//			System.out.println("connetct db"+resultSet.first());
			
			
			//suppose the result keep in the first record
			resultSet.first();
			ObjectInputStream evaluate;
			try {
				evaluate = new ObjectInputStream(resultSet.getBinaryStream(1));
				try {
					evaluateObject = (EvaluateObject) evaluate.readObject();
					recommenderArray = evaluateObject.getRecommenders();
					candidatesArray = evaluateObject.getCandidates();
					displayUserid = evaluateObject.getUserid();
					System.out.println("canditates are "+candidatesArray);
					System.out.println("search reputation table ,user id is "+evaluateObject.getUserid());
					System.out.println("HashTable is "+evaluateObject.getGIDandNum());
					
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			while(resultSet.next()){
//				ObjectInputStream evaluate;
//				try {
//					evaluate = new ObjectInputStream(resultSet.getBinaryStream(1));
//					try {
//						EvaluateObject evaluateObject = (EvaluateObject) evaluate.readObject();
//						System.out.println("search reputation table ,user id is "+evaluateObject.getUserid());
//						
//					} catch (ClassNotFoundException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	// create ViewForm
	private void createTopViewForm() {

//		topViewForm = new SashForm(sashForm, SWT.VERTICAL);
		topViewForm = new SashForm(sashForm,SWT.VERTICAL);
//		topViewForm.setLayout(new FormLayout());
//		topViewForm.setLayout(new GridLayout());

		createButtons();
		createWorkflow();
		
		topViewForm.setWeights(new int[] { 10, 90 });
	}

	private void createButtons() {

		ToolBar toolBar = new ToolBar(topViewForm, SWT.NONE);
		final FormData fd_toolBar = new FormData();
		fd_toolBar.top = new FormAttachment(0, 0);
		fd_toolBar.left = new FormAttachment(0, 0);
		fd_toolBar.right = new FormAttachment(100, 0);
		fd_toolBar.bottom = new FormAttachment(0, 30);
		toolBar.setLayoutData(fd_toolBar);

		ToolItem playButton = new ToolItem(toolBar, SWT.RADIO);
		playButton.setText("Play");
		playButton.addSelectionListener(new ButtonListener(playButton));

		// ToolItem p = new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem pauseButton = new ToolItem(toolBar, SWT.RADIO);
		pauseButton.setText("Pause");
		pauseButton.addSelectionListener(new ButtonListener(pauseButton));

//		topViewForm.setTopLeft(toolBar);
	}

	public void createWorkflow() {

		bottomcanvas = new Canvas(topViewForm, SWT.BORDER);
		final FormData fd_bottomcanvas = new FormData();
		fd_bottomcanvas.top = new FormAttachment(0, 30);
		fd_bottomcanvas.left = new FormAttachment(0, 0);
		fd_bottomcanvas.right = new FormAttachment(100, 0);
		fd_bottomcanvas.bottom = new FormAttachment(100, 0);
		bottomcanvas.setLayoutData(fd_bottomcanvas);
	//	bottomcanvas.setBounds(0, 20, 1000, 450);
		LightweightSystem lws = new LightweightSystem(bottomcanvas);
		parentFigure= new Figure();
		parentFigure.setLayoutManager(new XYLayout());
		lws.setContents(parentFigure);

		client_Workflow = parseWorkflow();
		displayWorkflow(client_Workflow, parentFigure);
		
//		topViewForm.setContent(bottomcanvas);
	}

	public Client_Workflow parseWorkflow() {
		Client_Workflow cWorkflow = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"resource/workflow.out"));
			cWorkflow = (Client_Workflow) in.readObject();
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return cWorkflow;
	}

	public void displayWorkflow(Client_Workflow cWorkflow, Figure parent) {
		Client_Workflow client_Workflow = cWorkflow;
		Transform transform = new Transform();
		transform.setScale(980.0/1000,210.0/450);
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
					loop.setName(client_Loop.getName());
					loop.setComment(client_Loop.getName());

					// set the loop location
					Point newpoint = transform.getTransformed(new Point(client_Loop
							.getX(),client_Loop.getY()));
					parent.add(loop, new Rectangle(zoomoutRelative(newpoint.x-50),newpoint.y, 120, 120));
//					parent.add(loop, new Rectangle(zoomoutRelative(client_Loop
//							.getX() - 183), client_Loop.getY(), 120, 120));

					// 设置是否为终止点
					if (client_Loop.isIsfinished()) {
						
						loop.setFinishTask(true);
					}
					// 设置是否为起始点
					if (client_Loop.isIsstart()) {
						firstServiceName = client_Loop.getName();
						loop.setStartTask(true);
					}

					// 设置基本属性
					NodeFigure nodeFigure = loop;
					data2figure.put(client_Node, nodeFigure); // 为接下来寻找nextnode服务
				} else if (client_Node.getName().equals(CHOICENODE)) {
					Client_Choice client_Choice = (Client_Choice) client_Node;
					ChoiceFigure choice = new ChoiceFigure();
					choice.setName(client_Choice.getName());
					choice.setComment(client_Choice.getName());

					// set the loop location
					Point newpoint = transform.getTransformed(new Point(client_Choice
							.getX(),client_Choice.getY()));
					parent.add(choice, new Rectangle(zoomoutRelative(newpoint.x-50),newpoint.y, 95, 85));
//					parent.add(choice, new Rectangle(
//							zoomoutRelative(client_Choice.getX() - 183),
//							client_Choice.getY(), 95, 85));

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
						firstServiceName = client_Choice.getName();
						choice.setStartTask(true);
					}

					NodeFigure nodeFigure = choice;
					data2figure.put(client_Node, nodeFigure); // 为接下来寻找nextnode服务
				} else {
					Client_ServiceTask client_ServiceTask = (Client_ServiceTask) client_Node;
					TaskFigure service = new TaskFigure();
					service.setName(client_ServiceTask.getName());
					service.setComment(client_ServiceTask.getName());

					// set the loop location
					Point newpoint = transform.getTransformed(new Point(client_ServiceTask
							.getX(),client_ServiceTask.getY()));
					parent.add(service, new Rectangle(zoomoutRelative(newpoint.x-50),newpoint.y, 135, 60));
//					parent.add(service, new Rectangle(
//							zoomoutRelative(client_ServiceTask.getX() - 183),
//							client_ServiceTask.getY(), 135, 60));

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
						firstServiceName = client_ServiceTask.getName();
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
													outputport_count++ + 1));
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
		int newInteger = (int) (primitive * 1.2);
		return newInteger;
	}

	class ButtonListener extends SelectionAdapter {
		private ToolItem button;
//
//		Point currentStart;
//		Point currentEndPoint;
//		Point currentPoint;

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

					Client_Node firstNode = client_Workflow.getHasFirstNode();
					displayRunningWorkflow(firstNode);
				}
			}.start();
		}

		//display running workflow ,use data flow and flicker circle
		public void displayRunningWorkflow(Client_Node beginNode) {

			// Client_Node currentNode = beginNode;
//			if (!beginNode.isfinished) {
				Client_Node firstNode = beginNode;
				NodeFigure currentNodeFigure = data2figure.get(firstNode);
				if (firstNode.getName().equals("Choice")) {
					Client_Choice choice = (Client_Choice) firstNode;
					ChoiceFigure choiceFigure = (ChoiceFigure) currentNodeFigure;
					
					if(choice.isstart){
//						display_flicker_circle_on_first_node(choiceFigure);
						display_flicker_circle(choiceFigure);
					}
					
					else{
						display_flicker_circle(choiceFigure);
					}
					

					int outputNum = 0;
					for (Iterator<Client_ConditionalOutputPort> iterator = choice
							.getConditionalOutputPort().iterator(); iterator
							.hasNext();) {
						Client_ConditionalOutputPort client_ConditionalOutputPort = iterator
								.next();
						OutputPortFigure outputPortFigure = choiceFigure
								.getOutputportList().get(outputNum);
						Point parentPoint = choiceFigure.getLocation();
						Point startPoint = new Point(outputPortFigure
								.getBounds().getRight().x
								+ parentPoint.x, outputPortFigure.getBounds()
								.getRight().y
								+ parentPoint.y);
						for (Iterator<Client_Node> iterator2 = client_ConditionalOutputPort
								.getHasNextNodes().iterator(); iterator2
								.hasNext();) {
							Client_Node client_Node = iterator2.next();
							NodeFigure nextFigure = data2figure
									.get(client_Node);
							InputPortFigure inputPortFigure = nextFigure
									.getDefaultInputPort();
							Point parentPoint2 = nextFigure.getLocation();
							Point endPoint = new Point(inputPortFigure
									.getBounds().getLeft().x
									+ parentPoint2.x, inputPortFigure
									.getBounds().getLeft().y
									+ parentPoint2.y);

							displayPartWorkflow(nextFigure, startPoint, endPoint, client_Node);
						}
					}
				} else if (firstNode.getName().equals("Loop")) {
					Client_Loop loop = (Client_Loop) firstNode;
					LoopTaskFigure loopTaskFigure = (LoopTaskFigure) currentNodeFigure;
					
					if(loop.isstart){
//						display_flicker_circle_on_first_node(loopTaskFigure);
						display_flicker_circle(loopTaskFigure);
					}
					
					else{
						display_flicker_circle(loopTaskFigure);
					}

					OutputPortFigure outputPortFigure = loopTaskFigure
							.getDefaultOutputPort();
					Point parentPoint = loopTaskFigure.getLocation();
					Point startPoint = new Point(outputPortFigure.getBounds()
							.getRight().x
							+ parentPoint.x, outputPortFigure.getBounds()
							.getRight().y
							+ parentPoint.y);
					for (Iterator<Client_Node> iterator = loop.getOutputPort()
							.getHasNextNodes().iterator(); iterator.hasNext();) {
						Client_Node client_Node = iterator.next();
						NodeFigure nextFigure = data2figure.get(client_Node);
						InputPortFigure inputPortFigure = nextFigure
								.getDefaultInputPort();
						Point parentPoint2 = nextFigure.getLocation();
						Point endPoint = new Point(inputPortFigure.getBounds()
								.getLeft().x
								+ parentPoint2.x, inputPortFigure.getBounds()
								.getLeft().y
								+ parentPoint2.y);
						
						displayPartWorkflow(nextFigure, startPoint, endPoint, client_Node);
					}
				} else {
					Client_ServiceTask client_ServiceTask = (Client_ServiceTask) firstNode;
					TaskFigure taskFigure = (TaskFigure) currentNodeFigure;
					
					if(client_ServiceTask.isstart){
//						display_flicker_circle_on_first_node(taskFigure);
						display_flicker_circle(taskFigure);
					}
					
					else{
						display_flicker_circle(taskFigure);
					}

					OutputPortFigure outputPortFigure = taskFigure
							.getDefaultOutputPort();
					Point parentPoint = taskFigure.getLocation();
					Point startPoint = new Point(outputPortFigure.getBounds()
							.getRight().x
							+ parentPoint.x, outputPortFigure.getBounds()
							.getRight().y
							+ parentPoint.y);
					for (Iterator<Client_Node> iterator = client_ServiceTask
							.getOutputPort().getHasNextNodes().iterator(); iterator
							.hasNext();) {
						Client_Node client_Node = iterator.next();
						NodeFigure nextFigure = data2figure.get(client_Node);
						InputPortFigure inputPortFigure = nextFigure
								.getDefaultInputPort();
						Point parentPoint2 = nextFigure.getLocation();
						Point endPoint = new Point(inputPortFigure.getBounds()
								.getLeft().x
								+ parentPoint2.x, inputPortFigure.getBounds()
								.getLeft().y
								+ parentPoint2.y);

						displayPartWorkflow(nextFigure, startPoint, endPoint, client_Node);
					}
//				}
			}
		}

		//display part of workflow ,as one line segment 
		public void displayPartWorkflow(final Figure figure, final Point start,final Point end,final Client_Node client_Node) {
			new Thread() {
				public void run() {
					
					display_data_flow(start, end);
					
//					System.out.println(client_Node.getName()+" "+client_Node.getPreNodeNum());
					if(client_Node.getName().equals(CHOICENODE)){
						displayRunningWorkflow(client_Node);
					}
					else{
						if(hasfinishedPreNode.get(client_Node)==null){
							hasfinishedPreNode.put(client_Node, 1);
						}
						else{
							hasfinishedPreNode.put(client_Node, hasfinishedPreNode.get(client_Node)+1);
						}
						if(hasfinishedPreNode.get(client_Node)==client_Node.getPreNodeNum()){
							displayRunningWorkflow(client_Node);
						}
					}
				}
			}.start();
		}

		public void display_flicker_circle_on_first_node(Figure figure){
			
			final Rectangle area = figure.getBounds();
			final Point flitter_circle_location = new Point(
					area.getLeft().x + 15, area.getTop().y + 5);

			final Ellipse ellipse = new Ellipse();
			ellipse.setBackgroundColor(ColorConstants.red);
			
			while(!isBindingServicefinished()){
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						parentFigure.add(ellipse, new Rectangle(
								flitter_circle_location.x,
								flitter_circle_location.y, 20, 20));
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						parentFigure.remove(ellipse);
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void display_flicker_circle(Figure figure) {
			final Rectangle area = figure.getBounds();
			final Point flitter_circle_location = new Point(
					area.getLeft().x + 15, area.getTop().y + 5);

			final Ellipse ellipse = new Ellipse();
			ellipse.setBackgroundColor(ColorConstants.red);

			// new Thread() {
			// public void run() {
			for (int i = 0; i < 5; i++) {

				button.getDisplay().syncExec(new Runnable() {
					public void run() {
						parentFigure.add(ellipse, new Rectangle(
								flitter_circle_location.x,
								flitter_circle_location.y, 20, 20));
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				button.getDisplay().syncExec(new Runnable() {
					public void run() {
						parentFigure.remove(ellipse);
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// }
			// }.start();
			// button.getDisplay().syncExec(new Runnable() {
			// public void run() {
			// final Ellipse ellipse = new Ellipse();
			// ellipse.setBackgroundColor(ColorConstants.red);
			// parentFigure.add(ellipse,new
			// Rectangle(flitter_circle_location.x,flitter_circle_location
			// .y,20,20));
			// }
			// });
		}

		public void display_data_flow(final Point start, final Point end) {

			final double stepX = (end.x - start.x) / 10;
			final double stepY = (end.y - start.y) / 10;

			Point currentPoint = new Point(start.x + (int) stepX,
					start.y + (int) stepY);
			final Circle redCircle = new Circle(new Point(10,10),new Point(currentPoint.x-5,currentPoint.y-5));

			button.getDisplay().syncExec(new Runnable() {
				public void run() {
					parentFigure.add(redCircle,new Rectangle(redCircle.getStartPoint().x,redCircle.getStartPoint().y,
							redCircle.getPoint().x,redCircle.getPoint().y));
				}
			});

			// button.getDisplay().syncExec(new Runnable() {
			// public void run() {
			int timeStep = 9;
			while (timeStep > 0) {
				timeStep--;
				// parentFigure.remove(connection);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(currentPoint.x+" "+currentPoint.y);
				currentPoint = new Point(start.x
						+ (int) (stepX * (10 - timeStep)), start.y
						+ (int) (stepY * (10 - timeStep)));
				redCircle.setStartPoint(new Point(currentPoint.x-5,currentPoint.y-5));

				button.getDisplay().syncExec(new Runnable() {
					public void run() {

						parentFigure.add(redCircle,new Rectangle(redCircle.getStartPoint().x,redCircle.getStartPoint().y,
								redCircle.getPoint().x,redCircle.getPoint().y));

						DeferredUpdateManager deferredUpdateManager = (DeferredUpdateManager) parentFigure
								.getUpdateManager();
						deferredUpdateManager.performUpdate();
					}
				});
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			button.getDisplay().syncExec(new Runnable() {
				public void run() {
					parentFigure.remove(redCircle);
				}
			});
			
		}

	}


	
	public void setButtonState(boolean bFlag) {
		startButton.setEnabled(bFlag);
		//stopButton.setEnabled(!bFlag);
	}
	
	public Label getMonitorLabel(int i) {
		return monitorLabel[i];
	}

	public Label getSearchingServicesLabel() {
		return searchingServicesLabel;
	}

	public Label getTrustChainDiscoveryLabel() {
		return trustChainDiscoveryLabel;
	}

	public Label getRecommenderEvaluationLabel() {
		return recommenderEvaluationLabel;
	}

	public Label getReputationEvaluationLabel() {
		return reputationEvaluationLabel;
	}

	public TabFolder getTabFolder() {
		return displayComposite;
	}
	
	public void moveProgress(int progress){
		if(!bar.isDisposed())
			bar.setSelection(progress);
	}
	
	public void updateCandidate(int[] index){
		table.remove(index);
		
	}
	
	public void updateServiceAspect(){

		TableItem cs1 = new TableItem(serviceAspect, 0);
		cs1.setText(new String[] { "weather forecast1", "0.9" });
		TableItem cs2 = new TableItem(serviceAspect, 0);
		cs2.setText(new String[] { "weather forecast2", "0.8" });
		TableItem cs3 = new TableItem(serviceAspect, 0);
		cs3.setText(new String[] { "weather forecast3", "0.7" });
		TableItem cs4 = new TableItem(serviceAspect, 0);
		cs4.setText(new String[] { "weather forecast4", "0.6" });
		TableItem cs5 = new TableItem(serviceAspect, 0);
		cs5.setText(new String[] { "weather forecast5", "0.5" });
		TableItem cs6 = new TableItem(serviceAspect, 0);
		cs6.setText(new String[] { "weather forecast6", "0.4" });
	}
	
	public void updateServiceCertified(){

		TableItem cs1 = new TableItem(serviceCertified, 0);
		cs1.setText(new String[] { "weather forecast1", "yes" });
		TableItem cs2 = new TableItem(serviceCertified, 0);
		cs2.setText(new String[] { "weather forecast2", "yes" });
		TableItem cs3 = new TableItem(serviceCertified, 0);
		cs3.setText(new String[] { "weather forecast3", "yes" });
		TableItem cs4 = new TableItem(serviceCertified, 0);
		cs4.setText(new String[] { "weather forecast4", "yes" });
		TableItem cs5 = new TableItem(serviceCertified, 0);
		cs5.setText(new String[] { "weather forecast5", "yes" });
		TableItem cs6 = new TableItem(serviceCertified, 0);
		cs6.setText(new String[] { "weather forecast6", "yes" });
		TableItem cs7 = new TableItem(serviceCertified, 0);
		cs7.setText(new String[] { "weather forecast7", "no" });
		TableItem cs8 = new TableItem(serviceCertified, 0);
		cs8.setText(new String[] { "weather forecast8", "no" });
		TableItem cs9 = new TableItem(serviceCertified, 0);
		cs9.setText(new String[] { "weather forecast9", "no" });
		TableItem cs10 = new TableItem(serviceCertified, 0);
		cs10.setText(new String[] { "weather forecast10", "no" });
		TableItem cs11 = new TableItem(serviceCertified, 0);
		cs11.setText(new String[] { "weather forecast11", "no" });
	}
	
	private SashForm csashForm = null;

	private ViewForm leftViewForm = null;

	private ToolBar tableToolBar = null;

	private ShowView host = null;
	
	private TrustChain trustChain = null;

	
	public Table serviceCertified;
	public Table serviceReputation;
	
	public void createChainSashForm(Composite sShell) {
		csashForm = new SashForm(sShell, SWT.BORDER);
		// sashForm.setLayout(new FillLayout());
		csashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createLeftViewForm();
//		// createTableViewForm1();
		createRightViewForm();

		csashForm.setWeights(new int[] { 28, 72 });

	}

	//
	private Composite createRightViewForm() {

		Composite child1 = new Composite(csashForm, SWT.NONE);			//contains the whole trustchain view
		child1.setLayout(new FormLayout());

		initialRules();						//initial the rules
		
		//the toppartcanvas
		trustChainCanvas = new Canvas(child1, SWT.NONE);
		final FormData fd_trustChainCanvas = new FormData();
		fd_trustChainCanvas.top = new FormAttachment(0, 0);
		fd_trustChainCanvas.left = new FormAttachment(0, 0);
		fd_trustChainCanvas.right = new FormAttachment(0, 862);
		fd_trustChainCanvas.bottom = new FormAttachment(0, 428);
		trustChainCanvas.setLayoutData(fd_trustChainCanvas);
		trustChainCanvas.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
//		trustChainCanvas.setBounds(0, 0, 862, 428);

		LightweightSystem lws = new LightweightSystem(trustChainCanvas);
		
		
		//the bottomcanvas
		tcParentFigure = new Figure();
		tcParentFigure.setLayoutManager(new XYLayout());
		lws.setContents(tcParentFigure);
		
		trustRulesCanvas = new Canvas(child1, SWT.BORDER);
		final FormData fd_trustRulesCanvas = new FormData();
		fd_trustRulesCanvas.top = new FormAttachment(0, 428);
		fd_trustRulesCanvas.left = new FormAttachment(0, 0);
		fd_trustRulesCanvas.right = new FormAttachment(0, 846);
		fd_trustRulesCanvas.bottom = new FormAttachment(0, 556);
		trustRulesCanvas.setLayoutData(fd_trustRulesCanvas);
//		trustRulesCanvas.setBounds(0, 428, 846, 148);
		LightweightSystem lws2 = new LightweightSystem(trustRulesCanvas);
		tcParentFigure2 = new Figure();
		tcParentFigure2.setLayoutManager(new XYLayout());
		lws2.setContents(tcParentFigure2);

//		host = new ShowView(child1, fr,this);

		return child1;

	}
	
	public void displayRunningAgent(final Figure figure,Point point){
		currentPoint = point;
		Display.getDefault().syncExec(new Runnable() {
            public void run() {
        		tcParentFigure.add(figure,new Rectangle(currentPoint.x-12,currentPoint.y-12,24,24));
            }
        });
	}
	
	public void displayFlickerFigure(final Figure figure,final Figure parent){
		for(int k=0;k<3;k++){
			try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            Display.getDefault().syncExec(new Runnable() {
                public void run() {
                	parent.remove(figure);
                }
            });
            
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            Display.getDefault().syncExec(new Runnable() {
                public void run() {
                	parent.add(figure);
                }
            });
		}
	}
	
	public void displayChangingcolorLabel(final org.eclipse.draw2d.Label label,final Color oriColor,final Color newColor){
		for(int k=0;k<3;k++){
			try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            Display.getDefault().syncExec(new Runnable() {
                public void run() {
                	label.setForegroundColor(oriColor);
                }
            });
            
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            Display.getDefault().syncExec(new Runnable() {
                public void run() {
                	label.setForegroundColor(newColor);
                }
            });
		}
	}
	
	public void displayRunningFigure(final Figure figure,Point startPoint,Point endPoint){
		currentLocation.setLocation(startPoint.x, startPoint.y);
    	stepX = (endPoint.x - startPoint.x)/10.0;
    	stepY = (endPoint.y - startPoint.y)/10.0;
    	
		for(int j=0;j<11;j++){
			try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            currentLocation.x = startPoint.x + (int)(stepX*j);
            currentLocation.y = startPoint.y + (int)(stepY*j);
            
            Display.getDefault().syncExec(new Runnable() {
                public void run() {
//                	figure.setPoint(figure);
            		tcParentFigure.add(figure,new Rectangle(currentLocation.x-12,currentLocation.y-12,24,24));
                }
            });
            
		}
	}


	// create ViewForm
	private void createLeftViewForm() {
		leftViewForm = new ViewForm(csashForm, SWT.BORDER | SWT.VERTICAL);
		leftViewForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		createToolBar();

	}

	// create the toolbar
	private void createToolBar() {
		tableToolBar = new ToolBar(leftViewForm, SWT.FLAT);


		final ToolItem evaluation = new ToolItem(tableToolBar, SWT.PUSH);
		evaluation.setText("Evaluation");
		evaluation.setImage(ImageFactory.loadImage(tableToolBar.getDisplay(),ImageFactory.EVALUATION));

		evaluation.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				evaButton.setEnabled(false);
				new Thread() {
                    public void run() {
                    	
                    	Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	
    	    					//first step
    	    					hostFigure = new HostFigure(new Point(408, 202));
    	    					tcParentFigure.add(hostFigure,new Rectangle(408, 202, 32, 32));
    	    					
    	    					tcParentFigure.add(hostdomainName,new Rectangle(368, 202, 32, 32));
    	    					
    	    					databaseFigure.setPoint(new Point(414,242));
    	    					tcParentFigure.add(databaseFigure,new Rectangle(414,242,18,22));
    	    					
//    	    					Label label = new Label("Test");
//    	    					label.setSize(127, 49);
    	    					bubblesFigure = new BubblesFigure(new Point(426,153));
    	    					tcParentFigure.add(bubblesFigure,new Rectangle(426,153,127,49));
    	    					
    	    					label.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
    	    					tcParentFigure.add(label,new Rectangle(426,153,127,49));
    	    					
    	    					helpFigure = new HelpFigure(new Point(529,168));
    	    					tcParentFigure.add(helpFigure,new Rectangle(529,166,16,16));
                            }
                        });
                    	
    					
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	label2.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(label2,new Rectangle(45, 2, 215, 27));
                            	label2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                            	
                            	label3.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(label3,new Rectangle(293, 2, 215, 27));
                            	
                            	label4.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(label4,new Rectangle(106, 46, 235, 27));
                            	
                            	label5.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(label5,new Rectangle(356, 46, 235, 27));
                            	
                            	label6.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(label6,new Rectangle(216, 90, 215, 27));
                            	
                            	label7.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(label7,new Rectangle(475, 90, 235, 27));
                            	
                            	label8.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(label8,new Rectangle(605, 46, 235, 27));
                    
                            }
                        });
                    	

    					//second step
    					
                    	//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                    	Display.getDefault().syncExec(new Runnable() {
                            public void run() {
//                            	parentFigure.remove(bubblesFigure);
//                            	parentFigure.remove(label);
//                            	parentFigure.remove(helpFigure);
                            	
                            	hostFigure.setPoint(new Point(408, 75));
    	    					tcParentFigure.add(hostFigure,new Rectangle(408, 75, 32, 32));
    	    					databaseFigure.setPoint(new Point(414,115));
    	    					tcParentFigure.add(databaseFigure,new Rectangle(414,115,18,22));
    	    					tcParentFigure.add(hostdomainName,new Rectangle(368, 75, 32, 32));
    	    					
    	    					bubblesFigure.setPoint(new Point(426,26));
    	    					tcParentFigure.add(bubblesFigure,new Rectangle(426,26,127,49));
    	    					
    	    					tcParentFigure.add(label,new Rectangle(426,26,127,49));
    	    					
    	    					helpFigure.setPoint(new Point(529,41));
    	    					tcParentFigure.add(helpFigure,new Rectangle(529,41,16,16));
    	    					
    	    					friendFigure = new FriendsFigure(new Point(98, 202));
    	    					tcParentFigure.add(friendFigure,new Rectangle(98, 202, 32, 32));
    	    					databaseFigure2.setPoint(new Point(104,242));
    	    					tcParentFigure.add(databaseFigure2,new Rectangle(104,242,18,22));
    	    					tcParentFigure.add(frienddomainName,new Rectangle(58, 202, 32, 32));
    	    					
    	    					friendFigure2 = new FriendsFigure(new Point(709, 202));
    	    					tcParentFigure.add(friendFigure2,new Rectangle(709, 202, 32, 32));
    	    					databaseFigure3.setPoint(new Point(716,242));
    	    					tcParentFigure.add(databaseFigure3,new Rectangle(716,242,18,22));
    	    					tcParentFigure.add(frienddomainName2,new Rectangle(669, 202, 32, 32));
    	    					
    	    					secondpolylineConnection = new PolylineConnection();
    	    					secondpolylineConnection.setStart(new Point(424,91));
    	    					secondpolylineConnection.setEnd(new Point(114,218));
    	    					
    	    					secondpolylineConnection2 = new PolylineConnection();
    	    					secondpolylineConnection2.setStart(new Point(424,91));
    	    					secondpolylineConnection2.setEnd(new Point(725,218));
    	    					
    	    					tcParentFigure.add(secondpolylineConnection);
    	    					tcParentFigure.add(secondpolylineConnection2);
    	    			
                            }
                        });

                    	try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        Display.getDefault().asyncExec(new Runnable() {
//                            public void run() {
//                            	agentFigure.setPoint(new Point(100,100));
//                            	agentFigure2.setPoint(new Point(200,200));
//                        		parentFigure.add(agentFigure,new Rectangle(100,100,24,24));
//                        		parentFigure.add(agentFigure2,new Rectangle(200,200,24,24));
//                            }
//                        });
                        
                    	startPoint = hostFigure.getPoint();
                    	endPoint = friendFigure.getPoint();
                    	endPoint2 = friendFigure2.getPoint();
                    	agentStepX = (endPoint.x - startPoint.x)/10.0;
                    	agentStepY = (endPoint.y - startPoint.y)/10.0;
                    	agentStepX2 = (endPoint2.x - startPoint.x)/10.0;
                    	agentStepY2 = (endPoint2.y - startPoint.y)/10.0;
//                    	currentPoint = startPoint;
//                    	currentPoint2 = startPoint;
    					for(int i=0;i<11;i++){
    						try {
	                            Thread.sleep(100);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        currentPoint.x = startPoint.x + (int)(agentStepX*i);
	                        currentPoint.y = startPoint.y + (int)(agentStepY*i);
	                        
	                        currentPoint2.x = startPoint.x + (int)(agentStepX2*i);
	                        currentPoint2.y = startPoint.y + (int)(agentStepY2*i);
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	agentFigure.setPoint(new Point(currentPoint.x-12,currentPoint.y-12));
	                            	agentFigure2.setPoint(new Point(currentPoint2.x-12,currentPoint2.y-12));
	                        		tcParentFigure.add(agentFigure,new Rectangle(currentPoint.x-12,currentPoint.y-12,24,24));
	                        		tcParentFigure.add(agentFigure2,new Rectangle(currentPoint2.x-12,currentPoint2.y-12,24,24));
	                            }
	                        });
	                        
    					}
    					
    					//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					//search database , display the result of searching
    					for(int k=0;k<3;k++){
    						try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
//	                            	label2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
//	                            	label3.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            	
	                            	label6.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            	label7.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            	label8.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            }
	                        });
	                        
	                        try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
//	                            	label2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
//	                            	label3.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	                            	
	                            	label6.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	                            	label7.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	                            	label8.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	                            }
	                        });
    					}
    					
    					//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					//search database , display the result of searching
    					for(int k=0;k<3;k++){
    						try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	label3.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            }
	                        });
	                        
	                        try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	label3.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	                            }
	                        });
    					}
    					
    					//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					//display the bubble of friend3 node
    					//there is no evident for the rule
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	
                            	bubblesFigure3 = new BubblesFigure(new Point(727,153));
    	    					tcParentFigure.add(bubblesFigure3,new Rectangle(727,153,127,49));
    	    					
    	    					label3forbubble.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
    	    					tcParentFigure.add(label3forbubble,new Rectangle(727,153,127,49));
    	    					
    	    					helpFigure3 = new HelpFigure(new Point(830,168));
    	    					tcParentFigure.add(helpFigure3,new Rectangle(830,168,16,16));
                            }
                        });
    					
    					//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	CustomRectangle customRectangle = new CustomRectangle(new Point(216,91),new Point(494,20));
                            	tcParentFigure2.add(customRectangle,new Rectangle(216,91,495,21));
                            	
//                            	merge5and6.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
//                            	parentFigure2.add(merge5and6,new Rectangle(490,92,235,27));
                            }
                        });
    					
    					//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                        Display.getDefault().syncExec(new Runnable() {
                            public void run() {
//                            	CustomRectangle customRectangle = new CustomRectangle(new Point(216,119),new Point(494,20));
//                            	parentFigure2.add(customRectangle,new Rectangle(216,119,495,21));
                            	
                            	merge5and6.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(merge5and6,new Rectangle(490,64,235,27));
                            }
                        });
    					
                        
                      //每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	virtualPolylineConnection = new PolylineConnection();
    	    					virtualPolylineConnection.setLineStyle(SWT.LINE_DOT);
    	    					virtualPolylineConnection.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

    	    					virtualPolylineConnection.setStart(new Point(114,218));
    	    					virtualPolylineConnection.setEnd(new Point(725,218));

    	    					tcParentFigure.add(virtualPolylineConnection);

                            }
                        });
    					
    					msgStartPoint = friendFigure.getPoint();
    					msgEndPoint = friendFigure2.getPoint();
                    	msgStepX = (msgEndPoint.x - msgStartPoint.x)/10.0;
                    	msgStepY = (msgEndPoint.y - msgStartPoint.y)/10.0;
                    	
    					for(int j=0;j<11;j++){
    						try {
	                            Thread.sleep(100);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        msgCurrentPoint.x = msgStartPoint.x + (int)(msgStepX*j);
	                        msgCurrentPoint.y = msgStartPoint.y + (int)(msgStepY*j);
	                        
	                        msgCurrentPoint2.x = msgEndPoint.x + (int)(-msgStepX*j);
	                        msgCurrentPoint2.y = msgEndPoint.y + (int)(-msgStepY*j);
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	msgFigure.setPoint(msgCurrentPoint);
	                            	msgFigure2.setPoint(msgCurrentPoint2);
	                        		tcParentFigure.add(msgFigure,new Rectangle(msgCurrentPoint.x-12,msgCurrentPoint.y-12,24,24));
	                        		tcParentFigure.add(msgFigure2,new Rectangle(msgCurrentPoint2.x-12,msgCurrentPoint2.y-12,24,24));
	                            }
	                        });
	                        
    					}

    					
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                        		tcParentFigure.remove(msgFigure);
                        		tcParentFigure.remove(msgFigure2);
                        		tcParentFigure.remove(virtualPolylineConnection);
                            }
                        });
    					
//                    	//每隔1秒一次循环，用于模拟表示一个需时较长的任务

    					//third step
    					
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    	Display.getDefault().syncExec(new Runnable() {
                            public void run() {
//                            	parentFigure.remove(secondpolylineConnection);
//                            	parentFigure.remove(secondpolylineConnection2);
                            	
    	    					friendFigure3 = new FriendsFigure(new Point(260, 349));
    	    					tcParentFigure.add(friendFigure3,new Rectangle(260, 349, 32, 32));
    	    					databaseFigure4.setPoint(new Point(266,389));
    	    					tcParentFigure.add(databaseFigure4,new Rectangle(266,389,18,22));
    	    					tcParentFigure.add(frienddomainName3,new Rectangle(220, 349, 32, 32));
    	    					
    	    					PolylineConnection connection4 = new PolylineConnection();
    	    					connection4.setStart(new Point(114,218));
    	    					connection4.setEnd(new Point(276,365));

    	    					tcParentFigure.add(connection4);
                            }
                        });
                    	
                    	startPoint = friendFigure.getPoint();
                    	endPoint = friendFigure3.getPoint();
                    	agentStepX = (endPoint.x - startPoint.x)/10.0;
                    	agentStepY = (endPoint.y - startPoint.y)/10.0;
                    	agentStepX2 = (endPoint2.x - startPoint.x)/10.0;
                    	agentStepY2 = (endPoint2.y - startPoint.y)/10.0;
//                    	currentPoint = startPoint;
//                    	currentPoint2 = startPoint;
    					for(int i=0;i<11;i++){
    						try {
	                            Thread.sleep(100);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        currentPoint.x = startPoint.x + (int)(agentStepX*i);
	                        currentPoint.y = startPoint.y + (int)(agentStepY*i);
	                        
	                        currentPoint2.x = startPoint.x + (int)(agentStepX2*i);
	                        currentPoint2.y = startPoint.y + (int)(agentStepY2*i);
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	agentFigure.setPoint(new Point(currentPoint.x-12,currentPoint.y-12));
//	                            	agentFigure2.setPoint(new Point(currentPoint2.x-12,currentPoint2.y-12));
	                        		tcParentFigure.add(agentFigure,new Rectangle(currentPoint.x-12,currentPoint.y-12,24,24));
//	                        		parentFigure.add(agentFigure2,new Rectangle(currentPoint2.x-12,currentPoint2.y-12,24,24));
	                            }
	                        });
	                        
    					}
    					
    					//search database , display the result of searching
    					for(int k=0;k<3;k++){
    						try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	label5.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            }
	                        });
	                        
	                        try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	label5.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	                            }
	                        });
    					}
    					
    					try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
    					//there is no evident for the rule
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	
                            	bubblesFigure4 = new BubblesFigure(new Point(278,300));
    	    					tcParentFigure.add(bubblesFigure4,new Rectangle(278,300,127,49));
    	    					
    	    					label5forbubble.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
    	    					tcParentFigure.add(label5forbubble,new Rectangle(278,300,127,49));
    	    					
    	    					helpFigure4 = new HelpFigure(new Point(401,315));
    	    					tcParentFigure.add(helpFigure4,new Rectangle(401,315,16,16));
                            }
                        });
    					
    					//pause for a second , represent the process of discovering rule
    					try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					//display the process of reasoning rule4
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	deductionLine2 = new PolylineConnection();
                            	PolygonDecoration arrow2 = new PolygonDecoration();
            					arrow2.setTemplate(PolygonDecoration.TRIANGLE_TIP);
            					arrow2.setScale(5, 5);
            					deductionLine2.setTargetDecoration(arrow2);
            					deductionLine2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
            					
                            	deductionLine2.setStart(new Point(490,91));
                            	deductionLine2.setEnd(new Point(490,69));
                            	tcParentFigure2.add(deductionLine2);
                            }
                        });
    					
    					try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					for(int k=0;k<3;k++){
    						try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	tcParentFigure2.remove(deductionLine2);
//	                            	deductionLine2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            }
	                        });
	                        
	                        try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	tcParentFigure2.add(deductionLine2);
//	                            	deductionLine2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	                            }
	                        });
    					}
    					
    					//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
                        //wenhao flickering
                        displayFlickerFigure(helpFigure4,tcParentFigure);
                        
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	tcParentFigure.remove(helpFigure4);
                            }
                        });
    					
    					
    					//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
    					//display the process of exchange information
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	virtualPolylineConnection = new PolylineConnection();
    	    					virtualPolylineConnection.setLineStyle(SWT.LINE_DOT);
    	    					virtualPolylineConnection.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
    	    					
    	    					virtualPolylineConnection.setStart(new Point(276,365));
    	    					virtualPolylineConnection.setEnd(new Point(725,218));
    	    					
    	    					tcParentFigure.add(virtualPolylineConnection);

                            }
                        });
    					
    					msgStartPoint = friendFigure3.getPoint();
    					msgEndPoint = friendFigure2.getPoint();
                    	msgStepX = (msgEndPoint.x - msgStartPoint.x)/10.0;
                    	msgStepY = (msgEndPoint.y - msgStartPoint.y)/10.0;
                    	
    					for(int j=0;j<11;j++){
    						try {
	                            Thread.sleep(100);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        msgCurrentPoint.x = msgStartPoint.x + (int)(msgStepX*j);
	                        msgCurrentPoint.y = msgStartPoint.y + (int)(msgStepY*j);
	                        
	                        msgCurrentPoint2.x = msgEndPoint.x + (int)(-msgStepX*j);
	                        msgCurrentPoint2.y = msgEndPoint.y + (int)(-msgStepY*j);
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	msgFigure.setPoint(msgCurrentPoint);
	                            	msgFigure2.setPoint(msgCurrentPoint2);
	                        		tcParentFigure.add(msgFigure,new Rectangle(msgCurrentPoint.x-12,msgCurrentPoint.y-12,24,24));
	                        		tcParentFigure.add(msgFigure2,new Rectangle(msgCurrentPoint2.x-12,msgCurrentPoint2.y-12,24,24));
	                            }
	                        });
	                        
    					}
    					
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                        		tcParentFigure.remove(msgFigure);
                        		tcParentFigure.remove(msgFigure2);
                        		tcParentFigure.remove(virtualPolylineConnection);
                            }
                        });
                    	
                    	//the forth step
    					
//                    	//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	
                            	friendFigure4 = new FriendsFigure(new Point(562, 349));
    	    					tcParentFigure.add(friendFigure4,new Rectangle(562, 349, 32, 32));
    	    					databaseFigure5.setPoint(new Point(568,389));
    	    					tcParentFigure.add(databaseFigure5,new Rectangle(568,389,18,22));
    	    					tcParentFigure.add(frienddomainName4,new Rectangle(462, 349, 102, 32));
    	    					
    	    					PolylineConnection connection3 = new PolylineConnection();
    	    					connection3.setStart(new Point(276,365));
    	    					connection3.setEnd(new Point(578,365));
    	    					
    	    					tcParentFigure.add(connection3);
                            }
                        });
                        
                        startPoint = friendFigure3.getPoint();
                    	endPoint = friendFigure4.getPoint();
                    	agentStepX = (endPoint.x - startPoint.x)/10.0;
                    	agentStepY = (endPoint.y - startPoint.y)/10.0;
                    	agentStepX2 = (endPoint2.x - startPoint.x)/10.0;
                    	agentStepY2 = (endPoint2.y - startPoint.y)/10.0;
//                    	currentPoint = startPoint;
//                    	currentPoint2 = startPoint;
    					for(int i=0;i<11;i++){
    						try {
	                            Thread.sleep(100);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        currentPoint.x = startPoint.x + (int)(agentStepX*i);
	                        currentPoint.y = startPoint.y + (int)(agentStepY*i);
	                        
	                        currentPoint2.x = startPoint.x + (int)(agentStepX2*i);
	                        currentPoint2.y = startPoint.y + (int)(agentStepY2*i);
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	agentFigure.setPoint(new Point(currentPoint.x-12,currentPoint.y-12));
//	                            	agentFigure2.setPoint(new Point(currentPoint2.x-12,currentPoint2.y-12));
	                        		tcParentFigure.add(agentFigure,new Rectangle(currentPoint.x-12,currentPoint.y-12,24,24));
//	                        		parentFigure.add(agentFigure2,new Rectangle(currentPoint2.x-12,currentPoint2.y-12,24,24));
	                            }
	                        });
	                        
    					}
    					
    					//每隔1秒一次循环，用于模拟表示一个需时较长的任务
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					//search the database
    					for(int k=0;k<3;k++){
    						try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	label4.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            }
	                        });
	                        
	                        try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	label4.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	                            }
	                        });
    					}
    					
    					//generate the second temperature rule
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	CustomRectangle customRectangle = new CustomRectangle(new Point(106,49),new Point(730,20));
                            	tcParentFigure2.add(customRectangle,new Rectangle(106,49,731,21));
                            	
//                            	merge3and4and7.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
//                            	parentFigure2.add(merge3and4and7,new Rectangle(370,50,235,27));
                            }
                        });
    					
    					try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
    					//generate the second temperature rule
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
//                            	CustomRectangle customRectangle = new CustomRectangle(new Point(106,77),new Point(730,20));
//                            	parentFigure2.add(customRectangle,new Rectangle(106,77,731,21));
                            	
                            	merge3and4and7.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
                            	tcParentFigure2.add(merge3and4and7,new Rectangle(370,22,235,27));
                            }
                        });
    					
    					try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                        Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	virtualPolylineConnection = new PolylineConnection();
    	    					virtualPolylineConnection.setLineStyle(SWT.LINE_DOT);
    	    					virtualPolylineConnection.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
    	    					
    	    					virtualPolylineConnection.setStart(new Point(578,365));
    	    					virtualPolylineConnection.setEnd(new Point(725,218));
    	    					
    	    					tcParentFigure.add(virtualPolylineConnection);

                            }
                        });

    					

    					//message exchange
    					msgStartPoint = friendFigure4.getPoint();
    					msgEndPoint = friendFigure2.getPoint();
                    	msgStepX = (msgEndPoint.x - msgStartPoint.x)/10.0;
                    	msgStepY = (msgEndPoint.y - msgStartPoint.y)/10.0;
                    	
    					for(int j=0;j<11;j++){
    						try {
	                            Thread.sleep(100);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        msgCurrentPoint.x = msgStartPoint.x + (int)(msgStepX*j);
	                        msgCurrentPoint.y = msgStartPoint.y + (int)(msgStepY*j);
	                        
	                        msgCurrentPoint2.x = msgEndPoint.x + (int)(-msgStepX*j);
	                        msgCurrentPoint2.y = msgEndPoint.y + (int)(-msgStepY*j);
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	msgFigure.setPoint(msgCurrentPoint);
	                            	msgFigure2.setPoint(msgCurrentPoint2);
	                        		tcParentFigure.add(msgFigure,new Rectangle(msgCurrentPoint.x-12,msgCurrentPoint.y-12,24,24));
	                        		tcParentFigure.add(msgFigure2,new Rectangle(msgCurrentPoint2.x-12,msgCurrentPoint2.y-12,24,24));
	                            }
	                        });
	                        
    					}
    					
    					
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                        		tcParentFigure.remove(msgFigure);
                        		tcParentFigure.remove(msgFigure2);
                        		tcParentFigure.remove(virtualPolylineConnection);
                            }
                        });
    					
    					//display the process of reasoning
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	deductionLine = new PolylineConnection();
                            	PolygonDecoration arrow = new PolygonDecoration();
            					arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
            					arrow.setScale(5, 5);
            					deductionLine.setTargetDecoration(arrow);
            					deductionLine.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
            					
                            	deductionLine.setStart(new Point(370,49));
                            	deductionLine.setEnd(new Point(370,22));
                            	tcParentFigure2.add(deductionLine);
                            }
                        });
    					
    					for(int k=0;k<3;k++){
    						try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	tcParentFigure2.remove(deductionLine);
//	                            	deductionLine.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                            }
	                        });
	                        
	                        try {
	                            Thread.sleep(300);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        
	                        Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	tcParentFigure2.add(deductionLine);
//	                            	deductionLine.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	                            }
	                        });
    					}
    					
    					
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	tcParentFigure.remove(helpFigure3);
                            }
                        });
    					
    					try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
    					
    					//generate the second temperature rule
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	CustomRectangle customRectangle = new CustomRectangle(new Point(45,2),new Point(462,20));
                            	tcParentFigure2.add(customRectangle,new Rectangle(45,2,463,21));
                            	
//                            	parentFigure.remove(helpFigure);
                            }
                        });
    					
    					displayFlickerFigure(helpFigure, tcParentFigure);
    					
    					Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                            	tcParentFigure.remove(helpFigure);
                            }
                        });
    					
    					
    					//goto the second step ,as feedback learning
    					new Thread(){
    						public void run(){
    							updateResult();
    						}
    					}.start();
	                        
                    }
                }.start();
			}
		});
		// event listener of the toolbar
//		Listener listener = new Listener() {
//			public void handleEvent(Event event) {
//				host.runDraw(event);
//			}
//		};
//
//		evaluation.addListener(SWT.Selection, listener);
		
//		evaluation.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				evaButton.setEnabled(false);
//				new Thread() {
//					public void run() {
//						host.runDraw(event);
//					}
//				}.start();
//			}
//		});
		
		
		leftViewForm.setTopLeft(tableToolBar);
		
		serviceCertified = new Table(leftViewForm,SWT.MULTI | SWT.FULL_SELECTION);
		serviceCertified.setHeaderVisible(true);
		serviceCertified.setLinesVisible(true);
		TableColumn co1 = new TableColumn(serviceCertified, SWT.NONE);
		co1.setText("Service");
		co1.setWidth(120);
		TableColumn co2 = new TableColumn(serviceCertified, SWT.NONE);
		co2.setText("Certified");
		co2.setWidth(80);
		
		//initial the exist actual service in trust chain discovery module
		for(Iterator<String> iterator = specialServiceNameList.iterator();iterator.hasNext();){
			TableItem tableItem = new TableItem(serviceCertified,0);
			tableItem.setText(new String[]{iterator.next(),null});
		}
		
		leftViewForm.setContent(serviceCertified);
	}


	public static FriendDataList makeFriendList() {
		FriendDataList list = new FriendDataList();

		FriendData temp1 = new FriendData("andynjux", "172.16.166.26", 0, 0);

		LinkedList<int[]> re1 = new LinkedList<int[]>();
		int[] a011 = { 70, 110 };
		int[] a021 = { 88, 110 };
		int[] a031 = { 60, 110 };
		int[] a041 = { 90, 110 };
		int[] a051 = { 100, 110 };
		re1.add(a011);
		re1.add(a021);
		re1.add(a031);
		re1.add(a041);
		re1.add(a051);
		temp1.setResult(re1);

		FriendData temp2 = new FriendData("ARTEMISPROJECTS", "10.0.1.132", 1, 0);

		LinkedList<int[]> re2 = new LinkedList<int[]>();
		int[] a1 = { 7, 10 };
		int[] a2 = { 8, 10 };
		int[] a3 = { 6, 10 };
		int[] a4 = { 9, 10 };
		int[] a5 = { 10, 10 };
		re2.add(a1);
		re2.add(a2);
		re2.add(a3);
		re2.add(a4);
		re2.add(a5);
		temp2.setResult(re2);

		FriendData temp3 = new FriendData("ARTEMISPROJECTS", "10.0.1.133", 1, 0);

		LinkedList<int[]> re3 = new LinkedList<int[]>();
		int[] a31 = { 7, 10 };
		int[] a32 = { 8, 10 };
		int[] a33 = { 6, 10 };
		int[] a34 = { 9, 10 };
		int[] a35 = { 10, 10 };
		re3.add(a31);
		re3.add(a32);
		re3.add(a33);
		re3.add(a34);
		re3.add(a35);
		temp3.setResult(re3);

		FriendData temp4 = new FriendData("ARTEMISPROJECTS", "10.0.1.134", 1, 0);

		LinkedList<int[]> re4 = new LinkedList<int[]>();
		int[] a41 = { 7, 10 };
		int[] a42 = { 8, 10 };
		int[] a43 = { 6, 10 };
		int[] a44 = { 9, 10 };
		int[] a45 = { 10, 10 };
		re4.add(a41);
		re4.add(a42);
		re4.add(a43);
		re4.add(a44);
		re4.add(a45);
		temp4.setResult(re4);

		FriendData temp5 = new FriendData("ARTEMISPROJECTS", "10.0.1.135", 2, 1);
		LinkedList<int[]> re5 = new LinkedList<int[]>();
		int[] a51 = { 7, 10 };
		int[] a52 = { 8, 10 };
		int[] a53 = { 6, 10 };
		int[] a54 = { 9, 10 };
		int[] a55 = { 10, 10 };
		re5.add(a51);
		re5.add(a52);
		re5.add(a53);
		re5.add(a54);
		re5.add(a55);
		temp5.setResult(re5);

		// FriendData temp41 = new FriendData("ARTEMISPROJECTS", "10.0.1.134",
		// -1, 1);

		FriendData temp6 = new FriendData("ARTEMISPROJECTS", "10.0.1.136", 2, 1);
		LinkedList<int[]> re6 = new LinkedList<int[]>();
		int[] a61 = { 7, 10 };
		int[] a62 = { 8, 10 };
		int[] a63 = { 6, 10 };
		int[] a64 = { 9, 10 };
		int[] a65 = { 10, 10 };
		re6.add(a61);
		re6.add(a62);
		re6.add(a63);
		re6.add(a64);
		re6.add(a65);
		temp6.setResult(re6);
		// FriendData temp61 = new FriendData("ARTEMISPROJECTS", "10.0.1.136",
		// -1, 2);

		FriendData temp7 = new FriendData("ARTEMISPROJECTS", "10.0.1.137", 2, 2);
		LinkedList<int[]> re7 = new LinkedList<int[]>();
		int[] a71 = { 7, 10 };
		int[] a72 = { 8, 10 };
		int[] a73 = { 6, 10 };
		int[] a74 = { 9, 10 };
		int[] a75 = { 10, 10 };
		re7.add(a71);
		re7.add(a72);
		re7.add(a73);
		re7.add(a74);
		re7.add(a75);
		temp7.setResult(re7);

		FriendData temp8 = new FriendData("ARTEMISPROJECTS", "10.0.1.138", 2, 2);
		LinkedList<int[]> re8 = new LinkedList<int[]>();
		int[] a81 = { 7, 10 };
		int[] a82 = { 8, 10 };
		int[] a83 = { 6, 10 };
		int[] a84 = { 9, 10 };
		int[] a85 = { 10, 10 };
		re8.add(a81);
		re8.add(a82);
		re8.add(a83);
		re8.add(a84);
		re8.add(a85);
		temp8.setResult(re8);
		// FriendData temp81 = new FriendData("ARTEMISPROJECTS", "10.0.1.138",
		// -1, 3);

		FriendData temp9 = new FriendData("ARTEMISPROJECTS", "10.0.1.139", 2, 3);
		LinkedList<int[]> re9 = new LinkedList<int[]>();
		int[] a91 = { 7, 10 };
		int[] a92 = { 8, 10 };
		int[] a93 = { 6, 10 };
		int[] a94 = { 9, 10 };
		int[] a95 = { 10, 10 };
		re9.add(a91);
		re9.add(a92);
		re9.add(a93);
		re9.add(a94);
		re9.add(a95);
		temp9.setResult(re9);

		FriendData temp10 = new FriendData("ARTEMISPROJECTS", "10.0.1.140", 3,
				4);
		LinkedList<int[]> re10 = new LinkedList<int[]>();
		int[] a01 = { 7, 10 };
		int[] a02 = { 8, 10 };
		int[] a03 = { 6, 10 };
		int[] a04 = { 9, 10 };
		int[] a05 = { 10, 10 };
		re10.add(a01);
		re10.add(a02);
		re10.add(a03);
		re10.add(a04);
		re10.add(a05);
		temp10.setResult(re10);

		FriendData temp11 = new FriendData("ARTEMISPROJECTS", "10.0.1.141", 3,
				6);
		LinkedList<int[]> re11 = new LinkedList<int[]>();
		int[] a0111 = { 7, 10 };
		int[] a012 = { 8, 10 };
		int[] a013 = { 6, 10 };
		int[] a014 = { 9, 10 };
		int[] a015 = { 10, 10 };
		re11.add(a0111);
		re11.add(a012);
		re11.add(a013);
		re11.add(a014);
		re11.add(a015);
		temp11.setResult(re11);

		list.add(temp1);
		list.add(temp2);
		list.add(temp3);
		list.add(temp4);
		// list.add(temp41);
		list.add(temp5);
		list.add(temp6);
		// list.add(temp61);
		list.add(temp7);
		list.add(temp8);
		// list.add(temp81);
		list.add(temp9);
		list.add(temp10);
		list.add(temp11);

		return list;

	}
	
	public SocialNetworkNode find(int id){
		for(Iterator<SocialNetworkNode> iterator = socialnetworkList.iterator();iterator.hasNext();){
			SocialNetworkNode socialNetworkNode = iterator.next();
			if(socialNetworkNode.getID()==id){
				return socialNetworkNode;
			}
		}
		return null;
	}
	
	public void updateNodeTrustRank(){
		
		pageRankResult = evaluateObject.getRanks();
		
		
		for(int i=0;i < pageRankResult.length;i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					rankTable.removeAll();
				}
			});
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			id = 1;
			
			for(int j=0;j<pageRankResult[i].length;j++){
				final int index_x = i;
				final int index_y = j,nodeid = j+1;
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						pageRankTableitem = new TableItem(rankTable,0);
						pageRankTableitem.setText(new String[]{Integer.toString(nodeid),Double.toString(pageRankResult[index_x][index_y])});
						System.out.println(pageRankResult[index_x][index_y]+" ");
					}
				});
			}
			
			System.out.println(i);
			System.out.println("\n");
			
		}
		
		recommenderArray = evaluateObject.getRecommenders();
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
//				table.setSelection(1);
//				for(int i=0;i<5;i++){
//					TableItem tableItem = rankTable.getItem(i);
//					tableItem.setBackground(new Color(Display.getCurrent(), 49,106,197));
//				}
				for(int i = 0;i<recommenderArray.length;i++){
					TableItem tableItem = rankTable.getItem(recommenderArray[i]);
					tableItem.setBackground(new Color(Display.getCurrent(), 49,106,197));
				}

			}
		});
	}
	
	public void displayNodeDisapear(final int nodeid){
		for(int i=0;i<5;i++){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					graphnodeList.get(nodeid-1).setVisible(false);
				}
			});
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					graphnodeList.get(nodeid-1).setVisible(true);
				}
			});
		}
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				graphnodeList.get(nodeid-1).setVisible(false);
			}
		});

	}
	
	public static void main(String[] args) {

		DisplaySWT thisClass = new DisplaySWT();
		thisClass.open();

	}

}
