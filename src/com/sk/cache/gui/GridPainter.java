package com.sk.cache.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GridPainter extends JPanel {

	private Color background = Color.black;
	private Color gridLines = new Color(150, 150, 150, 100);

	private final int rows, cols;
	private final GridGetter getter;

	private final Font gridFont = new Font("Courier New", 0, 8);

	private final int OUTSIDE_PAD = 5;
	private final int CELL_SIZE = 7;
	private final int CELL_PAD = 3;

	private Point curCell = new Point(-1, -1);

	public GridPainter(final GridGetter getter, final int width, final int height) {
		this.rows = height;
		this.cols = width;
		Dimension size = new Dimension((width + 1) * CELL_SIZE + (width) * CELL_PAD + 2 * OUTSIDE_PAD,
				(height + 1) * CELL_SIZE + (height - 1) * CELL_PAD + 2 * OUTSIDE_PAD);
		this.setPreferredSize(size);
		this.getter = getter;
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int xloc = (e.getX() - OUTSIDE_PAD - CELL_SIZE - CELL_PAD) / (CELL_PAD + CELL_SIZE);
				int yloc = rows - (e.getY() - OUTSIDE_PAD) / (CELL_PAD + CELL_SIZE);
				if (xloc < 0 || yloc < 0 || xloc >= width || yloc >= height)
					return;
				if (curCell.y != yloc || curCell.x != xloc) {
					getter.hoverCell(xloc, yloc);
					curCell.x = xloc;
					curCell.y = yloc;
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int x = 0; x < cols; x += 5) {
			g.setColor(gridLines);
			g.setFont(gridFont);
			g.drawString(String.format("%2d", x), OUTSIDE_PAD + CELL_SIZE + x * (CELL_PAD + CELL_SIZE),
					OUTSIDE_PAD + CELL_SIZE);
		}
		for (int y = 0; y < rows; ++y) {
			int sy = OUTSIDE_PAD + (rows - y) * (CELL_PAD + CELL_SIZE);
			g.setColor(gridLines);
			g.setFont(gridFont);
			g.drawString(String.format("%2d", y), OUTSIDE_PAD / 2, sy + CELL_SIZE);
			for (int x = 0; x < cols; x++) {
				int sx = OUTSIDE_PAD + CELL_SIZE + CELL_PAD + x * (CELL_PAD + CELL_SIZE);
				g.setColor(gridLines);
				g.drawRect(sx - 1, sy - 1, CELL_SIZE + 2, CELL_SIZE + 2);
				for (Side s : Side.values()) {
					Color c = getter.getColor(x, y, s);
					if (c != null) {
						g.setColor(c);
						s.draw(g, sx, sy, CELL_SIZE);
					}
				}
			}
		}
	}

	public static interface GridGetter {
		public Color getColor(int x, int y, Side side);

		public void hoverCell(int x, int y);
	}

	public enum Side {
		NORTH_WEST {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.drawLine(x + 1, y + size / 2 - 1, x + size / 2 - 1, y + 1);
			}
		},
		NORTH {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.drawLine(x, y, x + size, y);
			}
		},
		NORTH_EAST {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.drawLine(x + size / 2 + 1, y, x + size - 1, y + size / 2 - 1);
			}
		},
		EAST {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.drawLine(x + size, y, x + size, y + size);
			}
		},
		SOUTH_EAST {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.drawLine(x + size - 1, y + size / 2 + 1, x + size / 2 + 1, y + size - 1);
			}
		},
		SOUTH {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.drawLine(x + size, y + size, x, y + size);
			}
		},
		SOUTH_WEST {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.drawLine(x + size / 2 - 1, y + size - 1, x + 1, y + size / 2 + 1);
			}
		},
		WEST {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.drawLine(x, y + size, x, y);
			}
		},
		CENTER {
			@Override
			public void draw(Graphics g, int x, int y, int size) {
				g.fillOval(x + 2, y + 2, size - 4, size - 4);
			}
		};

		public abstract void draw(Graphics g, int x, int y, int size);
	}

}
