package models;

import models.particle.Limit;
import models.particle.Particle;
import utils.ForcesUtils;

import java.util.ArrayList;
import java.util.List;

import static utils.ForcesUtils.*;

public class Grid {

    private static final double DIM_X = 20.0;
    private static final double DIM_Y = 70.0;
    private static final int cols = 8;
    private static final int rows = 30;
    private static final double CELL_DIMENSION_Y = DIM_Y / (double) rows;
    private static final double CELL_DIMENSION_X = DIM_X / (double) cols;
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

    public void add(Particle particle){
        Cell cell = getCell(particle.getPosition().getX(), particle.getPosition().getY());
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
                            p.addToForce(0.0, p.getMass() * ForcesUtils.GRAVITY);

                            current.forEach(n -> {
                                double diff = pairDifference(p.getPosition(), n.getPosition());
                                double superposition = p.getRadius() + n.getRadius() - diff;

                                if (superposition > 0 && !n.equals(p)) {

                                    Pair<Double> normalVersor = new Pair<>(
                                            (n.getPosition().getX() - p.getPosition().getX()) / diff,
                                            (n.getPosition().getY() - p.getPosition().getY()) / diff
                                    );

                                    p.addToForce(
                                            getNormalForce(superposition) * normalVersor.getX(),
                                            getNormalForce(superposition) * normalVersor.getY()
                                    );

                                    Pair<Double> relativeVelocity = ForcesUtils.getRelativeVelocity(p, n);
                                    Pair<Double> tangencialForce = getTangencialForce(superposition, relativeVelocity, normalVersor);

                                    p.addToForce(
                                            tangencialForce.getX(),
                                            tangencialForce.getY()
                                    );
                                }
                            });

                            // Add particle forces
                            neighbours.forEach(
                                    n -> {

                                        double diff = pairDifference(p.getPosition(), n.getPosition());
                                        double superposition = p.getRadius() + n.getRadius() - diff;

                                        if (superposition > 0 && !n.equals(p)) {

                                            Pair<Double> normalVersor = new Pair<>(
                                                    (n.getPosition().getX() - p.getPosition().getX()) / diff,
                                                    (n.getPosition().getY() - p.getPosition().getY()) / diff
                                            );

                                            Pair<Double> normalForce = getNormalForce(superposition, normalVersor);

                                            p.addToForce(normalForce.getX(), normalForce.getY());
                                            n.addToForce(-1 * normalForce.getX(), -1 * normalForce.getY());

                                            Pair<Double> relativeVelocity = ForcesUtils.getRelativeVelocity(p, n);
                                            Pair<Double> tangencialForce = getTangencialForce(superposition, relativeVelocity, normalVersor);

                                            p.addToForce(
                                                    tangencialForce.getX(),
                                                    tangencialForce.getY()
                                            );
                                            n.addToForce(
                                                    -tangencialForce.getX(),
                                                    -tangencialForce.getY());
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

    private static final Pair<Double> FloorNormalVersor = new Pair<>(0.0, -1.0);

    private void updateForceFloor(List<Particle> particles) {
        particles.forEach(p -> {
            double superposition = p.getRadius() - (p.getPosition().getY() - bottomLeftLimit.getPosition().getY());
            if (superposition > 0)
                p.addToForce(
                        getWallForce(superposition, p.getVelocity(), FloorNormalVersor)
                );
        });
    }

    private static final Pair<Double> TopNormalVector = new Pair<>(0.0, 1.0);

    private void updateForceTop(List<Particle> particles) {
        particles.forEach(p -> {
            double superposition = p.getRadius() - (topRightLimit.getPosition().getY() - p.getPosition().getY());
            if (superposition > 0)
                p.addToForce(
                        getWallForce(superposition, p.getVelocity(), TopNormalVector)
                );
        });
    }

    private static final Pair<Double> LeftNormalVector = new Pair<>(-1.0, 0.0);

    private void updateForceLeftWall(List<Particle> particles) {
        particles.forEach(p -> {
            double superposition = p.getRadius() - (p.getPosition().getX() - bottomLeftLimit.getPosition().getX());
            if (superposition > 0)
                p.addToForce(
                        getWallForce(superposition, p.getVelocity(), LeftNormalVector)
                );
        });
    }

    private static final Pair<Double> RightNormalVector = new Pair<>(1.0, 0.0);

    private void updateForceRightWall(List<Particle> particles) {
        particles.forEach(p -> {
            double superposition = p.getRadius() - (topRightLimit.getPosition().getX() - p.getPosition().getX());
            if (superposition > 0)
                p.addToForce(
                        getWallForce(superposition, p.getVelocity(), RightNormalVector)
                );
        });
    }

    private List<Particle> getNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        if (row < rows - 1)
            particles.addAll(cells[row + 1][col].getParticles());

        if (row < rows - 1 && col < cols - 1)
            particles.addAll(cells[row + 1][col + 1].getParticles());

        if (col < cols - 1)
            particles.addAll(cells[row][col + 1].getParticles());

        if (row > 0 && col < cols - 1)
            particles.addAll(cells[row - 1][col + 1].getParticles());


        return particles;
    }

    public void update(){

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                for (int k = 0; k < cells[i][j].getParticles().size(); k++) {
                    updateParticleCell(cells[i][j].getParticles().get(k), i, j);
                }
            }

        }

        updateForces();
    }


    private Cell getCell(double x, double y) {
        if (x >= DIM_X || x < 0 || y < 0 || y >= DIM_Y)
            throw new IllegalStateException();
        int row = getIndexY(y);
        int col = getIndexX(x);
        return cells[row][col];
    }

    private int getIndexX(double value) {
        return (int) (value / CELL_DIMENSION_X);
    }

    private int getIndexY(double value) {
        return (int) (value / CELL_DIMENSION_Y);
    }

    private void moveFromCell(Particle particle, int row, int col, int newRow, int newCol) {
        try {
            cells[newRow][newCol].add(particle);
            cells[row][col].remove(particle);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException();
        }
    }

    private void updateParticleCell(Particle particle, int row, int col) {
        double inferiorLimitY = row * CELL_DIMENSION_Y;
        double superiorLimitY = (row + 1) * CELL_DIMENSION_Y;
        double inferiorLimitX = col * CELL_DIMENSION_X;
        double superiorLimitX = (col + 1) * CELL_DIMENSION_X;

        double X = particle.getPosition().getX();
        double Y = particle.getPosition().getY();

        if (X >= superiorLimitX) {
            if (Y >= superiorLimitY) { // se va para arriba a la derecha
                moveFromCell(particle, row, col, row + 1, col + 1);
            } else if (Y < inferiorLimitY) { // se va para abajo a la derecha
                moveFromCell(particle, row, col, row - 1, col + 1);
            } else { // se va para la derecha
                moveFromCell(particle, row, col, row, col + 1);
            }
        } else if (X < inferiorLimitX) {
            if (Y >= superiorLimitY) { // se va para arriba a la izq
                moveFromCell(particle, row, col, row + 1, col - 1);
            } else if (Y < inferiorLimitY) { // se va para abajo a la izq
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
}
