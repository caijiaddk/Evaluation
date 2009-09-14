package workflow;

import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;



class ConcurrencyViewModel extends AbstractViewModel
{

	ConcurrencyViewModel(AbstractViewModel parent, TrustNode node) {
		super(parent, node);
	}

	@Override
	public void calculateSize() {
		int height = 0;
		int width = 0;
		
		for(AbstractViewModel son : sons)
		{
			Point sonSize = son.getSize();
			height += sonSize.y;
			width = Math.max(width, sonSize.x);
		}
		height += 5 * (sons.size() - 1);
		width += 106;		
		setSize(width, height);
	}

	@Override
	public void locateViews() {
		int y = 0;
		Point size = getSize();
		
		for(AbstractViewModel son : sons)
		{
			son.setLocation(this.getLocation(), 106 + (size.x - 106 - son.getSize().x) / 2, y);
			y += son.getSize().y + 5;
		}
	}

	@Override
	public void draw(GC gc) {
		Point temp = new Point(this.getLocation().x, this.getLocation().y + this.getSize().y / 2);
		
		//gc.drawRectangle(temp.x, temp.y - 20, 5, 39);
		gc.drawImage(rectangle, temp.x, temp.y - 20);
		
		for(int count = 0; count < sons.size(); count++)
		{
			AbstractViewModel son = sons.get(count);
			Point sonlocation = son.getLocation();
			
			Utilities.drawArrow(gc, temp.x + 6, temp.y, temp.x + 36, sonlocation.y + son.getSize().y / 2);
			
			//gc.drawOval(temp.x + 36, sonlocation.y + son.getSize().y / 2 - 20, 39, 39);
			gc.drawImage(circle, temp.x + 36, sonlocation.y + son.getSize().y / 2 - 20);
			
			ArrayList<Point> starts = son.getFronts();
			for(Point start : starts)
				Utilities.drawArrow(gc, temp.x + 76, sonlocation.y + son.getSize().y / 2, start.x, start.y);
		}
	}

	@Override
	public ArrayList<Point> calculateEnds() {
		ArrayList<Point> result = new ArrayList<Point>();
		for(int count = 0; count < sons.size(); count++)
		{
			ArrayList<Point> temp = sons.get(count).getEnds();
			result.addAll(temp);
		}
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

