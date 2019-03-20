package de.innoberger.squares.square;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;

import de.innoberger.squares.Frame;
import de.innoberger.squares.input.SquareListener;

public class Square {

	public static final int SIZE = 40;
	public static final int MINE_PERCENTAGE = 10;

	private int posX;
	private int posY;
	private int nearbyMines;

	private SquareType type;
	private SquareState state;
	private Frame frame;
	private JButton button;

	public Square(int posX, int posY, Frame frame) {
		Random random = new Random();

		this.frame = frame;

		this.posX = posX;
		this.posY = posY;

		this.nearbyMines = 0;

		SquareType st = SquareType.DEFAULT;

		if (random.nextInt(101) <= MINE_PERCENTAGE) {
			st = SquareType.MINE;
		}

		this.type = st;
		this.state = SquareState.HIDDEN;

		this.button = new JButton();
		this.button.setModel(new SquareModel());
		this.button.setBounds(Frame.OFFSET_BETWEEN + this.posX * (Square.SIZE + Frame.OFFSET_BETWEEN),
				Frame.OFFSET_BETWEEN + this.posY * (Square.SIZE + Frame.OFFSET_BETWEEN), Square.SIZE, Square.SIZE);
		this.button.addMouseListener(new SquareListener(this));
		this.button.setBorder(null);
		this.button.setFocusable(false);
		this.button.setMargin(null);
		this.button.setForeground(Color.BLACK); // font color
		this.button.setFont(new Font(Frame.FONT_FAMILY, Font.BOLD, 20));
		this.frame.add(this.button);
	}

	public void draw() {
		if (!this.isRevealed()) {
			this.button.setBackground(Color.CYAN);
		} else if (this.isMine()) {
			this.button.setBackground(Color.RED);
			this.button.setIcon(Frame.mine);
		} else {
			this.button.setBackground(Color.WHITE);
			this.drawNearbyMines();
		}
	}

	private void drawNearbyMines() {
		if (this.nearbyMines == 1) {
			this.button.setForeground(Color.BLUE);
		} else if (this.nearbyMines == 2) {
			this.button.setForeground(Color.GREEN);
		} else if (this.nearbyMines == 3) {
			this.button.setForeground(Color.ORANGE);
		} else {
			this.button.setForeground(Color.RED);
		}

		if (this.nearbyMines > 0) {
			this.button.setText(this.nearbyMines + "");
		}
	}

	public void reveal(boolean multiple) {
		if ((!this.isRevealed()) && (!this.isMarked())) {
			this.state = SquareState.REVEALED;
			this.draw();
			this.forceUnmark();

			Frame.revealed += 1;

			if ((Frame.revealed == Frame.X_SQUARES * Frame.Y_SQUARES - Frame.getMineAmount()) && (!this.frame.freeze)) {
				this.frame.freeze = true;
				this.frame.revealAll(true);
			} else if ((this.isMine()) && (!this.frame.freeze)) {
				this.frame.freeze = true;
				this.frame.revealAll(false);
			}

			if (!multiple) {
				return;
			}

			if ((this.nearbyMines == 0) && (!this.frame.freeze)) {
				ArrayList<Square> surround = getSurroundingSquares();

				for (int i = 0; i < surround.size(); i++) {
					Square sq = (Square) surround.get(i);
					if (!sq.isMine()) {
						if (sq.getNearbyMines() == 0) {
							sq.reveal(true);
						} else {
							sq.reveal(false);
						}
					}
				}
			}
		}
	}

	public void toggleMark() {
		if (!isRevealed()) {
			if (isMarked()) {
				this.state = SquareState.HIDDEN;
				this.button.setIcon(null);

				System.out.println("Unmarked square at x: " + this.posX + " y: " + this.posY);
			} else {
				this.state = SquareState.MARKED;
				this.button.setIcon(Frame.marker);

				System.out.println("Marked square at x: " + this.posX + " y: " + this.posY);
			}
		}
	}

	public void forceUnmark() {
		if (this.isMarked()) {
			this.state = SquareState.HIDDEN;
			this.button.setIcon(null);
		}
	}

	public void setNearbyMines(int mines) {
		this.nearbyMines = mines;
	}

	public int getPosX() {
		return this.posX;
	}

	public int getPosY() {
		return this.posY;
	}

	public SquareType getType() {
		return this.type;
	}

	public SquareState getState() {
		return this.state;
	}

	public Frame getFrame() {
		return this.frame;
	}

	public boolean isMine() {
		return this.type == SquareType.MINE;
	}

	public boolean isRevealed() {
		return this.state == SquareState.REVEALED;
	}

	public boolean isMarked() {
		return this.state == SquareState.MARKED;
	}

	public int getNearbyMines() {
		return this.nearbyMines;
	}

	public ArrayList<Square> getSurroundingSquares() {
		ArrayList<Square> squares = new ArrayList<Square>();
		for (int i = 0; i < 8; i++) {
			int xx = 0;
			int yy = 0;
			switch (i) {
			case 0:
				xx = this.posX - 1;
				yy = this.posY - 1;
				break;
			case 1:
				xx = this.posX;
				yy = this.posY - 1;
				break;
			case 2:
				xx = this.posX + 1;
				yy = this.posY - 1;
				break;
			case 3:
				xx = this.posX + 1;
				yy = this.posY;
				break;
			case 4:
				xx = this.posX + 1;
				yy = this.posY + 1;
				break;
			case 5:
				xx = this.posX;
				yy = this.posY + 1;
				break;
			case 6:
				xx = this.posX - 1;
				yy = this.posY + 1;
				break;
			case 7:
				xx = this.posX - 1;
				yy = this.posY;
				break;
			default:
				xx = this.posX;
				yy = this.posY;
			}
			Square sq = this.frame.getSquareAt(xx, yy);
			if (sq != null) {
				squares.add(sq);
			}
		}
		return squares;
	}

}
