package de.innoberger.squares;

public class Main {
	private static Frame frame;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		frame = new Frame(this);
	}

	public void forceHardReset() {
		frame = new Frame(this);
	}

	public static Frame getFrame() {
		return frame;
	}
}
