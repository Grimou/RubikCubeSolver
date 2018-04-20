import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class RubikCubeSolver {

    public static final int difference = 20;

    public static void main(String[] args) {
        Model model = new Model("RubikCubeSolver");
        IntVar a = model.intVar("plusPetit", 0, 100);
        IntVar b = model.intVar("plusGrand", 0, 100);


        model.arithm(b, "-", a, ">=", difference).post();




        Solver solver = model.getSolver();
        solver.findSolution();
        solver.printStatistics();

        System.out.println("a : " + a.getValue());
        System.out.println("b : " + b.getValue());
    }
}
