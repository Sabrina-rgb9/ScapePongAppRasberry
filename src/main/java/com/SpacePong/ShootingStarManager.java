package com.SpacePong;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class ShootingStarManager {
    private float x, y;     // Posición actual (float para movimiento suave)
    private float vx, vy;   // Velocidad X e Y
    private boolean active = false; // Si la estrella está pasando ahora mismo
    private final int width, height;
    // Colores para la estela: Blanco -> Amarillo -> Naranja -> Rojo oscuro
    private final Color[] trailColors = {
        Color.WHITE,
        new Color(255, 255, 200), // Amarillo pálido
        new Color(255, 220, 100), // Naranja claro
        new Color(255, 150, 50),  // Naranja fuerte
        new Color(200, 50, 50),   // Rojo
        new Color(100, 0, 0)      // Rojo oscuro casi transparente
    };

    public ShootingStarManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Intenta lanzar una estrella aleatoriamente
    public void trySpawn() {
        if (active) return; // Ya hay una en pantalla

        // Probabilidad muy baja por frame (ej. 0.5%) para que sea "especial"
        if (Math.random() > 0.005) return;

        // Decidir desde dónde sale (para que cruce la pantalla diagonalmente)
        if (Math.random() < 0.5) {
            // Sale de la izquierda
            x = -5;
            y = (float) (Math.random() * height / 2); // Mitad superior
            vx = (float) (2.5 + Math.random() * 3.0);   // Rápido hacia derecha
            vy = (float) (1.0 + Math.random() * 2.0);   // Diagonal abajo
        } else {
            // Sale de arriba
            x = (float) (Math.random() * width);
            y = -5;
            vx = (float) (-3.0 + Math.random() * 6.0); // Izquierda o derecha
            vy = (float) (3.0 + Math.random() * 3.0);  // Rápido hacia abajo
        }
        active = true;
    }

    // Actualiza la posición
    public void update() {
        if (!active) return;
        x += vx;
        y += vy;

        // Si se sale mucho de la pantalla, desactivarla
        int margin = 20; // Margen grande para que la cola termine de salir
        if (x < -margin || x > width + margin || y < -margin || y > height + margin) {
            active = false;
        }
    }

    // Dibuja la cabeza y la estela
    public void draw(Graphics2D g) {
        if (!active) return;

        // 1. Dibujar la cola (retrocediendo desde la posición actual en dirección opuesta a la velocidad)
        for (int i = 1; i < trailColors.length; i++) {
            g.setColor(trailColors[i]);
            // Calculamos la posición de la cola. El factor '1.2f' separa un poco los puntos
            float tailX = x - (vx * i * 0.8f);
            float tailY = y - (vy * i * 0.8f);
            g.fillRect((int)tailX, (int)tailY, 1, 1);
        }

        // 2. Dibujar la cabeza (el punto más brillante) al final para que quede encima
        g.setColor(trailColors[0]); // Blanco brillante
        g.fillRect((int)x, (int)y, 1, 1);
        // Un pequeño brillo extra alrededor de la cabeza
        g.setColor(new Color(255, 255, 255, 100));
        g.fillRect((int)x-1, (int)y, 3, 1);
        g.fillRect((int)x, (int)y-1, 1, 3);
    }
}