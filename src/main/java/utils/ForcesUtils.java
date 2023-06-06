package utils;

import models.Pair;

public class ForcesUtils {

    public static final double K_NORMAL = 0.25;
    public static final double GRAVITY = -5;

    public static final double K_TAN = 2 * K_NORMAL;

    public static double getNormalForce(double superposition) {
        return -K_NORMAL * (superposition);
    }

    public static Pair getNormalForce(double superposition, Pair versor) {

        double force = getNormalForce(superposition);

        return versor.scale(force);
    }

    public static double getTangencialForce(double superposition, double relativeTangencialVelocity) {
        return -K_TAN * (superposition) * (relativeTangencialVelocity);
    }

    public static Pair getTangencialForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor) {

        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());

        double force = getTangencialForce(superposition, relativeTangencialVelocity.dot(tan));

        return tan.scale(force);
    }

    public static Pair getWallForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor) {

        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());

        double forceT = getTangencialForce(superposition, relativeTangencialVelocity.dot(tan));
        double forceN = getNormalForce(superposition);
        return normalVersor.scale(forceN).sum(tan.scale(forceT));
    }

}
