package menuutama;

import java.text.DecimalFormat;

/**
 * AHP Calculation class implementing the exact formulas and matrices from the research paper
 * This class handles all AHP calculations for motor selection criteria
 * 
 * @author Updated based on research paper
 */
public class AHPCalculation {
    
    private DecimalFormat df = new DecimalFormat("#.###");
    
    // Random Index Consistency (RI) values
    private static final double[] RI = {0.0, 0.0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49};
    
    // Main Criteria (4x4 matrix)
    private double[][] criteriaMatrix = new double[4][4];
    private double[][] criteriaNormalized = new double[4][4];
    private double[] criteriaWeights = new double[4];
    
    // Subcriteria matrices (3x3 each)
    private double[][][] subcriteriaMatrices = new double[4][3][3];
    private double[][][] subcriteriaNormalized = new double[4][3][3];
    private double[][] subcriteriaWeights = new double[4][3];
    
    public AHPCalculation() {
        initializeMatrices();
        calculateAll();
    }
    
    /**
     * Initialize all matrices based on the EXACT research paper data
     */
    private void initializeMatrices() {
        // MATRIKS PERBANDINGAN BERPASANGAN KRITERIA (from paper)
        // Expected totals: HARGA=1.676, MESIN/CC=4.533, IRIT BENSIN=9.333, DESAIN=16.000
        criteriaMatrix = new double[][]{
            {1.0,     3.0,     5.0,     7.0},        // HARGA
            {0.333,   1.0,     3.0,     5.0},        // MESIN/CC 
            {0.200,   0.333,   1.0,     3.0},        // IRIT BENSIN 
            {0.143,   0.200,   0.333,   1.0}         // DESAIN 
        };
        
        // 1. MATRIKS PERBANDINGAN BERPASANGAN SUBKRITERIA HARGA (from paper)
        // Expected totals: EKONOMIS=1.533, MENENGAH=4.333, PREMIUM=9.000
        subcriteriaMatrices[0] = new double[][]{
            {1.0,     3.0,     5.0},         // EKONOMIS
            {0.333,   1.0,     3.0},         // MENENGAH 
            {0.200,   0.333,   1.0}          // PREMIUM 
        };
        
        // 2. MATRIKS PERBANDINGAN BERPASANGAN SUBKRITERIA CC (from paper)
        // Expected totals: Kecil=2.333, Sedang=2.333, Besar=7.000
        subcriteriaMatrices[1] = new double[][]{
            {1.0,     1.0,     3.0},         // Kecil (Entry)
            {1.0,     1.0,     3.0},         // Sedang (Mid-range)
            {0.333,   0.333,   1.0}          // Besar (Premium) 
        };
        
        // 3. MATRIKS PERBANDINGAN BERPASANGAN SUBKRITERIA IRIT BENSIN (from paper)
        // Expected totals: Irit=1.476, Sedang=4.200, Boros=13.000
        subcriteriaMatrices[2] = new double[][]{
            {1.0,     3.0,     7.0},         // Irit
            {0.333,   1.0,     5.0},         // Sedang 
            {0.143,   0.200,   1.0}          // Boros 
        };
        
        // 4. MATRIKS PERBANDINGAN BERPASANGAN SUBKRITERIA DESAIN (from paper)
        // Expected totals: Sporty=1.533, Retro=4.333, Futuristik=9.000
        subcriteriaMatrices[3] = new double[][]{
            {1.0,     3.0,     5.0},         // Sporty/Agresif
            {0.333,   1.0,     3.0},         // Retro/Stylish 
            {0.200,   0.333,   1.0}          // Futuristik/Modern 
        };
    }
    
    /**
     * Calculate all AHP matrices and weights
     */
    public void calculateAll() {
        calculateCriteriaWeights();
        calculateSubcriteriaWeights();
    }
    
