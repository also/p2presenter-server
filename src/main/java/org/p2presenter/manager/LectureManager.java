package org.p2presenter.manager;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.Slide;

import edu.uoregon.cs.presenter.controller.FileManager;
import edu.uoregon.cs.presenter.dao.Dao;

public class LectureManager {
	private Dao dao;
	private FileManager fileManager;

	public static final int MAX_DIMENSION = 200;

	public void setDao(Dao dao) {
		this.dao = dao;
	}
	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void addSlide(Lecture lecture, Slide slide, InputStream in) throws IOException {
		slide.setIndex(lecture.getSlides().size());
		slide.setLecture(lecture);
		lecture.getSlides().add(slide);

		dao.save(slide);
		dao.flush();

		File file = fileManager.getFile("slide", slide.getId());
		FileOutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		in.close();
		out.close();

		BufferedImage slideImage = ImageIO.read(file);

		int w = slideImage.getWidth();
		int h = slideImage.getHeight();

		Dimension d = calculateThumbnailDimensions(slideImage);
		BufferedImage scaledImage = slideImage;
		do {
			w /= 2;
			h /= 2;
			if (w < d.width || h < d.height) {
				w = d.width;
				h = d.height;
			}
			BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(scaledImage, 0, 0, w, h, null);
			g2.dispose();

			scaledImage = tmp;
		}
		while (w != d.width);

		File thumbnailFile = fileManager.getFile("slideThumbnail", slide.getId());

		ImageIO.write(scaledImage, "png", thumbnailFile);
	}

	private Dimension calculateThumbnailDimensions(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();

		double ratio;

		if (w > h) {
			ratio = ((double) MAX_DIMENSION) / w;
		}
		else {
			ratio = ((double) MAX_DIMENSION) / h;
		}

		return new Dimension((int) (w * ratio), (int) (h * ratio));
	}
}
