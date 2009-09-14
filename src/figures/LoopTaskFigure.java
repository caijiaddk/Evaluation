/*
 * Created on Jul 30, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/LoopTaskFigure.java,v $
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
 * 		ddean		Initial version
 */
package figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.FrameBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.geometry.Insets;

/**
 * @author ddean
 *
 */
public class LoopTaskFigure extends CompoundTaskFigure {
	private LoopTaskBorder loopTaskBorder;
	
	/**
	 * 
	 */
	public LoopTaskFigure() {
		super();
		
		loopTaskBorder = new LoopTaskBorder();
		getCompoundPanel().setBorder( loopTaskBorder );
	}

	public void setWhileCondition( String whileCondition ) {
		loopTaskBorder.setCondition( whileCondition );
	}

	private class LoopTaskBorder extends FrameBorder {
		private WhileConditionBorder whileConditionBorder;
		private TitleBarBorder titleBorder;
		
		public LoopTaskBorder() {
		}
		
		protected void createBorders() {
			titleBorder = new TitleBarBorder();
			titleBorder.setTextColor( ColorConstants.white );
			whileConditionBorder = new WhileConditionBorder();		
			
			Border t = new CompoundBorder( whileConditionBorder, new LineBorder() );
			
			inner = new CompoundBorder( titleBorder, t );
			outer = new LineBorder(2);
		}
		
		public void setLabel( String s ) {
			titleBorder.setLabel( s );
		}
		
		public void setCondition( String s ) {
			whileConditionBorder.setLabel( "   while(" + ((s != null && s.length() > 0) ? s : "false") + ")" );
		}
	}
	
	private class WhileConditionBorder extends TitleBarBorder {
		public WhileConditionBorder() {
			super("   while(false)");
			setBackgroundColor( ColorConstants.white );
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.AbstractLabeledBorder#calculateInsets(org.eclipse.draw2d.IFigure)
		 */
		protected Insets calculateInsets(IFigure figure) {
			Insets result = super.calculateInsets(figure);
			result.left = result.top;
			result.bottom = -1;
			result.right = -1;
			
			return result;
		}
	}
}
