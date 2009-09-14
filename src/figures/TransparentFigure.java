package figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class TransparentFigure extends Shape {

	private Point point;

	private Point startPoint;
	
	public TransparentFigure(Point point,Point startPoint){
		this.point = point;
		this.startPoint = startPoint;
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		// TODO Auto-generated method stub
		graphics.drawRectangle(new Rectangle(startPoint.x,startPoint.y,point.x,point.y));
		graphics.setBackgroundColor(ColorConstants.buttonDarker);
		graphics.setAlpha(200);
		graphics.fillRectangle(new Rectangle(startPoint.x,startPoint.y,point.x,point.y));

	}

	@Override
	protected void outlineShape(Graphics graphics) {
		// TODO Auto-generated method stub

	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

}
