package models;

import java.util.Objects;

public class Pair {

    private Double x;
    private Double y;

    public Pair(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public static Pair copy(Pair pair) {
        return new Pair(pair.getX(), pair.getY());
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(x, pair.x) && Objects.equals(y, pair.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double dot(Pair other) {
        return this.x * other.getX() + this.y * other.getY();
    }

    public Pair scale(double scalar) {
        return new Pair(this.x * scalar, this.y * scalar);
    }

    public Pair subtract(Pair other) {
        return new Pair(this.x - other.x, this.y - other.y);
    }

    public Pair sum(Pair other) {
        return new Pair(this.x + other.x, this.y + other.y);
    }


}