    /**
     * Calculate main criteria weights (4x4 matrix)
     */
    private void calculateCriteriaWeights() {
        // Calculate column sums
        double[] columnSums = new double[4];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                columnSums[j] += criteriaMatrix[i][j];
            }
        }
        
        // Normalize matrix and calculate weights
        for (int i = 0; i < 4; i++) {
            double rowSum = 0;
            for (int j = 0; j < 4; j++) {
                criteriaNormalized[i][j] = criteriaMatrix[i][j] / columnSums[j];
                rowSum += criteriaNormalized[i][j];
            }
            criteriaWeights[i] = rowSum / 4; // Average of row
        }
    }
    
    /**
     * Calculate subcriteria weights for all 4 criteria (3x3 matrices each)
     */
    private void calculateSubcriteriaWeights() {
        for (int criteria = 0; criteria < 4; criteria++) {
            // Calculate column sums for current subcriteria matrix
            double[] columnSums = new double[3];
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    columnSums[j] += subcriteriaMatrices[criteria][i][j];
                }
            }
            
            // Normalize matrix and calculate weights
            for (int i = 0; i < 3; i++) {
                double rowSum = 0;
                for (int j = 0; j < 3; j++) {
                    subcriteriaNormalized[criteria][i][j] = subcriteriaMatrices[criteria][i][j] / columnSums[j];
                    rowSum += subcriteriaNormalized[criteria][i][j];
                }
                subcriteriaWeights[criteria][i] = rowSum / 3; // Average of row
            }
        }
    }
    
    /**
     * Calculate final score for a motor alternative
     * @param priceCategory 0=EKONOMIS, 1=MENENGAH, 2=PREMIUM
     * @param ccCategory 0=Kecil, 1=Sedang, 2=Besar
     * @param fuelCategory 0=Irit, 1=Sedang, 2=Boros
     * @param designCategory 0=Sporty/Agresif, 1=Retro/Stylish, 2=Futuristik/Modern
     * @return Final weighted score
     */
    public double calculateFinalScore(int priceCategory, int ccCategory, int fuelCategory, int designCategory) {
        double finalScore = 0.0;
        
        // Price contribution: subcriteria weight * criteria weight
        finalScore += subcriteriaWeights[0][priceCategory] * criteriaWeights[0];
        
        // CC contribution
        finalScore += subcriteriaWeights[1][ccCategory] * criteriaWeights[1];
        
        // Fuel efficiency contribution  
        finalScore += subcriteriaWeights[2][fuelCategory] * criteriaWeights[2];
        
        // Design contribution
        finalScore += subcriteriaWeights[3][designCategory] * criteriaWeights[3];
        
        return finalScore;
    }
    
    /**
     * Calculate consistency ratio for main criteria
     */
    public double calculateCriteriaConsistencyRatio() {
        return calculateConsistencyRatio(criteriaMatrix, criteriaWeights, 4);
    }
    
    /**
     * Calculate consistency ratio for subcriteria
     */
    public double calculateSubcriteriaConsistencyRatio(int criteriaIndex) {
        return calculateConsistencyRatio(subcriteriaMatrices[criteriaIndex], subcriteriaWeights[criteriaIndex], 3);
    }
    
    /**
     * Generic method to calculate consistency ratio
     */
    private double calculateConsistencyRatio(double[][] matrix, double[] weights, int n) {
        // Calculate λmax
        double[] weightedSum = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                weightedSum[i] += matrix[i][j] * weights[j];
            }
        }
        
        double lambdaMax = 0;
        for (int i = 0; i < n; i++) {
            lambdaMax += weightedSum[i] / weights[i];
        }
        lambdaMax /= n;
        
        // Calculate CI and CR
        double CI = (lambdaMax - n) / (n - 1);
        double CR = CI / RI[n - 1];
        
        return CR;
    }
    
    // Getter methods for displaying results
    public double[][] getCriteriaMatrix() { return criteriaMatrix; }
    public double[][] getCriteriaNormalized() { return criteriaNormalized; }
    public double[] getCriteriaWeights() { return criteriaWeights; }
    public double[][] getSubcriteriaWeights() { return subcriteriaWeights; }
    public double[][][] getSubcriteriaMatrices() { return subcriteriaMatrices; }
    public double[][][] getSubcriteriaNormalized() { return subcriteriaNormalized; }
    
    /**
     * Get formatted criteria weights as per paper
     * Returns: {0.558, 0.263, 0.122, 0.057} approximately
     */
    public String[] getFormattedCriteriaWeights() {
        String[] formatted = new String[4];
        for (int i = 0; i < 4; i++) {
            formatted[i] = df.format(criteriaWeights[i]);
        }
        return formatted;
    }
    
    /**
     * Get formatted subcriteria weights
     */
    public String[][] getFormattedSubcriteriaWeights() {
        String[][] formatted = new String[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                formatted[i][j] = df.format(subcriteriaWeights[i][j]);
            }
        }
        return formatted;
    }
    
    /**
     * Print detailed normalization process as shown in the paper
     */
    public void printDetailedNormalization() {
        System.out.println("=== DETAILED NORMALIZATION PROCESS ===");
        
        // 1. Show criteria matrix normalization
        System.out.println("\n1. CRITERIA MATRIX NORMALIZATION:");
        String[] criteriaNames = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        
        // Calculate column sums
        double[] columnSums = new double[4];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                columnSums[j] += criteriaMatrix[i][j];
            }
            System.out.printf("  %s column total: %.3f%n", criteriaNames[j], columnSums[j]);
        }
        
        System.out.println("\n  Normalized Matrix:");
        for (int i = 0; i < 4; i++) {
            System.out.printf("  %s: ", criteriaNames[i]);
            double rowSum = 0;
            for (int j = 0; j < 4; j++) {
                double normalized = criteriaMatrix[i][j] / columnSums[j];
                System.out.printf("%.3f ", normalized);
                rowSum += normalized;
            }
            double weight = rowSum / 4;
            System.out.printf("| Sum=%.3f, Weight=%.3f%n", rowSum, weight);
        }
        
        // 2. Show subcriteria normalization for each criterion
        String[] subcriteriaNames = {"Harga", "CC", "Irit Bensin", "Desain"};
        String[][] subNames = {
            {"EKONOMIS", "MENENGAH", "PREMIUM"},
            {"Kecil", "Sedang", "Besar"},
            {"Irit", "Sedang", "Boros"}, 
            {"Sporty", "Retro", "Futuristik"}
        };
        
        for (int criteria = 0; criteria < 4; criteria++) {
            System.out.println("\n" + (criteria + 2) + ". " + subcriteriaNames[criteria].toUpperCase() + " SUBCRITERIA NORMALIZATION:");
            
            // Calculate column sums
            double[] subColSums = new double[3];
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    subColSums[j] += subcriteriaMatrices[criteria][i][j];
                }
                System.out.printf("  %s column total: %.3f%n", subNames[criteria][j], subColSums[j]);
            }
            
            System.out.println("  Normalized Matrix:");
            for (int i = 0; i < 3; i++) {
                System.out.printf("  %s: ", subNames[criteria][i]);
                double rowSum = 0;
                for (int j = 0; j < 3; j++) {
                    double normalized = subcriteriaMatrices[criteria][i][j] / subColSums[j];
                    System.out.printf("%.3f ", normalized);
                    rowSum += normalized;
                }
                double weight = rowSum / 3;
                System.out.printf("| Sum=%.3f, Weight=%.3f%n", rowSum, weight);
            }
        }
    }
    public void printResults() {
        System.out.println("=== AHP CALCULATION RESULTS ===");
        
        System.out.println("\nCriteria Weights:");
        System.out.println("HARGA: " + df.format(criteriaWeights[0]));
        System.out.println("MESIN/CC: " + df.format(criteriaWeights[1]));
        System.out.println("IRIT BENSIN: " + df.format(criteriaWeights[2]));
        System.out.println("DESAIN: " + df.format(criteriaWeights[3]));
        
        String[] subcriteriaNames = {
            "Harga: EKONOMIS, MENENGAH, PREMIUM",
            "CC: Kecil, Sedang, Besar", 
            "Irit: Irit, Sedang, Boros",
            "Desain: Sporty, Retro, Futuristik"
        };
        
        System.out.println("\nSubcriteria Weights:");
        for (int i = 0; i < 4; i++) {
            System.out.println(subcriteriaNames[i]);
            for (int j = 0; j < 3; j++) {
                System.out.println("  " + j + ": " + df.format(subcriteriaWeights[i][j]));
            }
        }
        
        System.out.println("\nConsistency Ratios:");
        System.out.println("Criteria CR: " + df.format(calculateCriteriaConsistencyRatio()));
        for (int i = 0; i < 4; i++) {
            System.out.println("Subcriteria " + i + " CR: " + df.format(calculateSubcriteriaConsistencyRatio(i)));
        }
    }
    
    /**
     * Test method to validate calculations against EXACT paper values
     */
    public void validateAgainstPaper() {
        System.out.println("=== VALIDATION AGAINST EXACT PAPER VALUES ===");
        
        // EXACT values from paper
        double[] expectedCriteriaWeights = {0.558, 0.263, 0.122, 0.057};
        double[][] expectedSubWeights = {
            {0.633, 0.260, 0.106}, // Harga: EKONOMIS, MENENGAH, PREMIUM
            {0.429, 0.429, 0.143}, // CC: Kecil, Sedang, Besar
            {0.643, 0.283, 0.074}, // Irit: Irit, Sedang, Boros
            {0.633, 0.260, 0.106}  // Desain: Sporty, Retro, Futuristik
        };
        
        // Expected column totals from paper
        double[] expectedCriteriaTotals = {1.676, 4.533, 9.333, 16.000};
        double[][] expectedSubTotals = {
            {1.533, 4.333, 9.000}, // Harga
            {2.333, 2.333, 7.000}, // CC  
            {1.476, 4.200, 13.000}, // Irit
            {1.533, 4.333, 9.000}  // Desain
        };
        
        System.out.println("\n1. CRITERIA MATRIX VALIDATION:");
        // Calculate actual column sums
        double[] actualTotals = new double[4];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                actualTotals[j] += criteriaMatrix[i][j];
            }
        }
        
        String[] criteriaNames = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        for (int i = 0; i < 4; i++) {
            System.out.printf("  %s - Column Total: Expected=%.3f, Actual=%.3f%n", 
                criteriaNames[i], expectedCriteriaTotals[i], actualTotals[i]);
        }
        
        System.out.println("\n2. CRITERIA WEIGHTS VALIDATION:");
        for (int i = 0; i < 4; i++) {
            double diff = Math.abs(criteriaWeights[i] - expectedCriteriaWeights[i]);
            boolean matches = diff < 0.005; // Tighter tolerance
            System.out.printf("  K%d %s: Expected=%.3f, Calculated=%.3f, Diff=%.3f %s%n", 
                (i+1), criteriaNames[i], expectedCriteriaWeights[i], criteriaWeights[i], diff, 
                matches ? "✓" : "✗");
        }
        
        System.out.println("\n3. SUBCRITERIA MATRIX VALIDATION:");
        String[] subcriteriaNames = {"Harga", "CC", "Irit Bensin", "Desain"};
        String[][] subNames = {
            {"EKONOMIS", "MENENGAH", "PREMIUM"},
            {"Kecil", "Sedang", "Besar"},
            {"Irit", "Sedang", "Boros"}, 
            {"Sporty", "Retro", "Futuristik"}
        };
        
        for (int criteria = 0; criteria < 4; criteria++) {
            System.out.println("  " + subcriteriaNames[criteria] + " Matrix:");
            
            // Calculate column sums
            double[] colSums = new double[3];
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    colSums[j] += subcriteriaMatrices[criteria][i][j];
                }
            }
            
            // Check column totals
            for (int j = 0; j < 3; j++) {
                System.out.printf("    %s column total: Expected=%.3f, Actual=%.3f%n",
                    subNames[criteria][j], expectedSubTotals[criteria][j], colSums[j]);
            }
            
            // Check weights
            for (int j = 0; j < 3; j++) {
                double diff = Math.abs(subcriteriaWeights[criteria][j] - expectedSubWeights[criteria][j]);
                boolean matches = diff < 0.005;
                System.out.printf("    %s weight: Expected=%.3f, Calculated=%.3f, Diff=%.3f %s%n",
                    subNames[criteria][j], expectedSubWeights[criteria][j], 
                    subcriteriaWeights[criteria][j], diff, matches ? "✓" : "✗");
            }
        }
    }
    
    /**
     * Example calculation for the motors in the database matching the paper
     */
    public void calculateExampleMotors() {
        System.out.println("\n=== MOTOR CALCULATIONS BASED ON DATABASE ===");
        
        // Motor examples from database matching the paper
        String[] motorNames = {
            "Yamaha Gear 125",
            "Yamaha Fazzio Hybrid", 
            "Yamaha XMAX Connected",
            "Honda Vario 160 CBS",
            "Honda PCX 160 CBS",
            "Honda Scoopy"
        };
        
        // Motor categories based on corrected database data [price, cc, fuel, design]
        // Database categories:
        // Price: EKONOMIS ≤ 22 juta (0), MENENGAH 22-35 juta (1), PREMIUM > 35 juta (2)
        // CC: Kecil (Entry) 110-125 cc (0), Sedang (Mid-range) 150-160 cc (1), Besar (Premium) > 160 cc (2)  
        // Fuel: Irit ≥ 50 km/l (0), Sedang 40-49 km/l (1), Boros < 40 km/l (2)
        // Design: Sporty/Agresif (0), Retro/Stylish (1), Futuristik/Modern (2)
        int[][] motorCategories = {
            {0, 0, 0, 0}, // Yamaha Gear 125: EKONOMIS, Kecil, Irit, Sporty
            {0, 0, 0, 1}, // Yamaha Fazzio Hybrid: EKONOMIS, Kecil, Irit, Retro (corrected)
            {2, 2, 1, 2}, // Yamaha XMAX Connected: PREMIUM, Besar, Sedang, Futuristik
            {1, 1, 0, 0}, // Honda Vario 160 CBS: MENENGAH, Sedang, Irit, Sporty
            {1, 1, 0, 2}, // Honda PCX 160 CBS: MENENGAH, Sedang, Irit, Futuristik
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
        
        System.out.println("Motor Ranking (Formula: Σ(Subcriteria Weight × Criteria Weight)):");
        
        for (int i = 0; i < motorNames.length; i++) {
            double score = calculateFinalScore(
                motorCategories[i][0], 
                motorCategories[i][1], 
                motorCategories[i][2], 
                motorCategories[i][3]
            );
            
            System.out.printf("  %d. %s: %.3f", (i+1), motorNames[i], score);
            System.out.printf(" [%s, %s, %s, %s]%n",
                categoryNames[0][motorCategories[i][0]],
                categoryNames[1][motorCategories[i][1]], 
                categoryNames[2][motorCategories[i][2]],
                categoryNames[3][motorCategories[i][3]]
            );
            
            // Show detailed calculation
            double[] weights = getCriteriaWeights();
            double[][] subWeights = getSubcriteriaWeights();
            System.out.printf("    Detail: (%.3f×%.3f) + (%.3f×%.3f) + (%.3f×%.3f) + (%.3f×%.3f)%n",
                subWeights[0][motorCategories[i][0]], weights[0],
                subWeights[1][motorCategories[i][1]], weights[1], 
                subWeights[2][motorCategories[i][2]], weights[2],
                subWeights[3][motorCategories[i][3]], weights[3]
            );
            
            if (score > maxScore) {
                maxScore = score;
                bestMotor = motorNames[i];
            }
        }
        
        System.out.println("\n  🏆 RECOMMENDED MOTOR: " + bestMotor + " with score: " + String.format("%.3f", maxScore));
    }
}
