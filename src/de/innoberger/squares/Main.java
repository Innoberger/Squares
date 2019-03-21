package de.innoberger.squares;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
	
	public static ScheduledExecutorService executorService;
	private static Frame frame;
	public static Random random;
	
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		executorService = Executors.newSingleThreadScheduledExecutor();
		
		random = new Random();
		frame = new Frame(this);
	}

	public void forceHardReset() {
		frame = new Frame(this);
	}

	public static Frame getFrame() {
		return frame;
	}
}
