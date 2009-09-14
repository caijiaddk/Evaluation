/*
 * Created on Jul 18, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/OutputPortFigure.java,v $
 * $Revision: 1.3 $
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
 * @author ddean
 *
 */
public class OutputPortFigure extends PortFigure {
	private boolean		finishTask;
	
	/**
	 * 
	 */
	public OutputPortFigure() {
		super();

		setBoxColor( FigureConstants.PORT_OUTPUT_RECTANGLE_COLOR );
		setArrowColor( FigureConstants.PORT_OUTPUT_TRIANGLE_COLOR );
	}

	/**
	 * @param b
	 */
	public void setFinishTask( boolean b ) {
		finishTask = b;

		setBoxColor( finishTask ? FigureConstants.PORT_FINISH_COLOR : FigureConstants.PORT_OUTPUT_RECTANGLE_COLOR );
		setArrowVisible( !finishTask );
		repaint();
	}

}
