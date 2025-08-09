
package menuutama;

import java.text.DecimalFormat;

/**
 *
 * @author Neubri
 */
public class KriteriaAhp {
    /**
     * n banyak kriteria
     */
    protected int nBanyak = 4;
    /**
     * Random Index Consistency (RI)
     */
    protected double RI[] = { 0.0, 0.0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59 };

    /**
     * Sebuah Matriks berpasangan Kriteria
     */
    double[][] matriksBerpasangan = new double[nBanyak][nBanyak];
    double[] jumlahMatriksBerpasangan = new double[nBanyak];

    /**
     * Matriks Normalisasi Kriteria
     */
    double[][] matriksNormalisasi = new double[nBanyak][nBanyak];
    double[] jumlahMatriksNormalisasi = new double[nBanyak];
    double[] prioritas = new double[nBanyak];

    /**
     * Matriks Penjumlahan Kriteria
     */
    double[][] matriksPenjumlahan = new double[nBanyak][nBanyak];
    double[] jumlahMatriksPenjumlahan = new double[nBanyak];

    /**
     * cek konsistensi rasio kriteria
     */
    double[] jumlahCekKonsistensi = new double[nBanyak];

    /**
     * Format desimal
     */
    DecimalFormat df = new DecimalFormat("#.##");

    /**
     * Menentukan IR dari nBanyak kriteria/sub
     */
    double IR;

    // Dapatkan Nilai IR
    double getIR() {
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

    // set Nilai based on the paper - MATRIKS PERBANDINGAN BERPASANGAN
    public void setNilaiKriteria() {
        // nilai dari matriks berapasangan kriteria sesuai paper
        // K1=HARGA, K2=MESIN/CC, K3=IRIT BENSIN, K4=DESAIN
        // Expected column totals: HARGA=1.676, MESIN/CC=4.533, IRIT BENSIN=9.333, DESAIN=16.000
        double matriks[][] = {
                { 1.0, 3.0, 5.0, 7.0 },           // HARGA
                { 0.333, 1.0, 3.0, 5.0 },         // MESIN/CC (1/3 = 0.333)
                { 0.200, 0.333, 1.0, 3.0 },       // IRIT BENSIN (1/5 = 0.200, 1/3 = 0.333)
                { 0.143, 0.200, 0.333, 1.0 }      // DESAIN (1/7 = 0.143, 1/5 = 0.200, 1/3 = 0.333)
        };
        for (int row = 0; row < nBanyak; row++) {
            for (int col = 0; col < nBanyak; col++) {
                matriksBerpasangan[row][col] = matriks[row][col];
            }
        }
    }

    // Membuat Matriks Kriteria Normalisasi Metode AHP
    public void MatriksBerpasangan() {
        // masukkan nilai matriks berpasangan
        setNilaiKriteria();
        // Jumlah PerKolom Pada Matriks berpasangan
        for (int row = 0; row < nBanyak; row++) {
            for (int col = 0; col < nBanyak; col++) {
                jumlahMatriksBerpasangan[col] += matriksBerpasangan[row][col];
            }
        }
    }

    // membuat matriks normalisasi kriteria
    public void MatriksNormalisasi() {
        // perhitungan nilai dari matriks normalisasi kriteria
        // Jumlah setiap baris dan nilai prioritas
        for (int row = 0; row < nBanyak; row++) {
            for (int col = 0; col < nBanyak; col++) {
                matriksNormalisasi[row][col] = matriksBerpasangan[row][col] / jumlahMatriksBerpasangan[col];
                jumlahMatriksNormalisasi[row] += matriksNormalisasi[row][col];
                prioritas[row] = jumlahMatriksNormalisasi[row] / nBanyak;
            }
        }
    }

    // membuat matriks penjumlahan setiap baris untuk kriteria
    public void MatriksPenjumlahan() {
        for (int row = 0; row < nBanyak; row++) {
            for (int col = 0; col < nBanyak; col++) {
                matriksPenjumlahan[row][col] = matriksBerpasangan[row][col] * prioritas[col];
                jumlahMatriksPenjumlahan[row] += matriksPenjumlahan[row][col];
            }
        }
    }

    // cek konsistensi rasio kriteria
    public String getCekKonsistensi() {
        double totalJumlah = 0;
        for (int row = 0; row < nBanyak; row++) {
            jumlahCekKonsistensi[row] = jumlahMatriksPenjumlahan[row] + prioritas[row];
            totalJumlah += jumlahCekKonsistensi[row];
        }
        double ir = getIR();
        double lamdaMaks = totalJumlah / nBanyak;
        double CI = (lamdaMaks - nBanyak) / (nBanyak - 1);
        double CR = CI / ir;
        if (CR <= 0.1) {
            return "Konsisten";
        } else {
            return "Tidak Konsisten";
        }
    }

    public KriteriaAhp() {
        MatriksBerpasangan();
        MatriksNormalisasi();
        MatriksPenjumlahan();
    }

    // Method to perform full criteria calculation
    public void hitungKriteria() {
        MatriksBerpasangan();
        MatriksNormalisasi();
        MatriksPenjumlahan();
    }
}
//