/*
 * Created on Jul 23, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/PortConnectionAnchor.java,v $
 * $Revision: 1.4 $
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

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author ddean
 *
 */
public class PortConnectionAnchor extends AbstractConnectionAnchor {

	/**
	 * 
	 */
	public PortConnectionAnchor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 */
	public PortConnectionAnchor(IFigure owner) {
		super(owner);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
	 */
	public Point getLocation(Point arg0) {
		boolean onLeft;
		
		// TODO : switch location to left or right depending on location of port
		Rectangle parent = getOwner().getParent().getBounds();
		Point parentLeft = new PrecisionPoint( parent.getTopLeft() );
		getOwner().getParent().translateToAbsolute( parentLeft );
		
		Rectangle r = getOwner().getBounds();
		Point left = new PrecisionPoint( r.getTopLeft() );
		getOwner().translateToAbsolute( left );
		
		onLeft = Math.abs( parentLeft.x - left.x ) < 10;	// fudge factor
		Point result = new PrecisionPoint( onLeft ? r.x : r.x + r.width, r.y + r.height / 2 );
		getOwner().translateToAbsolute( result );
		
		return result;
	}

}
