package models.particle;

import Pool.models.Pair;

import java.util.ArrayList;
import java.util.List;

public class FixedParticle {

    private final Pair<Double> position;
    private final Double radius;

    public FixedParticle(Double x, Double y, Double radius) {
        this.position = new Pair<>(x, y);
        this.radius = radius;
    }

    public List<Particle> getCollisions(List<Particle> particles) {
        List<Particle> collisions = new ArrayList<>();
        particles.forEach(
                p -> {
                    if (p.borderDistanceTo(position, radius) <= 0)
                        collisions.add(p);
                }
        );
        return collisions;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + 0.0 + " " + 0.0 + " " + radius + " 0 0 0";
    }

    public Pair<Double> getPosition() {
        return position;
    }
}
