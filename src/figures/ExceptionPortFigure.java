/*
 * Created on Jul 18, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/ExceptionPortFigure.java,v $
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
 * The figure for drawing exception ports. Just sets properties on its PortFigure superclass
 * to get the visual used for exception ports
 * 
 * @author ddean
 *
 */
public class ExceptionPortFigure extends PortFigure {

	/**
	 * 
	 */
	public ExceptionPortFigure() {
		super();
		
		setBoxVisible( false );
		setArrowVisible( true );
		setArrowColor( FigureConstants.PORT_EXCEPTION_TRIANGLE_COLOR );
	}

}
