package models.particle;

import algorithms.Beeman;
import models.Pair;

import java.util.Objects;

public class Particle {
    private final Pair<Double> force;
    private Pair<Double> position;
    private Pair<Double> velocity;
    private final Double radius;
    private final Double mass;
    private final int id;

    // Beeman information
    private final Double dt;
    private final Double sqrDt;
    private Pair<Double> prevAcceleration;
    private Pair<Double> actualAcceleration;
    private Pair<Double> actualVelocity;
    private static final double GRAVITY = -981.0;

    public Particle(int id, Pair<Double> position, Double radius, Double mass, Double dt) {
        this.id = id;
        this.position = position;
        this.radius = radius;
        this.mass = mass;
        this.force = new Pair<>(0.0, mass * GRAVITY);
        this.velocity = new Pair<>(0.0, 0.0);
        this.dt = dt;
        this.sqrDt = Math.pow(dt, 2);

        prevAcceleration = new Pair<>(0.0, GRAVITY);
    }

    public void resetForce(){
        force.setX(0.0);
        force.setY(0.0);
    }

    public void addToForce(double x, double y){
        force.setX(force.getX() + x);
        force.setY(force.getY() + y);
    }

    public void addToForceY(double y){
        force.setY(force.getY() + y);
    }

    public void addToForceX(double x){
        force.setX(force.getX() + x);
    }

    public Pair<Double> getForce() {
        return force;
    }

    public Pair<Double> getAcceleration(){
        return new Pair<>(getForce().getX()/getMass(), getForce().getY()/getMass());
    }

    public Pair<Double> getPosition() {
        return position;
    }

    public Pair<Double> getVelocity() {
        return velocity;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + velocity.getX() + " " + velocity.getY() + " " + radius + " " + id;
    }


    // BEEMAN
    public void prediction() {

        double newX = this.getPosition().getX() + this.getVelocity().getX() * dt + (2.0 / 3.0) * this.getAcceleration().getX() * sqrDt - (1.0 / 6.0) * prevAcceleration.getX() * sqrDt;
        double newY = this.getPosition().getY() + this.getVelocity().getY() * dt + (2.0 / 3.0) * this.getAcceleration().getY() * sqrDt - (1.0 / 6.0) * prevAcceleration.getY() * sqrDt;

        //System.out.println(newX + " " + newY);
        double predictedVx = this.getVelocity().getX() + 1.5 * this.getAcceleration().getX() * dt - 0.5 * prevAcceleration.getX() * dt;
        double predictedVy = this.getVelocity().getY() + 1.5 * this.getAcceleration().getY() * dt - 0.5 * prevAcceleration.getY() * dt;

        actualVelocity = this.getVelocity();
        actualAcceleration = this.getAcceleration();

        this.position = new Pair<>(newX, newY);
        this.velocity = new Pair<>(predictedVx, predictedVy);

    }

    public void correction(){
        double newVx = actualVelocity.getX() + (1.0/3.0)*this.getAcceleration().getX()*dt + (5.0/6.0)* actualAcceleration.getX()*dt - (1.0/6.0)* prevAcceleration.getX()*dt;
        double newVy = actualVelocity.getY() + (1.0/3.0)*this.getAcceleration().getY()*dt + (5.0/6.0)* actualAcceleration.getY()*dt - (1.0/6.0)* prevAcceleration.getY()*dt;


        this.velocity = new Pair<>(newVx, newVy);

        prevAcceleration = actualAcceleration;
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
}
