package com.sk.cache.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapPainter extends JPanel {

	private Point offset = new Point(2217, 1713);
	private Point start;
	private Point mouse = new Point(-1, -1);

	private static final int bx = 2047, by = 4159;
	public static final int mapScale = 2, windowScale = 4;

	private MapClickListener mcl;

	private static BufferedImage map;
	static {
		try {
			map = ImageIO.read(new File("/Users/Strikeskids/Runescape/rsmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MapPainter(final MapClickListener mcl) {
		this.mcl = mcl;
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 && start != null) {
					Point cur = e.getPoint();
					offset.translate(start.x - cur.x, start.y - cur.y);
					repaint();
					start = cur;
				}
				mouse = e.getPoint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mouse = e.getPoint();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				start = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				start = null;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mouse = e.getPoint();
				Point coords = getCoords();
				mcl.mapClicked(coords.x, coords.y);
				repaint();
			}
		});
		setPreferredSize(new Dimension(windowScale * 128, windowScale * 128));
	}

	public Point getCoords() {
		return getCoords(mouse);
	}

	public Point getCoords(Point onPanel) {
		int dx = offset.x / mapScale + onPanel.x / windowScale;
		int dy = offset.y / mapScale + onPanel.y / windowScale;
		return new Point(bx + dx, by - dy);
	}

	@Override
	public void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		int mw = w * mapScale / windowScale;
		int mh = h * mapScale / windowScale;
		g.drawImage(map, 0, 0, w, h, offset.x, offset.y, offset.x + mw, offset.y + mh, null);
		Graphics2D overlay = (Graphics2D) g.create();
		Point coords = getCoords(new Point(0, 0));
		mcl.addOverlay(overlay, coords.x, coords.y);
	}

	public static interface MapClickListener {
		public void mapClicked(int mx, int my);

		public void addOverlay(Graphics g, int mx, int my);
	}
}
