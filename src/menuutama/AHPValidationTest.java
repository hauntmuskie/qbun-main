package menuutama;

/**
 * Test class to validate AHP calculations against the research paper
 * 
 * This class demonstrates and validates that our implementation matches
 * the exact calculations shown in the research paper.
 */
public class AHPValidationTest {
    
    public static void main(String[] args) {
        System.out.println("=== AHP VALIDATION TEST ===");
        System.out.println("Testing calculations against research paper values...\n");
        
        // Create AHP calculation instance
        AHPCalculation ahp = new AHPCalculation();
        
        // Test 1: Validate criteria weights
        testCriteriaWeights(ahp);
        
        // Test 2: Validate subcriteria weights  
        testSubcriteriaWeights(ahp);
        
        // Test 3: Test consistency ratios
        testConsistencyRatios(ahp);
        
        // Test 4: Calculate example motors from paper
        testExampleMotors(ahp);
        
        System.out.println("\n=== VALIDATION COMPLETE ===");
    }
    
    private static void testCriteriaWeights(AHPCalculation ahp) {
        System.out.println("1. TESTING CRITERIA WEIGHTS");
        System.out.println("Expected from paper: K1=0.558, K2=0.263, K3=0.122, K4=0.057");
        
        double[] weights = ahp.getCriteriaWeights();
        double[] expected = {0.558, 0.263, 0.122, 0.057};
        String[] names = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        
        boolean allMatch = true;
        for (int i = 0; i < 4; i++) {
            double diff = Math.abs(weights[i] - expected[i]);
            boolean matches = diff < 0.01; // Allow small tolerance
            System.out.printf("  %s: Calculated=%.3f, Expected=%.3f, Diff=%.3f %s%n", 
                names[i], weights[i], expected[i], diff, matches ? "✓" : "✗");
            if (!matches) allMatch = false;
        }
        
        System.out.println("  Result: " + (allMatch ? "PASSED" : "FAILED") + "\n");
    }
    
    private static void testSubcriteriaWeights(AHPCalculation ahp) {
        System.out.println("2. TESTING SUBCRITERIA WEIGHTS");
        
        double[][] weights = ahp.getSubcriteriaWeights();
        double[][] expected = {
            {0.633, 0.260, 0.106}, // Harga: EKONOMIS, MENENGAH, PREMIUM
            {0.429, 0.429, 0.143}, // CC: Kecil, Sedang, Besar  
            {0.643, 0.283, 0.074}, // Irit: Irit, Sedang, Boros
            {0.633, 0.260, 0.106}  // Desain: Sporty, Retro, Futuristik
        };
        
        String[] criteriaNames = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        String[][] subNames = {
            {"EKONOMIS", "MENENGAH", "PREMIUM"},
            {"Kecil", "Sedang", "Besar"},
            {"Irit", "Sedang", "Boros"}, 
            {"Sporty", "Retro", "Futuristik"}
        };
        
        boolean allMatch = true;
        for (int i = 0; i < 4; i++) {
            System.out.println("  " + criteriaNames[i] + ":");
            for (int j = 0; j < 3; j++) {
                double diff = Math.abs(weights[i][j] - expected[i][j]);
                boolean matches = diff < 0.01;
                System.out.printf("    %s: Calculated=%.3f, Expected=%.3f, Diff=%.3f %s%n",
                    subNames[i][j], weights[i][j], expected[i][j], diff, matches ? "✓" : "✗");
                if (!matches) allMatch = false;
            }
        }
        
        System.out.println("  Result: " + (allMatch ? "PASSED" : "FAILED") + "\n");
    }
    
    private static void testConsistencyRatios(AHPCalculation ahp) {
        System.out.println("3. TESTING CONSISTENCY RATIOS");
        System.out.println("All CR values should be < 0.1 for consistency");
        
        double criteriaCR = ahp.calculateCriteriaConsistencyRatio();
        System.out.printf("  Criteria CR: %.3f %s%n", criteriaCR, criteriaCR < 0.1 ? "✓" : "✗");
        
        String[] subNames = {"Harga", "CC", "Irit Bensin", "Desain"};
        boolean allConsistent = criteriaCR < 0.1;
        
        for (int i = 0; i < 4; i++) {
            double subCR = ahp.calculateSubcriteriaConsistencyRatio(i);
            boolean consistent = subCR < 0.1;
            System.out.printf("  %s CR: %.3f %s%n", subNames[i], subCR, consistent ? "✓" : "✗");
            if (!consistent) allConsistent = false;
        }
        
        System.out.println("  Result: " + (allConsistent ? "PASSED - All matrices are consistent" : "FAILED - Some matrices are inconsistent") + "\n");
    }
    
