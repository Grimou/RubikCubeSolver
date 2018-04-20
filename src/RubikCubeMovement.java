import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RubikCubeMovement {
    public static final int NB_FACE = 6;

    public Map<Position, List<PossibleMovement>> possibleMovement;
    private int cubeSize;

    public RubikCubeMovement(int cubeSize) {
        this.cubeSize = cubeSize;
        this.possibleMovement = new HashMap<>();
    }

    public List<PossibleMovement> possibleMouvement(int face, int line, int column) {
        return possibleMovement.get(new Position(face, line, column));
    }

    private void initPossibleMovement() {
        for (int a = 0; a < NB_FACE; a++) {
            for (int line = 0; line < cubeSize; line++) {
                for (int column = 0; column < cubeSize; column++) {
                    List<PossibleMovement> possibleMovements = new ArrayList<>();
                    CubeFace face = CubeFace.values()[a];

                    for (RotationAxis axis : Stream.of(face.lineAxis, face.columnAxis)
                            .collect(Collectors.toList())) {

                    }

                }
            }
        }
    }

    private boolean tileMoving(int m, int a, int i, int j) {
        Movement movement = new Movement(m);
        CubeFace face = CubeFace.values()[a];
        if (face.lineAxis == movement.axis) {
            if (face.lineAxisRotation == 1) {
                if (movement.getLine() == i) {
                    return true;
                }
            } else {

            }
        }
        if (face.columnAxis == movement.axis) {
            return true;
        }
        return false;
    }



    private int rotate(int face, RotationAxis axis, int numberOfRotation) {
        return axis.rotation.get(axis.rotation.indexOf(face)+numberOfRotation);
    }

    private Position circularRotation(int i, int j, int nbOfRotation) {
        return null;
    }

    private enum RotationAxis {
        XAxis(0, 1, 2, 3),
        YAxis(0, 4, 2, 5),
        ZAxis(3, 4, 1, 5);

        private List<Integer> rotation;

        RotationAxis(int first, int second, int third, int fourth) {
            rotation = Arrays.asList(first, second, third, fourth);
        }

        public int linePassingThrough(CubeFace face, int i, int j, int cubeSize) {
            int line = 0;
            if (this == XAxis) {
                //we are sure it's a columnAxis
                line = i;
            }
            if (this == YAxis) {
                //we are sure it's a lineAxis
                if (face.columnAxisRotation == 1) {
                    line = j;
                } else {
                    line = cubeSize - j - 1;
                }
            }
            if (this == ZAxis) {
            }
            return line;
        }
    }

    private enum CubeFace {
        ZERO(RotationAxis.YAxis, 1, RotationAxis.XAxis, 1),
        ONE(RotationAxis.ZAxis, -1, RotationAxis.XAxis, 1),
        TWO(RotationAxis.YAxis, -1, RotationAxis.XAxis, 1),
        THREE(RotationAxis.ZAxis, 1, RotationAxis.XAxis, 1),
        FOUR(RotationAxis.YAxis, 1, RotationAxis.ZAxis, 1),
        FIVE(RotationAxis.YAxis, 1, RotationAxis.ZAxis, -1);

        private RotationAxis lineAxis;
        private int lineAxisRotation;
        private RotationAxis columnAxis;
        private int columnAxisRotation;

        CubeFace(RotationAxis lineAxis, int lineAxisRotation, RotationAxis columnAxis, int columnAxisRotation) {
            this.lineAxis = lineAxis;
            this.lineAxisRotation = lineAxisRotation;
            this.columnAxis = columnAxis;
            this.columnAxisRotation = columnAxisRotation;
        }
    }

    private class Movement {
        private RotationAxis axis;
        private int nbOfTurn;
        private int line;

        public Movement(RotationAxis axis, int nbOfTurn, int line) {
            this.axis = axis;
            this.nbOfTurn = nbOfTurn;
            this.line = line;
        }

        public Movement(RotationAxis axis, int nbOfTurn, CubeFace face, int i, int j) {
            this.axis = axis;
            this.nbOfTurn = nbOfTurn;
            this.line = axis.linePassingThrough(face, i, j, cubeSize);
        }

        public Movement(int encodedMovement) {
            this.axis = RotationAxis.values()[(encodedMovement)/(3*cubeSize)];
            int remainder = encodedMovement%(3*cubeSize);
            this.nbOfTurn = remainder/cubeSize;
            this.line = remainder%cubeSize;

        }

        public RotationAxis getAxis() {
            return axis;
        }

        public void setAxis(RotationAxis axis) {
            this.axis = axis;
        }

        public int getNbOfTurn() {
            return nbOfTurn;
        }

        public void setNbOfTurn(int nbOfTurn) {
            this.nbOfTurn = nbOfTurn;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public int encode() {
            return encode(this.axis, this.nbOfTurn, this.line);
        }

        public int encode(RotationAxis axis, int nbOfTurn, int line) {
            return axis.ordinal()*3*cubeSize + nbOfTurn*cubeSize + line;
        }

    }

}
