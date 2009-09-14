package displaySWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.DeferredUpdateManager;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.ics.tcg.web.workflow.server.engine.util.CredentialChainResult;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.swtdesigner.SWTResourceManager;

import figures.AgentFigure;
import figures.BubblesFigure;
import figures.CustomLabel;
import figures.CustomRectangle;
import figures.DatabaseFigure;
import figures.FriendsFigure;
import figures.GoodPeopleFigure;
import figures.HelpFigure;
import figures.HostFigure;
import figures.MsgFigure;
import figures.TransparentFigure;

public class TrustChain {


//		private Table table;
		protected Shell shell;
		protected Canvas trustChainCanvas;
		protected Canvas trustRulesCanvas;
		protected Figure tcParentFigure,tcParentFigure2;
		protected Button button;
		HostFigure hostFigure;
		FriendsFigure friendFigure,friendFigure2,friendFigure3,friendFigure4;
		Label hostdomainName = new Label("IBM")
		,frienddomainName = new Label("ISO")
		,frienddomainName2 = new Label("A")
		,frienddomainName3 = new Label("XX")
		,frienddomainName4 = new Label("TrustedGroup");
		DatabaseFigure databaseFigure = new DatabaseFigure(new Point(0,0))
		,databaseFigure2 = new DatabaseFigure(new Point(0,0))
		,databaseFigure3 = new DatabaseFigure(new Point(0,0))
		,databaseFigure4 = new DatabaseFigure(new Point(0,0))
		,databaseFigure5 = new DatabaseFigure(new Point(0,0)); 
		
		BubblesFigure bubblesFigure,bubblesFigure2,bubblesFigure3,bubblesFigure4,bubblesFigure5;
		
		PolylineConnection secondpolylineConnection,secondpolylineConnection2;
		
		Label label = new Label("[Service->\nISO.rankA]A")
		,label2 = new Label("[Service->IBM.service]IBM")
		,label3 = new Label("[IBM.serivce->ISO.rankA]A")
		,label3forbubble = new Label("[IBM.serivce\n->ISO.rankA]A")
		,label4 = new Label("[A->TrustedGroup.member]TrustedGroup")
		,label5 = new Label("[TrusteGroup.member->ISO.certifier]XX")
		,label5forbubble = new Label("[TrusteGroup.member\n->ISO.certifier]XX")
		,label6 = new Label("[XX->ISO.administrator]ISO")
		,label7 = new Label("[ISO.administrator->ISO.certifier']ISO")
		,label8 = new Label("[ISO.certifier->ISO.rankA']ISO")
		,merge5and6 = new Label("[XX->ISO.certifier']ISO")
		,merge3and4and7 = new Label("[A->ISO.rankA']ISO");
		
		
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
				
		public Shell getShell() {
			return shell;
		}

		public void setShell(Shell shell) {
			this.shell = shell;
		}				

