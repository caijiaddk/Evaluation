package workflow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class Utilities {
	
	public static void drawArrow(GC gc, int x1, int y1, int x2, int y2)
	{
		gc.drawLine(x1, y1, x2, y2);
		
		double angle = Math.asin((1.0 * (y1-y2))/Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)));
		if (x1>x2)
			angle = Math.PI - angle;

		int x3 = x2 + (int)(10 * Math.cos(angle + Math.PI *11/ 12));
		int y3 = y2 - (int)(10 * Math.sin(angle + Math.PI *11/ 12));
		int x4 = x2 + (int)(10 * Math.cos(angle + Math.PI *13/ 12));
		int y4 = y2 - (int)(10 * Math.sin(angle + Math.PI *13/ 12));
		gc.drawLine(x3, y3, x2, y2);
		gc.drawLine(x4, y4, x2, y2);
	}
	
	public static Point pointMap(Composite from, Composite to, Point point)
	{
		return Display.getCurrent().map(from, to, point);
	}
	
	public static int style = SWT.NONE;
	public static Thread controlThread = null;
}