    private static void testExampleMotors(AHPCalculation ahp) {
        System.out.println("4. TESTING EXAMPLE MOTOR CALCULATIONS");
        
        // Motor examples from paper with their characteristics
        String[] motorNames = {
            "Yamaha Gear 125",
            "Yamaha Fazzio Hybrid", 
            "Yamaha XMAX Connected",
            "Honda Vario 160 CBS",
            "Honda PCX 160 CBS",
            "Honda Scoopy"
        };
        
        // Motor categories [price, cc, fuel, design] based on paper data
        int[][] motorCategories = {
            {0, 0, 0, 0}, // Yamaha Gear 125: EKONOMIS, Kecil, Irit, Sporty
            {0, 0, 1, 1}, // Yamaha Fazzio Hybrid: EKONOMIS, Kecil, Sedang, Retro
            {2, 2, 2, 2}, // Yamaha XMAX Connected: PREMIUM, Besar, Boros, Futuristik
            {1, 1, 1, 0}, // Honda Vario 160 CBS: MENENGAH, Sedang, Sedang, Sporty
            {1, 1, 1, 2}, // Honda PCX 160 CBS: MENENGAH, Sedang, Sedang, Futuristik
            {0, 0, 0, 1}  // Honda Scoopy: EKONOMIS, Kecil, Irit, Retro
        };
        
        String[][] categoryNames = {
            {"EKONOMIS", "MENENGAH", "PREMIUM"},
            {"Kecil", "Sedang", "Besar"},
            {"Irit", "Sedang", "Boros"},
            {"Sporty", "Retro", "Futuristik"}
        };
        
        double maxScore = 0;
        String bestMotor = "";
        
        for (int i = 0; i < motorNames.length; i++) {
            double score = ahp.calculateFinalScore(
                motorCategories[i][0], 
                motorCategories[i][1], 
                motorCategories[i][2], 
                motorCategories[i][3]
            );
            
            System.out.printf("  %s: %.3f", motorNames[i], score);
            System.out.printf(" [%s, %s, %s, %s]%n",
                categoryNames[0][motorCategories[i][0]],
                categoryNames[1][motorCategories[i][1]], 
                categoryNames[2][motorCategories[i][2]],
                categoryNames[3][motorCategories[i][3]]
            );
            
            if (score > maxScore) {
                maxScore = score;
                bestMotor = motorNames[i];
            }
        }
        
        System.out.println("  Best Motor: " + bestMotor + " with score: " + String.format("%.3f", maxScore));
        System.out.println("  Result: CALCULATED - Ranking completed based on AHP weights\n");
    }
    
    /**
     * Utility method to demonstrate the AHP calculation formula step by step
     */
    public static void demonstrateCalculationFormula() {
        System.out.println("=== AHP CALCULATION FORMULA DEMONSTRATION ===");
        
        AHPCalculation ahp = new AHPCalculation();
        
        System.out.println("Formula: Final Score = Σ(Subcriteria Weight × Criteria Weight)");
        System.out.println("\nStep-by-step calculation for Yamaha Gear 125:");
        System.out.println("Categories: EKONOMIS, Kecil, Irit, Sporty/Agresif");
        
        double[] criteriaWeights = ahp.getCriteriaWeights();
        double[][] subWeights = ahp.getSubcriteriaWeights();
        
        System.out.printf("\n1. HARGA (EKONOMIS): %.3f × %.3f = %.3f%n", 
            subWeights[0][0], criteriaWeights[0], subWeights[0][0] * criteriaWeights[0]);
        System.out.printf("2. MESIN/CC (Kecil): %.3f × %.3f = %.3f%n", 
            subWeights[1][0], criteriaWeights[1], subWeights[1][0] * criteriaWeights[1]);
        System.out.printf("3. IRIT BENSIN (Irit): %.3f × %.3f = %.3f%n", 
            subWeights[2][0], criteriaWeights[2], subWeights[2][0] * criteriaWeights[2]);
        System.out.printf("4. DESAIN (Sporty): %.3f × %.3f = %.3f%n", 
            subWeights[3][0], criteriaWeights[3], subWeights[3][0] * criteriaWeights[3]);
        
        double total = ahp.calculateFinalScore(0, 0, 0, 0);
        System.out.printf("\nTotal Score: %.3f%n", total);
    }
}
