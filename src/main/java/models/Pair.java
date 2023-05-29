package models;

import java.util.Objects;

public class Pair<T> {

    private T x;
    private T y;

    public Pair(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?> pair = (Pair<?>) o;
        return Objects.equals(x, pair.x) && Objects.equals(y, pair.y);
    }

    public static<T> Pair<T> copy(Pair<T> pair){
        return new Pair<>(pair.getX(), pair.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
