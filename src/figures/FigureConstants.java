/*
 * Created on Jul 18, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/FigureConstants.java,v $
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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * 
 * Constants for figure dimensions, etc. used in the sample app
 * @author ddean
 *
 */
public class FigureConstants {

	/**
	 * 
	 */
	private FigureConstants() {
	}

	public static final	int		ARROW_SIDE = 17;
	public static final int 	PORT_SIDE = 15;
	
	// PORT_SIDE = (int)Math.round( ARROW_SIDE * Math.cos( 30 ) );

	public static final Color	PORT_INPUT_TRIANGLE_COLOR = ColorConstants.white;
	public static final Color	PORT_OUTPUT_TRIANGLE_COLOR = ColorConstants.black;
	public static final Color	PORT_EXCEPTION_TRIANGLE_COLOR = ColorConstants.red;
	
	public static final Color	PORT_START_COLOR = ColorConstants.green;
	public static final Color	PORT_FINISH_COLOR = ColorConstants.red;
	
	public static final int		PORT_SPACING = ARROW_SIDE / 2 + 1;

	public static final Color	PORT_INPUT_RECTANGLE_COLOR = ColorConstants.black;
	public static final Color	PORT_OUTPUT_RECTANGLE_COLOR = ColorConstants.white;
}
