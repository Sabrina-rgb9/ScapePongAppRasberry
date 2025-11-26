package com.SpacePong;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ScoreBoardRenderer {

    private final int width;
    private final BufferedImage maskBuffer;
    private final Graphics2D maskG;
    private final Font scoreFont;

    // Colores base
    private final Color colorP1 = Color.decode("#00FF9C"); // Verde Cyber
    private final Color colorP2 = Color.decode("#f8acff"); // Lila Retro

    public ScoreBoardRenderer(int width, int height) {
        this.width = width;
        // Creamos un buffer solo para la zona superior (donde va el marcador)
        // Altura 15px es suficiente para el texto
        this.maskBuffer = new BufferedImage(width, 20, BufferedImage.TYPE_INT_ARGB);
        this.maskG = maskBuffer.createGraphics();
        
        // Configuración de calidad para el texto
        maskG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        maskG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Fuente un poco más grande y negrita
        this.scoreFont = new Font("SansSerif", Font.BOLD, 10);
    }

    public void draw(Graphics2D mainG, int score1, int score2) {
        // 1. Limpiar el buffer de máscara
        maskG.setBackground(new Color(0, 0, 0, 0)); // Transparente
        maskG.clearRect(0, 0, width, 20);

        // 2. Dibujar el texto en BLANCO sobre el buffer transparente
        maskG.setColor(Color.WHITE);
        maskG.setFont(scoreFont);
        
        String txt1 = String.valueOf(score1);
        String txt2 = String.valueOf(score2);
        
        FontMetrics fm = maskG.getFontMetrics();
        int w1 = fm.stringWidth(txt1);
        int w2 = fm.stringWidth(txt2);
        
        // Posiciones (Centrados en sus mitades respectivas)
        // P1 a la izquierda del centro, P2 a la derecha
        int centerX = width / 2;
        int yPos = 12; // Ajuste vertical

        int x1 = centerX - 10 - w1;
        int x2 = centerX + 10;

        maskG.drawString(txt1, x1, yPos);
        maskG.drawString(txt2, x2, yPos);

        // 3. Efecto "Shimmering Neon"
        long time = System.currentTimeMillis();
        
        // Recorremos los píxeles del buffer. Si hay pixel (alpha > 0), dibujamos el efecto en el mainG
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < width; x++) {
                int argb = maskBuffer.getRGB(x, y);
                int alpha = (argb >> 24) & 0xFF;

                if (alpha > 0) {
                    // Determinar si es P1 o P2 basado en la posición X
                    Color baseColor = (x < width / 2) ? colorP1 : colorP2;

                    // --- MAGIA MATEMÁTICA PARA EL EFECTO ---
                    // Creamos una onda que se mueve con el tiempo
                    // x * 0.2 hace que la onda sea ancha
                    // time * 0.005 define la velocidad
                    double wave = Math.sin((x * 0.2) - (time * 0.008)); 
                    
                    // Normalizamos la onda de -1..1 a 0..1
                    float brightness = (float) ((wave + 1.0) / 2.0);
                    
                    // Hacemos que el brillo sea más intenso (mix con blanco)
                    // Si brightness es alto -> tiende a Blanco. Si es bajo -> Color Base
                    Color finalColor = blend(baseColor, Color.WHITE, brightness * 0.7f);

                    mainG.setColor(finalColor);
                    mainG.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    // Mezcla dos colores
    private Color blend(Color c1, Color c2, float ratio) {
        if (ratio > 1f) ratio = 1f;
        else if (ratio < 0f) ratio = 0f;
        
        int r = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
        int g = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
        int b = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
        
        return new Color(r, g, b);
    }
}