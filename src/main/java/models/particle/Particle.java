package models.particle;

import Pool.algorithms.DynamicsAlgorithm;
import Pool.algorithms.GearPredictorCorrector;
import Pool.models.Pair;

import java.util.List;
import java.util.Objects;

public class Particle {
    public enum Color{
        RED(255, 0, 0),
        GREEN(0, 255, 0),
        BLUE(0, 0, 255),
        ORANGE(244, 171, 0),
        WHITE(255, 255, 255);
        final int r;
        final int g;
        final int b;

        Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
    private final Color color;
    private static final Double MAX_X = 224.0;
    private static final Double MAX_Y = 112.0;
    private static final Double K = 10000.0 / 100;
    private final Pair<Double> position;
    private final Pair<Double> velocity;
    private final Double radius;
    private final Double mass;
    private final DynamicsAlgorithm algorithm;
    private final Pair<Double> force;

    public static Particle copy(Particle particle, Double dt, Color color){
        return new Particle(particle.getX(), particle.getY(), particle.getVx(), particle.getVy(), particle.radius, particle.mass, new GearPredictorCorrector(dt), color);
    }

    public Particle(Double x, Double y, Double vx, Double vy, Double radius, Double mass, DynamicsAlgorithm algorithm, Color color) {
        this.color = color;
        this.position = new Pair<>(x, y);
        this.velocity = new Pair<>(vx, vy);
        this.radius = radius;
        this.mass = mass;
        this.algorithm = algorithm;
        this.force = new Pair<>(0.0, 0.0);
        this.algorithm.setParticle(this);
    }


    public void move(List<Particle> particles) {
        algorithm.prediction();
        this.updateForce(particles);
        this.algorithm.calculateNext();

    }

    private void updateForce(List<Particle> particles) {
        this.force.setX(0.0);
        this.force.setY(0.0);
        particles.forEach(
                p -> {
                    if (this.borderDistanceTo(p.position, p.radius) <= 0) {
                        Pair<Double> interactionForce = this.getInteractionForce(p);
                        this.force.setX(this.force.getX() + interactionForce.getX());
                        this.force.setY(this.force.getY() + interactionForce.getY());
                    }
                }
        );

        if (position.getX() < this.radius)
            this.force.setX(this.force.getX() - (position.getX() - this.radius) * K);
        if (position.getX() > MAX_X - this.radius)
            this.force.setX(this.force.getX() - (position.getX() - (MAX_X - this.radius)) * K);

        if (position.getY() < this.radius)
            this.force.setY(this.force.getY() - (position.getY() - this.radius) * K);
        if (position.getY() > MAX_Y - this.radius)
            this.force.setY(this.force.getY() - (position.getY() - (MAX_Y - this.radius)) * K);
    }


    public Pair<Double> getInteractionForce(Particle particle) {

        double radiusSum = particle.getRadius() + getRadius();

        double deltaX = particle.getX() - getX();
        double deltaY = particle.getY() - getY();

        double modulo = Math.sqrt(
                Math.pow(deltaX, 2) + Math.pow(deltaY, 2)
        );

        double f = (modulo - radiusSum) * K;

        return new Pair<>(
                (deltaX / modulo) * f,
                (deltaY / modulo) * f
        );
    }

    public Double getX() {
        return position.getX();
    }


    public Double getY() {
        return position.getY();
    }

    public Double getVx() {
        return velocity.getX();
    }


    public Double getVy() {
        return velocity.getY();
    }


    public void setX(Double x) {
        position.setX(x);
    }


    public void setY(Double y) {
        position.setY(y);
    }

    public void setVx(Double Vx) {
        velocity.setX(Vx);
    }


    public void setVy(Double Vy) {
        velocity.setY(Vy);
    }


    public Double getRadius() {
        return radius;
    }

    public Double borderDistanceTo(Pair<Double> o, Double radius) {
        return Math.sqrt(
                Math.pow(getX() - o.getX(), 2) + Math.pow(getY() - o.getY(), 2)
        ) - (getRadius() + radius);
    }

    public Double getMass() {
        return mass;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + velocity.getX() + " " + velocity.getY() + " " + radius + " " + color.r + " " + color.g + " " + color.b;
    }

    public Double getForceX() {
        return force.getX();
    }

    public Double getForceY() {
        return force.getY();
    }

    public Pair<Double> getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return color == particle.color && Objects.equals(position, particle.position) && Objects.equals(velocity, particle.velocity) && Objects.equals(radius, particle.radius) && Objects.equals(mass, particle.mass) && Objects.equals(algorithm, particle.algorithm) && Objects.equals(force, particle.force);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, position, velocity, radius, mass, algorithm, force);
    }
}
