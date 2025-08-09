package menuutama;

/**
 * Simple test to validate AHP calculations match the paper exactly
 */
public class SimpleAHPTest {
    public static void main(String[] args) {
        System.out.println("=== TESTING AHP IMPLEMENTATION AGAINST PAPER ===\n");
        
        AHPCalculation ahp = new AHPCalculation();
        
        // Test 1: Check matrix values are correctly set
        testMatrixValues(ahp);
        
        // Test 2: Show detailed normalization process
        ahp.printDetailedNormalization();
        
        // Test 3: Validate against paper
        ahp.validateAgainstPaper();
        
        // Test 4: Calculate example motors
        ahp.calculateExampleMotors();
        
        // Test 5: Show step-by-step calculation for one motor
        demonstrateStepByStep(ahp);
    }
    
    private static void testMatrixValues(AHPCalculation ahp) {
        System.out.println("1. TESTING MATRIX VALUES:");
        
        double[][] criteriaMatrix = ahp.getCriteriaMatrix();
        System.out.println("  Criteria Matrix:");
        String[] labels = {"HARGA", "MESIN/CC", "IRIT", "DESAIN"};
        
        for (int i = 0; i < 4; i++) {
            System.out.printf("  %s: ", labels[i]);
            for (int j = 0; j < 4; j++) {
                System.out.printf("%.3f ", criteriaMatrix[i][j]);
            }
            System.out.println();
        }
        
        // Check column totals
        System.out.println("\n  Column Totals:");
        double[] expectedTotals = {1.676, 4.533, 9.333, 16.000};
        for (int j = 0; j < 4; j++) {
            double sum = 0;
            for (int i = 0; i < 4; i++) {
                sum += criteriaMatrix[i][j];
            }
            System.out.printf("  %s: %.3f (expected: %.3f) %s%n", 
                labels[j], sum, expectedTotals[j], 
                Math.abs(sum - expectedTotals[j]) < 0.01 ? "✓" : "✗");
        }
        System.out.println();
    }
    
    private static void demonstrateStepByStep(AHPCalculation ahp) {
        System.out.println("\n4. STEP-BY-STEP CALCULATION FOR YAMAHA GEAR 125:");
        System.out.println("  Categories: EKONOMIS, Kecil, Irit, Sporty/Agresif");
        
        double[] criteriaWeights = ahp.getCriteriaWeights();
        double[][] subWeights = ahp.getSubcriteriaWeights();
        
        System.out.println("  Formula: Final Score = Σ(Subcriteria Weight × Criteria Weight)");
        System.out.println();
        
        double total = 0;
        String[] criteriaNames = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        String[] subNames = {"EKONOMIS", "Kecil", "Irit", "Sporty"};
        
        for (int i = 0; i < 4; i++) {
            double contribution = subWeights[i][0] * criteriaWeights[i];
            total += contribution;
            System.out.printf("  %s (%s): %.3f × %.3f = %.3f%n", 
                criteriaNames[i], subNames[i], subWeights[i][0], criteriaWeights[i], contribution);
        }
        
        System.out.printf("  Total Score: %.3f%n", total);
        
        // Verify with direct calculation
        double directScore = ahp.calculateFinalScore(0, 0, 0, 0);
        System.out.printf("  Direct calculation: %.3f %s%n", directScore, 
            Math.abs(total - directScore) < 0.001 ? "✓" : "✗");
    }
}
