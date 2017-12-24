package de.innoberger.squares;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.innoberger.squares.input.Mouse;
import de.innoberger.squares.square.Square;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final String TITLE = "Squares";
	public static final String VERSION = "1.0";
	public static final int xOffset = 50;
	public static final int yOffset = 50;
	public static final int xSquares = 25;
	public static final int ySquares = 15;
	public static final int WIDTH = 1172;
	public static final int HEIGHT = 792;
	public static ArrayList<Square> field;
	public static ArrayList<Square> safeSquares;
	public static int revealed;
	private BufferedImage image;
	private Mouse mouse;
	public boolean freeze;
	private Main main;

	public Frame(Main main) {
		super("Squares 1.0");

		this.main = main;
		this.freeze = false;
		revealed = 0;

		setPreferredSize(new Dimension(1172, 792));
		setDefaultCloseOperation(3);
		setResizable(false);

		this.mouse = setupMouse();

		addMouseListener(this.mouse);

		this.image = setupImage();
		draw();
	}

	public void draw() {
		Graphics gr = this.image.createGraphics();

		gr.setColor(Color.BLACK);
		gr.fillRect(0, 0, 1172, 792);

		this.freeze = false;
		revealed = 0;

		drawGrid(gr);

		gr.dispose();

		attachChanges();
	}

	public void refreshGrid() {
		Graphics gr = this.image.createGraphics();
		for (int i = 0; i < field.size(); i++) {
			((Square) field.get(i)).draw(gr);
		}
		gr.dispose();

		attachChanges();
	}

	private void drawGrid(Graphics gr) {
		field = new ArrayList<Square>();
		safeSquares = new ArrayList<Square>();
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 25; x++) {
				Square sq = new Square(x, y, this);
				if (!sq.isMine()) {
					safeSquares.add(sq);
				}
				field.add(sq);

				sq.draw(gr);
			}
		}
		countNearbyMines();
	}

	public void drawVictory() {
		String text = "VICTORY: You isolated every single mine! Click to restart.";

		System.out.println("Victory!");

		Graphics gr = this.image.createGraphics();
		int textSize = 27;

		gr.setColor(Color.GREEN);
		gr.setFont(new Font("Courier New", 1, textSize));

		FontMetrics fm = gr.getFontMetrics();
		int txtWidth = fm.stringWidth(text);

		gr.drawString(text, (1172 - txtWidth) / 2, 695 + 3 * textSize / 2);
		gr.dispose();

		attachChanges();
	}

	public void drawGameOver() {
		String text = "GAME OVER: You stabbed on a mine! Click to restart.";

		System.out.println("Game Over!");

		Graphics gr = this.image.createGraphics();
		int textSize = 27;

		gr.setColor(Color.RED);
		gr.setFont(new Font("Courier New", 1, textSize));

		FontMetrics fm = gr.getFontMetrics();
		int txtWidth = fm.stringWidth(text);

		gr.drawString(text, (1172 - txtWidth) / 2, 695 + 3 * textSize / 2);
		gr.dispose();

		attachChanges();
	}

	public void revealAll(boolean victory) {
		revealSelection(field, true);
		if (victory) {
			drawVictory();
		} else {
			drawGameOver();
		}
	}

	public void revealSelection(ArrayList<Square> sel, boolean forceUnmark) {
		for (int i = 0; i < sel.size(); i++) {
			Square sq = (Square) sel.get(i);
			if (!sq.isRevealed()) {
				if (forceUnmark) {
					sq.forceUnmark();
				}
				sq.reveal(false);
			}
		}
	}

	public void revealSafe() {
		revealSelection(safeSquares, true);
	}

	public void attachChanges() {
		getContentPane().add(new JLabel(new ImageIcon(this.image)));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void countNearbyMines() {
		for (int i = 0; i < field.size(); i++) {
			Square sq = (Square) field.get(i);
			int mines = 0;
			if (!sq.isMine()) {
				ArrayList<Square> surround = sq.getSurroundingSquares();
				for (int j = 0; j < surround.size(); j++) {
					Square s = (Square) surround.get(j);
					if (s.isMine()) {
						mines++;
					}
				}
				sq.setNearbyMines(mines);
			}
		}
		System.out.println("Amount of mines: " + getMineAmount());
	}

	private BufferedImage setupImage() {
		return new BufferedImage(1172, 792, 1);
	}

	private Mouse setupMouse() {
		return new Mouse(this.main);
	}

	public BufferedImage getImage() {
		return this.image;
	}

	public Square getSquareAt(int xPos, int yPos) {
		Square square = null;
		for (int i = 0; i < field.size(); i++) {
			Square sq = (Square) field.get(i);
			if ((sq.getPosX() == xPos) && (sq.getPosY() == yPos)) {
				square = sq;
				break;
			}
		}
		return square;
	}

	public static int getMineAmount() {
		int mines = 0;
		for (int i = 0; i < field.size(); i++) {
			if (((Square) field.get(i)).isMine()) {
				mines++;
			}
		}
		return mines;
	}
}
