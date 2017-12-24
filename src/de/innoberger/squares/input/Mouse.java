package de.innoberger.squares.input;

import de.innoberger.squares.Frame;
import de.innoberger.squares.Main;
import de.innoberger.squares.square.Square;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	private Main main;

	public Mouse(Main main) {
		this.main = main;
	}

	public void mouseClicked(MouseEvent e) {
		Frame frame = this.main.getFrame();
		int button = e.getButton();

		int posX = e.getX();
		int posY = e.getY();
		if (!frame.freeze) {
			if ((posX < 50) || (posY < 50) || (posX > 1122) || (posY > 692)) {
				return;
			}
			Square square = null;
			for (int i = 0; i < Frame.field.size(); i++) {
				Square sq = (Square) Frame.field.get(i);

				int centerX = sq.getCenterX();
				int centerY = sq.getCenterY();
				if ((posX > centerX - 20) && (posY > centerY - 20) && (posX < centerX + 20) && (posY < centerY + 20)) {
					square = sq;
					break;
				}
			}
			if (square == null) {
				return;
			}
			if (button == 1) {
				square.reveal(true);
			} else if (button == 3) {
				square.toggleMark();
			}
			frame.refreshGrid();
		} else {
			frame.freeze = false;
			frame.draw();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
