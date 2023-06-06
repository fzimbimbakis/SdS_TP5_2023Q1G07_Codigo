package utils;

import models.Pair;
import models.particle.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils {

    private static final double MAX_RADIUS = 1.15;
    private static final double MIN_RADIUS = 0.85;
    public static List<Particle> generateParticles(Double W, Double L, int N, Double mass, Double dt) {
    List<Particle> particles = new ArrayList<>();
    double x, y, radius;
    boolean overlap;

    for (int i = 1; i <= N; i++) {
        // Generate initial particle
        radius = MIN_RADIUS + Math.random() * (MAX_RADIUS - MIN_RADIUS);
        x = radius + Math.random() * (W - 2 * radius);
        y = radius + L/10 + Math.random() * (L - 2 * radius);
        Particle newParticle = new Particle(i, new Pair(x, y), radius, mass, dt);

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

    public static void reInjectParticles(List<Particle> particles, List<Particle> particlesToInject, Double W, Double L) {

        boolean overlap;
        double minY = ((L/7) * 4); // = 40


        for (int i = 0; i < particlesToInject.size(); i++) {

            Particle p = particlesToInject.get(i);

            p.getPosition().setX(p.getRadius() + Math.random() * (W - 2 * p.getRadius()));
            p.getPosition().setY(minY + L/10 + Math.random() * ((L-minY) - p.getRadius()));
            // Y e [47, 77-radio]

            overlap = false;

            // Check for overlap with existing particles
            for (Particle existingParticle : particles) {
                if( !existingParticle.equals(p)){
                    double distance = calculateDistance(p, existingParticle);
                    if (distance < p.getRadius() + existingParticle.getRadius()) {
                        overlap = true;
                        break;
                    }
                }
            }

            // If overlap detected, generate a new particle
            if (overlap) {
                System.out.println("OVERLAP");
                i--;
            }
        }
    }

    private static double calculateDistance(Particle p1, Particle p2) {
        double x1 = p1.getPosition().getX();
        double y1 = p1.getPosition().getY();
        double x2 = p2.getPosition().getX();
        double y2 = p2.getPosition().getY();

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}