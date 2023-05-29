package models;

import models.particle.Limit;
import models.particle.Particle;
import utils.ParticleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {

    private static final double DIM_X = 20.0;
    private static final double DIM_Y = 70.0;
    private static final int cols = 17;
    private static final int rows = 60;
    private static final double GRAVITY = -981.0;
    private static final double CELL_DIMENSION_Y = DIM_Y/(double) rows;
    private static final double CELL_DIMENSION_X = DIM_X/(double) cols;
    private static final double K_NORMAL = 2.5;
    private static final double K_TAN = 2*K_NORMAL;
    private static final double DT = Math.pow(10, -4);
    private final Limit topRightLimit;
    private final Limit bottomLeftLimit;

    private final Cell[][] cells;

    public Grid(Limit topRightLimit, Limit bottomLeftLimit) {
        cells = new Cell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell();
            }
        }
        this.bottomLeftLimit = bottomLeftLimit;
        this.topRightLimit = topRightLimit;
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

    private double pairDifference(Pair<Double> a, Pair<Double> b){
        return Math.sqrt(
                Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)
        );
    }

    public void updateForces(){
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                List<Particle> neighbours = getNeighbours(row, col);
                List<Particle> current = cells[row][col].getParticles();

                current.forEach(
                        p -> {
                            // Add gravity
                            p.addToForce(0.0, p.getMass() * GRAVITY);

                            // Add particle forces
                            neighbours.forEach(
                                    n -> {
                                        double diff = pairDifference(p.getPosition(), n.getPosition());
                                        double superposition = p.getRadius() + n.getRadius() - diff;
                                        if(superposition > 0){
                                            Pair<Double> relativeVelocity = new Pair<>(
                                                    n.getVelocity().getX() - p.getVelocity().getX(),
                                                    n.getVelocity().getY() - p.getVelocity().getY()
                                            );
                                            double xVersor = (n.getPosition().getX() - p.getPosition().getX()) / diff;
                                            double yVersor = (n.getPosition().getY() - p.getPosition().getY()) / diff;

                                            p.addToForce(
                                                    getNormalForce(superposition) * xVersor,
                                                    getNormalForce(superposition) * yVersor);
                                            n.addToForce(
                                                    getNormalForce(superposition) * -xVersor,
                                                    getNormalForce(superposition) * -yVersor);

                                            p.addToForce(
                                                    getTangencialForce(superposition, relativeVelocity.getX() * -yVersor) * -yVersor,
                                                    getTangencialForce(superposition, relativeVelocity.getY() * xVersor) * xVersor
                                            );
                                            p.addToForce(
                                                    getTangencialForce(superposition, relativeVelocity.getX() * -yVersor) * -yVersor,
                                                    getTangencialForce(superposition, relativeVelocity.getY() * xVersor) * xVersor
                                            );
                                        }
                                    }
                            );
                        }
                );

                if(row == 0)
                    updateForceFloor(current);

                if(row == rows - 1)
                    updateForceTop(current);

                if(col == 0)
                    updateForceLeftWall(current);

                if(col == cols - 1)
                    updateForceRightWall(current);

            }
        }
    }

    private double getNormalForce(double superposition){
        return -K_NORMAL* superposition;
    }

    private double getTangencialForce(double superposition, double relativeTangencialVelocity){
        return -K_TAN * superposition * relativeTangencialVelocity;
    }

    private void updateForceFloor(List<Particle> particles){
        particles.forEach(p -> {
        double superposition = p.getRadius() - (p.getPosition().getY() - bottomLeftLimit.getPosition().getY());
        if(superposition > 0)
            p.addToForceY(
                    getNormalForce(superposition)
                            +
                            getTangencialForce(superposition, p.getVelocity().getX())
            );
    });
    }
    private void updateForceTop(List<Particle> particles){
        particles.forEach(p -> {
            double superposition = p.getRadius() - (topRightLimit.getPosition().getY() - p.getPosition().getY());
            if(superposition > 0)
                p.addToForceY(
                        getNormalForce(superposition)
                                +
                                getTangencialForce(superposition, p.getVelocity().getX())
                );
        });
    }
    private void updateForceLeftWall(List<Particle> particles){
        particles.forEach(p -> {
            double superposition = p.getRadius() - (p.getPosition().getX() - bottomLeftLimit.getPosition().getX());
            if(superposition > 0)
                p.addToForceX(
                        getNormalForce(superposition)
                                +
                                getTangencialForce(superposition, p.getVelocity().getY())
                );
        });
    }
    private void updateForceRightWall(List<Particle> particles){
        particles.forEach(p -> {
            double superposition = p.getRadius() - (topRightLimit.getPosition().getX() - p.getPosition().getX());
            if(superposition > 0)
                p.addToForceX(
                        getNormalForce(superposition)
                                +
                                getTangencialForce(superposition, p.getVelocity().getY())
                );
        });
    }

    private List<Particle> getNeighbours(int row, int col){
        List<Particle> particles = new ArrayList<>();

        if(row < rows - 1)
            particles.addAll(cells[row + 1][col].getParticles());

        if(row < rows - 1 && col < cols - 1)
            particles.addAll(cells[row + 1][col + 1].getParticles());

        if(col < cols - 1)
            particles.addAll(cells[row][col + 1].getParticles());

        if(row > 0 && col < cols - 1)
            particles.addAll(cells[row - 1][col + 1].getParticles());

        return particles;
    }

    public void update(){

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                for (int k = 0; k < cells[i][j].getParticles().size(); k++) {
                    updateParticleCell(cells[i][j].getParticles().get(k), i, j);
                }
//                cells[i][j].getParticles().forEach(p -> {
//                    updateParticleCell(p, finalI, finalJ);
//                });
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

    private void moveFromCell( Particle particle, int row , int col, int newRow, int newCol){
        cells[newRow][newCol].add(particle);
        cells[row][col].remove(particle);
    }

    private void updateParticleCell(Particle particle, int row, int col){
        double inferiorLimitY = row * CELL_DIMENSION_Y;
        double superiorLimitY = (row + 1) * CELL_DIMENSION_Y;
        double inferiorLimitX = col * CELL_DIMENSION_X;
        double superiorLimitX = (col + 1) * CELL_DIMENSION_X;

        double X = particle.getPosition().getX();
        double Y = particle.getPosition().getY();

        if( X >= superiorLimitX){
            if( Y >= superiorLimitY){ // se va para arriba a la derecha
                moveFromCell(particle, row, col, row + 1 , col + 1);
            }else if (Y < inferiorLimitY){ // se va para abajo a la derecha
                moveFromCell(particle, row, col, row - 1 , col + 1);
            }else{ // se va para la derecha
                moveFromCell(particle, row, col, row , col + 1);
            }
        }else if (X < inferiorLimitX){
            if( Y >= superiorLimitY){ // se va para arriba a la izq
                moveFromCell(particle, row, col, row + 1 , col - 1);
            }else if (Y < inferiorLimitY){ // se va para abajo a la izq
                moveFromCell(particle, row, col, row - 1 , col - 1);
            }else{ // se va para la izquierda
                moveFromCell(particle, row, col, row , col - 1);
            }
        }else{
            if( Y >= superiorLimitY){ // se va para arriba
                moveFromCell(particle, row, col, row + 1 , col);
            }else if (Y < inferiorLimitY){ // se va para abajo
                moveFromCell(particle, row, col, row - 1 , col);
        }
    }

}
