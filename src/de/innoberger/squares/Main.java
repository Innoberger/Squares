package de.innoberger.squares;

public class Main {
	private Frame frame;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		this.frame = new Frame(this);
	}

	public void forceHardReset() {
		this.frame = new Frame(this);
	}

	public Frame getFrame() {
		return this.frame;
	}
}
