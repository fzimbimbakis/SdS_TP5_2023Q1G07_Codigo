package utils;

import Pool.algorithms.GearPredictorCorrector;
import Pool.models.particle.FixedParticle;
import Pool.models.particle.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils {

    private static Double randomEpsilon(Double min, Double max){
        return min + Math.random() * Math.abs(max - min);
    }

    public static List<FixedParticle> generateFixedParticles(JsonConfigReader config){
        Double RADIUS = config.getRadius();

        List<FixedParticle> list = new ArrayList<>();

        //// Fixed
        list.add(new FixedParticle(0.0, 0.0, 2*RADIUS));
        list.add(new FixedParticle(config.getMaxX() / 2, 0.0, 2*RADIUS));
        list.add(new FixedParticle(config.getMaxX(), 0.0, 2*RADIUS));

        list.add(new FixedParticle(0.0, config.getMaxY(), 2*RADIUS));
        list.add(new FixedParticle(config.getMaxX() / 2, config.getMaxY(), 2*RADIUS));
        list.add(new FixedParticle(config.getMaxX(), config.getMaxY(), 2*RADIUS));

        return list;
    }

    public static List<FixedParticle> generateInvisible(JsonConfigReader config){

        List<FixedParticle> list = new ArrayList<>();

        //// Fixed
        list.add(new FixedParticle(0.0, 0.0, 0.0));
        list.add(new FixedParticle(config.getMaxX() / 2, 0.0, 0.0));
        list.add(new FixedParticle(config.getMaxX(), 0.0, 0.0));

        list.add(new FixedParticle(0.0, config.getMaxY(), 0.0));
        list.add(new FixedParticle(config.getMaxX() / 2, config.getMaxY(), 0.0));
        list.add(new FixedParticle(config.getMaxX(), config.getMaxY(), 0.0));

        return list;
    }

    public static List<Particle> generateInitialParticles(JsonConfigReader config){

        Double RADIUS = config.getRadius();
        Double MASS = config.getMass();
        Double MAX_EPSILON = config.getMaxEpsilon();
        Double MIN_EPSILON = config.getMinEpsilon();

        List<Particle> list = new ArrayList<>();

        //// White
        list.add(new Particle(config.getWhiteX(), config.getWhiteY(), config.getWhiteV(), 0.0, RADIUS, MASS, new GearPredictorCorrector(config.getDt()), Particle.Color.WHITE));

        //// Default balls
        Double triangleX = config.getTriangleX();
        Double triangleY = config.getTriangleY();

        int n = 1;
        double deltaY = RADIUS * 2 + MAX_EPSILON;
        double deltaX = Math.cos(Math.PI / 6) * (RADIUS * 2 + MAX_EPSILON);

        for (int i = 0; i < 5; i++) {
            list.add(new Particle(triangleX + randomEpsilon(MIN_EPSILON, MAX_EPSILON), triangleY + randomEpsilon(MIN_EPSILON, MAX_EPSILON), 0.0, 0.0, RADIUS, MASS, new GearPredictorCorrector(config.getDt()), Particle.Color.RED));

            for (int j = 1; j < n; j++) {
                list.add(new Particle(triangleX + randomEpsilon(MIN_EPSILON, MAX_EPSILON), triangleY - j * deltaY + randomEpsilon(MIN_EPSILON, MAX_EPSILON), 0.0, 0.0, RADIUS, MASS, new GearPredictorCorrector(config.getDt()), Particle.Color.RED));
            }

            n++;
            triangleX += deltaX;
            triangleY += deltaY / 2;
        }

        return list;
    }

    public static List<Particle> generateInitialParticles(JsonConfigReader config, double whiteY){

        Double RADIUS = config.getRadius();
        Double MASS = config.getMass();
        Double MAX_EPSILON = config.getMaxEpsilon();
        Double MIN_EPSILON = config.getMinEpsilon();

        List<Particle> list = new ArrayList<>();

        //// White
        list.add(new Particle(config.getWhiteX(), whiteY, config.getWhiteV(), 0.0, RADIUS, MASS, new GearPredictorCorrector(config.getDt()), Particle.Color.WHITE));

        //// Default balls
        Double triangleX = config.getTriangleX();
        Double triangleY = config.getTriangleY();

        int n = 1;
        double deltaY = RADIUS * 2 + MAX_EPSILON;
        double deltaX = Math.cos(Math.PI / 6) * (RADIUS * 2 + MAX_EPSILON);

        for (int i = 0; i < 5; i++) {
            list.add(new Particle(triangleX + randomEpsilon(MIN_EPSILON, MAX_EPSILON), triangleY + randomEpsilon(MIN_EPSILON, MAX_EPSILON), 0.0, 0.0, RADIUS, MASS, new GearPredictorCorrector(config.getDt()), Particle.Color.RED));

            for (int j = 1; j < n; j++) {
                list.add(new Particle(triangleX + randomEpsilon(MIN_EPSILON, MAX_EPSILON), triangleY - j * deltaY + randomEpsilon(MIN_EPSILON, MAX_EPSILON), 0.0, 0.0, RADIUS, MASS, new GearPredictorCorrector(config.getDt()), Particle.Color.RED));
            }

            n++;
            triangleX += deltaX;
            triangleY += deltaY / 2;
        }

        return list;
    }

}
