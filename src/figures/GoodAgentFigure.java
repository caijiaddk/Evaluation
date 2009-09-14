package figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class GoodAgentFigure extends Shape {


	private Point point;
	
	public GoodAgentFigure(Point point){
		this.point = point;
	}
	@Override
	protected void fillShape(Graphics graphics) {
		// TODO Auto-generated method stub
		Image image = new Image(null,"resource/icons/goodpeople.png");
//		image.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		graphics.drawImage(image, point);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		// TODO Auto-generated method stub
		
	}
}
