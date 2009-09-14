package figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class Circle extends Shape {


	private Point point;

	private Point startPoint;
	
	public Circle(Point point,Point startPoint){
		this.point = point;
		this.startPoint = startPoint;
	}

	protected void fillShape(Graphics graphics) {
//		PointList points = new PointList();
//		Point point = new Point(5,5);
//		Point point2 = new Point(25,25);
//		points.addPoint(point);
//		points.addPoint(point2);
//		graphics.drawPolyline(points);
//		Image image = new Image(null,"icons/del.png");
//		image.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
//		graphics.drawImage(image, new Point(30,30));
//		graphics.drawOval(new Rectangle(100,100,20,20));
		graphics.setBackgroundColor(ColorConstants.red);
		graphics.fillOval(new Rectangle(startPoint.x,startPoint.y,point.x,point.y));
//		graphics.setForegroundColor(ColorConstants.red);
	}


	protected void outlineShape(Graphics graphics) {
//		graphics.setLineWidth(5);
//		graphics.drawOval(10, 10, 10, 10);
//		graphics.setBackgroundColor(ColorConstants.orange);
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
