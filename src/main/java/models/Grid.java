package models;

import Pool.models.particle.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {

    private static final Double MAX_X = 224.0;
    private static final Double MAX_Y = 112.0;
    private static final Double CELL_DIMENSION = 5.7435897435897435897435897435897;
    private final Map<Integer, Map<Integer, Cell>> map = new HashMap<>();

    public Grid() {

        this.createCellsAndFillDataStructures();

    }

    public List<Particle> getNeighbours(Double x, Double y){
        List<Particle> particles = new ArrayList<>();

        for (Double deltaX = -CELL_DIMENSION; deltaX <= CELL_DIMENSION; deltaX += CELL_DIMENSION){
            for (Double deltaY = -CELL_DIMENSION; deltaY <= CELL_DIMENSION; deltaY += CELL_DIMENSION){
                Cell cell = getCell(x + deltaX, y + deltaY);
                if( cell == null )
                    continue;
                particles.addAll(cell.getParticles());
            }
        }

        return particles;
    }

    public boolean remove(Particle particle){
        double x = Math.min(MAX_X, particle.getX());
        x = Math.max(0, x);
        double y = Math.min(MAX_Y, particle.getY());
        y = Math.max(0, y);
        Cell cell = getCell(x, y);
        if (cell != null) {
            return cell.remove(particle);
        }else throw new IllegalStateException("Cell does not exists");
    }

    public void add(Particle particle){
        double x = Math.min(MAX_X, particle.getX());
        x = Math.max(0, x);
        double y = Math.min(MAX_Y, particle.getY());
        y = Math.max(0, y);
        Cell cell = getCell(x, y);
        if (cell != null) {
            cell.add(particle);
        }else throw new IllegalStateException("Cell does not exists");
    }

    public  void addAll(List<Particle> particles){
        particles.forEach(this::add);
    }

    private void createCellsAndFillDataStructures(){
        for (double x = 0.0; x < MAX_X; x += CELL_DIMENSION){
            map.put((int) x, new HashMap<>());
            for (double y = 0.0; y < MAX_Y; y += CELL_DIMENSION) {
                map.get((int) x).put((int) y, new Cell());
            }
        }
    }

    private Cell getCell(double x, double y){
        if(x > MAX_X || x < 0 || y < 0 || y > MAX_Y)
            return null;
        if(x == MAX_X)
            x -= 1.0;
        if(y == MAX_Y)
            y -= 1.0;
        int xi = getIndex(x);
        int yi = getIndex(y);
        Cell cell = map.get(xi).get(yi);
        return cell;
    }

    private int getIndex(double value){
        return (int)(((int)(value / CELL_DIMENSION)) * CELL_DIMENSION) ;
    }

}
