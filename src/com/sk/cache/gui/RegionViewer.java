package com.sk.cache.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

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

import com.sk.cache.DataSource;
import com.sk.cache.dist.unpack.PackedRegion;
import com.sk.cache.dist.unpack.ProtocolUnpacker;
import com.sk.cache.dist.unpack.Unpacker;
import com.sk.cache.fs.CacheSource;
import com.sk.cache.gui.GridPainter.GridGetter;
import com.sk.cache.gui.GridPainter.Side;
import com.sk.cache.wrappers.ObjectDefinition;
import com.sk.cache.wrappers.loaders.RegionLoader;
import com.sk.cache.wrappers.region.LocalObject;
import com.sk.cache.wrappers.region.Region;

public class RegionViewer {

	private static Region region;
	private static PackedRegion packedRegion;
	private static int plane;
	private static boolean shouldShowObjects = false;
	private static boolean unpack = true;

	public static void main(String[] args) throws IOException {
		CacheSource sys = new CacheSource(new DataSource(new File("/Users/Strikeskids/jagexcache/Runescape/LIVE")));
		final Unpacker<PackedRegion> unpckr = new ProtocolUnpacker<>(PackedRegion.class, new RandomAccessFile(
				"packed/PackedRegion.packed", "r"));
		final RegionLoader rl = new RegionLoader(sys);
		final JFrame frame = new JFrame("Regions");
		frame.getContentPane().setLayout(new BorderLayout());
		JPanel top = new JPanel();
		final JTextField xval = new JTextField(5);
		final JTextField yval = new JTextField(5);
		final JTextField pval = new JTextField(5);
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
						int x = Integer.parseInt(xval.getText());
						int y = Integer.parseInt(yval.getText());
						System.out.println(x | y << 7);
						if (unpack) {
							packedRegion = unpckr.unpack(x | y << 7);
						} else {
							region = rl.load(x, y);
						}
						plane = Integer.parseInt(pval.getText());
						frame.repaint();
					}
				});
			}
		});
		top.add(xval);
		top.add(yval);
		top.add(pval);
		top.add(update);
		top.add(landscape);
		top.add(coords);
		frame.getContentPane().add(top, BorderLayout.NORTH);
		frame.getContentPane().add(new GridPainter(new GridGetter() {
			@Override
			public Color getColor(int x, int y, Side side) {
				int flag;
				if (region != null) {
					flag = region.flags[plane][x][y];
				} else if (packedRegion != null) {
					flag = packedRegion.getFlag(x, y, plane);
				} else {
					return null;
				}
				if ((flag & 0xff) == 0xff) {
					return side == Side.CENTER ? Color.red : null;
				}
				if ((flag & 0x200100) != 0 && side == Side.CENTER)
					return Color.red;
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
				if (region == null)
					return;
				landscape.setText(Integer.toHexString(region.landscapeData[plane][x][y]));
				objectModel.clear();
				coords.setText(x + " " + y);
				for (LocalObject o : region.objects.getObjects()) {
					if (o.x == x && o.y == y && o.plane == plane) {
						objectModel.addElement(region.getLoader().objectDefinitionLoader.load(o.id));
					}
				}
			}
		}, Region.width, Region.height), BorderLayout.CENTER);
		if (shouldShowObjects)
			frame.getContentPane().add(new JScrollPane(new JList<>(objectModel)), BorderLayout.EAST);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
