import models.particle.Particle;
import utils.JsonConfigReader;
import utils.Ovito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class VaryD {

    private final List<Particle> particleList;
    private final JsonConfigReader config;
    private final double freq;

    public VaryD(List<Particle> particleList, JsonConfigReader config, double freq) {
        this.particleList = particleList;
        this.config = config;
        this.freq = freq;
    }

    public void run() throws InterruptedException {

        double[] holeSizes = {0.04, 0.05, 0.06};

        List<GranularMediaSystem> systems = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(holeSizes.length);

        for (double holeSize : holeSizes) {
            GranularMediaSystem system = new GranularMediaSystem(
                    config.getL(),
                    config.getW(),
                    config.getDt(),
                    holeSize,
                    config.getMaxTime(),
                    freq,
                    "output_D_" + holeSize + "_",
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
                Ovito.createFile("caudals_D", "txt"),
                true
        );

        for (GranularMediaSystem system :
                systems) {
            Ovito.writeListToFIle(
                    system.getTimes(),
                    Ovito.createFile("times_D", "txt"),
                    true
            );
        }

        for (GranularMediaSystem system :
                systems) {
            Ovito.writeListToFIle(
                    system.getEnergy(),
                    Ovito.createFile("energy_D", "txt"),
                    true
            );
        }
    }

}
