import models.Grid;
import models.particle.Limit;
import models.particle.Particle;
import utils.JsonConfigReader;
import utils.Ovito;
import utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String JSON_CONFIG_PATH = "./src/main/java/config.json";

    public static void main(String[] args) {

        JsonConfigReader config = new JsonConfigReader(JSON_CONFIG_PATH);

        List<Particle> particleList = ParticleUtils.generateParticles(config.getW(), config.getL(), config.getN(), config.getMass(), config.getDt());

        List<Limit> limits = new ArrayList<>();
        Limit l1 = new Limit(config.getW(), config.getL());
        limits.add(l1);
        Limit l2 = new Limit(0.0, 0.0);
        limits.add(l2);

        Grid grid = new Grid(l1, l2);

        grid.addAll(particleList);

        String path = Ovito.createFile("output", "xyz");
        Ovito.writeParticlesToFileXyz(path, particleList, limits);

        for (double i = 0; i < config.getMaxTime(); i++) {

            System.out.println("Iteración: " + i);

            particleList.forEach(Particle::prediction);
            particleList.forEach(Particle::resetForce);
            grid.update();
            particleList.forEach(Particle::correction);
            particleList.forEach(Particle::resetForce);
            grid.updateForces();

            if (i % 100 == 0)
                Ovito.writeParticlesToFileXyz(path, particleList, limits);

        }







    }
}
