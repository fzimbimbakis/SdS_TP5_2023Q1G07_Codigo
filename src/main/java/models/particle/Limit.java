package models.particle;

import models.Pair;

public class Limit {

    private Pair<Double> position;

    public Limit(Double x, Double y) {
        this.position = new Pair<>(x, y);
    }

    public void setPosition(Pair<Double> position){
        this.position = position;
    }

    public Pair<Double> getPosition() {
        return position;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + 0.0 + " " + 0.0 + " " + 0.0 + " " + -1 + " " + 0.0 + " " + 0.0;
    }
}
