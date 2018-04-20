import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RubikCubeMovement {
    private static final int NB_FACE = 6;

    public Map<Position, List<PossibleMovement>> possibleMovementMap;
    private int cubeSize;

    public RubikCubeMovement(int cubeSize) {
        this.cubeSize = cubeSize;
        this.possibleMovementMap = new HashMap<>();
        initPossibleMovement();
    }

    public List<PossibleMovement> possibleMovement(int face, int line, int column) {
        return possibleMovementMap.get(new Position(face, line, column));
    }

    private void initPossibleMovement() {
        for (int a = 0; a < NB_FACE; a++) {
            for (int line = 0; line < cubeSize; line++) {
                for (int column = 0; column < cubeSize; column++) {
                    List<PossibleMovement> possibleMovements = new ArrayList<>();
                    CubeFace face = CubeFace.values()[a];

                    //iterating over 6/9 possible movements
                    for (RotationAxis axis : Stream.of(face.lineAxis, face.columnAxis)
                            .collect(Collectors.toList())) {

                        //iterating over possible number of quarter of turn
                        for (int k = 0; k < 3; k++) {
                            Movement movement = new Movement(axis, k, face, line, column);
                            possibleMovements.add(new PossibleMovement(movement.encode(), movement.moveFrom(face, line, column)));
                        }
                    }

                    //iterating over last 3/9 possible movements (parallel axis) if applicable i.e. not the middle of a face
                    if (line != ((double)cubeSize-1)/2 || column != ((double)cubeSize-1)/2) {
                        //iterating over possible number of quarter of turn
                        for (int k = 0; k < 3; k++) {
                            int movementLine = 0;
                            if (!face.parallelAxisLineIsOrigin) {
                                movementLine = cubeSize-1;
                            }
                            Movement movement = new Movement(face.parallelAxis, k, movementLine);
                            List<Integer> newCoordinates = circularRotation(line, column, k, face.clockwise);
                            possibleMovements.add(new PossibleMovement(movement.encode(), new Position(face.ordinal(), newCoordinates.get(0), newCoordinates.get(1))));
                        }
                    }
                    possibleMovementMap.put(new Position(face.ordinal(), line, column), possibleMovements);
                }
            }
        }
    }

    private CubeFace rotateFace(CubeFace face, RotationAxis axis, int numberOfRotation) {
        int newFaceIndex = (axis.rotation.indexOf(face.ordinal()) + numberOfRotation+1) % axis.rotation.size();
        return CubeFace.values()[axis.rotation.get(newFaceIndex)];
    }

    private List<Integer> circularRotation(int x, int y, int nbOfTurn, boolean clockwise) {
        if (clockwise) {
            return counterClockwiseCircularRotation(x, y, 2 - nbOfTurn);
        } else {
            return counterClockwiseCircularRotation(x, y, nbOfTurn);
        }
    }

    private List<Integer> counterClockwiseCircularRotation(int x, int y, int nbOfTurn) {
        double circleCenter = ((double) cubeSize -1)/2;
        double rotationAsRadiant = (nbOfTurn+1)*Math.PI/2;

        int rotatedX = (int) (cos(rotationAsRadiant)*(x-circleCenter) - sin(rotationAsRadiant)*(y-circleCenter)+circleCenter);

        int rotatedY = (int) (sin(rotationAsRadiant)*(x-circleCenter) + cos(rotationAsRadiant)*(y-circleCenter)+circleCenter);

        return Arrays.asList(rotatedX, rotatedY);
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
                if (face.lineAxis == ZAxis) {
                    if (face.lineAxisRotation == 1) {
                        //face #3
                        line = j;
                    } else {
                        //face #1
                        line = cubeSize - j - 1;
                    }
                } else {
                    if (face.columnAxisRotation == 1) {
                        //face #4
                        line = cubeSize - i - 1;
                    } else {
                        //face #5
                        line = i;
                    }
                }
            }
            return line;
        }
    }

    /**
     * represent the face of a cube in relation to the rotation axis instead of the number
     */
    private enum CubeFace {
        ZERO(RotationAxis.YAxis, 1, RotationAxis.XAxis, 1, RotationAxis.ZAxis, false, false),
        ONE(RotationAxis.ZAxis, -1, RotationAxis.XAxis, 1, RotationAxis.YAxis, false, false),
        TWO(RotationAxis.YAxis, -1, RotationAxis.XAxis, 1, RotationAxis.ZAxis, true, true),
        THREE(RotationAxis.ZAxis, 1, RotationAxis.XAxis, 1, RotationAxis.YAxis, true, true),
        FOUR(RotationAxis.YAxis, 1, RotationAxis.ZAxis, 1, RotationAxis.XAxis, true, false),
        FIVE(RotationAxis.YAxis, 1, RotationAxis.ZAxis, -1, RotationAxis.XAxis, false, true);

        private RotationAxis lineAxis;
        private int lineAxisRotation;
        private RotationAxis columnAxis;
        private int columnAxisRotation;
        private RotationAxis parallelAxis;
        private boolean clockwise;
        private boolean parallelAxisLineIsOrigin;



        CubeFace(RotationAxis lineAxis, int lineAxisRotation, RotationAxis columnAxis, int columnAxisRotation, RotationAxis parallelAxis, boolean clockwise, boolean parallelAxisLineIsOrigin) {
            this.lineAxis = lineAxis;
            this.lineAxisRotation = lineAxisRotation;
            this.columnAxis = columnAxis;
            this.columnAxisRotation = columnAxisRotation;
            this.parallelAxis = parallelAxis;
            this.clockwise = clockwise;
            this.parallelAxisLineIsOrigin = parallelAxisLineIsOrigin;
        }
    }

    private class Movement {
        private RotationAxis axis;
        //range from 0 to 2, so actual number of turn is nbOfTurn+1
        private int nbOfTurn;
        private int line;

        private Movement(RotationAxis axis, int nbOfTurn, int line) {
            this.axis = axis;
            this.nbOfTurn = nbOfTurn;
            this.line = line;
        }

        private Movement(RotationAxis axis, int nbOfTurn, CubeFace face, int i, int j) {
            this.axis = axis;
            this.nbOfTurn = nbOfTurn;
            this.line = axis.linePassingThrough(face, i, j, cubeSize);
        }

        private int encode() {
            return encode(this.axis, this.nbOfTurn, this.line);
        }

        private int encode(RotationAxis axis, int nbOfTurn, int line) {
            return axis.ordinal()*3*cubeSize + nbOfTurn*cubeSize + line;
        }

        private Position moveFrom(CubeFace face, int i, int j) {
            CubeFace newFace = rotateFace(face, this.axis, this.nbOfTurn);

            Position newPosition;
            if (this.axis == RotationAxis.XAxis) {
                //Xaxis, coordinate do not change
                newPosition = new Position(newFace.ordinal(), i, j);
            } else if (this.axis == RotationAxis.YAxis) {
                //Yaxis coordinates change only on face #2
                if (face == CubeFace.TWO || newFace == CubeFace.TWO) {
                    newPosition = new Position(newFace.ordinal(), cubeSize - i - 1, cubeSize - j - 1);
                } else {
                    newPosition = new Position(newFace.ordinal(), i, j);
                }
            } else {
                //ZAxis, coordinates always change in a counter-clockwise rotation
                List<Integer> newCoordinates = circularRotation(i, j, nbOfTurn, false);
                newPosition = new Position(newFace.ordinal(), newCoordinates.get(0), newCoordinates.get(1));
            }

            return newPosition;
        }
    }

}
