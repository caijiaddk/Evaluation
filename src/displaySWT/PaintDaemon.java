package displaySWT;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PaintDaemon {
	private DisplaySWT gui; // �õ�ǰ̨�������

	private boolean stopFlag; // �Ƿ�ֹͣ�ı�ʶ

	/**
	 * ���캯����ȡ��ǰ̨�������
	 */
	public PaintDaemon(DisplaySWT taskGUI) {
		this.gui = taskGUI;
	}

	/**
	 * ִֹͣ��
	 */
	public void stop() {
		stopFlag = true;
	}

	/**
	 * ��ʼִ��
	 */
	public void start(int taskCount) {
		stopFlag = false; // ��ִ��״̬��ʼ����ִ��
		intsertConsoleText("��̨�߳̿�ʼִ������......\n");
		for (int i = 0; i < taskCount; i++) {
			// һ������ִ��״̬Ϊֹͣ�����˳���ѭ��
			if (stopFlag)
				break;
			// ÿ��0.1��һ��ѭ��������ģ���ʾһ����ʱ�ϳ�������

			setIntroductionLabelHighlight(i);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			addIntroductionTabItem(i);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gotoDiscovery();
			// notifyOneTaskFinish(i+1);

		}
		intsertConsoleText("��̨�߳̽���ִ������!!!!!!\n");
		// �޸Ľ��水ť״̬
		setTaskGUIButtonState(true);
	}

	public void startEvaluation() {

		double[][] monitor = new double[100][5];

		java.sql.Connection conn;
		java.sql.Statement stmt;
		try {
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

			conn = DriverManager.getConnection(
					"jdbc:mysql://172.16.167.230:3306/project", "admin", "");
			stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("select trust1"
					+ ",trust2,trust3,trust4,trust5 from display_feedback");
			// System.out.println("connetct db"+resultSet.first());

			int privot = 0;
			while (resultSet.next()) {
				monitor[privot][0] = resultSet.getDouble("trust1");
//				System.out.println("privot")
				monitor[privot][1] = resultSet.getDouble("trust2");
				monitor[privot][2] = resultSet.getDouble("trust3");
				monitor[privot][3] = resultSet.getDouble("trust4");
				monitor[privot][4] = resultSet.getDouble("trust5");
				privot++;
				if(privot>=100){
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 1; i <= 100; i++) {

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			addSeries(monitor, i);
		}

		updateResult();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		updateService();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gotoReputation();

	}

	private void updateResult() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				gui.getMonitorLabel(0).setVisible(false);
				gui.updateServiceAspect();
			}
		});
	}

	private void updateService() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {

				// gui.updateCandidate(new int[]{6,5,4});
				gui.moveProgress(3);

			}
		});
	}

	private void gotoReputation() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {

				gui.getReputationEvaluationLabel().setForeground(
						Display.getCurrent().getSystemColor(SWT.COLOR_RED));
				try {
					gui.createReputationEvaluation();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TabFolder tabFolder = gui.getTabFolder();
				tabFolder.setSelection(2);
			}
		});
	}

	private void addSeries(final double[][] monitor, final int i) {

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				XYSeriesCollection cc = gui.chart.getMasterSeriesCollection();
				XYSeries s = cc.getSeries(0);

				s.add(i, monitor[i - 1][0]);

				XYSeries s2 = cc.getSeries(1);
				s2.add(i, monitor[i - 1][1]);
				// 8.7 update.............................
				XYSeries s3 = cc.getSeries(2);
				s3.add(i, monitor[i - 1][2]);

				XYSeries s4 = cc.getSeries(3);
				s4.add(i, monitor[i - 1][3]);

				XYSeries s5 = cc.getSeries(4);
				s5.add(i, monitor[i - 1][4]);
			}
		});
	}

	/**
	 * ���水ť״̬
	 */
	private void setTaskGUIButtonState(final boolean bFlag) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				gui.setButtonState(bFlag);
			}
		});
	}

	/**
	 * �����ı�
	 */
	private void intsertConsoleText(final String str) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				// gui.getConsoleText().insert(str);
			}
		});
	}

	private void setIntroductionLabelHighlight(final int index) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				TabFolder tabFolder = gui.getTabFolder();
				if (index == 0) {
					gui.getSearchingServicesLabel().setForeground(
							Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					// gui.createSerchingService();
				} else if (index == 1) {
					gui.getTrustChainDiscoveryLabel().setForeground(
							Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					gui.createTrustChainDiscovery();
					tabFolder.setSelection(0);
				} else if (index == 2) {
					gui.getRecommenderEvaluationLabel().setForeground(
							Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					gui.createRecommenderChart();
					tabFolder.setSelection(1);
				} else {
					gui.getReputationEvaluationLabel().setForeground(
							Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					try {
						gui.createReputationEvaluation();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tabFolder.setSelection(2);
				}
			}
		});
	}

	private void gotoDiscovery() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {

				gui.getTrustChainDiscoveryLabel().setForeground(
						Display.getCurrent().getSystemColor(SWT.COLOR_RED));
				gui.createTrustChainDiscovery();

			}
		});
	}

	private void addIntroductionTabItem(final int index) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				TabFolder tabFolder = gui.getTabFolder();
				if (index == 0) {
					gui.createSerchingService();
					gui.moveProgress(1);

				} else if (index == 1) {
					// final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
					// tabItem.setText("Trust chain discovery");
					gui.createTrustChainDiscovery();
					tabFolder.setSelection(0);
				} else if (index == 2) {
					// final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
					// tabItem.setText("Recommender evaluation");
					gui.createRecommenderChart();
					tabFolder.setSelection(1);
				} else {
					// final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
					// tabItem.setText("Reputation evaluation");
					try {
						gui.createReputationEvaluation();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tabFolder.setSelection(2);
				}
			}
		});
	}
}
