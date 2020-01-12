import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import processing.core.PApplet;
public class Application extends PApplet{
	
	private Robot r;
	private ColorScanner cs;
	private boolean canStart;
	//private final String command = "/warp island";
	private final int[] command = {KeyEvent.VK_ENTER, KeyEvent.VK_SLASH, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_R, 
			KeyEvent.VK_P, KeyEvent.VK_SPACE, KeyEvent.VK_I, KeyEvent.VK_S, KeyEvent.VK_L, KeyEvent.VK_A,
			KeyEvent.VK_N, KeyEvent.VK_D, KeyEvent.VK_ENTER};
	
	public static void main(String[] args) {
		PApplet.main("Application");
	}
	
	public void settings() {
		this.size(200, 200);
	}
	
	public void setup() {
		try {
			r = new Robot();
		} catch (AWTException e) {
			System.out.println("Error initializing Robot: ");
			e.printStackTrace();
		}
		cs = new ColorScanner(r);
		canStart = false;
		System.out.println("\nPress space to start the script (be in the AFK pond first).");
		System.out.println("Press q to quit.");
	}
	
	public void draw() {
		this.background(0);
		String time = java.time.LocalTime.now().toString();
		time = time.substring(0, time.length() - 10);
		String head = "[" + time + "] ";
		if (canStart) {
			wait(20_000);
			boolean startState = inInventory();
			wait(50);
			if (startState) {
				r.keyPress(KeyEvent.VK_E);
			}
			wait(400);
			System.out.println(head + "Checking colors");
			cs.update();
			System.out.print(head);
			if (cs.isAFK()) {
				System.out.println("Detected as in AFK pool. Doing nothing");
				if (startState) 
					r.keyPress(KeyEvent.VK_E);
			} else {
				System.out.println("Detected as moved. Moving back to AFK pool.");
				escapePattern(head);
				if (startState) 
					r.keyPress(KeyEvent.VK_E);
				wait(5000);
			}	
		}
	}
	
	public void escapePattern(String head) {
		System.out.println(head + "Pressing mouse down.");
		for (int i = 0; i < 5; i++) {
			r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			wait(300);
		}
		System.out.println(head + "Warping to island");
		wait(2000);
		for (int n : command) {
			r.keyPress(n);
			r.keyRelease(n);
			wait(50);
		}
		wait(5000);
		System.out.println(head + "Moving right.");
		// Oh god does this work?
	    r.keyPress(KeyEvent.VK_D);
	    wait(1000);
	    r.keyRelease(KeyEvent.VK_D);
	    System.out.println(head + "Done.");
	}
	
	public boolean inInventory() {
		Color first = r.getPixelColor(width / 4, height / 4);
		r.keyPress(KeyEvent.VK_E);
		wait(100);
		Color second = r.getPixelColor(width / 4, height / 4);
		wait(100);
		r.keyPress(KeyEvent.VK_E);
		int diff = (first.getRed() - second.getRed()) + (first.getGreen() - second.getGreen()) + 
				(first.getBlue() - second.getBlue());
		return diff < 0;
	}
	
	public void wait(int num) {
		try {
			Thread.sleep(num);
		} catch (InterruptedException e) {
			System.out.println("Problem with thread: ");
			e.printStackTrace();
		}
	}
	
	public void keyPressed() {
		if (this.key == 'q') {
			exit();
		}
		else if (this.key == ' ') {
			this.background(255);
			canStart = true;
		} 
		else if (this.key == 'b') {
			System.out.println("A");
			wait(5000);
			escapePattern(":)");
		}
		else {
			System.out.println(this.key + " is not a recognized command.");
		}
	}
	
}