		/**
		 * Launch the application
		 * 
		 * @param args
		 */
		public static void main(String[] args) {
			try {
				TrustChain window = new TrustChain();
				window.open();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Open the window
		 */
		public void open() {
			final Display display = Display.getDefault();
			shell = new Shell();
			shell.setSize(862, 614);
			shell.setText("SWT Application");
			
			createContents(shell);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
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
				"jdbc:mysql://192.168.13.139:3306/project", "admin", "");
				stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery("select chain from display_chain where serviceName = 'BookAirTicketService' and businessName ='Microsoft'");
//				System.out.println("connetct db "+resultSet.first());
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
							System.out.println("the rule is \n"+rule);
							System.out.println("the rule2 is \n"+rule2);
							System.out.println("the rule3 is \n"+rule3);
							System.out.println("the rule4 is \n"+rule4);
							System.out.println("the rule5 is \n"+rule5);
							System.out.println("the rule6 is \n"+rule6);
							System.out.println("the rule7 is \n"+rule7);
							System.out.println("the rule8 is \n"+rule8);
							
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
		/**
		 * Create contents of the window
		 */
		protected void createContents(Composite composite) {
			
			initialRules();
			
//			shell = new Shell();
//			shell.setSize(862, 614);
//			shell.setText("SWT Application");

			trustChainCanvas = new Canvas(composite, SWT.NONE);
			trustChainCanvas.setBackground(Display.getCurrent().getSystemColor(
					SWT.COLOR_WHITE));
			trustChainCanvas.setBounds(0, 28, 862, 428);

			LightweightSystem lws = new LightweightSystem(trustChainCanvas);

			button = new Button(shell, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					//
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
	    	    					
//	    	    					Label label = new Label("Test");
//	    	    					label.setSize(127, 49);
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
	                            	tcParentFigure2.add(label2,new Rectangle(45, 30, 215, 27));
	                            	label2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	                            	
	                            	label3.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
	                            	tcParentFigure2.add(label3,new Rectangle(293, 30, 215, 27));
	                            	
	                            	label4.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
	                            	tcParentFigure2.add(label4,new Rectangle(106, 74, 235, 27));
	                            	
	                            	label5.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
	                            	tcParentFigure2.add(label5,new Rectangle(356, 74, 235, 27));
	                            	
	                            	label6.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
	                            	tcParentFigure2.add(label6,new Rectangle(216, 118, 215, 27));
	                            	
	                            	label7.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
	                            	tcParentFigure2.add(label7,new Rectangle(475, 118, 235, 27));
	                            	
	                            	label8.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
	                            	tcParentFigure2.add(label8,new Rectangle(605, 74, 235, 27));
	                            	
//	                            	deductionLine = new PolylineConnection();
//	                            	PolygonDecoration arrow = new PolygonDecoration();
//	            					arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
//	            					arrow.setScale(5, 5);
//	            					deductionLine.setTargetDecoration(arrow);
//	            					
//	                            	deductionLine.setStart(new Point(370,77));
//	                            	deductionLine.setEnd(new Point(370,50));
//	                            	parentFigure2.add(deductionLine);
//	                            	
//	                            	deductionLine2 = new PolylineConnection();
//	                            	PolygonDecoration arrow2 = new PolygonDecoration();
//	            					arrow2.setTemplate(PolygonDecoration.TRIANGLE_TIP);
//	            					arrow2.setScale(5, 5);
//	            					deductionLine2.setTargetDecoration(arrow2);
//	            					
//	                            	deductionLine2.setStart(new Point(490,119));
//	                            	deductionLine2.setEnd(new Point(490,92));
//	                            	parentFigure2.add(deductionLine2);
	                    
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
//	                            	parentFigure.remove(bubblesFigure);
//	                            	parentFigure.remove(label);
//	                            	parentFigure.remove(helpFigure);
	                            	
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
//	                        Display.getDefault().asyncExec(new Runnable() {
//	                            public void run() {
//	                            	agentFigure.setPoint(new Point(100,100));
//	                            	agentFigure2.setPoint(new Point(200,200));
//	                        		parentFigure.add(agentFigure,new Rectangle(100,100,24,24));
//	                        		parentFigure.add(agentFigure2,new Rectangle(200,200,24,24));
//	                            }
//	                        });
	                        
	                    	startPoint = hostFigure.getPoint();
	                    	endPoint = friendFigure.getPoint();
	                    	endPoint2 = friendFigure2.getPoint();
	                    	agentStepX = (endPoint.x - startPoint.x)/10.0;
	                    	agentStepY = (endPoint.y - startPoint.y)/10.0;
	                    	agentStepX2 = (endPoint2.x - startPoint.x)/10.0;
	                    	agentStepY2 = (endPoint2.y - startPoint.y)/10.0;
//	                    	currentPoint = startPoint;
//	                    	currentPoint2 = startPoint;
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
//		                            	label2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
//		                            	label3.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		                            	
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
//		                            	label2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
//		                            	label3.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		                            	
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
	                            	CustomRectangle customRectangle = new CustomRectangle(new Point(216,119),new Point(494,20));
	                            	tcParentFigure2.add(customRectangle,new Rectangle(216,119,495,21));
	                            	
//	                            	merge5and6.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
//	                            	parentFigure2.add(merge5and6,new Rectangle(490,92,235,27));
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
//	                            	CustomRectangle customRectangle = new CustomRectangle(new Point(216,119),new Point(494,20));
//	                            	parentFigure2.add(customRectangle,new Rectangle(216,119,495,21));
	                            	
	                            	merge5and6.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
	                            	tcParentFigure2.add(merge5and6,new Rectangle(490,92,235,27));
	                            }
	                        });
	    					
	    					//display the process of reasoning
//	    					for(int k=0;k<3;k++){
//	    						try {
//		                            Thread.sleep(300);
//		                        } catch (InterruptedException e) {
//		                            e.printStackTrace();
//		                        }
//		                        
//		                        Display.getDefault().syncExec(new Runnable() {
//		                            public void run() {
//		                            	deductionLine2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
//		                            }
//		                        });
//		                        
//		                        try {
//		                            Thread.sleep(300);
//		                        } catch (InterruptedException e) {
//		                            e.printStackTrace();
//		                        }
//		                        
//		                        Display.getDefault().syncExec(new Runnable() {
//		                            public void run() {
//		                            	deductionLine2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
//		                            }
//		                        });
//	    					}
	    					
//	    					Display.getDefault().syncExec(new Runnable() {
//	                            public void run() {
//	                            	label5.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
//	                            }
//	                        });
	                        
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
	    					
//	    					for(int i=10;i>=0;i--){
//	    						try {
//		                            Thread.sleep(100);
//		                        } catch (InterruptedException e) {
//		                            e.printStackTrace();
//		                        }
//		                        
//		                        currentPoint.x = startPoint.x + (int)(stepX*i);
//		                        currentPoint.y = startPoint.y + (int)(stepY*i);
//		                        
//		                        currentPoint2.x = startPoint.x + (int)(stepX2*i);
//		                        currentPoint2.y = startPoint.y + (int)(stepY2*i);
//		                        
//		                        Display.getDefault().syncExec(new Runnable() {
//		                            public void run() {
//		                            	agentFigure.setPoint(new Point(currentPoint.x-12,currentPoint.y-12));
//		                            	agentFigure2.setPoint(new Point(currentPoint2.x-12,currentPoint2.y-12));
//		                        		parentFigure.add(agentFigure,new Rectangle(currentPoint.x-12,currentPoint.y-12,24,24));
//		                        		parentFigure.add(agentFigure2,new Rectangle(currentPoint2.x-12,currentPoint2.y-12,24,24));
//		                            }
//		                        });
//		                        
//	    					}
	    					
	    					
	    					
//	                    	//每隔1秒一次循环，用于模拟表示一个需时较长的任务

	    					//third step
	    					
	                        try {
	                            Thread.sleep(1000);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }

	                    	Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
//	                            	parentFigure.remove(secondpolylineConnection);
//	                            	parentFigure.remove(secondpolylineConnection2);
	                            	
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
//	                    	currentPoint = startPoint;
//	                    	currentPoint2 = startPoint;
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
//		                            	agentFigure2.setPoint(new Point(currentPoint2.x-12,currentPoint2.y-12));
		                        		tcParentFigure.add(agentFigure,new Rectangle(currentPoint.x-12,currentPoint.y-12,24,24));
//		                        		parentFigure.add(agentFigure2,new Rectangle(currentPoint2.x-12,currentPoint2.y-12,24,24));
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
	            					
	                            	deductionLine2.setStart(new Point(490,119));
	                            	deductionLine2.setEnd(new Point(490,97));
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
//		                            	deductionLine2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
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
//		                            	deductionLine2.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
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
	    					//display the process of reasoning
//	    					for(int k=0;k<3;k++){
//	    						try {
//		                            Thread.sleep(300);
//		                        } catch (InterruptedException e) {
//		                            e.printStackTrace();
//		                        }
//		                        
//		                        Display.getDefault().syncExec(new Runnable() {
//		                            public void run() {
////		                            	deductionLine.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
//		                            }
//		                        });
//		                        
//		                        try {
//		                            Thread.sleep(300);
//		                        } catch (InterruptedException e) {
//		                            e.printStackTrace();
//		                        }
//		                        
//		                        Display.getDefault().syncExec(new Runnable() {
//		                            public void run() {
////		                            	deductionLine.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
//		                            }
//		                        });
//	    					}
//	    					
//	    					Display.getDefault().syncExec(new Runnable() {
//	                            public void run() {
//	                            	label3.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
//	                            }
//	                        });
	    					
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
	    					
//	                    	//每隔1秒一次循环，用于模拟表示一个需时较长的任务
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
//	                    	currentPoint = startPoint;
//	                    	currentPoint2 = startPoint;
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
//		                            	agentFigure2.setPoint(new Point(currentPoint2.x-12,currentPoint2.y-12));
		                        		tcParentFigure.add(agentFigure,new Rectangle(currentPoint.x-12,currentPoint.y-12,24,24));
//		                        		parentFigure.add(agentFigure2,new Rectangle(currentPoint2.x-12,currentPoint2.y-12,24,24));
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
	                            	CustomRectangle customRectangle = new CustomRectangle(new Point(106,77),new Point(730,20));
	                            	tcParentFigure2.add(customRectangle,new Rectangle(106,77,731,21));
	                            	
//	                            	merge3and4and7.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
//	                            	parentFigure2.add(merge3and4and7,new Rectangle(370,50,235,27));
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
//	                            	CustomRectangle customRectangle = new CustomRectangle(new Point(106,77),new Point(730,20));
//	                            	parentFigure2.add(customRectangle,new Rectangle(106,77,731,21));
	                            	
	                            	merge3and4and7.setFont(SWTResourceManager.getFont("", 9, SWT.NONE));
	                            	tcParentFigure2.add(merge3and4and7,new Rectangle(370,50,235,27));
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
	            					
	                            	deductionLine.setStart(new Point(370,77));
	                            	deductionLine.setEnd(new Point(370,50));
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
//		                            	deductionLine.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
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
//		                            	deductionLine.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
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
	                            	CustomRectangle customRectangle = new CustomRectangle(new Point(45,30),new Point(462,20));
	                            	tcParentFigure2.add(customRectangle,new Rectangle(45,30,463,21));
	                            	
//	                            	parentFigure.remove(helpFigure);
	                            }
	                        });
	    					
	    					displayFlickerFigure(helpFigure, tcParentFigure);
	    					
	    					Display.getDefault().syncExec(new Runnable() {
	                            public void run() {
	                            	tcParentFigure.remove(helpFigure);
	                            }
	                        });
	    					
		                        
	                    }
	                }.start();

				}
	
			});
			button.setText("button");
			button.setBounds(0, 0, 48, 22);

			tcParentFigure = new Figure();
			tcParentFigure.setLayoutManager(new XYLayout());
			lws.setContents(tcParentFigure);
			
			trustRulesCanvas = new Canvas(shell, SWT.BORDER);
//			canvas2.setBackground(Display.getCurrent().getSystemColor(
//					SWT.COLOR_WHITE));
			trustRulesCanvas.setBounds(0, 428, 846, 148);
			LightweightSystem lws2 = new LightweightSystem(trustRulesCanvas);
			tcParentFigure2 = new Figure();
			tcParentFigure2.setLayoutManager(new XYLayout());
			lws2.setContents(tcParentFigure2);

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
		
		public void displayChangingcolorLabel(final Label label,final Color oriColor,final Color newColor){
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
//                    	figure.setPoint(figure);
                		tcParentFigure.add(figure,new Rectangle(currentLocation.x-12,currentLocation.y-12,24,24));
                    }
                });
                
			}
		}

}
