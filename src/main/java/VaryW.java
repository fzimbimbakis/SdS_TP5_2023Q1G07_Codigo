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
    private final List<Particle> particleList;
    private final JsonConfigReader config;
    public VaryW(List<Particle> particleList, JsonConfigReader config) {
        this.particleList = particleList;
        this.config = config;
    }

    public void run() throws InterruptedException {

        double[] frequencies = {5, 10, 15, 20, 30, 40, 50};

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
                    "output_F_" + freq + "_",
                    this.particleList
                    );
            systems.add(system);
            executor.execute(system);
        }

        executor.shutdown();
        if(!executor.awaitTermination(10, TimeUnit.HOURS))
            throw new IllegalStateException("Threads timeout");

        Ovito.writeListToFIle(
                systems.stream().map(GranularMediaSystem::getCaudal).collect(Collectors.toList()),
                Ovito.createFile("caudals_F", "txt"),
                true
        );

        for (GranularMediaSystem system :
                systems) {
            Ovito.writeListToFIle(
                    system.getTimes(),
                    Ovito.createFile("times_F", "txt"),
                    true
            );
        }

        for (GranularMediaSystem system :
                systems) {
            Ovito.writeListToFIle(
                    system.getEnergy(),
                    Ovito.createFile("energy_F", "txt"),
                    true
            );
        }
    }

}
