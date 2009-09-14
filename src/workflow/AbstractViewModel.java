package workflow;


import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import util.ImageFactory;



public abstract class AbstractViewModel
{
	AbstractViewModel(AbstractViewModel parent, TrustNode node)
	{
		this.parent = parent;
		this.node = node;

		if ((node.getMembers().size()) > 0 && !(this instanceof ComponentViewModel))
		for(Member member : node.getMembers())
			sons.add(getViewModel(this, member.getNode()));
		
		calculateSize();
	}
	
	public abstract void calculateSize();
	
	public void layout()
	{
		locateViews();
		
		for(AbstractViewModel son : sons)
			son.layout();
	}
	
	public abstract void locateViews();
	
	public void paint(GC gc)
	{
		if (!this.isVisible())
			return;
		Color oldBackground = null;
		Color oldForeground = null;
		Color selectcolor;
		if (this == PetriView.selected)
		{
			oldBackground = gc.getBackground();
			oldForeground = gc.getForeground();
			selectcolor = new Color(Display.getCurrent(),164,216,255);//Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			
			gc.setBackground(selectcolor);
			gc.fillRectangle(this.getLocation().x-5, this.getLocation().y-5, this.getSize().x+10, this.getSize().y+10);
			
		}
		
			if (this.isExpanded())
			{
				for(AbstractViewModel son : sons)
					son.paint(gc);
				draw(gc);
			}
			else
				primDraw(gc);
		
		if (this == PetriView.selected)
		{
			gc.setBackground(oldBackground);
			gc.setForeground(oldForeground);
		}
	}
	
	public abstract void draw(GC gc);
	
	public void primDraw(GC gc)
	{
		Point temp = new Point(this.getLocation().x + this.getSize().x / 2, this.getLocation().y + this.getSize().y / 2);
	//	gc.drawImage(rectangle, temp.x - 3, temp.y - 20);
		if (this == PetriView.selected) 
			gc.fillRectangle(temp.x - 2, temp.y - 18, 4, 36);
		//gc.drawRectangle(temp.x - 3, temp.y - 20, 5, 39);
	}
	
	public ArrayList<Point> getFronts()
	{
		if (this.isExpanded())
			return calculateFronts();
		Point temp = new Point(this.getLocation().x + this.getSize().x / 2 - 3, this.getLocation().y + this.getSize().y / 2);
		ArrayList<Point> result = new ArrayList<Point>();
		result.add(temp);
		return result;
	}
	
	public abstract ArrayList<Point> calculateFronts();
	
	public ArrayList<Point> getEnds()
	{
		if (this.isExpanded())
			return calculateEnds();
		Point temp = new Point(this.getLocation().x + this.getSize().x / 2 + 3, this.getLocation().y + this.getSize().y / 2);
		ArrayList<Point> result = new ArrayList<Point>();
		result.add(temp);
		return result;
	}
	
	public abstract ArrayList<Point> calculateEnds();
	
	public boolean contains(Point p)
	{
		if (p.x < this.getLocation().x + this.getSize().x)
			if (p.x > this.getLocation().x)
				if (p.y < this.getLocation().y + this.getSize().y)
					if (p.y > this.getLocation().y)
						return true;
		return false;
	}
	
	public void doubleClick(Point p)
	{
		if (!this.contains(p))
			return;
		
		boolean temp = false;
		for(AbstractViewModel son : sons)
			if (son.contains(p))
			{
				son.doubleClick(p);
				temp = true;
			}
		
		if (!temp)
		{
			this.setExpanded(!this.isExpanded());
		}

	}
	
	public AbstractViewModel find(String ID)
	{
		if (this.node.getID().equals(ID))
			return this;
		
		for(AbstractViewModel son : sons)
		{
			AbstractViewModel result = son.find(ID);
			if (result != null)
				return result;
		}
		return null;
	}
	
	public static AbstractViewModel getViewModel(AbstractViewModel parent, TrustNode node)
	{
		AbstractViewModel result = null;
		if (node instanceof Sequence)
			result = new SequenceViewModel(parent, node);
		if (node instanceof Selection)
			result = new SelectionViewModel(parent, node);
		if ((node instanceof AndConcurrency) || (node instanceof OrConcurrency))
			result = new ConcurrencyViewModel(parent, node);
		if (node instanceof Repetition)
			result = new RepetitionViewModel(parent, node);
		if (result == null)
			result = new ComponentViewModel(parent, node);
		return result;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	public void expande()
	{
		this.expanded = true;
	}
	
	public void collapse()
	{
		this.expanded = false;
	}

	public Point getLocation() {
		return new Point(location.x, location.y);
	}

	public void setLocation(Point start, int x, int y)
	{
		this.location.x = start.x + x;
		this.location.y = start.y + y;
	}
	
	public void setLocation(int x, int y) {
		this.setLocation(new Point(0, 0), x, y);
	}

	public Point getSize() {
		return new Point(size.x, size.y);
	}

	public void setSize(int x, int y) {
		this.size.x = x;
		this.size.y = y;
	}
	
	public TrustNode node = null;
	protected AbstractViewModel parent = null;
	public ArrayList<AbstractViewModel> sons = new ArrayList<AbstractViewModel>();
	private boolean expanded = true;
	private boolean visible = true;
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	private Point location = new Point(0,0);
	private Point size = new Point(0,0);

	
	public static Image circle = ImageFactory.loadImage(Display.getCurrent(),ImageFactory.CIRCLE);
	public static Image rectangle = ImageFactory.loadImage(Display.getCurrent(),ImageFactory.RECTANGLE);
}