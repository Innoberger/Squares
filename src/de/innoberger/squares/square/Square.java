package de.innoberger.squares.square;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import de.innoberger.squares.Frame;

public class Square {
	public static final int SIZE = 40;
	public static final int OFFSET_BETWEEN = 3;
	public static final int MINE_PERCENTAGE = 12;
	private int posX;
	private int posY;
	private int centerX;
	private int centerY;
	private int nearbyMines;
	private SquareType type;
	private SquareState state;
	private Frame frame;

	public Square(int posX, int posY, Frame frame) {
		Random random = new Random();

		this.frame = frame;

		this.posX = posX;
		this.posY = posY;

		this.nearbyMines = 0;

		this.centerX = (50 + this.posX * 43 + 20);
		this.centerY = (50 + this.posY * 43 + 20);

		SquareType st = SquareType.DEFAULT;
		if (random.nextInt(101) <= 12) {
			st = SquareType.MINE;
		}
		this.type = st;
		this.state = SquareState.HIDDEN;
	}

	public void draw(Graphics gr) {
		boolean drawNearbyMines = false;
		if (!isRevealed()) {
			gr.setColor(Color.CYAN);
			if (isMarked()) {
				gr.setColor(Color.ORANGE);
			}
		} else if (isMine()) {
			gr.setColor(Color.RED);
		} else {
			gr.setColor(Color.WHITE);
			drawNearbyMines = true;
		}
		gr.fillRoundRect(this.centerX - 20, this.centerY - 20, 40, 40, 13, 13);
		if ((drawNearbyMines) && (this.nearbyMines > 0)) {
			drawNearbyMines(gr);
		}
	}

	private void drawNearbyMines(Graphics gr) {
		int textSize = 26;
		if (this.nearbyMines == 1) {
			gr.setColor(Color.BLUE);
		} else if (this.nearbyMines == 2) {
			gr.setColor(Color.GREEN);
		} else if (this.nearbyMines == 3) {
			gr.setColor(Color.ORANGE);
		} else {
			gr.setColor(Color.RED);
		}
		gr.setFont(new Font("Courier New", 1, textSize));

		FontMetrics fm = gr.getFontMetrics();
		int txtWidth = fm.stringWidth(String.valueOf(this.nearbyMines));

		gr.drawString(String.valueOf(this.nearbyMines), this.centerX - txtWidth / 2, this.centerY + textSize / 3);
	}

	public void reveal(boolean multiple) {
		if ((!isRevealed()) && (!isMarked())) {
			this.state = SquareState.REVEALED;

			Frame.revealed += 1;
			if ((Frame.revealed == 375 - Frame.getMineAmount()) && (!this.frame.freeze)) {
				this.frame.freeze = true;
				this.frame.revealAll(true);
			} else if ((isMine()) && (!this.frame.freeze)) {
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

				System.out.println("Unmarked square at x: " + this.posX + " y: " + this.posY);
			} else {
				this.state = SquareState.MARKED;

				System.out.println("Marked square at x: " + this.posX + " y: " + this.posY);
			}
		}
	}

	public void forceUnmark() {
		if (isMarked()) {
			this.state = SquareState.HIDDEN;
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

	public int getCenterX() {
		return this.centerX;
	}

	public int getCenterY() {
		return this.centerY;
	}

	public SquareType getType() {
		return this.type;
	}

	public SquareState getState() {
		return this.state;
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
