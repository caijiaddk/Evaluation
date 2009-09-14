package workflow;
import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;




public class SequenceViewModel extends AbstractViewModel
{

	SequenceViewModel(AbstractViewModel parent, TrustNode node) {
		super(parent, node);
		// 
	}

	@Override
	public void calculateSize() {
		int height = 0;
		int width = 0;
		
		for(AbstractViewModel son : sons)
		{
			Point sonSize = son.getSize();
			width += sonSize.x;
			height = Math.max(height, sonSize.y);
		}
		width += 100 * (sons.size() - 1);
		
		setSize(width, height);
	}

	@Override
	public void locateViews() {
		int x = 0;
		Point size = getSize();
		
		for(AbstractViewModel son : sons)
		{
			son.setLocation(this.getLocation(), x, (size.y - son.getSize().y) / 2);
			x += son.getSize().x + 100;
		}
	}

	@Override
	public void draw(GC gc) {
		for(int count = 1; count < sons.size(); count++)
		{
			AbstractViewModel son = sons.get(count);
			AbstractViewModel forward = sons.get(count - 1);
			Point temp = new Point(son.getLocation().x - 70, this.getLocation().y + this.getSize().y / 2 - 20);
			
			ArrayList<Point> ends = forward.getEnds();
			for(Point end : ends)
				Utilities.drawArrow(gc, end.x, end.y, temp.x, temp.y + 20);

			gc.drawImage(circle, temp.x, temp.y);
			//gc.drawOval(temp.x, temp.y, 39, 39);
			
			ArrayList<Point> starts = son.getFronts();
			for(Point start : starts)
				Utilities.drawArrow(gc, temp.x + 40, temp.y + 20, start.x, start.y);
		}
	}

	@Override
	public ArrayList<Point> calculateEnds() {
		int lastindex = sons.size() - 1;
		AbstractViewModel end = sons.get(lastindex);
		return end.getEnds();
	}

	@Override
	public ArrayList<Point> calculateFronts() {
		AbstractViewModel start = sons.get(0);
		return start.getFronts();
	}
	
}


	


