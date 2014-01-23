package com.sk.cache.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.EnumSet;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ScalingGridPainter extends JPanel {

	private int cellSize = 12;
	private Point bottomCorner = new Point(3000, 3000);
	private static final Color DEFAULT_WALL_COLOR = new Color(150, 150, 150, 50);

	private AffineTransform transform = new AffineTransform();

	private int scaleAmount = 0;
	private BufferedImage transformImage;
	private Point dragStart = null;

	public ScalingGridPainter() {
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollAmount() != 0) {
					scaleAmount -= e.getWheelRotation();
					createTransform(e.getPoint());
					repaint();
					if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) == 0) {
						finishTransform(e.getPoint());
						repaint();
					}
					e.consume();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragStart != null) {
					createTransform(e.getPoint());
					repaint();
					e.consume();
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					createTransform(e.getPoint());
					e.consume();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (dragStart != null) {
					finishTransform(e.getPoint());
					e.consume();
				}
			}
		});
	}

	private void finishTransform(Point end) {
		if (dragStart == null)
			return;
		int xdif = end.x - dragStart.x;
		int ydif = end.y - dragStart.y;
		int newSize = changeScale(scaleAmount);
		if (newSize != cellSize) {
			Point cell = getCellAtPoint(dragStart);
			cell.translate(-bottomCorner.x, -bottomCorner.y);
			bottomCorner.translate(cell.x - cell.x * cellSize / newSize, cell.y - cell.y * cellSize / newSize);
		}
		bottomCorner.translate(-xdif / cellSize, ydif / cellSize);

		cellSize = newSize;
		scaleAmount = 0;
		dragStart = null;
		transform.setToIdentity();
		repaint();
	}

	private void createTransform(Point current) {
		initializeDrag(current);
		transform.setToIdentity();
		transform.translate(current.x, current.y);
		if (scaleAmount != 0) {
			double newSize = changeScale(scaleAmount);
			transform.scale(newSize / cellSize, newSize / cellSize);
		}
		transform.translate(-dragStart.x, -dragStart.y);
	}

	private void initializeDrag(Point start) {
		if (dragStart == null)
			dragStart = start;
	}

	private int changeScale(int dif) {
		int cellSize = this.cellSize + dif * 2;
		if (cellSize < 3)
			cellSize = 3;
		if (cellSize > 31)
			cellSize = 31;
		return cellSize;
	}

	public void paintGrid(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		Rectangle bounds = g1.getClipBounds();
		if (bounds == null)
			bounds = new Rectangle(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.fill(bounds);
		Dimension cells = getCellCount(bounds.getSize());
		for (Point cell = new Point(bottomCorner); cell.y < cells.height + bottomCorner.y; ++cell.y) {
			for (cell.x = bottomCorner.x; cell.x < cells.width + bottomCorner.x; ++cell.x) {
				Point curPoint = getTopLeftCellPoint(cell, bounds);
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
	}

	private void resizeTransformImage() {
		if (transformImage == null || transformImage.getWidth() != getWidth()
				|| transformImage.getHeight() != getHeight()) {
			transformImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
	}

	private void paintToTransformImage() {
		resizeTransformImage();
		Graphics2D g = transformImage.createGraphics();
		paintGrid(g);
		g.dispose();
	}

	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		if (transformImage == null || dragStart == null) {
			resizeTransformImage();
			paintToTransformImage();
		}
		Graphics2D g = (Graphics2D) g1;
		if (transform != null)
			g.setTransform(transform);
		g.drawImage(transformImage, 0, 0, null);
	}

	public Dimension getCellCount() {
		return getCellCount(getSize());
	}

	public Dimension getCellCount(Dimension size) {
		int width = size.width;
		int height = size.height;
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

	public Point getTopLeftCellPoint(Point cell, Rectangle bounds) {
		Point ret = new Point(bounds.getLocation());
		ret.translate((cell.x - bottomCorner.x) * cellSize, bounds.height - (cell.y - bottomCorner.y + 1)
				* cellSize);
		return ret;
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
