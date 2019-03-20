package de.innoberger.squares;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.innoberger.squares.input.GlobalListener;
import de.innoberger.squares.square.Square;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final String TITLE = "Squares";
	public static final String VERSION = "2.0";

	public static final int OFFSET_BETWEEN = 4;
	public static final int BOTTOM_OFFSET = 50;
	public static final int X_SQUARES = 25;
	public static final int Y_SQUARES = 15;

	public static final String FONT_FAMILY = "Courier New";

	public static ArrayList<Square> field;
	public static ArrayList<Square> safeSquares;

	public static int revealed;
	public boolean freeze;

	private Main main;

	public static ImageIcon marker;
	public static ImageIcon mine;

	private JPanel fieldPanel;
	private JPanel bottomPanel;

	public static final int WIDTH = (int) (OFFSET_BETWEEN * 2.5) + (Square.SIZE + OFFSET_BETWEEN) * X_SQUARES;
	public static final int HEIGHT = BOTTOM_OFFSET * 2 + OFFSET_BETWEEN * 2
			+ (Square.SIZE + OFFSET_BETWEEN) * Y_SQUARES;

	public Frame(Main main) {
		super(TITLE + " " + VERSION);

		this.main = main;
		this.freeze = false;
		revealed = 0;

		System.out.println("Window size: " + WIDTH + " x " + HEIGHT);

		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBackground(Color.BLACK);
		addMouseListener(new GlobalListener(this));

		try {
			this.readRessources();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.draw();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void draw() {
		this.freeze = false;
		revealed = 0;

		this.drawGrid();
		this.setupPanels();

		this.bottomPanel.repaint();

		pack();
	}

	public void refreshGrid() {
		getContentPane().removeAll();

		this.draw();
	}

	private void drawGrid() {
		field = new ArrayList<Square>();
		safeSquares = new ArrayList<Square>();
		for (int y = 0; y < Frame.Y_SQUARES; y++) {
			for (int x = 0; x < Frame.X_SQUARES; x++) {
				Square sq = new Square(x, y, this);

				if (!sq.isMine()) {
					safeSquares.add(sq);
				}

				field.add(sq);
				sq.draw();
			}
		}

		this.countNearbyMines();
	}

	public void drawVictory() {
		System.out.println("Victory!");

		String text = "VICTORY: You isolated every single mine! Click to restart.";
		JLabel label = new JLabel(text);

		label.setFont(this.bottomPanel.getFont());
		label.setForeground(Color.GREEN);

		this.bottomPanel.add(label);
	}

	public void drawGameOver() {
		System.out.println("Game Over!");

		String text = "GAME OVER: You stepped on a mine! Click to restart.";
		JLabel label = new JLabel(text);

		label.setFont(this.bottomPanel.getFont());
		label.setForeground(Color.RED);

		this.bottomPanel.add(label);
	}

	public void revealAll(boolean victory) {
		this.revealSelection(field, true);

		if (victory) {
			this.drawVictory();
		} else {
			this.drawGameOver();
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
		this.revealSelection(safeSquares, true);
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

	private void setupPanels() {
		this.fieldPanel = new JPanel();
		this.fieldPanel.removeAll();
		this.fieldPanel.setBackground(getBackground());
		this.fieldPanel.setBounds(0, 0, WIDTH, (int) (HEIGHT - BOTTOM_OFFSET * 1.8));
		this.fieldPanel.setBorder(null);
		this.fieldPanel.setFont(new Font(FONT_FAMILY, Font.BOLD, 30));

		this.add(this.fieldPanel);

		this.bottomPanel = new JPanel();
		this.bottomPanel.removeAll();
		this.bottomPanel.setBackground(getBackground());
		this.bottomPanel.setBounds(0, (int) (HEIGHT - BOTTOM_OFFSET * 1.8), WIDTH, (int) (BOTTOM_OFFSET * 1.8));
		this.bottomPanel.setBorder(null);
		this.bottomPanel.setFont(new Font(FONT_FAMILY, Font.BOLD, 30));

		this.add(this.bottomPanel);
	}

	public Main getMain() {
		return this.main;
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

	private void readRessources() throws IOException {
		Frame.marker = new ImageIcon(Main.class.getResource("/res/marker.png"));
		Frame.mine = new ImageIcon(Main.class.getResource("/res/mine.png"));

		Image img1 = Frame.marker.getImage();
		Image newImg1 = img1.getScaledInstance(Square.SIZE, Square.SIZE, java.awt.Image.SCALE_DEFAULT);
		Frame.marker = new ImageIcon(newImg1);

		Image img2 = Frame.mine.getImage();
		Image newImg2 = img2.getScaledInstance(Square.SIZE, Square.SIZE, java.awt.Image.SCALE_DEFAULT);
		Frame.mine = new ImageIcon(newImg2);
	}

}
