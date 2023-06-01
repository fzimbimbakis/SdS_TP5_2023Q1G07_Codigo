package models;

import models.particle.Limit;
import models.particle.Particle;
import utils.ForcesUtils;

import java.util.ArrayList;
import java.util.List;

import static utils.ForcesUtils.*;

public class Grid {

    private static final double DIM_X = 20.0;
    private static final double DIM_Y = 77.0; // se tiene en cuenta el espacio fuera de la "caja"
    private static final int cols = 8;
    private static final int rowsInside = 30;
    private static final int rowsTotal = 33;
    private static final double CELL_DIMENSION_Y = DIM_Y / (double) rowsTotal;
    private static final double CELL_DIMENSION_X = DIM_X / (double) cols;
    private final Limit topRightLimit;
    private final Limit bottomLeftLimit;
    private final double leftLimitHole;
    private final double rightLimitHole;

    private final Cell[][] cells;

    public Grid(Limit topRightLimit, Limit bottomLeftLimit, double holeSize) {
        cells = new Cell[rowsTotal][cols];
        for (int row = 0; row < rowsTotal; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell();
            }
        }
        this.bottomLeftLimit = bottomLeftLimit;
        this.topRightLimit = topRightLimit;
        leftLimitHole = topRightLimit.getPosition().getX() / 2 - holeSize / 2;
        rightLimitHole = topRightLimit.getPosition().getX() / 2 + holeSize / 2;
    }

    public void add(Particle particle) {
        Cell cell = getCell(particle.getPosition().getX(), particle.getPosition().getY());
        if (cell != null) {
            cell.add(particle);
        } else throw new IllegalStateException("Cell does not exists");
    }

    public void addAll(List<Particle> particles) {
        particles.forEach(this::add);
    }

    private double pairDifference(Pair<Double> a, Pair<Double> b) {
        return Math.sqrt(
                Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)
        );
    }

    public void updateForces() {
        for (int row = 0; row < rowsTotal; row++) {
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

                if (row == (rowsTotal - rowsInside)) //pared inferior con el agujero
                    updateForceFloor(current);

                if (row == rowsTotal - 1)
                    updateForceTop(current);

                if (col == 0)
                    updateForceLeftWall(current);

                if (col == cols - 1)
                    updateForceRightWall(current);

            }
        }
    }

    private static final Pair<Double> FloorNormalVersor = new Pair<>(0.0, -1.0);

    private void updateForceFloor(List<Particle> particles) {
        particles.forEach(p -> {
            if (outsideHole(p)) { //si pasa por el agujero, no choca con la pared
                double superposition = p.getRadius() - (p.getPosition().getY() - bottomLeftLimit.getPosition().getY());
                if (superposition > 0)
                    p.addToForce(
                            getWallForce(superposition, p.getVelocity(), FloorNormalVersor)
                    );
            }
        });
    }

    private boolean outsideHole(Particle particle){
        return particle.getPosition().getX() < leftLimitHole || particle.getPosition().getX() > rightLimitHole;
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

        if (row < rowsTotal - 1)
            particles.addAll(cells[row + 1][col].getParticles());

        if (row < rowsTotal - 1 && col < cols - 1)
            particles.addAll(cells[row + 1][col + 1].getParticles());

        if (col < cols - 1)
            particles.addAll(cells[row][col + 1].getParticles());

        if (row > 0 && col < cols - 1)
            particles.addAll(cells[row - 1][col + 1].getParticles());


        return particles;
    }

    public List<Particle> update() {

        List<Particle> particleList = new ArrayList<>();
        Particle aux;

        for (int i = 0; i < rowsTotal; i++) {
            for (int j = 0; j < cols; j++) {

                for (int k = 0; k < cells[i][j].getParticles().size(); k++) {
                    aux = updateParticleCell(cells[i][j].getParticles().get(k), i, j);
                    if (aux != null)
                        particleList.add(aux);

                }
            }

        }
        return particleList;
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

    private Particle moveFromCell(Particle particle, int row, int col, int newRow, int newCol) {
        try {
            if (newRow < 0) {
                particle.reInject();
                cells[row][col].remove(particle);
                return particle;
            } else {
                cells[newRow][newCol].add(particle);
                cells[row][col].remove(particle);
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            //System.out.println("Particle x: " + particle.getPosition().getX());
            //System.out.println("Particle y: " + particle.getPosition().getY());
            //System.out.println("newCol: " + newCol);
            //System.out.println("newRow: " + newRow);
            throw new IllegalStateException();
        }
    }

    private Particle updateParticleCell(Particle particle, int row, int col) {
        double inferiorLimitY = row * CELL_DIMENSION_Y;
        double superiorLimitY = (row + 1) * CELL_DIMENSION_Y;
        double inferiorLimitX = col * CELL_DIMENSION_X;
        double superiorLimitX = (col + 1) * CELL_DIMENSION_X;

        double X = particle.getPosition().getX();
        double Y = particle.getPosition().getY();

        if (X >= superiorLimitX) {
            if (Y >= superiorLimitY) { // se va para arriba a la derecha
                return moveFromCell(particle, row, col, row + 1, col + 1);
            } else if (Y < inferiorLimitY) { // se va para abajo a la derecha
                return moveFromCell(particle, row, col, row - 1, col + 1);
            } else { // se va para la derecha
                return moveFromCell(particle, row, col, row, col + 1);
            }
        } else if (X < inferiorLimitX) {
            if (Y >= superiorLimitY) { // se va para arriba a la izq
                return moveFromCell(particle, row, col, row + 1, col - 1);
            } else if (Y < inferiorLimitY) { // se va para abajo a la izq
                return moveFromCell(particle, row, col, row - 1, col - 1);
            } else { // se va para la izquierda
                return moveFromCell(particle, row, col, row, col - 1);
            }
        } else {
            if (Y >= superiorLimitY) { // se va para arriba
                return moveFromCell(particle, row, col, row + 1, col);
            } else if (Y < inferiorLimitY) { // se va para abajo
                return moveFromCell(particle, row, col, row - 1, col);
            }
            return null;
        }

    }
}
