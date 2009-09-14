package reputation;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;

public class NodeInfoView{
		
		Point location;
		double childrenstartdirection;
		double childrenenddirection;
		Figure figure;

		public NodeInfoView(Point location,double start,double end){
			this.location = location;
			childrenstartdirection = start;
			childrenenddirection = end;
		}
		
		public Point getLocation() {
			return location;
		}
		public void setLocation(Point location) {
			this.location = location;
		}
		public double getChildrenstartdirection() {
			return childrenstartdirection;
		}
		public void setChildrenstartdirection(double childrenstartdirection) {
			this.childrenstartdirection = childrenstartdirection;
		}
		public double getChildrenenddirection() {
			return childrenenddirection;
		}
		public void setChildrenenddirection(double childrenenddirection) {
			this.childrenenddirection = childrenenddirection;
		}
		
		public Figure getFigure() {
			return figure;
		}

		public void setFigure(Figure figure) {
			this.figure = figure;
		}
	}