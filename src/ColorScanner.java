import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;

public class ColorScanner {
	
	Robot r;
	private boolean isAFK;
	private int sWidth;
	private int sHeight;
	
	private final int X_PARSES = 20;
	private final int Y_PARSES = 20;
	private final int DETAIL = 100;
	
	private final int[] MODEL_COLOR = {70, 82, 162};
	
	public ColorScanner(Robot r) {
		this.r = r;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		sWidth = (int) screenSize.getWidth();
		sHeight = (int) screenSize.getHeight();
	}
	
	public boolean isAFK() {
		return isAFK;
	}
	
	public void update() {
		int pixelWidth = sWidth / X_PARSES;
		int pixelHeight = sHeight / Y_PARSES;
		boolean oneCheck = false;
		for (int i = 0; !oneCheck && i < X_PARSES; i++) {
			for (int j = 0; !oneCheck && j < Y_PARSES; j++) {
				double[] temp = getAverage(pixelWidth * i, pixelHeight * j, pixelWidth, pixelHeight);
				boolean tempCheck = true;
				for (int k = 0; k < 3; k++) {
					if (Math.abs(temp[k] - MODEL_COLOR[k]) > 30) {
						tempCheck = false;
					}
				}
				oneCheck = tempCheck;
			}
		}
		isAFK = oneCheck;
	}
	
	private double[] getAverage(int initX, int initY, int width, int height) {
		double[] res = new double[3];
		int total = 0;
		for (int x = 0; x < width; x += DETAIL) {
			for (int y = 0; y < height; y += DETAIL) {
				total++;
				Color temp = r.getPixelColor(x + initX, y + initY);
				res[0] += temp.getRed();
				res[1] += temp.getGreen();
				res[2] += temp.getBlue();
			}
		}
		res[0] /= total;
		res[1] /= total;
		res[2] /= total;
		return res;
	}
}
