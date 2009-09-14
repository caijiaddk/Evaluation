package figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public class SandGlassFigure extends Shape {

	private Point point;
	
	public SandGlassFigure(Point point){
		this.point = point;
	}
	@Override
	protected void fillShape(Graphics graphics) {
		// TODO Auto-generated method stub
		Image image = new Image(null,"icons/green_sandglass.gif");
		graphics.drawImage(image, point);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		// TODO Auto-generated method stub

	}

}
