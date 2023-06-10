import models.Grid;
import models.particle.Limit;
import models.particle.Particle;
import utils.Ovito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GranularMediaSystem implements Runnable{

    private final double dt;
    private final double frequency;
    private final List<Particle> particles;
    private final Grid grid;
    private final List<Limit> limits;
    private final int iterations;
    private final String path;
    private final List<Double> times = new ArrayList<>();
    private final List<Double> energy = new ArrayList<>();

    public GranularMediaSystem(double l, double w, double dt, double d, double maxTime, double frequency, String outputFileName, List<Particle> particles) {
        this.dt = dt;
        this.iterations = (int)(maxTime/dt);
        this.frequency = frequency;
        this.particles = particles.stream().map(Particle::copy).collect(Collectors.toList());

        this.limits = new ArrayList<>();
        Limit l1 = new Limit(w, l + l /10);
        this.limits.add(l1);
        Limit l2 = new Limit(0.0, l /10);
        this.limits.add(l2);
        Limit l3 = new Limit(w, 0.0);
        this.limits.add(l3);
        Limit leftHole = new Limit(w / 2 - d / 2, l /10);
        this.limits.add(leftHole);
        Limit rightHole = new Limit(w / 2 + d / 2, l /10);
        this.limits.add(rightHole);

        this.grid = new Grid(l1, l2, d);

        grid.addAll(this.particles);

        this.path = Ovito.createFile(outputFileName, "xyz");
        Ovito.writeParticlesToFileXyz(path, this.particles, this.limits);
        
    }

    @Override
    public void run() {

        for (int i = 0; i < iterations; i++) {

            grid.shake(i * dt, frequency);

            particles.forEach(Particle::prediction);
            particles.forEach(Particle::resetForce);

            for (int j = 0; j < grid.update(); j++)
                times.add(i * dt);

            grid.updateForces();

            particles.forEach(Particle::correction);

            particles.forEach(Particle::resetForce);
            grid.updateForces();

            if (i % 100 == 0) {
                if (i % 100000 == 0)
                    System.out.println(hashCode() + ": iteración-" + i);
                energy.add(particles.stream().mapToDouble(Particle::getEnergy).sum());
                Ovito.writeParticlesToFileXyz(path, particles, limits);
            }
        }

    }

    public List<Double> getTimes() {
        return times;
    }

    public double getCaudal(){
        return times.size() / (iterations * dt);
    }

    public List<Double> getEnergy() {
        return energy;
    }
}
