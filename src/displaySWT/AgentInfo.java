package displaySWT;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public class AgentInfo {
	
	Image agentImage;
	Point startPoint;
	Point endPoint;
	Point currentPoint;
	int currentposotion = 0;
	int interval;
	double stepX, stepY;

	public AgentInfo(Image image, Point start, Point end) {
		agentImage = image;
		startPoint = new Point(start.x, start.y);
		endPoint = new Point(end.x, end.y);
		currentPoint = new Point(start.x, start.y);
	}

	public Image getAgentImage() {
		return agentImage;
	}

	public void setAgentImage(Image agentImage) {
		this.agentImage = agentImage;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Point getCurrentPoint() {
		return currentPoint;
	}

	public void setCurrentPoint(Point currentPoint) {
		this.currentPoint = currentPoint;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
		stepX = 1.0 * (endPoint.x - startPoint.x) / interval;
		stepY = 1.0 * (endPoint.y - startPoint.y) / interval;
	}

	public void increase() {
		currentposotion++;
		currentPoint.x = startPoint.x + (int) (stepX * currentposotion);
		currentPoint.y = startPoint.y + (int) (stepY * currentposotion);
	}

	public void decrease() {
		currentposotion--;
		currentPoint.x = startPoint.x + (int) (stepX * currentposotion);
		currentPoint.y = startPoint.y + (int) (stepY * currentposotion);
	}
}
