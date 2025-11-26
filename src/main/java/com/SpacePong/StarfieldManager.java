package com.SpacePong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarfieldManager {
    
    private static class Star {
        float x, y;
        float speed;     // Velocidad basada en profundidad
        float size;      // 0 = punto pequeño, 1 = punto brillante
        float baseAlpha; // Brillo base
        float phase;     // Para el parpadeo
        Color tint;      // Matiz de color (azulado, rojizo, blanco)
        boolean hasGlint;// Si es una estrella que hace "destellos"
    }

    private final int width, height;
    private final List<Star> stars = new ArrayList<>();
    private final Random random = new Random();
    
    // Paleta de colores estelares (Blanco, Azulado, Rojizo, Dorado)
    private final Color[] starTints = {
        new Color(200, 200, 255), // Azul hielo
        new Color(255, 220, 200), // Rojizo tenue
        new Color(255, 255, 220), // Dorado pálido
        new Color(220, 220, 220)  // Blanco puro
    };

    public StarfieldManager(int width, int height) {
        this.width = width;
        this.height = height;
        initStars();
    }

    private void initStars() {
        // Generamos 3 capas de estrellas
        // Capa 1: Fondo (Muchas, lentas, oscuras) -> 30 estrellas
        createLayer(30, 0.05f, 0.1f, 40, 100, false);
        
        // Capa 2: Medio (Normales) -> 15 estrellas
        createLayer(15, 0.15f, 0.3f, 100, 180, false);
        
        // Capa 3: Frente (Pocas, rápidas, brillantes y con destellos) -> 5 estrellas
        createLayer(5, 0.4f, 0.6f, 180, 255, true);
    }

    private void createLayer(int count, float minSpeed, float maxSpeed, int minBri, int maxBri, boolean canGlint) {
        for (int i = 0; i < count; i++) {
            Star s = new Star();
            s.x = random.nextInt(width);
            s.y = random.nextInt(height);
            s.speed = minSpeed + random.nextFloat() * (maxSpeed - minSpeed);
            s.baseAlpha = minBri + random.nextInt(maxBri - minBri);
            s.phase = random.nextFloat() * (float)Math.PI * 2;
            s.tint = starTints[random.nextInt(starTints.length)];
            // Solo algunas de la capa frontal tienen destello cruzado
            s.hasGlint = canGlint && (random.nextFloat() > 0.6); 
            stars.add(s);
        }
    }

    public void update() {
        for (Star s : stars) {
            // Movimiento lateral (parallax)
            s.x -= s.speed;
            
            // Si sale por la izquierda, entra por la derecha
            if (s.x < 0) {
                s.x = width;
                s.y = random.nextInt(height);
            }
        }
    }

    public void draw(Graphics2D g) {
        long time = System.currentTimeMillis();

        for (Star s : stars) {
            // Efecto "respiración" (parpadeo suave usando seno)
            double twinkle = Math.sin((time * 0.003) + s.phase); // -1 a 1
            
            // Ajustar brillo final
            float alphaFactor = (float) ((twinkle + 2.0) / 3.0); // Normalizar para que no se apague del todo
            int alpha = (int) (s.baseAlpha * alphaFactor);
            alpha = Math.max(0, Math.min(255, alpha));

            // Crear color final con el brillo calculado
            Color finalColor = new Color(
                (s.tint.getRed() * alpha) / 255,
                (s.tint.getGreen() * alpha) / 255,
                (s.tint.getBlue() * alpha) / 255
            );
            
            g.setColor(finalColor);

            int x = (int) s.x;
            int y = (int) s.y;

            // Dibujar estrella
            if (s.hasGlint && alpha > 200) {
                // Si es brillante y tiene propiedad "glint", dibujamos una cruz pequeña
                // Centro
                g.fillRect(x, y, 1, 1); 
                // Destellos tenues (arriba/abajo/izq/der)
                g.setColor(new Color(finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue(), 100));
                g.fillRect(x - 1, y, 3, 1);
                g.fillRect(x, y - 1, 1, 3);
            } else {
                // Estrella normal (pixel simple)
                g.fillRect(x, y, 1, 1);
            }
        }
    }
}