import java.util.List;
import java.util.Map;

public class RubikCubeMovement {
    public static final int NB_FACE = 6;

    public static Map<Position, List<PossibleMovement>> possibleMovement;

    public RubikCubeMovement(int cubeSize) {
        initPossibleMouvement(cubeSize);
    }

    private static void initPossibleMouvement(int cubeSize) {
        for (int a = 0; a < NB_FACE; a++) {
            for (int i = 0; i < cubeSize; i++) {
                for (int j = 0; j < cubeSize; j++) {

                }
            }
        }
    }

    public static List<PossibleMovement> possibleMouvement(int cubeSize) {
        //TODO
        return null;
    }

}