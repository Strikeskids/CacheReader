package com.sk.cache.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.sk.cache.dist.pack.SanitizedRegion;
import com.sk.cache.dist.pack.SanitizedRegion.StairPair;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.gui.GridPainter.GridGetter;
import com.sk.cache.gui.GridPainter.Side;
import com.sk.cache.wrappers.ObjectDefinition;
import com.sk.cache.wrappers.region.LocalObject;
import com.sk.cache.wrappers.region.LocalObjects;
import com.sk.cache.wrappers.region.Region;

public class SanitizedRegionViewer {

	private static SanitizedRegion region;
	private static Region source;
	private static LocalObjects objects;
	private static int plane;
	private static boolean shouldShowObjects = false;
	private static int x, y;
	private static CacheSystem sys;

	private static void reloadRegion() {
		if (source == null || source.getId() != (x | y << 7) && sys.regionLoader.canLoad(x | y << 7)) {
			System.out.println(x + " " + y);
			source = sys.regionLoader.load(x, y);
			objects = source == null ? null : source.objects;
			region = source == null ? null : new SanitizedRegion(source);
		}
	}

	public static void main(String[] args) throws IOException {
		sys = new CacheSystem(new File("/Users/Strikeskids/jagexcache/Runescape/LIVE"));
		final JFrame frame = new JFrame("Regions");
		frame.getContentPane().setLayout(new BorderLayout());
		JPanel top = new JPanel();
		final JTextField xval = new JTextField(5);
		final JTextField yval = new JTextField(5);
		final JLabel landscape = new JLabel();
		final JLabel coords = new JLabel();
		final DefaultListModel<ObjectDefinition> objectModel = new DefaultListModel<>();
		JButton update = new JButton("Update");
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						x = Integer.parseInt(xval.getText());
						y = Integer.parseInt(yval.getText());
						reloadRegion();
						frame.repaint();
					}
				});
			}
		});
		top.add(xval);
		top.add(yval);
		top.add(update);
		top.add(landscape);
		top.add(coords);
		KeyListener k = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					if (plane < 3)
						plane++;
					break;
				case KeyEvent.VK_DOWN:
					if (plane > 0)
						plane--;
					break;
				case KeyEvent.VK_W:
					y++;
					break;
				case KeyEvent.VK_A:
					x--;
					break;
				case KeyEvent.VK_D:
					x++;
					break;
				case KeyEvent.VK_S:
					y--;
					break;
				default:
					return;
				}
				reloadRegion();
				frame.repaint();
			}
		};
		xval.addKeyListener(k);
		yval.addKeyListener(k);
		update.addKeyListener(k);
		frame.addKeyListener(k);
		top.addKeyListener(k);
		frame.getContentPane().add(top, BorderLayout.NORTH);
		frame.getContentPane().add(new GridPainter(new GridGetter() {
			@Override
			public Color getColor(int x, int y, Side side) {
				int flag;
				if (region != null) {
					flag = region.getFlag(x, y, plane);
				} else {
					return null;
				}
				if (side == Side.CENTER) {
					for (StairPair sp : region.stairs) {
						if (plane == sp.up.plane)
							for (Point p : sp.upList) {
								if (p.x == x && p.y == y)
									return Color.yellow;
							}
						if (plane == sp.down.plane)
							for (Point p : sp.downList)
								if (p.x == x && p.y == y)
									return Color.yellow;
					}
				}
				if ((flag & 0xff) == 0xff) {
					return side == Side.CENTER ? Color.red : null;
				}
				if (side == Side.NORTH && (flag & 0x2) != 0)
					return Color.green;
				if (side == Side.EAST && (flag & 0x8) != 0)
					return Color.blue;
				if (side == Side.SOUTH && (flag & 0x20) != 0)
					return Color.yellow;
				if (side == Side.WEST && (flag & 0x80) != 0)
					return Color.orange;
				return null;
			}

			@Override
			public void hoverCell(int x, int y) {

			}

			@Override
			public void clickCell(int x, int y) {
				for (LocalObject o : objects.getObjectsAt(x, y, plane)) {
					System.out.println(o + " " + o.getDefinition());
				}
				System.out.print(Integer.toHexString(source.landscapeData[plane][x][y]));
				for (int i = 0; i < 5; ++i) {
					System.out.print(" ");
					System.out.print(Integer.toHexString(source.extraData[plane][x][y][i]));
				}
				System.out.println();
			}
		}, Region.width, Region.height), BorderLayout.CENTER);
		if (shouldShowObjects)
			frame.getContentPane().add(new JScrollPane(new JList<>(objectModel)), BorderLayout.EAST);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
