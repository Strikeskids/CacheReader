package com.sk.cache.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.sk.cache.dist.unpack.PackedRegion;
import com.sk.cache.dist.unpack.ProtocolUnpacker;
import com.sk.cache.dist.unpack.Unpacker;
import com.sk.cache.gui.MapPainter.MapClickListener;
import com.sk.path.PathFinder;
import com.sk.path.PathNode;
import com.sk.path.RegionSource;

public class PathViewer {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		final Unpacker<PackedRegion> upckr = new ProtocolUnpacker<>(PackedRegion.class, new RandomAccessFile(
				"packed/PackedRegion.packed", "r"));

		final PathFinder pf = new PathFinder(new RegionSource() {
			@Override
			public PackedRegion getRegion(int rx, int ry) {
				return upckr.unpack(rx | ry << 7);
			}
		});
		JFrame frame = new JFrame("Path Viewer");
		MapPainter mp = new MapPainter(new MapClickListener() {

			private PathNode start = null;
			private List<PathNode> nodes = new ArrayList<>();

			@Override
			public void mapClicked(int mx, int my) {
				PathNode cur = new PathNode(mx, my, 0);
				if (start == null) {
					start = cur;
				} else {
					System.out.println("path " + start + " " + cur);
					nodes = pf.findPath(start, cur);
					start = null;
				}
			}

			@Override
			public void addOverlay(Graphics g, int mx, int my) {
				Point coords = new Point(mx, my);
				if (start != null) {
					g.setColor(Color.red);
					drawNode(g, coords, start);
				}
				if (nodes != null) {
					g.setColor(Color.green);
					for (PathNode node : nodes) {
						drawNode(g, coords, node);
					}
				}
			}

			private void drawNode(Graphics g, Point coords, PathNode node) {
				int dx = (node.x - coords.x) * MapPainter.windowScale;
				int dy = (node.y - coords.y) * MapPainter.windowScale;
				g.drawRect(dx, -dy, MapPainter.windowScale, MapPainter.windowScale);
			}
		});
		frame.setContentPane(mp);
		frame.pack();
		frame.setVisible(true);
	}
}
