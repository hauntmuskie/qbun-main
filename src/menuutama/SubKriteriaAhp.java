
package menuutama;

import java.text.DecimalFormat;

/**
 *
 * @author Neubri
 */
public class SubKriteriaAhp {
    /**
     * n banyak kriteria
     */
    protected static int nBanyak4x4 = 4, nBanyak3x3 = 3;
    /**
     * Random Index Consistency (RI)
     */
    protected static double RI[] = { 0.0, 0.0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57,
            1.59 };

    /**
     * Sebuah Matriks berpasangan
     */

    static double[][] matriksBerpasangan4x4 = new double[nBanyak4x4][nBanyak4x4];
    static double[] jumlahMatriksBerpasangan4x4 = new double[nBanyak4x4];
    static double[][] matriksBerpasangan3x3 = new double[nBanyak3x3][nBanyak3x3];
    static double[] jumlahMatriksBerpasangan3x3 = new double[nBanyak3x3];

    /**
     * Matriks Normalisasi Kriteria
     */
    static double[][] matriksNormalisasi4x4 = new double[nBanyak4x4][nBanyak4x4];
    static double[] jumlahMatriksNormalisasi4x4 = new double[nBanyak4x4];
    static double[] prioritas4x4 = new double[nBanyak4x4];
    double[] prioritasSub4x4 = new double[nBanyak4x4];
    static double[][] matriksNormalisasi3x3 = new double[nBanyak3x3][nBanyak3x3];
    static double[] jumlahMatriksNormalisasi3x3 = new double[nBanyak3x3];
    static double[] prioritas3x3 = new double[nBanyak3x3];
    double[] prioritasSub3x3 = new double[nBanyak3x3];

    /**
     * Matriks Penjumlahan Kriteria
     */
    static double[][] matriksPenjumlahan4x4 = new double[nBanyak4x4][nBanyak4x4];
    static double[] jumlahMatriksPenjumlahan4x4 = new double[nBanyak4x4];
    static double[][] matriksPenjumlahan3x3 = new double[nBanyak3x3][nBanyak3x3];
    static double[] jumlahMatriksPenjumlahan3x3 = new double[nBanyak3x3];

    /**
     * cek konsistensi rasio kriteria
     */
    static double[] jumlahCekKonsistensi4x4 = new double[nBanyak4x4];
    static double[] jumlahCekKonsistensi3x3 = new double[nBanyak3x3];

    /**
     * Format desimal
     */
    static DecimalFormat df = new DecimalFormat("#.##");

    /**
     * Menentukan IR dari nBanyak4x4 kriteria/sub
     */
    static double IR;

    // Dapatkan Nilai IR
    static double getIR(int nBanyak) {
        if (nBanyak == 1 || nBanyak == 2) {
            IR = RI[0];
        } else if (nBanyak == 3) {
            IR = RI[2];
        } else if (nBanyak == 4) {
            IR = RI[3];
        } else if (nBanyak == 5) {
            IR = RI[4];
        } else if (nBanyak == 6) {
            IR = RI[5];
        } else if (nBanyak == 7) {
            IR = RI[6];
        } else if (nBanyak == 8) {
            IR = RI[7];
        } else if (nBanyak == 9) {
            IR = RI[8];
        } else if (nBanyak == 10) {
            IR = RI[9];
        }
        return IR;
    }

    // set Nilai
    public static void setNilaiKriteria4x4() {
        // nilai dari matriks berapasangan kriteria sesuai paper
        // Expected column totals: HARGA=1.676, MESIN/CC=4.533, IRIT BENSIN=9.333, DESAIN=16.000
        double matriks[][] = {
                { 1.0, 3.0, 5.0, 7.0 },           // HARGA
                { 0.333, 1.0, 3.0, 5.0 },         // MESIN/CC (1/3 = 0.333)
                { 0.200, 0.333, 1.0, 3.0 },       // IRIT BENSIN (1/5 = 0.200, 1/3 = 0.333)
                { 0.143, 0.200, 0.333, 1.0 }      // DESAIN (1/7 = 0.143, 1/5 = 0.200, 1/3 = 0.333)
        };
        for (int row = 0; row < nBanyak4x4; row++) {
            for (int col = 0; col < nBanyak4x4; col++) {
                matriksBerpasangan4x4[row][col] = matriks[row][col];
            }
        }
    }

    // Membuat Matriks Kriteria Normalisasi Metode AHP
    public static void MatriksBerpasangan4x4() {
        // masukkan nilai matriks berpasangan
        setNilaiKriteria4x4();
        // Jumlah PerKolom Pada Matriks berpasangan
        for (int row = 0; row < nBanyak4x4; row++) {
            for (int col = 0; col < nBanyak4x4; col++) {
                jumlahMatriksBerpasangan4x4[col] += matriksBerpasangan4x4[row][col];
            }
        }
    }

