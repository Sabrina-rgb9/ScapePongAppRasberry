package com.piomatter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PioMatterSim {

    public static class FB {
        public byte[] data;
        public int strideBytes;
    }

    private final int width, height;
    private final JFrame frame;
    private final JLabel label;
    private final BufferedImage display;

    public PioMatterSim(int width, int height, int addr, int lanes, int brightness, int dummy) {
        this.width = width;
        this.height = height;
        this.display = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        frame = new JFrame("PioMatterSim " + width + "x" + height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label = new JLabel(new ImageIcon(display));
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }

    public FB mapFramebuffer() {
        FB fb = new FB();
        fb.data = new byte[width * height * 3]; // RGB888
        fb.strideBytes = width * 3;
        return fb;
    }

    public void swap(FB fb) {
        // Copiar el framebuffer simulado al BufferedImage
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = y * fb.strideBytes + x * 3;
                int r = fb.data[idx] & 0xFF;
                int g = fb.data[idx + 1] & 0xFF;
                int b = fb.data[idx + 2] & 0xFF;
                display.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        label.repaint();
    }

    public void close() {
        frame.dispose();
    }

    public static void flushBlack(PioMatterSim pm, FB fb, int fadeSteps, int delayMs) {
        for (int i = 0; i < fb.data.length; i++) fb.data[i] = 0;
        pm.swap(fb);
    }

    public static void copyBufferedImageToRGB888(BufferedImage src, byte[] dst, int stride, int w, int h, int brightness) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = src.getRGB(x, y);
                int idx = y * stride + x * 3;
                dst[idx] = (byte)((rgb >> 16) & 0xFF);
                dst[idx + 1] = (byte)((rgb >> 8) & 0xFF);
                dst[idx + 2] = (byte)(rgb & 0xFF);
            }
        }
    }
}
