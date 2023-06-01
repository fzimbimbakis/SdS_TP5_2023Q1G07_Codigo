package models.particle;

import models.Pair;

public class Limit {

    private final Pair<Double> position;

    public Limit(Double x, Double y) {
        this.position = new Pair<>(x, y);
    }

    public void setY(Double Y){
        this.position.setY(Y);
    }

    public Double getY(){
        return this.position.getY();
    }

    public Pair<Double> getPosition() {
        return position;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + 0.0 + " " + 0.0 + " " + 0.0 + " " + -1 + " " + 0.0 + " " + 0.0;
    }
}
