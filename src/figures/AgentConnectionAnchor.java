package figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

public class AgentConnectionAnchor extends AbstractConnectionAnchor {

	/**
	 * 
	 */
	public AgentConnectionAnchor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 */
	public AgentConnectionAnchor(IFigure owner) {
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
		
		if(result.x>arg0.x){
			result.x = result.x-(r.width);
		}
		return result;
	}

}
