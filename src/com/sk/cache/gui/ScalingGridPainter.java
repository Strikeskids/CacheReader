package com.sk.cache.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EnumSet;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ScalingGridPainter extends JPanel {

	private int cellSize = 12;
	private Point bottomCorner = new Point(3000, 3000);
	private static final Color DEFAULT_WALL_COLOR = new Color(150, 150, 150, 50);

	private Point dragStart = null, dragCenter = null;

	public ScalingGridPainter() {
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollAmount() != 0) {
					if (zoom(e.getPoint(), e.getWheelRotation()))
						repaint();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragStart != null) {
					Point next = e.getPoint();
					int xdif = next.x - dragStart.x;
					int ydif = next.y - dragStart.y;
					bottomCorner.move(dragCenter.x - xdif / cellSize, dragCenter.y + ydif / cellSize);
					repaint();
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					dragStart = e.getPoint();
					dragCenter = new Point(bottomCorner);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dragStart = null;
				dragCenter = null;
			}
		});
	}

	private boolean zoom(Point around, int amount) {
		if (amount == 0)
			return false;
		int offX = around.x;
		int offY = getHeight() - around.y;
		Point cell = getCellAtPoint(around);
		if (!changeScale(-amount))
			return false;
		bottomCorner.move(cell.x, cell.y);
		int cellX = offX / cellSize, cellY = offY / cellSize;
		bottomCorner.translate(-cellX, -cellY);
		return true;
	}

	private boolean changeScale(int dif) {
		int start = cellSize;
		cellSize += dif * 2;
		if (cellSize < 3)
			cellSize = 3;
		if (cellSize > 31)
			cellSize = 31;
		return start != cellSize;
	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		Dimension cells = getCellCount();
		long start = System.currentTimeMillis();
		for (Point cell = new Point(bottomCorner); cell.y < cells.height + bottomCorner.y; ++cell.y) {
			for (cell.x = bottomCorner.x; cell.x < cells.width + bottomCorner.x; ++cell.x) {
				Point curPoint = getTopLeftCellPoint(cell);
				for (CellPart part : CellPart.values()) {
					Color cur = getColor(part, cell.x, cell.y);
					if (cur == null) {
						if (part.isSide()) {
							cur = DEFAULT_WALL_COLOR;
						} else {
							continue;
						}
					}
					g.setColor(cur);
					part.draw(g, curPoint.x, curPoint.y, cellSize - 1);
				}
			}
		}
		System.out.println("Paint time " + (System.currentTimeMillis() - start));
	}

	public Dimension getCellCount() {
		int width = getWidth();
		int height = getHeight();
		if (width % cellSize != 0)
			width += cellSize;
		if (height % cellSize != 0)
			height += cellSize;
		return new Dimension(width / cellSize, height / cellSize);
	}

	public Point getCellAtPoint(Point panel) {
		int ofx = panel.x;
		int ofy = this.getHeight() - panel.y;
		return new Point(bottomCorner.x + ofx / cellSize, bottomCorner.y + ofy / cellSize);
	}

	public Point getTopLeftCellPoint(Point cell) {
		return new Point((cell.x - bottomCorner.x) * cellSize, getHeight() - (cell.y - bottomCorner.y + 1)
				* cellSize);
	}

	public abstract Color getColor(CellPart part, int x, int y);

	public enum CellPart {
		CENTER {
			@Override
			public void draw(Graphics g, int cellX, int cellY, int cellSize) {
				g.fillRect(cellX, cellY, cellSize, cellSize);
			}
		},
		NORTH {
			@Override
			public void draw(Graphics g, int x, int y, int s) {
				g.drawLine(x, y, x + s, y);
			}
		},
		SOUTH {
			@Override
			public void draw(Graphics g, int x, int y, int s) {
				g.drawLine(x, y + s, x + s, y + s);
			}
		},
		EAST {
			@Override
			public void draw(Graphics g, int x, int y, int s) {
				g.drawLine(x + s, y, x + s, y + s);
			}
		},
		WEST {
			@Override
			public void draw(Graphics g, int x, int y, int s) {
				g.drawLine(x, y, x, y + s);
			}
		},
		NORTH_EAST {
			@Override
			public void draw(Graphics g, int x, int y, int s) {
				int cornerSize = getCornerSize(s);
				for (int i = 0; i < cornerSize; ++i) {
					g.drawLine(x + s - i, y, x + s - i, y + cornerSize - i);
				}
			}
		},
		SOUTH_EAST {
			@Override
			public void draw(Graphics g, int x, int y, int s) {
				int cornerSize = getCornerSize(s);
				for (int i = 0; i < cornerSize; ++i) {
					g.drawLine(x + s - i, y + s, x + s - i, y + s - cornerSize + i);
				}
			}
		},
		NORTH_WEST {
			@Override
			public void draw(Graphics g, int x, int y, int s) {
				int cornerSize = getCornerSize(s);
				for (int i = 0; i < cornerSize; ++i) {
					g.drawLine(x + i, y, x + i, y + cornerSize - i);
				}
			}
		},
		SOUTH_WEST {
			@Override
			public void draw(Graphics g, int x, int y, int s) {
				int cornerSize = getCornerSize(s);
				for (int i = 0; i < cornerSize; ++i) {
					g.drawLine(x + i, y + s, x + i, y + s - cornerSize + i);
				}
			}
		};

		public abstract void draw(Graphics g, int x, int y, int s);

		protected int getCornerSize(int s) {
			return (s + 1) / 4;
		}

		private static final EnumSet<CellPart> sides = EnumSet.of(CellPart.NORTH, CellPart.EAST, CellPart.SOUTH,
				CellPart.WEST);

		public boolean isSide() {
			return sides.contains(this);
		}
	}

}
