package de.innoberger.squares.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import de.innoberger.squares.Frame;

public class GlobalListener implements MouseListener {
	
	private Frame frame;
	
	public GlobalListener(Frame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.frame.freeze) {
			this.frame.refreshGrid();
			return;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
