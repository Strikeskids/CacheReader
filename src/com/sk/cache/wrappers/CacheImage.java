package com.sk.cache.wrappers;

import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;

import com.sk.cache.wrappers.loaders.ImageLoader;
import com.sk.datastream.Stream;

public class CacheImage extends StreamedWrapper {

	public int imageCount, paletteLength, biggestWidth, biggestHeight;
	public ImageData[] images;
	public int[] palette;

	public CacheImage(ImageLoader loader, int id) {
		super(loader, id);
	}

	@Override
	public void decode(Stream stream) {
		stream.seek(stream.getLength() - 2);
		imageCount = stream.getUShort();
		if (imageCount != 0xFFFF) {
			decodedIndexedImage(stream);
		} else {
			decodeBasicImage(stream);
		}
	}

	private void decodedIndexedImage(Stream s) {
		readMetaData(s);
		readPalette(s);
		s.seek(0);
		for (int i = 0; i < imageCount; ++i) {
			readIndividualImage(s, images[i]);
		}
	}

	private void readMetaData(Stream s) {
		s.seek(s.getLength() - 7 - imageCount * 8);
		biggestWidth = s.getUShort();
		biggestHeight = s.getUShort();
		paletteLength = s.getUByte();

		images = new ImageData[imageCount];
		for (int i = 0; i < imageCount; ++i) {
			images[i] = new ImageData();
		}

		for (int i = 0; i < imageCount; ++i)
			images[i].minX = s.getUShort();
		for (int i = 0; i < imageCount; ++i)
			images[i].minY = s.getUShort();
		for (int i = 0; i < imageCount; ++i)
			images[i].width = s.getUShort();
		for (int i = 0; i < imageCount; ++i)
			images[i].height = s.getUShort();
	}

	private void readPalette(Stream s) {
		palette = new int[paletteLength + 1];
		s.seek(s.getLength() - 7 - imageCount * 8 - paletteLength * 3);
		for (int i = 1; i <= paletteLength; ++i) {
			palette[i] = s.getUInt24();
			if (palette[i] == 0)
				palette[i] = 1;
		}
	}

	private void readIndividualImage(Stream s, ImageData cur) {
		int size = cur.width * cur.height;
		cur.image = new byte[size];
		cur.palette = palette;
		int formatMask = s.getUByte();
		readArray(s, cur.image = new byte[size], formatMask, cur.width, cur.height);
		if ((formatMask & 2) != 0) {
			cur.hasAlpha = true;
			readArray(s, cur.alpha = new byte[size], formatMask, cur.width, cur.height);
		}
	}

	private void readArray(Stream s, byte[] array, int formatMask, int width, int height) {
		if ((formatMask & 1) == 0) {
			for (int j = 0, size = width * height; j < size; ++j)
				array[j] = s.getByte();
		} else {
			for (int c = 0; c < width; ++c)
				for (int r = 0; r < height; ++r)
					array[c + r * width] = s.getByte();
		}
	}

	private void decodeBasicImage(Stream s) {
		s.seek(0);
		imageCount = 1;
		ImageData cur = new ImageData();
		if (s.getUByte() != 0)
			return;
		cur.hasAlpha = s.getUByte() == 1;
		cur.width = s.getUShort();
		cur.height = s.getUShort();
		cur.imageColors = new int[cur.width * cur.height];
		for (int i = 0; i < cur.imageColors.length; ++i) {
			cur.imageColors[i] = 0xFF000000 | s.getUInt24();
		}
		if (cur.hasAlpha) {
			for (int i = 0; i < cur.imageColors.length; ++i)
				cur.imageColors[i] = (cur.imageColors[i] & ~0xFF000000) | (s.getUByte() << 24);
		}
		images = new ImageData[] { cur };
	}

	public class ImageData {
		int minX, minY, width, height;
		boolean hasAlpha;
		int[] palette;
		byte[] image;
		byte[] alpha;
		int[] imageColors;

		private SoftReference<BufferedImage> imageRef = new SoftReference<>(null);

		public BufferedImage getImage() {
			if (height == 0 && width == 0)
				return null;
			BufferedImage ret = imageRef.get();
			if (ret != null)
				return ret;
			ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			int[] imageColors = this.imageColors;
			if (imageColors == null) {
				imageColors = new int[width * height];
				for (int i = 0; i < image.length; ++i) {
					int color = palette[image[i] & 0xff];
					if (hasAlpha)
						color |= (alpha[i] & 0xff) << 24;
					else if (color != 0)
						color |= 0xFF000000;
					imageColors[i] = color;
				}
			}
			ret.setRGB(0, 0, width, height, imageColors, 0, width);
			imageRef = new SoftReference<BufferedImage>(ret);
			return ret;
		}
	}

}
