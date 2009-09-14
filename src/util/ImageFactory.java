package util;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Image factory is used to load and dispose image resource.
 * 
 * @author andy
 * 
 */
public class ImageFactory {

	private ImageFactory() {
	}

	public static final String REAL_PATH = "resource\\icons\\";

	public static final String AGENT = "agent.ico";

	public static final String FRIEND = "pc3.ico";

	public static final String HOST = "adim1.png";

	public static final String CIRCLE = "circle40.png";
	public static final String RECTANGLE = "rectangle.png";

	public static final String SAMPLES = "etool16\\samples.gif";

	public static final String SCOPY_EDIT = "etool16\\copy_edit.gif";

	public static final String MAIN = "eclipse48.png";

	public static final String EVALUATION = "search(1).gif";

	public static final String RESULT = "etool16\\result.png";

	@SuppressWarnings("unchecked")
	private static Hashtable htImage = new Hashtable();

	@SuppressWarnings("unchecked")
	public static Image loadImage(Display display, String imageName) {
		Image image = (Image) htImage.get(imageName.toUpperCase());
		if (image == null) {
			image = new Image(display, REAL_PATH + imageName);
			htImage.put(imageName.toUpperCase(), image);
		}
		return image;
	}

	@SuppressWarnings("unchecked")
	public static void dispose() {
		Enumeration e = htImage.elements();
		while (e.hasMoreElements()) {
			Image image = (Image) e.nextElement();
			if (!image.isDisposed())
				image.dispose();
		}
	}
}
