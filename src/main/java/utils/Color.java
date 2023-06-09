package utils;

public enum Color{
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255),
    ORANGE(244, 171, 0),
    WHITE(255, 255, 255);
    final int r;
    final int g;
    final int b;

    Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public String toString() {
        return r + " " + g + " " + b;
    }
}
