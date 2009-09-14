/*
 * Created on Jul 16, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/CompoundTaskFigure.java,v $
 * $Revision: 1.11 $
 * 
 * (c) Copyright IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 * 		ddean		Initial version
 */
package figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FrameBorder;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The figure for Compound tasks
 * 
 * @author ddean
 *
 */
public class CompoundTaskFigure extends NodeFigure {
	private	CompoundPanel compoundPanel = new CompoundPanel( "Task" );
	
	public CompoundTaskFigure() {
		super();

		add( compoundPanel,new Rectangle( FigureConstants.PORT_SIDE, 0, -1, -1 ) );
	

		addDefaultInput();
		addDefaultOutput();
	}

	/**
	 *
	 * @return the pane for parenting child figures
	 */
	public Layer getPane() {
		return compoundPanel.getPane();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		if( wHint < 0 || hHint < 0 ) {
			return new Dimension( 120, 120 );
		}
		return super.getPreferredSize(wHint, hHint);
	}


	/* (non-Javadoc)
	 * @see com.ibm.itso.sal330r.gefdemo.figures.NodeFigure#setName(java.lang.String)
	 */
	public void setName(String name) {
		compoundPanel.setText( name );	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#layout()
	 */
	public void validate() {
		LayoutManager layout = getLayoutManager();
		//layout.setConstraint( compoundPanel, new Rectangle( FigureConstants.PORT_SIDE, 0, -1, -1 ) );

		super.validate();
	}
	
	/**
	 * @return
	 */
	public CompoundPanel getCompoundPanel() {
		return compoundPanel;
	}

	/**
	 * <code>CompoundPanel</code> figure for the Scroll pane that contains the
	 * subworkflow figures
	 *
	 */
	protected class CompoundPanel extends Figure {
		private Layer pane;
		/**
		 * @param s
		 */
		public CompoundPanel(String s) {
			ScrollPane scrollpane = new ScrollPane();
			pane = new FreeformLayer();
			pane.setLayoutManager(new FreeformLayout());
			setLayoutManager(new StackLayout());
			scrollpane.setViewport(new FreeformViewport());
			scrollpane.setContents(pane);
			add(scrollpane);

			setOpaque( true );
			setBackgroundColor( ColorConstants.white );
			
			setBorder( new CompoundTaskBorder() );
		}
		
		public Layer getPane() {
			return pane;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
		 */
		public Dimension getPreferredSize(int wHint, int hHint) {
			Dimension	preferredSize = getParent().getSize();
			preferredSize.width -= 2 * FigureConstants.PORT_SIDE;
			preferredSize.height -= FigureConstants.PORT_SIDE / 2;
			
			return preferredSize;
		}

		public void setText( String text ) {
			((FrameBorder)getBorder()).setLabel( text );
			repaint();
		}
	}
	
	private class CompoundTaskBorder extends FrameBorder {
		protected void createBorders() {
			TitleBarBorder	titleBorder = new TitleBarBorder();
			titleBorder.setTextColor( ColorConstants.white );
			
			inner = titleBorder;
			outer = new LineBorder(2);
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		Rectangle rc = compoundPanel.getBounds().getCopy() ;
		translateToParent(rc);
		rc.translate(6,6);

		// draw the shadow
		graphics.setBackgroundColor( ColorConstants.lightGray );
		graphics.fillRectangle( rc );
	}

}
