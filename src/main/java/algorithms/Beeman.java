package algorithms;

//import models.Pair;
//import models.particle.Particle;

public class Beeman {

    // Algorithm information
//    private Pair<Double> prevAcceleration;
//    private Pair<Double> actualAcceleration;
//    private Pair<Double> actualVelocity;
//
//    private final double dt;
//    private static final double GRAVITY = 9.81;
//
//    public Beeman(double dt){
//        this.dt = dt;
//
//        prevAcceleration = new Pair<>(0.0, - GRAVITY);
//
//    }

//    public void prediction(Particle particle) {
//        double dt2 = Math.pow(dt, 2);
//        double newX = particle.getPosition().getX() + particle.getVelocity().getX() * dt + (2.0 / 3.0) * particle.getAcceleration().getX() * dt2 - (1.0 / 6.0) * prevAcceleration.getX() * dt2;
//        double newY = particle.getPosition().getY() + particle.getVelocity().getY() * dt + (2.0 / 3.0) * particle.getAcceleration().getY() * dt2 - (1.0 / 6.0) * prevAcceleration.getY() * dt2;
//
//        double predictedVx = particle.getVelocity().getX() + 1.5 * particle.getAcceleration().getX() * dt - 0.5 * prevAcceleration.getX() * dt;
//        double predictedVy = particle.getVelocity().getY() + 1.5 * particle.getAcceleration().getY() * dt - 0.5 * prevAcceleration.getY() * dt;
//
//        actualVelocity = particle.getVelocity();
//        actualAcceleration = particle.getAcceleration();
//
//        particle.setPosition(new Pair<>(newX, newY));
//        particle.setVelocity(new Pair<>(predictedVx, predictedVy));
//
//    }
//
//    public void correction(Particle particle){
//        double newVx = actualVelocity.getX() + (1.0/3.0)*particle.getAcceleration().getX()*dt + (5.0/6.0)* actualAcceleration.getX()*dt - (1.0/6.0)* prevAcceleration.getX()*dt;
//        double newVy = actualVelocity.getY() + (1.0/3.0)*particle.getAcceleration().getY()*dt + (5.0/6.0)* actualAcceleration.getY()*dt - (1.0/6.0)* prevAcceleration.getY()*dt;
//
//        prevAcceleration = actualAcceleration;
//        particle.setVelocity(new Pair<>(newVx, newVy));
//    }
}
