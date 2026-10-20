
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.scholarship.algorithm.CombinationOptimizer;

public class CombinationOptimizerManualTest {
    public static void main(String[] args) {
        CombinationOptimizer optimizer = new CombinationOptimizer();

        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        Map<String, String> compatibility = new HashMap<>();
        compatibility.put("1-2", "COMPATIBLE");
        compatibility.put("1-3", "COMPATIBLE");
        compatibility.put("2-3", "COMPATIBLE");

        Map<String, Double> benefits = new HashMap<>();
        benefits.put("1", 20000.0);
        benefits.put("2", 30000.0);
        benefits.put("3", 50000.0);

        List<Long> best =
                optimizer.optimize(ids, compatibility, benefits);

        double total = 0;
        for (Long id : best) {
            total += benefits.get(String.valueOf(id));
        }

        System.out.println("Best combination: " + best);
        System.out.println("Total benefit: Rs. " + total);

        if (best.equals(Arrays.asList(1L, 2L, 3L))
        && total == 100000.0) {
            System.out.println("PASS: Combination optimizer works!");
        } else {
            System.out.println("FAIL: Check the optimizer logic.");
        }
    }
}
