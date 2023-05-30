package utils;

import models.Pair;
import models.particle.Particle;

public class ForcesUtils {

    public static final double K_NORMAL = 250;
    public static final double GRAVITY = -9.81;

    public static final double K_TAN = 2 * K_NORMAL;

    public static Pair<Double> getRelativeVelocity(Particle A, Particle B) {
        return new Pair<>(
                A.getVelocity().getX() - B.getVelocity().getX(),
                A.getVelocity().getY() - B.getVelocity().getY()
        );
    }

    public static double getNormalForce(double superposition) {
        return -K_NORMAL * (superposition / 100);
    }

    public static Pair<Double> getNormalForce(double superposition, Pair<Double> versor) {
        double force = getNormalForce(superposition);
        return new Pair<>(force * versor.getX(), force * versor.getY());
    }

    public static double getTangencialForce(double superposition, double relativeTangencialVelocity) {
//        return 0.0;
        return -K_TAN * (superposition / 100) * (relativeTangencialVelocity / 100);
    }

    public static Pair<Double> getTangencialForce(double superposition, Pair<Double> relativeTangencialVelocity, Pair<Double> normalVersor) {
        double force = getTangencialForce(superposition,
                relativeTangencialVelocity.getX() * -normalVersor.getY()
                        + relativeTangencialVelocity.getY() * normalVersor.getX());
        return new Pair<>(
                force * -normalVersor.getY(),
                force * normalVersor.getX()
        );
    }

    public static Pair<Double> getWallForce(double superposition, Pair<Double> relativeTangencialVelocity, Pair<Double> normalVersor) {
        double forceT = getTangencialForce(superposition,
                relativeTangencialVelocity.getX() * -normalVersor.getY() + relativeTangencialVelocity.getY() * normalVersor.getX());
        double forceN = getNormalForce(superposition);
        return new Pair<>(
                forceT * -normalVersor.getY() + forceN * normalVersor.getX(),
                forceT * normalVersor.getX() + forceN * normalVersor.getY()
        );
    }

}
