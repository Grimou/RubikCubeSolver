import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class RubikCubeSolver {

    public static final int difference = 20;

    public enum Color {BLANC,BLEU,VERT,JAUNE,ROUGE,ORANGE};

    public static void main(String[] args) {
        Model model = new Model("RubikCubeSolver");




        Solver solver = model.getSolver();
        solver.findSolution();
        solver.printStatistics();
    }
}
