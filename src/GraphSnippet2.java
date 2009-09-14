/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This snippet creates a very simple graph with an Icon and Label.
 * 
 * This snippet shows how to use directed edges and self loops.
 * 
 * @author Ian Bull
 * 
 */
public class GraphSnippet2 {

	static ArrayList<GraphNode> graphnodeList = new ArrayList<GraphNode>();

	public static void main(String[] args) throws IOException {
		Display d = new Display();
		Shell shell = new Shell(d);
		Image image1 = Display.getDefault()
				.getSystemImage(SWT.ICON_INFORMATION);
		Image image2 = Display.getDefault().getSystemImage(SWT.ICON_WARNING);
		Image image3 = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
		shell.setLayout(new FormLayout());
		shell.setSize(1400, 1400);

		final Composite composite = new Composite(shell, SWT.BORDER);
		final FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 0);
		fd_composite.bottom = new FormAttachment(10, 0);
		fd_composite.right = new FormAttachment(100, 0);
		fd_composite.left = new FormAttachment(0, 0);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FormLayout());

		final Button button = new Button(composite, SWT.NONE);

		final FormData fd_button = new FormData();
		fd_button.left = new FormAttachment(0, 17);
		fd_button.right = new FormAttachment(0, 65);
		fd_button.top = new FormAttachment(0, 15);
		button.setLayoutData(fd_button);
		button.setText("button");

		final Composite composite_1 = new Composite(shell, SWT.NONE);
		final FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(10, 0);
		fd_composite_1.bottom = new FormAttachment(100, 0);
		fd_composite_1.left = new FormAttachment(0, 0);
		fd_composite_1.right = new FormAttachment(100, 0);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FillLayout());

		Graph g = new Graph(composite_1, SWT.NONE);
		g.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);

		BufferedReader in = new BufferedReader(new FileReader(
				"resource/tn1.net"));
		String s;
		s = in.readLine();
		while ((s = in.readLine()) != null) {
			String[] result = s.split(" ");
			if (result.length == 4 && result[2].equals("ic")) {
				int id = Integer.parseInt(result[1]);
				GraphNode n = new GraphNode(g, SWT.NONE, result[1]);
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

		// GraphNode n1 = new G.........raphNode(g, SWT.NONE);
		// GraphNode n2 = new GraphNode(g, SWT.NONE);
		// GraphNode n3 = new GraphNode(g, SWT.NONE);
		//
		// new GraphConnection(g, SWT.NONE, n1, n2);
		// new GraphConnection(g, SWT.NONE, n2, n3);
		// new GraphConnection(g, SWT.NONE, n3, n3);

		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		
		button.addMouseListener(new MouseListener(){

		//	@Override
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			//@Override
			public void mouseDown(org.eclipse.swt.events.MouseEvent arg0) {
				// TODO Auto-generated method stub
				graphnodeList.get(5).setVisible(false);
			}

		//	@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent arg0) {
				// TODO Auto-generated method stub
				graphnodeList.get(5).setVisible(true);
			}
			
		});
		
//		graphnodeList.get(5).setVisible(false);
//		Point point = graphnodeList.get(5).getLocation();
//		System.out.println(point.x+" "+point.y);
		
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
		image1.dispose();
		image2.dispose();
		image3.dispose();
	}
}
