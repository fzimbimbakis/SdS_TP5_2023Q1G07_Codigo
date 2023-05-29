package models;

import Pool.models.particle.Particle;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Comparable<Cell>{

    private final List<Particle> particles = new ArrayList<>();

    public Cell() {
    }

    public void add(Particle particle){
        particles.add(particle);
    }

    public boolean remove(Particle particle){
        return particles.remove(particle);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    @Override
    public int compareTo(Cell o) {
        return Integer.compare(this.particles.size(), o.particles.size());
    }
}
