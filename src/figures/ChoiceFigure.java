package figures;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * @author ddean
 *
 */
public class ChoiceFigure extends NodeFigure {
	private ChoiceBody bodyFigure;
	private Map conditionLabels = new HashMap();
	private static final Font CONDITION_FONT =
		new Font(Display.getCurrent(), "Arial", 8, SWT.NORMAL);
	private ConditionPortRequestListener portRequestListener;
	private ArrayList<OutputPortFigure> outputportList = new ArrayList<OutputPortFigure>(); 

	public ArrayList<OutputPortFigure> getOutputportList() {
		return outputportList;
	}

	public ChoiceFigure() {
		super();

		addDefaultInput();
		bodyFigure = new ChoiceBody();
		add(bodyFigure);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		if (wHint < 0 || hHint < 0) {
			return new Dimension(
				96,
				(outputConnectionAnchors.size() + 2)
					* 3
					/ 2
					* FigureConstants.PORT_SIDE);
		}
		return super.getPreferredSize(wHint, hHint);
	}

	public void addConditionalOutput(String name, String condition) {
		OutputPortFigure outputPortFigure = addOutput(name);
		outputportList.add(outputPortFigure);

		Label conditionLabel = new Label(condition);
		conditionLabel.setBorder(new MarginBorder(2));
		conditionLabel.setFont(CONDITION_FONT);
		conditionLabel.setTextAlignment(PositionConstants.RIGHT);
		conditionLabel.setLabelAlignment(PositionConstants.LEFT);

		conditionLabels.put(name, conditionLabel);
		bodyFigure.add(conditionLabel);
	}

	public void removeConditionalOutput(String condition) {
		removeOutput(condition);
		outputportList.remove(outputportList.size()-1);

		Label conditionLabel = new Label(condition);
		conditionLabel.setBorder(new MarginBorder(2));
		conditionLabel.setFont(CONDITION_FONT);
		conditionLabel.setTextAlignment(PositionConstants.RIGHT);
		conditionLabel.setLabelAlignment(PositionConstants.LEFT);

		Label f = (Label) conditionLabels.get(condition);
		conditionLabels.remove(condition);
		bodyFigure.remove(f);
	}

