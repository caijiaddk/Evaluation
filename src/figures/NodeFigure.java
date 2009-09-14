/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package figures;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
//
//import com.ibm.itso.sal330r.workflow.FaultPort;
//import com.ibm.itso.sal330r.workflow.InputPort;
//import com.ibm.itso.sal330r.workflow.OutputPort;

/**
 * The <code>NodeFigure</code> class is the base class for all the figures representing
 * WorkflowNodes, ie those that can have ports with connections to other nodes.
 * 
 * Assumes that all NodeFigures have a FaultPort and some number of input and output
 * ports
 * 
 * @author ddean
 *
 */
public class NodeFigure 
	extends Figure
{
	private ExceptionPortFigure	faultPort = new ExceptionPortFigure();

	protected Hashtable connectionAnchors = new Hashtable(7);
	protected Vector inputConnectionAnchors = new Vector(2,2);
	protected Vector outputConnectionAnchors = new Vector(2,2);
	
	
	public NodeFigure() {
		PortConnectionAnchor	anchor;
		
		XYLayout	layout = new XYLayout();
		setLayoutManager( layout );
		setBackgroundColor( ColorConstants.white );
		setOpaque( false );
		
		// add the FaultPort
		anchor = new PortConnectionAnchor( faultPort );
		add( faultPort );
		getSourceConnectionAnchors().add( anchor );		
		connectionAnchors.put( "false", anchor );
	}
	
	/**
	 * Add an input port and its anchor
	 * 
	 * @param portName unique name to refer to the port
	 */
	public void addInput( String portName ) {
		InputPortFigure	inputPort = new InputPortFigure();
		add( inputPort );

		PortConnectionAnchor anchor = new PortConnectionAnchor( inputPort );
		getTargetConnectionAnchors().add( anchor );		
		connectionAnchors.put( portName, anchor );
	}
	
	/**
	 * A convenience method for nodes that have a single input port. It is assigned the default name
	 */
	public void addDefaultInput() {
		addInput( "input" );
	}
	
	/**
	 * Add an output port and its anchor
	 * 
	 * @param portName unique name to refer to the port
	 */
	public void addOutput( String portName, String condition ) {
		OutputPortFigure outputPort = new OutputPortFigure();
		add( outputPort );

		PortConnectionAnchor anchor = new PortConnectionAnchor( outputPort );
		getSourceConnectionAnchors().add( anchor );		
		connectionAnchors.put( portName, anchor );
	}
	
	public OutputPortFigure addOutput( String portName) {
		OutputPortFigure outputPort = new OutputPortFigure();
		add( outputPort );

		PortConnectionAnchor anchor = new PortConnectionAnchor( outputPort );
		getSourceConnectionAnchors().add( anchor );		
		connectionAnchors.put( portName, anchor );
		
		return outputPort;
	}
	
	/**
	 * Remove an output port and its anchor, given the port name
	 * 
	 * @param portName unique name to refer to the port
	 */
	public void removeOutput( String portName ) {
		PortConnectionAnchor anchor = (PortConnectionAnchor)connectionAnchors.get( portName );
		if( anchor != null ) {
			connectionAnchors.remove( portName );
			getSourceConnectionAnchors().remove( anchor );		
			remove( anchor.getOwner() );
		}
	}
	
	/**
	 * A convenience method for nodes that have a single output port. It is assigned the default name
	 */
	public void addDefaultOutput() {
		addOutput( "output", "output" );
	}
	
	/**
	 * The comment property of a WorkflowNode is displayed as a tool tip. This method is used to set it. Passing
	 * a null or zero-length string will remove the tool tip
	 * 
	 * @param comment the comment to use for this node's tool tip
	 */
	public void setComment( String comment ) {
		Label toolTip = null;
		
		if( comment != null && comment.length() > 0 ) {
			toolTip = new Label( comment );
			toolTip.setBorder( new MarginBorder( 3 ) );
		}
		
		setToolTip( toolTip );
	}

	/**
	 * Searches for and returns the anchor on this figure that is closest to the reference point <code>p</code>
	 * @param p the reference point
	 * @return the anchor on this figure that is closest to <code>p</code>
	 */
	public ConnectionAnchor connectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;
	
		Enumeration e = getSourceConnectionAnchors().elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		e = getTargetConnectionAnchors().elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}
	
	/**
	 * returns an anchor given its name
	 * 
	 * @param portName name of the anchor
	 * @return the anchor with the name <code>portName</code>
	 */
	public ConnectionAnchor getConnectionAnchor(String portName) {
		return (ConnectionAnchor)connectionAnchors.get( portName );
	}
	
	/**
	 * returns the name of the specified anchor
	 * 
	 * @param c the anchor whose name is requested
	 * @return the name of the specifed anchor
	 */
	public String getConnectionAnchorName(ConnectionAnchor c){
		Enumeration enumeration = connectionAnchors.keys();
		String key;
		while (enumeration.hasMoreElements()){
			key = (String)enumeration.nextElement();
			if (connectionAnchors.get(key).equals(c))
				return key;
		}
		return null;
	}
	
	/**
	 * returns the source connection anchor that is closest to the reference point
	 * @param p the reference point
	 * @return the closest connection anchor to <code>p</code>
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;
	
		Enumeration e = getSourceConnectionAnchors().elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}
	
	/**
	 * returns all the source connection anchors on this node figure
	 * @return a vector of all the source connection anchors for this figure
	 */
	public Vector getSourceConnectionAnchors() {
		return outputConnectionAnchors;
	}
	
	/**
	 * returns the target connection anchor that is closest to the reference point
	 * @param p the reference point
	 * @return the closest target connection anchor to <code>p</code>
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;
	
		Enumeration e = getTargetConnectionAnchors().elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}
	
	/**
	 * returns all the target connection anchors on this node figure
	 * @return a vector of all the target connection anchors for this figure
	 */
	public Vector getTargetConnectionAnchors() {
		return inputConnectionAnchors;
	}

	/**
	 * Returns the name of the specified anchor
	 * 
	 * @param anchor the connectio anchor whose name is requested
	 * @return the anchor's name
	 */
	public String getNameForAnchor( ConnectionAnchor anchor ) {
		Iterator it = connectionAnchors.keySet().iterator();
		String name;
		
		while( it.hasNext() ) {
			name = (String)it.next();
			
			if( anchor.equals( connectionAnchors.get( name ) ) ) {
				return name;
			}
		}
		
		return null;
	}

	/**
	 * Returns the node's exception port (Nodes are assumed to only have one by design).
	 * @return the exception port
	 */
	public ExceptionPortFigure getExceptionPort() {
		return faultPort;
	}

	/** 
	 * layout method is overwritten to arrange the input and output ports around the edges of the node figures
	 */
	public void validate() {
		Iterator it;
		ConnectionAnchor port;
		int y, x;
			
		x = 1;
		y = FigureConstants.PORT_SPACING;	
		it = getTargetConnectionAnchors().iterator();
		while( it.hasNext() ) {
			port = (ConnectionAnchor)it.next();
			
			getLayoutManager().setConstraint( port.getOwner(), new Rectangle( x, y, -1, -1 ) );
											  
			y += FigureConstants.PORT_SIDE + FigureConstants.PORT_SPACING;
		}
		
		x = getSize().width - FigureConstants.PORT_SIDE - 1;
		y = FigureConstants.PORT_SPACING;	
		it = getSourceConnectionAnchors().iterator();
		while( it.hasNext() ) {
			port = (ConnectionAnchor)it.next();
			if( port.getOwner() instanceof ExceptionPortFigure )
				continue;
			
			getLayoutManager().setConstraint( port.getOwner(), new Rectangle( x, y, -1, -1 ) );
											  
			y += FigureConstants.PORT_SIDE + FigureConstants.PORT_SPACING ;
		}
		
		getLayoutManager().setConstraint( faultPort, new Rectangle( x, getSize().height - FigureConstants.PORT_SIDE - FigureConstants.PORT_SPACING,
											-1, -1 ) );
		
		super.validate();
	}

	/**
	 * convenience method to get the input port figure with the default name, for nodes that have only the one default input
	 * @return the port's figure
	 */
	public InputPortFigure getDefaultInputPort() {
		InputPortFigure port = connectionAnchors.containsKey( "input" ) ? ((InputPortFigure)getConnectionAnchor( "input" ).getOwner()) : null;
		
		return port;
	}

	/**
	 * convenience method to get the output port figure with the default name, for nodes that have only the one default output
	 * @return the port's figure
	 */
	public OutputPortFigure getDefaultOutputPort() {
		OutputPortFigure port = connectionAnchors.containsKey( "output" ) ? ((OutputPortFigure)getConnectionAnchor( "output" ).getOwner()) : null;
		
		return port;
	}
	
	/**
	 * called to set all the node's input ports to display whether the node's WorkflowNode is a start task or not
	 * @param isStart true if the node is a start task
	 */
	public void setStartTask( boolean isStart ) {
		ConnectionAnchor anchor;
		Iterator it = getTargetConnectionAnchors().iterator();
		while( it.hasNext() ) {
			anchor = (ConnectionAnchor)it.next();
			
			((InputPortFigure)anchor.getOwner()).setStartTask( isStart );
		}
	}
	
	/**
	 * called to set all the node's input ports to display whether the node's WorkflowNode is a start task or not
	 * @param isFinish true if the task is a finsh task
	 */
	public void setFinishTask( boolean isFinish ) {
		ConnectionAnchor anchor;
		Iterator it = getSourceConnectionAnchors().iterator();
		while( it.hasNext() ) {
			anchor = (ConnectionAnchor)it.next();
			if( anchor.getOwner() instanceof ExceptionPortFigure )
				continue;
			
			((OutputPortFigure)anchor.getOwner()).setFinishTask( isFinish );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	protected boolean useLocalCoordinates() {
		return true;
	}

	public void setName( String name ) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize(int wHint, int hHint) {
		if( wHint < 0 || hHint < 0 ) {
			int nAnchors = Math.max( inputConnectionAnchors.size(), outputConnectionAnchors.size() );
			return new Dimension( 64, nAnchors * (FigureConstants.PORT_SIDE + FigureConstants.PORT_SPACING) + FigureConstants.PORT_SPACING );
		}
		return super.getMinimumSize(wHint, hHint);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return getMinimumSize(wHint, hHint);
	}

}