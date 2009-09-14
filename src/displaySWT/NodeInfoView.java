package displaySWT;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.widgets.GraphNode;

class NodeInfoView{
		
		Point position;
		GraphNode graphNode;
		Color color;
		
		public Point getPosition() {
			return position;
		}

		public void setPosition(Point position) {
			this.position = position;
		}

		public GraphNode getGraphNode() {
			return graphNode;
		}

		public void setGraphNode(GraphNode graphNode) {
			this.graphNode = graphNode;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		
		public NodeInfoView(Point position,GraphNode graphNode,Color color){
			this.position = position;
			this.graphNode = graphNode;
			this.color = color;
		}
		
		public NodeInfoView(GraphNode graphNode){
			this.graphNode = graphNode;
		}
	}