package utils;

import models.Pair;
import models.particle.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils {

    private static Double randomEpsilon(Double min, Double max){
        return min + Math.random() * Math.abs(max - min);
    }


//    public static List<Particle> generateParticles(Double W, Double L, int N, Double mass, Double dt) {
//        List<Particle> particles = new ArrayList<>();
//        double x, y, radius;
//        for (int i = 0; i < N; i++) {
//            // Generate
//            x = Math.random() * W;
//            y = Math.random() * L;
//            radius = Math.random() * 1.15;
//            particles.add(new Particle(i, new Pair<>(x, y), radius, mass, dt));
//        }
//        return particles;
//    }
public static List<Particle> generateParticles(Double W, Double L, int N, Double mass, Double dt) {
    List<Particle> particles = new ArrayList<>();
    double x, y, radius;
    boolean overlap;

    for (int i = 0; i < N; i++) {
        // Generate initial particle
        radius = 0.85 + Math.random() * (1.15 - 0.85);
        x = radius + Math.random() * (W - 2 * radius);
        y = radius + Math.random() * (L - 2 * radius);
        Particle newParticle = new Particle(i, new Pair<>(x, y), radius, mass, dt);

        overlap = false;

        // Check for overlap with existing particles
        for (Particle existingParticle : particles) {
            double distance = calculateDistance(newParticle, existingParticle);
            if (distance < newParticle.getRadius() + existingParticle.getRadius()) {
                overlap = true;
                break;
            }
        }

        // If overlap detected, generate a new particle
        if (overlap) {
            System.out.println("OVERLAP");
            i--;
        } else {
            particles.add(newParticle);
        }
    }

    return particles;
}

    private static double calculateDistance(Particle p1, Particle p2) {
        double x1 = p1.getPosition().getX();
        double y1 = p1.getPosition().getY();
        double x2 = p2.getPosition().getX();
        double y2 = p2.getPosition().getY();

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}