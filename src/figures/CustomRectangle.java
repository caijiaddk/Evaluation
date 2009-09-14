package figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class CustomRectangle extends Shape {
	
	private Point location;
	private Point region;

	public Point getPoint() {
		return location;
	}
	public void setPoint(Point point) {
		this.location = point;
	}
	
	public Point getRegion() {
		return region;
	}
	public void setRegion(Point region) {
		this.region = region;
	}
	
	public CustomRectangle(Point location,Point region){
		this.location = location;
		this.region = region;
	}
	@Override
	protected void fillShape(Graphics graphics) {
		
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		graphics.setBackgroundColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		graphics.drawRectangle(location.x, location.y, region.x, region.y);
		
	}
}
