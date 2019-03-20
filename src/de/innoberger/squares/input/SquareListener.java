package de.innoberger.squares.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import de.innoberger.squares.square.Square;

public class SquareListener implements MouseListener {

	private Square square;

	public SquareListener(Square square) {
		this.square = square;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.square.getFrame().freeze) {
			this.square.getFrame().refreshGrid();
			return;
		}

		if (SwingUtilities.isRightMouseButton(e)) {
			this.square.toggleMark();
			return;
		}

		if (this.square.isMarked()) {
			return;
		}

		if (this.square.isMine()) {
			System.out.println("You stabbed on a mine!");
			this.square.reveal(false);
		} else {
			this.square.reveal(true);
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
