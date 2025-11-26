package com.SpacePong;

import java.awt.Color;
import java.awt.Graphics2D;

public class PaddleRenderer {

    // Mezcla dos colores
    private Color blend(Color c1, Color c2, float ratio) {
        if (ratio > 1f) ratio = 1f;
        else if (ratio < 0f) ratio = 0f;
        
        int r = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
        int g = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
        int b = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
        
        return new Color(r, g, b);
    }

    public void draw(Graphics2D g, int x, int y, int w, int h, Color baseColor) {
        long time = System.currentTimeMillis();

        // 1. Dibujar un "Glow" (resplandor) suave detrás para dar volumen
        // Usamos el color base pero muy transparente
        g.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 60));
        //g.fillRect(x - 1, y - 1, w + 2, h + 2);

        // 2. Dibujar la pala línea a línea (Efecto Plasma Vertical)
        for (int i = 0; i < h; i++) {
            // Coordenada Y actual de esta línea
            int currentY = y + i;

            // --- MAGIA MATEMÁTICA ---
            // Creamos una onda basada en la altura (i) y el tiempo.
            // i * 0.5: Define la "frecuencia" espacial (cuán apretadas están las ondas)
            // time * 0.015: Define la velocidad de la animación
            double wave = Math.sin((i * 0.5) - (time * 0.005)); 
            
            // Normalizamos -1..1 a 0..1
            float brightness = (float) ((wave + 1.0) / 2.0);
            
            // Intensidad del efecto:
            // Mezclamos el color base con BLANCO.
            // brightness * 0.8f significa que en el pico de la onda será 80% blanco.
            Color finalColor = blend(baseColor, Color.WHITE, brightness * 0.8f);

            g.setColor(finalColor);
            
            // Dibujamos una línea horizontal de la pala
            g.fillRect(x, currentY, w, 1);
        }
        
        // 3. (Opcional) Un borde muy fino blanco en los extremos para definir la forma
        g.setColor(new Color(255,255,255, 100));
        g.fillRect(x, y, w, 1); // Borde arriba
        g.fillRect(x, y + h - 1, w, 1); // Borde abajo
    }
}