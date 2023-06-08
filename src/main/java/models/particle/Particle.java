package models.particle;

import models.Pair;
import utils.ForcesUtils;

import java.util.Objects;

public class Particle {
    private final static double B = (2.0 / 3.0);
    private final static double C = -(1.0 / 6.0);
    private final Pair force;
    private final Double radius;
    private final Double mass;
    private final int id;
    private boolean reInjected = false;

    // Beeman information
    private final Double dt;
    private final Double sqrDt;
    private Pair position;
    private Pair velocity;
    private Pair prevAcceleration;
    private Pair actualAcceleration;
    private Pair actualVelocity;

    public void resetForce() {
        force.setX(0.0);
        force.setY(0.0);
    }

    public void addToForce(double x, double y) {
        force.setX(force.getX() + x);
        force.setY(force.getY() + y);
    }

    public Particle(int id, Pair position, Double radius, Double mass, Double dt) {
        this.id = id;
        this.position = position;
        this.radius = radius;
        this.mass = mass;
        this.force = new Pair(0.0, mass * ForcesUtils.GRAVITY);
        this.velocity = new Pair(0.0, 0.0);
        this.dt = dt;
        this.sqrDt = Math.pow(dt, 2);

        prevAcceleration = new Pair(0.0, ForcesUtils.GRAVITY);
    }

    public Particle copy() {
        return new Particle(id, position, radius, mass, dt);
    }

    public void addToForce(Pair pair) {
        force.setX(force.getX() + pair.getX());
        force.setY(force.getY() + pair.getY());
    }

    public Pair getAcceleration() {
        return force.scale(1.0 / mass);
    }

    public void reInject() {
        reInjected = true;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + velocity.getX() + " " + velocity.getY() + " " + radius + " " + id + " " + force.getX() + " " + force.getY();
    }

    public Pair getPosition() {
        return position;
    }

    public Pair getVelocity() {
        return velocity;
    }

    // BEEMAN
    public void prediction() {

//        double newX = this.getPosition().getX() + this.getVelocity().getX() * dt + (2.0 / 3.0) * this.getAcceleration().getX() * sqrDt - (1.0 / 6.0) * prevAcceleration.getX() * sqrDt;
//        double newY = this.getPosition().getY() + this.getVelocity().getY() * dt + (2.0 / 3.0) * this.getAcceleration().getY() * sqrDt - (1.0 / 6.0) * prevAcceleration.getY() * sqrDt;

        this.position = this.position.sum(
                this.velocity.scale(dt).sum(
                        this.getAcceleration().scale(sqrDt * B).sum(
                                prevAcceleration.scale(C * sqrDt)
                        )
                )
        );

        this.actualVelocity = this.getVelocity();
        this.actualAcceleration = this.getAcceleration();

        this.velocity = this.actualVelocity.sum(
                this.actualAcceleration.scale(1.5 * dt).sum(
                        prevAcceleration.scale(-0.5 * dt)
                )
        );

//        double predictedVx = this.getVelocity().getX() + 1.5 * this.getAcceleration().getX() * dt - 0.5 * prevAcceleration.getX() * dt;
//        double predictedVy = this.getVelocity().getY() + 1.5 * this.getAcceleration().getY() * dt - 0.5 * prevAcceleration.getY() * dt;
//
//
////        this.position = new Pair(newX, newY);
//        this.velocity = new Pair(predictedVx, predictedVy);

    }

    public void correction(){
        if (reInjected){
            this.velocity = new Pair(0.0, 0.0);
            reInjected = false;
            prevAcceleration = new Pair(0.0, ForcesUtils.GRAVITY);
        }else {
//            double newVx = actualVelocity.getX() + (1.0/3.0)*this.getAcceleration().getX()*dt + (5.0/6.0)* actualAcceleration.getX()*dt - (1.0/6.0)* prevAcceleration.getX()*dt;
//            double newVy = actualVelocity.getY() + (1.0/3.0)*this.getAcceleration().getY()*dt + (5.0/6.0)* actualAcceleration.getY()*dt - (1.0/6.0)* prevAcceleration.getY()*dt;
//            this.velocity = new Pair(newVx, newVy);

            this.velocity = actualVelocity.sum(
                    this.getAcceleration().scale((1.0 / 3.0) * dt).sum(
                            actualAcceleration.scale((5.0 / 6.0) * dt).sum(
                                    prevAcceleration.scale(-(1.0 / 6.0) * dt) // TODO CHECK signo menos
                            )
                    )
            );

            prevAcceleration = actualAcceleration;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle)) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }
}
