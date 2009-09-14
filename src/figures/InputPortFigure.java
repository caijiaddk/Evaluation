/*
 * Created on Jul 18, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/InputPortFigure.java,v $
 * $Revision: 1.5 $
 * 
 * (c) Copyright IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 * 		ddean			Initial version
 */
package figures;


/**
 * Figure used to draw input ports
 * @author ddean
 *
 */
public class InputPortFigure extends PortFigure {
	private boolean	startTask = false;
	
	/**
	 * 
	 */
	public InputPortFigure() {
		super();
	
		setBoxColor( FigureConstants.PORT_INPUT_RECTANGLE_COLOR );
		setArrowColor( FigureConstants.PORT_INPUT_TRIANGLE_COLOR );
	}
	/**
	 * @param b
	 */
	public void setStartTask( boolean b ) {
		startTask = b;

		setBoxColor( startTask ? FigureConstants.PORT_START_COLOR : FigureConstants.PORT_INPUT_RECTANGLE_COLOR );
		setArrowVisible( !startTask );
		repaint();
	}

}
