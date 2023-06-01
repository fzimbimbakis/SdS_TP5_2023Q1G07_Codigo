import models.particle.Particle;
import utils.JsonConfigReader;
import utils.Ovito;
import utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class VaryW {
    private static final String JSON_CONFIG_PATH = "./src/main/java/config.json";

    public static void main(String[] args) throws InterruptedException {

        JsonConfigReader config = new JsonConfigReader(JSON_CONFIG_PATH);

        List<Particle> particleList = ParticleUtils.generateParticles(config.getW(), config.getL(), config.getN(), config.getMass(), config.getDt());

        double[] frequencies = {5, 10, 20, 30, 40, 50};

        List<GranularMediaSystem> systems = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(frequencies.length);

        for (double freq : frequencies) {
            GranularMediaSystem system = new GranularMediaSystem(
                    config.getL(),
                    config.getW(),
                    config.getDt(),
                    config.getHoleSize(),
                    config.getMaxTime(),
                    freq,
                    "output_" + freq + "_",
                    particleList
                    );
            systems.add(system);
            executor.execute(system);
        }

        executor.shutdown();
        if(!executor.awaitTermination(10, TimeUnit.HOURS))
            throw new IllegalStateException("Threads timeout");

        Ovito.writeListToFIle(
                systems.stream().map(GranularMediaSystem::getCaudal).collect(Collectors.toList()),
                Ovito.createFile("caudals", "txt"),
                true
        );

        for (GranularMediaSystem system :
                systems) {
            Ovito.writeListToFIle(
                    system.getTimes(),
                    Ovito.createFile("times", "txt"),
                    true
            );
        }
    }

}
