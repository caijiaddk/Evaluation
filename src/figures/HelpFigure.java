package figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public class HelpFigure extends Shape {

	private Point point;
	
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public HelpFigure(Point point){
		this.point = point;
	}
	@Override
	protected void fillShape(Graphics graphics) {
		// TODO Auto-generated method stub
		Image image = new Image(null,"resource\\icons\\help.gif");
//		image.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		graphics.drawImage(image, point);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		// TODO Auto-generated method stub

	}

}
