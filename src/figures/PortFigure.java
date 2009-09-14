/*
 * Created on Jul 18, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/PortFigure.java,v $
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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * Provides the base implementation for all the port figures.
 * 
 * @author ddean
 *
 */
public class PortFigure extends Figure {
	private boolean showBox = true;
	private boolean showArrow = true;
	private Color boxFillColor = ColorConstants.white;
	private Color arrowFillColor;
	
	/**
	 * 
	 */
	public PortFigure() {
		super();
		
		setOpaque( false );
		setBackgroundColor( ColorConstants.white );
	}

	/**
	 * @param b
	 */
	public void setBoxVisible(boolean b) {
		showBox = b;
		repaint();
	}

	public void setBoxColor( Color c ) {
		boxFillColor = c;
		repaint();
	}

	public void setArrowVisible(boolean b) {
		showArrow = b;
		repaint();
	}

	public void setArrowColor( Color c ) {
		arrowFillColor = c;
		repaint();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#paint(org.eclipse.draw2d.Graphics)
	 */
	public void paintFigure(Graphics g) {
		super.paintFigure(g);
		
		Rectangle bounds = getBounds();
		
		g.setForegroundColor( ColorConstants.black );

		if( showBox ) {
			// draw outer rectangle
			g.setBackgroundColor( boxFillColor );
			
			g.fillRectangle( getBounds().getCropped( new Insets(0,1,1,0) ) );
			Rectangle	r = getBounds().getExpanded( -1, -1 ).getTranslated(0, -1);
			r.height += 1;
			g.drawRectangle( r );
		}

		if( showArrow ) {
			// draw arrowhead
			g.setBackgroundColor( arrowFillColor );
			PointList	pts = new PointList();
			pts.addPoint( bounds.getTopLeft() );
			pts.addPoint( bounds.x + FigureConstants.PORT_SIDE, bounds.y + FigureConstants.ARROW_SIDE / 2 );
			pts.addPoint( bounds.x, bounds.y + FigureConstants.PORT_SIDE );
			pts.addPoint( bounds.getTopLeft() );
			g.fillPolygon( pts );
			g.drawPolyline( pts );		 
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension( FigureConstants.PORT_SIDE, FigureConstants.ARROW_SIDE );
	}
}