    // membuat matriks normalisasi kriteria
    public void MatriksNormalisasi4x4() {
        // perhitungan nilai dari matriks normalisasi kriteria
        // Jumlah setiap baris dan nilai prioritas
        for (int row = 0; row < nBanyak4x4; row++) {
            for (int col = 0; col < nBanyak4x4; col++) {
                matriksNormalisasi4x4[row][col] = matriksBerpasangan4x4[row][col] / jumlahMatriksBerpasangan4x4[col];
                jumlahMatriksNormalisasi4x4[row] += matriksNormalisasi4x4[row][col];
                prioritas4x4[row] = jumlahMatriksNormalisasi4x4[row] / nBanyak4x4;
            }
        }

        // mencari nilai terbesar atau maks dari prioritas untuk prioritas SubKriteria
        double maxNum = prioritas4x4[0];
        for (double j : prioritas4x4) {
            if (j > maxNum)
                maxNum = j;
        }
        for (int i = 0; i < nBanyak4x4; i++) {
            // Perhitungan Prioritas SubKriteria
            this.prioritasSub4x4[i] = prioritas4x4[i] / maxNum;
        }
    }

    // membuat matriks penjumlahan setiap baris untuk kriteria
    public static void MatriksPenjumlahan4x4() {
        for (int row = 0; row < nBanyak4x4; row++) {
            for (int col = 0; col < nBanyak4x4; col++) {
                matriksPenjumlahan4x4[row][col] = matriksBerpasangan4x4[row][col] * prioritas4x4[col];
                jumlahMatriksPenjumlahan4x4[row] += matriksPenjumlahan4x4[row][col];
            }
        }
    }

    // cek konsistensi rasio kriteria
    public static String getCekKonsistensi4x4() {
        double totalJumlah = 0;
        for (int row = 0; row < nBanyak4x4; row++) {
            jumlahCekKonsistensi4x4[row] = jumlahMatriksPenjumlahan4x4[row] + prioritas4x4[row];
            totalJumlah += jumlahCekKonsistensi4x4[row];
        }
        double ir = getIR(nBanyak4x4);
        double lamdaMaks = totalJumlah / nBanyak4x4;
        double CI = (lamdaMaks - nBanyak4x4) / (nBanyak4x4 - 1);
        double CR = CI / ir;
        if (CR <= 0.1) {
            return "Konsisten";
        } else {
            return "Tidak Konsisten";
        }
    }

    // set Nilai - using values from the research paper for all subcriteria
    public static void setNilaiKriteria3x3() {
        // Default matrix based on paper pattern (HARGA subcriteria as default)
        // Expected column totals: 1.533, 4.333, 9.000
        double matriks[][] = {
                { 1.0, 3.0, 5.0 },        // First subcriteria (highest priority)
                { 0.333, 1.0, 3.0 },      // Second subcriteria (medium priority) (1/3 = 0.333)
                { 0.200, 0.333, 1.0 },    // Third subcriteria (lowest priority) (1/5 = 0.200, 1/3 = 0.333)
        };
        for (int row = 0; row < nBanyak3x3; row++) {
            for (int col = 0; col < nBanyak3x3; col++) {
                matriksBerpasangan3x3[row][col] = matriks[row][col];
            }
        }
    }
    
    // MATRIKS PERBANDINGAN BERPASANGAN SUBKRITERIA HARGA
    // EKONOMIS, MENENGAH, PREMIUM
    // Expected column totals: EKONOMIS=1.533, MENENGAH=4.333, PREMIUM=9.000
    public static void setNilaiSubkriteriaHarga() {
        double matriks[][] = {
                { 1.0, 3.0, 5.0 },      // EKONOMIS
                { 0.333, 1.0, 3.0 },    // MENENGAH (1/3 = 0.333)
                { 0.200, 0.333, 1.0 }   // PREMIUM (1/5 = 0.200, 1/3 = 0.333)
        };
        for (int row = 0; row < nBanyak3x3; row++) {
            for (int col = 0; col < nBanyak3x3; col++) {
                matriksBerpasangan3x3[row][col] = matriks[row][col];
            }
        }
    }
    
    // MATRIKS PERBANDINGAN BERPASANGAN SUBKRITERIA CC (Kapasitas Mesin)
    // Kecil (Entry), Sedang (Mid-range), Besar (Premium)
    // Expected column totals: Kecil=2.333, Sedang=2.333, Besar=7.000
    public static void setNilaiSubkriteriaCC() {
        double matriks[][] = {
                { 1.0, 1.0, 3.0 },      // Kecil (Entry)
                { 1.0, 1.0, 3.0 },      // Sedang (Mid-range)
                { 0.333, 0.333, 1.0 }   // Besar (Premium) (1/3 = 0.333)
        };
        for (int row = 0; row < nBanyak3x3; row++) {
            for (int col = 0; col < nBanyak3x3; col++) {
                matriksBerpasangan3x3[row][col] = matriks[row][col];
            }
        }
    }
    
