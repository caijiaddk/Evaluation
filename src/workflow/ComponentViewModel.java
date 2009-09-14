package workflow;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;




class ComponentViewModel extends AbstractViewModel
{
	ComponentViewModel(AbstractViewModel parent, TrustNode node) {
		super(parent, node);
	}

	@Override
	public void calculateSize() {
		setSize(5, 39);
	}

	@Override
	public void locateViews() {
	}

	@Override
	public void draw(GC gc) {
		Point p = this.getLocation();
		Color newcolor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		Color oldcolor = gc.getBackground();
		gc.setBackground(newcolor);
		gc.fillRectangle(p.x, p.y, 5, 39);		
		gc.setBackground(oldcolor);
//		gc.drawText(""+id, p.x+10, p.y+20);
	}

	@Override
	public ArrayList<Point> calculateFronts() {
		ArrayList<Point> result = new ArrayList<Point>();
		result.add(new Point(this.getLocation().x, this.getLocation().y + 19));
		return result;
	}

	@Override
	public ArrayList<Point> calculateEnds() {
		ArrayList<Point> result = new ArrayList<Point>();
		result.add(new Point(this.getLocation().x + 6, this.getLocation().y + 19));
		return result;
	}

}

