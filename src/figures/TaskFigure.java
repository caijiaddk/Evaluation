/*
 * Created on Jul 16, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/TaskFigure.java,v $
 * $Revision: 1.12 $
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
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author ddean
 *
 */
public class TaskFigure extends NodeFigure {
	private static Dimension size;

	private	TaskLabel 			nameLabel = new TaskLabel( "Task" );
	
		
	public TaskFigure() {
		add( nameLabel );
	
		addDefaultInput();
		addDefaultOutput();
	}
	
	public void setName( String name ) {
		nameLabel.setText( name );
	}

	private Dimension getFixedSize() {
		if( size == null ) {
			size = FigureUtilities.getStringExtents( "W",
													 getFont() );
			size.height = 2 * FigureConstants.PORT_SIDE + 3 * FigureConstants.PORT_SPACING + 2 + 1; 
			size.width *= 10;
			size.width += 2 * FigureConstants.PORT_SIDE;
			size.width += 1;
		}
		return size;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return getFixedSize();
	}



	private class TaskLabel extends Figure {
		Label		label;
		/**
		 * @param s
		 */
		public TaskLabel(String s) {
			label = new Label( s );
			add( label );

			setOpaque( true );
			setBackgroundColor( ColorConstants.white );
			setBorder( new LineBorder( 2 ) );
			
			FlowLayout layout = new FlowLayout();
			layout.setMajorAlignment( FlowLayout.ALIGN_CENTER );
			setLayoutManager( layout );
		}

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
		 */
		public Dimension getPreferredSize(int wHint, int hHint) {
			Dimension	preferredSize = getParent().getSize();
			preferredSize.width -= 2 * FigureConstants.PORT_SIDE;
			
			return preferredSize;
		}

		public void setText( String text ) {
			label.setText( text );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#layout()
	 */
	public void validate() {
		LayoutManager layout = getLayoutManager();
		layout.setConstraint( nameLabel, new Rectangle( FigureConstants.PORT_SIDE, 0, -1, -1 ) );

		super.validate();
	}
}
