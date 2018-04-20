import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;
import java.util.Map;

public class RubikCubeSolver {
    public static final int NB_FACE = 6;

    public static Map<Position, List<PossibleMovement>> possibleMovement;

    public static void main(String[] args) {
        Model model = new Model("RubikCubeSolver");




        Solver solver = model.getSolver();
        solver.findSolution();
        solver.printStatistics();

    }


    public static void initPossibleMouvement(int cubeSize) {
        for (int a = 0; a < NB_FACE; a++) {

        }
    }

    public static List<PossibleMovement> possibleMouvement(int cubeSize) {
        //TODO
        return null;
    }



}