	public void setName(String name) {
		bodyFigure.setName(name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#layout()
	 */
	public void validate() {
		LayoutManager layout = getLayoutManager();
		layout.setConstraint(
			bodyFigure,
			new Rectangle(FigureConstants.PORT_SIDE, 0, -1, -1));

		super.validate();
	}


	/**
	 * Updates the condition label in the Choice figure.
	 * @param portName
	 * @param prevCondition
	 * @param newCondition
	 */
	public void updateCondition(
		String portName,
		String prevCondition,
		String newCondition) {

		Label label = (Label) conditionLabels.get(portName);
		label.setText(newCondition);
		revalidate();
	}

	/**
	 * @param pt
	 * @return the condition label at the specified pt
	 */
	public Label getLabelAt(Point pt) {
		Iterator it = conditionLabels.values().iterator();
		Label label;
		Rectangle rc;

		while (it.hasNext()) {
			label = (Label) it.next();

			rc = new PrecisionRectangle(label.getBounds());
			label.translateToAbsolute(rc);
			if (rc.contains(pt)) {
				return label;
			}
		}

		return null;
	}

	/**
	 * @return
	 */
	public ConditionPortRequestListener getPortRequestListener() {
		return portRequestListener;
	}

	/**
	 * @param listener
	 */
	public void setPortRequestListener(ConditionPortRequestListener listener) {
		portRequestListener = listener;
	}

	private class ChoiceBody extends Figure {
		ConditionGlyph addConditionButton = new ConditionGlyph();
		Label nameLabel = new Label();

		public ChoiceBody() {
			setBorder(new LineBorder());
			setLayoutManager(new XYLayout());
			setOpaque(true);
			add(addConditionButton);
			nameLabel.setLabelAlignment(PositionConstants.LEFT);
			nameLabel.setTextAlignment(PositionConstants.RIGHT);
			add(nameLabel);

			// listener to invoke the portRequestListener when the "add condition" glyph is clicked
			addConditionButton.addMouseListener(new MouseListener() {
				public void mousePressed(MouseEvent me) {
					if (portRequestListener != null) {
						portRequestListener.addPortRequest();
					}
				}

				public void mouseReleased(MouseEvent me) {
				}

				public void mouseDoubleClicked(MouseEvent me) {
				}
			});
		}

		public void setName(String name) {
			nameLabel.setText(name);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.IFigure#paint(org.eclipse.draw2d.Graphics)
		 */
		public void paint(Graphics graphics) {
			graphics.setLineStyle(Graphics.LINE_DASH);

			super.paint(graphics);
		}

		public Dimension getPreferredSize(int wHint, int hHint) {
			Dimension preferredSize = getParent().getSize();
			preferredSize.width -= 2 * FigureConstants.PORT_SIDE;
			preferredSize.height -= FigureConstants.PORT_SIDE / 2;

			return preferredSize;
		}

		/**
		 * recalculate the constraints of all the child figures so that:\
		 * <ul>
		 * <li>the name label and addConditionButton share the top "row" of the parent figure</li>
		 * <li>the labels for port conditon strings are positioned next to the ports they label<li>
		 * </ul>
		 */
		public void validate() {
			String condition;
			Iterator it = conditionLabels.keySet().iterator();
			LayoutManager layout = getLayoutManager();
			Label label;
			int y = 0;
			int yMax = 0;
			int offset;

			getLayoutManager().setConstraint(
				addConditionButton,
				new Rectangle(
					getPreferredSize().width - 15,
					getLocation().y + 1,
					-1,
					-1));
			getLayoutManager().setConstraint(
				nameLabel,
				new Rectangle(4, 2, getPreferredSize().width - 18, -1));

			while (it.hasNext()) {
				condition = (String) it.next();
				ConnectionAnchor anchor = getConnectionAnchor(condition);
				Point pt = anchor.getOwner().getBounds().getCenter();

				label = (Label) conditionLabels.get(condition);
				offset =
					(FigureConstants.PORT_SIDE - label.getTextBounds().height)
						/ 2;
				y = pt.y + offset - FigureConstants.PORT_SPACING;
				if (y > yMax) {
					yMax = y;
				}
				layout.setConstraint(
					label,
					new Rectangle(
						getLocation().x
							+ getSize().width
							- 20
							- label.getTextBounds().width,
						y,
						-1,
						-1));
			}

			super.validate();
		}
	}

	/**
	 * <code>ConditionGlyph</code> a figure that when clicked on
	 * will call the portRequestListener, indicating that the user
	 * is requesting the creation of a new Conditional. The figure itself
	 * does not have any functionality beyond painting itself however.
	 * 
	 * @author ddean
	 *
	 */
	private class ConditionGlyph extends Figure {
		public ConditionGlyph() {
			setOpaque(true);
		}

		public Dimension getPreferredSize(int wHint, int hHint) {
			return new Dimension(
				FigureConstants.PORT_SPACING + 2,
				FigureConstants.PORT_SPACING + 2);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics graphics) {
			super.paintFigure(graphics);

			setForegroundColor(ColorConstants.darkGray);
			Rectangle rc = getBounds().getExpanded(-2, -2);
			graphics.setLineWidth(2);
			graphics.drawRectangle(rc);
			graphics.setLineWidth(1);
			graphics.drawLine(
				rc.x,
				rc.y + rc.height / 2,
				rc.x + rc.width,
				rc.y + rc.height / 2);
			graphics.drawLine(
				rc.x + rc.width / 2,
				rc.y,
				rc.x + rc.width / 2,
				rc.y + rc.height);
		}
	}
}
