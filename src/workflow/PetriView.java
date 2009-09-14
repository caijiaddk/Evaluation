package workflow;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;




public class PetriView  extends Composite{

	public PetriView(Composite parent, int style, TrustNode root) {
		super(parent, style);
		
		model = AbstractViewModel.getViewModel(null, root);
		model.setLocation(70, 0);
		model.layout();
		
		setSize(model.getSize().x + 140, model.getSize().y);
		addPaintListener(new PaintListener()
		{

			public void paintControl(PaintEvent e) {
				model.paint(e.gc);
				
				Composite c = (Composite)e.widget;
				
				e.gc.drawImage(model.circle, 0, c.getSize().y /2 - 20);
				//e.gc.drawOval(0, c.getSize().y /2 - 20, 39, 39);
				
				for(Point point : model.getFronts())
					Utilities.drawArrow(e.gc, 39, c.getSize().y / 2, point.x, point.y);
				
				for(Point point : model.getEnds())
					Utilities.drawArrow(e.gc, point.x, point.y, c.getSize().x - 40, c.getSize().y / 2);
				
				e.gc.drawImage(model.circle, c.getSize().x - 40, c.getSize().y / 2 - 20);
				//e.gc.drawOval(c.getSize().x - 40, c.getSize().y / 2 - 20, 39, 39);
			}
				
		});
		
		addMouseListener(new MouseAdapter()
		{
			public void mouseDoubleClick(MouseEvent e)
			{
				Point p = new Point(e.x, e.y);
				model.doubleClick(p);
				Composite c = (Composite)e.widget;
				c.redraw();
			}
		});
	}

	public AbstractViewModel model = null;
	public static AbstractViewModel selected= null;
}



