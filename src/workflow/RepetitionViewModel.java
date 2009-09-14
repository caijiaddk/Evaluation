package workflow;

import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;



public class RepetitionViewModel extends AbstractViewModel
{

	RepetitionViewModel(AbstractViewModel parent, TrustNode node) {
		super(parent, node);
	}

	@Override
	public void calculateSize() {
		AbstractViewModel son = sons.get(0);
		setSize(212 + son.getSize().x, son.getSize().y + 12);
	}

	@Override
	public void locateViews() {
		sons.get(0).setLocation(this.getLocation(), 106, 6);
	}

	@Override
	public void draw(GC gc) {
		Point temp = new Point(this.getLocation().x, this.getLocation().y + this.getSize().y / 2);
		
		//gc.drawRectangle(temp.x, temp.y - 20, 5, 39);
		gc.drawImage(rectangle, temp.x, temp.y - 20);
		
		Utilities.drawArrow(gc, temp.x + 6, temp.y, temp.x + 36, temp.y);
		
		//gc.drawOval(temp.x + 36, temp.y - 20, 39, 39);
		gc.drawImage(circle, temp.x + 36, temp.y - 20);
		
		ArrayList<Point> starts = sons.get(0).getFronts();
		for(Point start : starts)
			Utilities.drawArrow(gc, temp.x + 76, temp.y, start.x, start.y);
		
		temp.x = this.getLocation().x + this.getSize().x - 76;
		
		ArrayList<Point> ends = sons.get(0).getEnds();
		for(Point end : ends)
			Utilities.drawArrow(gc, end.x, end.y, temp.x, temp.y);
		
		//gc.drawOval(temp.x, temp.y - 20, 39, 39);
		gc.drawImage(circle, temp.x, temp.y - 20);
		
		Utilities.drawArrow(gc, temp.x + 40, temp.y, temp.x +70, temp.y);
		
		//gc.drawRectangle(temp.x + 70, temp.y - 20, 5, 39);
		gc.drawImage(rectangle, temp.x + 70, temp.y - 20);
		
		gc.drawLine(temp.x + 20, temp.y - 20, temp.x + 20, this.getLocation().y + 1);
		gc.drawLine(temp.x + 20, this.getLocation().y + 1, this.getLocation().x + 3, this.getLocation().y + 1);
		Utilities.drawArrow(gc, this.getLocation().x + 3, this.getLocation().y + 1, this.getLocation().x + 3, this.getLocation().y + this.getSize().y / 2 - 20);;
	}

	@Override
	public ArrayList<Point> calculateEnds() {
		ArrayList<Point> result = new ArrayList<Point>();
		Point startPoint = new Point(this.getLocation().x + this.getSize().x, this.getLocation().y + this.getSize().y / 2);
		result.add(startPoint);
		return result;
	}

	@Override
	public ArrayList<Point> calculateFronts() {
		ArrayList<Point> result = new ArrayList<Point>();
		Point startPoint = new Point(this.getLocation().x, this.getLocation().y + this.getSize().y / 2);
		result.add(startPoint);
		return result;
	}
	
}
