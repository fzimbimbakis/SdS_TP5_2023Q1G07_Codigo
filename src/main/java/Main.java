import models.particle.Particle;
import utils.JsonConfigReader;
import utils.ParticleUtils;

import java.util.List;

public class Main {

    private static final String JSON_CONFIG_PATH = "./src/main/java/config.json";

    public static void main(String[] args) throws InterruptedException {

        JsonConfigReader config = new JsonConfigReader(JSON_CONFIG_PATH);

        List<Particle> particleList = ParticleUtils.generateParticles(config.getW(), config.getL(), config.getN(), config.getMass(), config.getDt());

        System.out.println(particleList.stream().mapToDouble(Particle::getRadius).average());

        GranularMediaSystem system = new GranularMediaSystem(
                    config.getL(),
                    config.getW(),
                    config.getDt(),
                    0.0,
                    config.getMaxTime(),
                0.0,
                "no_D_no_W",
                particleList
        );
        system.run();

        VaryW varyW = new VaryW(particleList, config);

        varyW.run();


        VaryD varyD = new VaryD(particleList, config, varyW.getBestFrequency());

        varyD.run();


    }

}
