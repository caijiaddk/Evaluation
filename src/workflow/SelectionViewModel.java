package workflow;

import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;




class SelectionViewModel extends AbstractViewModel
{

	SelectionViewModel(AbstractViewModel parent, TrustNode node) {
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
		
		setSize(width, height);
	}

	@Override
	public void locateViews() {
		int y = 0;
		Point size = getSize();
		
		for(AbstractViewModel son : sons)
		{
			son.setLocation(this.getLocation(), (size.x - son.getSize().x) / 2, y);
			y += son.getSize().y + 5;
		}
	}

	@Override
	public void draw(GC gc) {
		return;
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
		for(int count = 0; count < sons.size(); count++)
		{
			ArrayList<Point> temp = sons.get(count).getFronts();
			result.addAll(temp);
		}
		return result;
	}	
}