    // MATRIKS PERBANDINGAN BERPASANGAN SUBKRITERIA IRIT BENSIN
    // Irit, Sedang, Boros
    // Expected column totals: Irit=1.476, Sedang=4.200, Boros=13.000
    public static void setNilaiSubkriteriaIrit() {
        double matriks[][] = {
                { 1.0, 3.0, 7.0 },      // Irit
                { 0.333, 1.0, 5.0 },    // Sedang (1/3 = 0.333)
                { 0.143, 0.200, 1.0 }   // Boros (1/7 = 0.143, 1/5 = 0.200)
        };
        for (int row = 0; row < nBanyak3x3; row++) {
            for (int col = 0; col < nBanyak3x3; col++) {
                matriksBerpasangan3x3[row][col] = matriks[row][col];
            }
        }
    }
    
    // MATRIKS PERBANDINGAN BERPASANGAN SUBKRITERIA DESAIN
    // Sporty/Agresif, Retro/Stylish, Futuristik/Modern
    // Expected column totals: Sporty=1.533, Retro=4.333, Futuristik=9.000
    public static void setNilaiSubkriteriaDesain() {
        double matriks[][] = {
                { 1.0, 3.0, 5.0 },      // Sporty/Agresif
                { 0.333, 1.0, 3.0 },    // Retro/Stylish (1/3 = 0.333)
                { 0.200, 0.333, 1.0 }   // Futuristik/Modern (1/5 = 0.200, 1/3 = 0.333)
        };
        for (int row = 0; row < nBanyak3x3; row++) {
            for (int col = 0; col < nBanyak3x3; col++) {
                matriksBerpasangan3x3[row][col] = matriks[row][col];
            }
        }
    }

    // Membuat Matriks Kriteria Normalisasi Metode AHP
    public static void MatriksBerpasangan3x3() {
        // masukkan nilai matriks berpasangan
        setNilaiKriteria3x3();
        // Jumlah PerKolom Pada Matriks berpasangan
        for (int row = 0; row < nBanyak3x3; row++) {
            for (int col = 0; col < nBanyak3x3; col++) {
                jumlahMatriksBerpasangan3x3[col] += matriksBerpasangan3x3[row][col];
            }
        }
    }

    // membuat matriks normalisasi kriteria
    public void MatriksNormalisasi3x3() {
        // perhitungan nilai dari matriks normalisasi kriteria
        // Jumlah setiap baris dan nilai prioritas
        for (int row = 0; row < nBanyak3x3; row++) {
            for (int col = 0; col < nBanyak3x3; col++) {
                matriksNormalisasi3x3[row][col] = matriksBerpasangan3x3[row][col] / jumlahMatriksBerpasangan3x3[col];
                jumlahMatriksNormalisasi3x3[row] += matriksNormalisasi3x3[row][col];
                prioritas3x3[row] = jumlahMatriksNormalisasi3x3[row] / nBanyak3x3;
            }
        }

        // mencari nilai terbesar atau maks dari prioritas untuk prioritas SubKriteria
        double maxNum = prioritas3x3[0];
        for (double j : prioritas3x3) {
            if (j > maxNum)
                maxNum = j;
        }
        for (int i = 0; i < nBanyak3x3; i++) {
            // Perhitungan Prioritas SubKriteria
            this.prioritasSub3x3[i] = prioritas3x3[i] / maxNum;
        }
    }

    // membuat matriks penjumlahan setiap baris untuk kriteria
    public static void MatriksPenjumlahan3x3() {
        for (int row = 0; row < nBanyak3x3; row++) {
            for (int col = 0; col < nBanyak3x3; col++) {
                matriksPenjumlahan3x3[row][col] = matriksBerpasangan3x3[row][col] * prioritas3x3[col];
                jumlahMatriksPenjumlahan3x3[row] += matriksPenjumlahan3x3[row][col];
            }
        }

    }

    // cek konsistensi rasio kriteria
    public static String getCekKonsistensi3x3() {
        double totalJumlah = 0;
        for (int row = 0; row < nBanyak3x3; row++) {
            jumlahCekKonsistensi3x3[row] = jumlahMatriksPenjumlahan3x3[row] + prioritas3x3[row];
            totalJumlah += jumlahCekKonsistensi3x3[row];
        }
        double ir = getIR(nBanyak3x3);
        double lamdaMaks = totalJumlah / nBanyak3x3;
        double CI = (lamdaMaks - nBanyak3x3) / (nBanyak3x3 - 1);
        double CR = CI / ir;
        if (CR <= 0.1) {
            return "Konsisten";
        } else {
            return "Tidak Konsisten";
        }
    }

    public SubKriteriaAhp() {
        MatriksBerpasangan4x4();
        this.MatriksNormalisasi4x4();
        MatriksPenjumlahan4x4();
        MatriksBerpasangan3x3();
        this.MatriksNormalisasi3x3();
        MatriksPenjumlahan3x3();
    }

    // Method to perform full 3x3 subcriteria calculation
    public void hitungSubKriteria3x3() {
        MatriksBerpasangan3x3();
        this.MatriksNormalisasi3x3();
        MatriksPenjumlahan3x3();
    }
}
