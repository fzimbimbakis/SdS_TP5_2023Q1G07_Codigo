package models;

import models.particle.Particle;

import java.util.ArrayList;
import java.util.List;

public class Cell{

    private final List<Particle> particles = new ArrayList<>();

    public void add(Particle particle){
        particles.add(particle);
    }

    public boolean remove(Particle particle){
        return particles.remove(particle);
    }

    public List<Particle> getParticles() {
        return particles;
    }

}
