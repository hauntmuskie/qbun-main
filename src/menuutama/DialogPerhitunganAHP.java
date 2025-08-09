
package menuutama;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import koneksi.Koneksi;

/**
 *
 * @author Neubri
 */
public class DialogPerhitunganAHP extends javax.swing.JDialog {
    private Connection conn = new Koneksi().connect();
    protected KriteriaAhp kriteria = new KriteriaAhp();
    protected SubKriteriaAhp SubK = new SubKriteriaAhp();
    protected AHPCalculation ahpCalc = new AHPCalculation(); // New AHP calculator
    DecimalFormat df = new DecimalFormat("#.###");
    ArrayList<String> K = new ArrayList<String>();
    ArrayList<Double> KS4x4 = new ArrayList<Double>();
    ArrayList<Double> KS3x3 = new ArrayList<Double>();
    String noIdAlternatif, namaMotorAlternatif, merekAlternatif;
    String kategoriHargaAlternatif, kategoriCcAlternatif, kategoriIritAlternatif, kategoriDesainAlternatif;
    double nilaiAlternatif, totalNilai;

    /**
     * Creates new form DialogTambahData
     */
    public DialogPerhitunganAHP(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getRelasiId();

        // add Panel, add panel(sebuah panel)
        Pane.add(PanelPerhitungan);
        Pane.repaint();
        Pane.revalidate();
    }

    void kosong() {
        TotalNilai.setText("");
    }

    // nilai matriks berpasangan kriteria
    public void getMatriksK() {
        k1k1.setText(df.format(kriteria.matriksBerpasangan[0][0]));
        k1k2.setText(df.format(kriteria.matriksBerpasangan[0][1]));
        k1k3.setText(df.format(kriteria.matriksBerpasangan[0][2]));
        k1k4.setText(df.format(kriteria.matriksBerpasangan[0][3]));
        k2k1.setText(df.format(kriteria.matriksBerpasangan[1][0]));
        k2k2.setText(df.format(kriteria.matriksBerpasangan[1][1]));
        k2k3.setText(df.format(kriteria.matriksBerpasangan[1][2]));
        k2k4.setText(df.format(kriteria.matriksBerpasangan[1][3]));
        k3k1.setText(df.format(kriteria.matriksBerpasangan[2][0]));
        k3k2.setText(df.format(kriteria.matriksBerpasangan[2][1]));
        k3k3.setText(df.format(kriteria.matriksBerpasangan[2][2]));
        k3k4.setText(df.format(kriteria.matriksBerpasangan[2][3]));
        k4k1.setText(df.format(kriteria.matriksBerpasangan[3][0]));
        k4k2.setText(df.format(kriteria.matriksBerpasangan[3][1]));
        k4k3.setText(df.format(kriteria.matriksBerpasangan[3][2]));
        k4k4.setText(df.format(kriteria.matriksBerpasangan[3][3]));
    }

    // nilai matriks berpasangan kriteria
    public void getMatriksNorK() {
        k1k1N.setText(df.format(kriteria.matriksNormalisasi[0][0]));
        k1k2N.setText(df.format(kriteria.matriksNormalisasi[0][1]));
        k1k3N.setText(df.format(kriteria.matriksNormalisasi[0][2]));
        k1k4N.setText(df.format(kriteria.matriksNormalisasi[0][3]));
        k2k1N.setText(df.format(kriteria.matriksNormalisasi[1][0]));
        k2k2N.setText(df.format(kriteria.matriksNormalisasi[1][1]));
        k2k3N.setText(df.format(kriteria.matriksNormalisasi[1][2]));
        k2k4N.setText(df.format(kriteria.matriksNormalisasi[1][3]));
        k3k1N.setText(df.format(kriteria.matriksNormalisasi[2][0]));
        k3k2N.setText(df.format(kriteria.matriksNormalisasi[2][1]));
        k3k3N.setText(df.format(kriteria.matriksNormalisasi[2][2]));
        k3k4N.setText(df.format(kriteria.matriksNormalisasi[2][3]));
        k4k1N.setText(df.format(kriteria.matriksNormalisasi[3][0]));
        k4k2N.setText(df.format(kriteria.matriksNormalisasi[3][1]));
        k4k3N.setText(df.format(kriteria.matriksNormalisasi[3][2]));
        k4k4N.setText(df.format(kriteria.matriksNormalisasi[3][3]));
        
        // Display priorities with validation against paper values
        Prior1.setText(df.format(kriteria.prioritas[0]));
        Prior2.setText(df.format(kriteria.prioritas[1]));
        Prior3.setText(df.format(kriteria.prioritas[2]));
        Prior4.setText(df.format(kriteria.prioritas[3]));
        
        // Validate against expected paper values
        double[] expectedPriorities = {0.558, 0.263, 0.122, 0.057};
        String[] criteriaNames = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        
        System.out.println("\n=== CRITERIA PRIORITIES VALIDATION ===");
        for (int i = 0; i < 4; i++) {
            double diff = Math.abs(kriteria.prioritas[i] - expectedPriorities[i]);
            boolean matches = diff < 0.01;
            System.out.printf("K%d %s: %.3f (expected: %.3f) diff: %.3f %s%n", 
                (i+1), criteriaNames[i], kriteria.prioritas[i], expectedPriorities[i], 
                diff, matches ? "✓" : "✗");
        }

    }

    // nilai prioritas untuk sub-kriteria dari kriteria yang tersedia
    // Menggunakan nilai dari paper research yang sudah dihitung dengan AHP
    public void getPrioritasSub() {
        getKriteria();
        
        // Calculate subcriteria weights for each criterion using the new AHP calculation
        ahpCalc.calculateAll();
        double[][] subWeights = ahpCalc.getSubcriteriaWeights();
        
        // Expected values from paper for validation
        double[][] expectedSubWeights = {
            {0.633, 0.260, 0.106}, // Harga: EKONOMIS, MENENGAH, PREMIUM
            {0.429, 0.429, 0.143}, // CC: Kecil, Sedang, Besar  
            {0.643, 0.283, 0.074}, // Irit: Irit, Sedang, Boros
            {0.633, 0.260, 0.106}  // Desain: Sporty, Retro, Futuristik
        };
        
        System.out.println("\n=== SUBCRITERIA WEIGHTS CALCULATION AND DISPLAY ===");
        
        // K1 - HARGA: EKONOMIS, MENENGAH, PREMIUM (Expected: 0.633, 0.260, 0.106)
        // Use HARGA specific matrix
        SubKriteriaAhp.setNilaiSubkriteriaHarga();
        SubKriteriaAhp.MatriksBerpasangan3x3();  
        SubK.MatriksNormalisasi3x3();
        PriorS11.setText(df.format(subWeights[0][0])); // EKONOMIS
        PriorS12.setText(df.format(subWeights[0][1])); // MENENGAH  
        PriorS13.setText(df.format(subWeights[0][2])); // PREMIUM
        PriorS14.setText(""); // Not used for 3x3 matrix
        
        System.out.printf("HARGA: EKONOMIS=%.3f (exp: %.3f), MENENGAH=%.3f (exp: %.3f), PREMIUM=%.3f (exp: %.3f)%n",
            subWeights[0][0], expectedSubWeights[0][0],
            subWeights[0][1], expectedSubWeights[0][1], 
            subWeights[0][2], expectedSubWeights[0][2]);
        
        // K2 - MESIN/CC: Kecil, Sedang, Besar (Expected: 0.429, 0.429, 0.143)
        // Use CC specific matrix
        SubKriteriaAhp.setNilaiSubkriteriaCC();
        SubKriteriaAhp.MatriksBerpasangan3x3();
        SubK.MatriksNormalisasi3x3();
        PriorS21.setText(df.format(subWeights[1][0])); // Kecil (Entry)
        PriorS22.setText(df.format(subWeights[1][1])); // Sedang (Mid-range)
        PriorS23.setText(df.format(subWeights[1][2])); // Besar (Premium)
        PriorS24.setText(""); // Not used for 3x3 matrix
        
        System.out.printf("MESIN/CC: Kecil=%.3f (exp: %.3f), Sedang=%.3f (exp: %.3f), Besar=%.3f (exp: %.3f)%n",
            subWeights[1][0], expectedSubWeights[1][0],
            subWeights[1][1], expectedSubWeights[1][1], 
            subWeights[1][2], expectedSubWeights[1][2]);
        
        // K3 - IRIT BENSIN: Irit, Sedang, Boros (Expected: 0.643, 0.283, 0.074)
        // Use IRIT specific matrix
        SubKriteriaAhp.setNilaiSubkriteriaIrit();
        SubKriteriaAhp.MatriksBerpasangan3x3();
        SubK.MatriksNormalisasi3x3();
        PriorS31.setText(df.format(subWeights[2][0])); // Irit
        PriorS32.setText(df.format(subWeights[2][1])); // Sedang
        PriorS33.setText(df.format(subWeights[2][2])); // Boros
        PriorS34.setText(""); // Not used for 3x3 matrix
        
        System.out.printf("IRIT BENSIN: Irit=%.3f (exp: %.3f), Sedang=%.3f (exp: %.3f), Boros=%.3f (exp: %.3f)%n",
            subWeights[2][0], expectedSubWeights[2][0],
            subWeights[2][1], expectedSubWeights[2][1], 
            subWeights[2][2], expectedSubWeights[2][2]);
        
        // K4 - DESAIN: Sporty/Agresif, Retro/Stylish, Futuristik/Modern (Expected: 0.633, 0.260, 0.106)
        // Use DESAIN specific matrix
        SubKriteriaAhp.setNilaiSubkriteriaDesain();
        SubKriteriaAhp.MatriksBerpasangan3x3();
        SubK.MatriksNormalisasi3x3();
        PriorS41.setText(df.format(subWeights[3][0])); // Sporty/Agresif
        PriorS42.setText(df.format(subWeights[3][1])); // Retro/Stylish
        PriorS43.setText(df.format(subWeights[3][2])); // Futuristik/Modern
        PriorS44.setText(""); // Not used for 3x3 matrix
        
        System.out.printf("DESAIN: Sporty=%.3f (exp: %.3f), Retro=%.3f (exp: %.3f), Futuristik=%.3f (exp: %.3f)%n",
            subWeights[3][0], expectedSubWeights[3][0],
            subWeights[3][1], expectedSubWeights[3][1], 
            subWeights[3][2], expectedSubWeights[3][2]);
    }

    // menentukan kriteria pada kode K1, K2, K3, K4
    public void getKriteria() {
        String sql = "SELECT nama_kriteria FROM kriteria ORDER BY kd_kriteria";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String a = hasil.getString("nama_kriteria");
                K.add(a);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Mendapatkan alternatif dari data motor yang ada
    public void getAlternatif() {
        // Check if a valid motor is selected
        if (cbIdMotor.getSelectedIndex() == 0
                || cbIdMotor.getSelectedItem().toString().equals("-- Pilih Motor --")) {
            JOptionPane.showMessageDialog(null, "Silakan pilih motor terlebih dahulu!");
            return;
        }

        String sql = "SELECT DISTINCT * FROM data_motor WHERE id_motor='"
                + cbIdMotor.getSelectedItem().toString()
                + "'";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String a = hasil.getString("id_motor");
                String b = hasil.getString("nama_motor");
                String c = hasil.getString("merek");
                String d = hasil.getString("kategori_harga");
                String e = hasil.getString("kategori_cc");
                String f = hasil.getString("kategori_irit");
                String g = hasil.getString("kategori_desain");

                noIdAlternatif = a;
                namaMotorAlternatif = b;
                merekAlternatif = c;
                kategoriHargaAlternatif = d;
                kategoriCcAlternatif = e;
                kategoriIritAlternatif = f;
                kategoriDesainAlternatif = g;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // melakukan perhitungan dari alternatif yang dipilih
    // untuk mendapatkan hasil penilaian menggunakan formula dari paper
    public void getPenilaian() {
        totalNilai = 0.0; // Reset total before calculation
        getAlternatif();
        kriteria.hitungKriteria();
        
        // Use the new AHP calculation based on the paper
        ahpCalc.calculateAll();
        
        // Get criteria weights from the new calculation (should match paper values)
        double[] criteriaWeights = ahpCalc.getCriteriaWeights();
        double[][] subcriteriaWeights = ahpCalc.getSubcriteriaWeights();
        
        System.out.println("\n=== DETAILED CALCULATION FOR: " + namaMotorAlternatif + " ===");
        System.out.println("Motor Categories:");
        System.out.println("  Harga: " + kategoriHargaAlternatif);
        System.out.println("  CC: " + kategoriCcAlternatif);
        System.out.println("  Irit: " + kategoriIritAlternatif); 
        System.out.println("  Desain: " + kategoriDesainAlternatif);
        
        // Map categories to indices for calculation
        int priceIndex = getPriceIndex(kategoriHargaAlternatif);
        int ccIndex = getCCIndex(kategoriCcAlternatif);  
        int fuelIndex = getFuelIndex(kategoriIritAlternatif);
        int designIndex = getDesignIndex(kategoriDesainAlternatif);
        
        System.out.println("\nFormula: Total Score = Σ(Subcriteria Weight × Criteria Weight)");
        
        // Calculate using the paper's formula: 
        // Total Score = Σ(Subcriteria Weight × Criteria Weight)
        
        // HARGA contribution (K1)
        if (priceIndex >= 0) {
            double hargaContribution = subcriteriaWeights[0][priceIndex] * criteriaWeights[0];
            totalNilai += hargaContribution;
            System.out.printf("K1 HARGA: %.3f × %.3f = %.3f%n", 
                subcriteriaWeights[0][priceIndex], criteriaWeights[0], hargaContribution);
        }
        
        // MESIN/CC contribution (K2)  
        if (ccIndex >= 0) {
            double ccContribution = subcriteriaWeights[1][ccIndex] * criteriaWeights[1];
            totalNilai += ccContribution;
            System.out.printf("K2 MESIN/CC: %.3f × %.3f = %.3f%n", 
                subcriteriaWeights[1][ccIndex], criteriaWeights[1], ccContribution);
        }
        
        // IRIT BENSIN contribution (K3)
        if (fuelIndex >= 0) {
            double fuelContribution = subcriteriaWeights[2][fuelIndex] * criteriaWeights[2];
            totalNilai += fuelContribution;
            System.out.printf("K3 IRIT BENSIN: %.3f × %.3f = %.3f%n", 
                subcriteriaWeights[2][fuelIndex], criteriaWeights[2], fuelContribution);
        }
        
        // DESAIN contribution (K4)
        if (designIndex >= 0) {
            double designContribution = subcriteriaWeights[3][designIndex] * criteriaWeights[3];
            totalNilai += designContribution;
            System.out.printf("K4 DESAIN: %.3f × %.3f = %.3f%n", 
                subcriteriaWeights[3][designIndex], criteriaWeights[3], designContribution);
        }
        
        System.out.printf("TOTAL SCORE: %.3f%n", totalNilai);
        TotalNilai.setText(df.format(totalNilai));
        
        // Verify with direct AHP calculation
        if (priceIndex >= 0 && ccIndex >= 0 && fuelIndex >= 0 && designIndex >= 0) {
            double directScore = ahpCalc.calculateFinalScore(priceIndex, ccIndex, fuelIndex, designIndex);
            System.out.printf("Direct AHP calculation: %.3f %s%n", directScore, 
                Math.abs(totalNilai - directScore) < 0.001 ? "✓ Match" : "✗ Mismatch");
        }
    }
    
    // Helper methods to map category strings to indices
    private int getPriceIndex(String kategori) {
        if (kategori.contains("EKONOMIS")) return 0;
        if (kategori.contains("MENENGAH")) return 1;
        if (kategori.contains("PREMIUM")) return 2;
        return -1;
    }
    
    private int getCCIndex(String kategori) {
        if (kategori.contains("Kecil")) return 0;
        if (kategori.contains("Sedang")) return 1;
        if (kategori.contains("Besar")) return 2;
        return -1;
    }
    
    private int getFuelIndex(String kategori) {
        if (kategori.contains("Irit")) return 0;
        if (kategori.contains("Sedang")) return 1;
        if (kategori.contains("Boros")) return 2;
        return -1;
    }
    
    private int getDesignIndex(String kategori) {
        if (kategori.contains("Sporty/Agresif")) return 0;
        if (kategori.contains("Retro/Stylish")) return 1;
        if (kategori.contains("Futuristik/Modern")) return 2;
        return -1;
    }

    // Mendapatkan relasi pada combobox pada database data motor
    public void getRelasiId() {
        // Add default selection option
        cbIdMotor.addItem("-- Pilih Motor --");

        String sql = "SELECT DISTINCT id_motor, nama_motor FROM data_motor ORDER BY id_motor";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String a = hasil.getString("id_motor");
                cbIdMotor.addItem(a);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelPerhitungan = new javax.swing.JPanel();
        judul = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        mulaiHitung = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        k1k1 = new javax.swing.JTextField();
        k1k2 = new javax.swing.JTextField();
        k1k3 = new javax.swing.JTextField();
        k1k4 = new javax.swing.JTextField();
        k2k1 = new javax.swing.JTextField();
        k2k2 = new javax.swing.JTextField();
        k2k3 = new javax.swing.JTextField();
        k2k4 = new javax.swing.JTextField();
        k3k1 = new javax.swing.JTextField();
        k3k2 = new javax.swing.JTextField();
        k3k3 = new javax.swing.JTextField();
        k3k4 = new javax.swing.JTextField();
        k4k1 = new javax.swing.JTextField();
        k4k2 = new javax.swing.JTextField();
        k4k3 = new javax.swing.JTextField();
        k4k4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        k1k1N = new javax.swing.JTextField();
        k1k2N = new javax.swing.JTextField();
        k1k3N = new javax.swing.JTextField();
        k1k4N = new javax.swing.JTextField();
        k2k1N = new javax.swing.JTextField();
        k2k2N = new javax.swing.JTextField();
        k2k3N = new javax.swing.JTextField();
        k2k4N = new javax.swing.JTextField();
        k3k1N = new javax.swing.JTextField();
        k3k2N = new javax.swing.JTextField();
        k3k3N = new javax.swing.JTextField();
        k3k4N = new javax.swing.JTextField();
        k4k1N = new javax.swing.JTextField();
        k4k2N = new javax.swing.JTextField();
        k4k3N = new javax.swing.JTextField();
        k4k4N = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        Prior1 = new javax.swing.JTextField();
        Prior2 = new javax.swing.JTextField();
        Prior3 = new javax.swing.JTextField();
        Prior4 = new javax.swing.JTextField();
        Simpan = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        PriorS11 = new javax.swing.JTextField();
        PriorS12 = new javax.swing.JTextField();
        PriorS13 = new javax.swing.JTextField();
        PriorS14 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        PriorS21 = new javax.swing.JTextField();
        PriorS22 = new javax.swing.JTextField();
        PriorS23 = new javax.swing.JTextField();
        PriorS24 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        PriorS31 = new javax.swing.JTextField();
        PriorS32 = new javax.swing.JTextField();
        PriorS33 = new javax.swing.JTextField();
        PriorS34 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        PriorS41 = new javax.swing.JTextField();
        PriorS42 = new javax.swing.JTextField();
        PriorS43 = new javax.swing.JTextField();
        PriorS44 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        Motor = new javax.swing.JTextField();
        TotalNilai = new javax.swing.JTextField();
        cbIdMotor = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        Pane = new javax.swing.JPanel();

        PanelPerhitungan.setBackground(new java.awt.Color(204, 204, 204));

        judul.setBackground(new java.awt.Color(255, 0, 0));
        judul.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        judul.setForeground(new java.awt.Color(255, 255, 255));
        judul.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        judul.setText("Hitung Hasil Penilaian Motor ");
        judul.setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        mulaiHitung.setBackground(new java.awt.Color(0, 0, 51));
        mulaiHitung.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        mulaiHitung.setForeground(new java.awt.Color(255, 255, 255));
        mulaiHitung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image (9).png"))); // NOI18N
        mulaiHitung.setText("HITUNG");
        mulaiHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mulaiHitungActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Matriks Perbandingan Kriteria");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("K1");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("K2");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("K3");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("K4");

        k1k1.setEditable(false);
        k1k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k2.setEditable(false);
        k1k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k3.setEditable(false);
        k1k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k4.setEditable(false);
        k1k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k1.setEditable(false);
        k2k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k2.setEditable(false);
        k2k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k3.setEditable(false);
        k2k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k4.setEditable(false);
        k2k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k1.setEditable(false);
        k3k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k2.setEditable(false);
        k3k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k3.setEditable(false);
        k3k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k4.setEditable(false);
        k3k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k1.setEditable(false);
        k4k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k2.setEditable(false);
        k4k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k3.setEditable(false);
        k4k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k4.setEditable(false);
        k4k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("K1");

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("K2");

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("K3");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("K4");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(k2k1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(k2k2,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel5)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(k3k1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(k3k2,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel6)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(k4k1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(k4k2,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(k1k1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(k1k2,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(k1k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k2k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k3k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k4k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(k1k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k2k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k3k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k4k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addComponent(jLabel7)
                                                .addGap(40, 40, 40)
                                                .addComponent(jLabel8)
                                                .addGap(38, 38, 38)
                                                .addComponent(jLabel9)
                                                .addGap(42, 42, 42)
                                                .addComponent(jLabel10))
                                        .addComponent(jLabel2))
                                .addGap(20, 20, 20)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel10))
                                .addGap(5, 5, 5)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k1k1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k2k1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k3k1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel5))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k4k1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel6)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k1k3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k1k2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k2k3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k2k2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k3k3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k3k2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k4k3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k4k2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(k1k4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(k2k4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(k3k4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(k4k4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(10, 10, 10)));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Matriks Normalisasi");

        jLabel12.setForeground(new java.awt.Color(255, 237, 192));
        jLabel12.setText("K1");

        jLabel13.setForeground(new java.awt.Color(255, 237, 192));
        jLabel13.setText("K2");

        jLabel14.setForeground(new java.awt.Color(255, 237, 192));
        jLabel14.setText("K3");

        jLabel15.setForeground(new java.awt.Color(255, 237, 192));
        jLabel15.setText("K4");

        k1k1N.setEditable(false);
        k1k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k2N.setEditable(false);
        k1k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k3N.setEditable(false);
        k1k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k4N.setEditable(false);
        k1k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k1N.setEditable(false);
        k2k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k2N.setEditable(false);
        k2k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k3N.setEditable(false);
        k2k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k4N.setEditable(false);
        k2k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k1N.setEditable(false);
        k3k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k2N.setEditable(false);
        k3k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k3N.setEditable(false);
        k3k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k4N.setEditable(false);
        k3k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k1N.setEditable(false);
        k4k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k2N.setEditable(false);
        k4k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k3N.setEditable(false);
        k4k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k4N.setEditable(false);
        k4k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel16.setForeground(new java.awt.Color(255, 237, 192));
        jLabel16.setText("K1");

        jLabel17.setForeground(new java.awt.Color(255, 237, 192));
        jLabel17.setText("K2");

        jLabel18.setForeground(new java.awt.Color(255, 237, 192));
        jLabel18.setText("K3");

        jLabel19.setForeground(new java.awt.Color(255, 237, 192));
        jLabel19.setText("K4");

        jLabel20.setForeground(new java.awt.Color(255, 237, 192));
        jLabel20.setText("Prioritas");

        Prior1.setEditable(false);
        Prior1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Prior2.setEditable(false);
        Prior2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Prior3.setEditable(false);
        Prior3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Prior4.setEditable(false);
        Prior4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                                .addComponent(jLabel13)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(k2k1N,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        41,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(k2k2N,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        41,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                                .addComponent(jLabel14)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(k3k1N,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        41,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(k3k2N,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        41,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                                .addComponent(jLabel15)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(k4k1N,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        41,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(k4k2N,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        41,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                                .addComponent(jLabel12)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(k1k1N,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        41,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(k1k2N,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        41,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(10, 10, 10)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(k1k3N,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                41,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(k2k3N,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                41,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(k3k3N,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                41,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(k4k3N,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                41,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(k1k4N,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                41,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(k2k4N,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                41,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(k3k4N,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                41,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(k4k4N,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                41,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(29, 29, 29)
                                                                .addComponent(jLabel16)
                                                                .addGap(40, 40, 40)
                                                                .addComponent(jLabel17)
                                                                .addGap(36, 36, 36)
                                                                .addComponent(jLabel18)
                                                                .addGap(40, 40, 40)
                                                                .addComponent(jLabel19)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(Prior4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel20)
                                                        .addComponent(Prior3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Prior1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Prior2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(20, 20, 20)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(jLabel17)
                                        .addComponent(jLabel18)
                                        .addComponent(jLabel19)
                                        .addComponent(jLabel20))
                                .addGap(5, 5, 5)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k1k1N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel12))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k2k1N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel13))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k3k1N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel14))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k4k1N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel15)))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k1k3N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k1k2N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k2k3N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k2k2N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k3k3N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k3k2N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k4k3N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(k4k2N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k1k4N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Prior1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k2k4N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Prior2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k3k4N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Prior3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(k4k4N, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Prior4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(10, 10, 10)));

        Simpan.setBackground(new java.awt.Color(0, 0, 51));
        Simpan.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        Simpan.setForeground(new java.awt.Color(255, 255, 255));
        Simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image (4).png"))); // NOI18N
        Simpan.setText("SIMPAN");
        Simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpanActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 237, 192));
        jLabel23.setText("Prioritas SubKriteria Sesuai Kriteria");

        jLabel28.setForeground(new java.awt.Color(255, 237, 192));
        jLabel28.setText("K1");

        jLabel29.setForeground(new java.awt.Color(255, 237, 192));
        jLabel29.setText("K2");

        jLabel30.setForeground(new java.awt.Color(255, 237, 192));
        jLabel30.setText("K3");

        jLabel31.setForeground(new java.awt.Color(255, 237, 192));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("K4");

        jLabel21.setForeground(new java.awt.Color(255, 237, 192));
        jLabel21.setText("Prioritas Sub-Kriteria");

        PriorS11.setEditable(false);
        PriorS11.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS12.setEditable(false);
        PriorS12.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS13.setEditable(false);
        PriorS13.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS14.setEditable(false);
        PriorS14.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel24.setForeground(new java.awt.Color(255, 237, 192));
        jLabel24.setText("Prioritas Sub-Kriteria");

        PriorS21.setEditable(false);
        PriorS21.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS22.setEditable(false);
        PriorS22.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS23.setEditable(false);
        PriorS23.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS24.setEditable(false);
        PriorS24.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel25.setForeground(new java.awt.Color(255, 237, 192));
        jLabel25.setText("Prioritas Sub-Kriteria");

        PriorS31.setEditable(false);
        PriorS31.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS32.setEditable(false);
        PriorS32.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS33.setEditable(false);
        PriorS33.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS34.setEditable(false);
        PriorS34.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel26.setForeground(new java.awt.Color(255, 237, 192));
        jLabel26.setText("Prioritas Sub-Kriteria");

        PriorS41.setEditable(false);
        PriorS41.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS42.setEditable(false);
        PriorS42.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS43.setEditable(false);
        PriorS43.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        PriorS44.setEditable(false);
        PriorS44.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(jLabel23))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(48, 48, 48)
                                                                .addComponent(jLabel28))
                                                        .addGroup(jPanel4Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                .addComponent(PriorS14)
                                                                .addComponent(PriorS13)
                                                                .addComponent(PriorS12)
                                                                .addComponent(PriorS11,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 102,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel21,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel4Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel4Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                .addComponent(PriorS24)
                                                                .addComponent(PriorS23)
                                                                .addComponent(PriorS22)
                                                                .addComponent(PriorS21,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 102,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel24,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING))
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(43, 43, 43)
                                                                .addComponent(jLabel29)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel4Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(PriorS34)
                                                                        .addComponent(PriorS33)
                                                                        .addComponent(PriorS32)
                                                                        .addComponent(PriorS31,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                102,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel25,
                                                                                javax.swing.GroupLayout.Alignment.TRAILING))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel4Layout.createSequentialGroup()
                                                                        .addComponent(jLabel30)
                                                                        .addGap(59, 59, 59)))
                                                .addGroup(jPanel4Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(PriorS44)
                                                        .addComponent(PriorS43)
                                                        .addComponent(PriorS42)
                                                        .addComponent(PriorS41, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel26,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(jLabel31,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 76,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(20, 20, 20)));

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
                new java.awt.Component[] { PriorS11, PriorS12, PriorS13, PriorS14, PriorS21, PriorS22, PriorS23,
                        PriorS24, PriorS31, PriorS32, PriorS33, PriorS34, PriorS41, PriorS42, PriorS43, PriorS44 });

        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel25)
                                                .addGap(5, 5, 5)
                                                .addComponent(jLabel30)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS31, javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS32, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS33, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS34, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel24)
                                                .addGap(5, 5, 5)
                                                .addComponent(jLabel29)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS21, javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS22, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS23, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS24, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel21)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel28)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS11, javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS12, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS13, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS14, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel26)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel31)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS41, javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS42, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS43, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PriorS44, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(20, 20, 20)));

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL,
                new java.awt.Component[] { PriorS11, PriorS12, PriorS13, PriorS14, PriorS21, PriorS22, PriorS23,
                        PriorS24, PriorS31, PriorS32, PriorS33, PriorS34, PriorS41, PriorS42, PriorS43, PriorS44 });

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID Motor");

        jLabel27.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Nama Motor");

        jLabel22.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Total Penilaian Motor");

        Motor.setEditable(false);
        Motor.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

        TotalNilai.setEditable(false);
        TotalNilai.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

        cbIdMotor.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        cbIdMotor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbIdMotorItemStateChanged(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Pilih Data Motor");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel6Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        false)
                                                                .addComponent(jLabel27,
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jLabel22,
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel6Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(Motor)
                                                        .addComponent(TotalNilai)
                                                        .addComponent(cbIdMotor, 0, 150, Short.MAX_VALUE)))
                                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbIdMotor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Motor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(TotalNilai, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(mulaiHitung, javax.swing.GroupLayout.PREFERRED_SIZE, 112,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Simpan)
                                                .addGap(125, 125, 125))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(30, 30, 30)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50,
                                                        Short.MAX_VALUE)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(20, 20, 20)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(mulaiHitung)
                                        .addComponent(Simpan))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)));

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout PanelPerhitunganLayout = new javax.swing.GroupLayout(PanelPerhitungan);
        PanelPerhitungan.setLayout(PanelPerhitunganLayout);
        PanelPerhitunganLayout.setHorizontalGroup(
                PanelPerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(judul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(jScrollPane1));
        PanelPerhitunganLayout.setVerticalGroup(
                PanelPerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PanelPerhitunganLayout.createSequentialGroup()
                                .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 62,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                                .addContainerGap()));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("dialog3"); // NOI18N

        Pane.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));

        setSize(new java.awt.Dimension(888, 577));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
     // tombol mulai perhitungan

    private void mulaiHitungActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mulaiHitungActionPerformed
        // Validate that a motor is selected
        if (cbIdMotor.getSelectedIndex() == 0
                || cbIdMotor.getSelectedItem().toString().equals("-- Pilih Motor --")) {
            JOptionPane.showMessageDialog(null, "Pilih motor terlebih dahulu!");
            return;
        }

        try {
            System.out.println("=== STARTING AHP CALCULATION BASED ON RESEARCH PAPER ===");
            
            // Initialize new AHP calculation with exact paper values
            ahpCalc = new AHPCalculation();
            
            // Validate our matrices match the paper exactly
            validateMatricesAgainstPaper();
            
            // Mendapatkan perhitungan metode AHP menggunakan formula dari paper
            kriteria.hitungKriteria();
            ahpCalc.calculateAll();

            // Display matrices and calculations
            getMatriksK();
            getMatriksNorK();
            getPrioritasSub();
            getAlternatif();
            getPenilaian();
            
            // Show detailed validation results
            System.out.println("\n=== VALIDATION RESULTS ===");
            validateCriteriaWeights();
            validateSubcriteriaWeights();
            
            JOptionPane.showMessageDialog(null,
                    "Perhitungan AHP berhasil berdasarkan paper research!\n" +
                    "Total nilai: " + TotalNilai.getText() + "\n" +
                    "Motor: " + namaMotorAlternatif + "\n" +
                    "Lihat console untuk detail perhitungan dan validasi.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error dalam perhitungan AHP: " + e.getMessage());
            e.printStackTrace();
        }

    }// GEN-LAST:event_mulaiHitungActionPerformed
    
    /**
     * Validate that our matrices match the paper exactly
     */
    private void validateMatricesAgainstPaper() {
        System.out.println("\n=== MATRIX VALIDATION ===");
        
        double[][] criteriaMatrix = ahpCalc.getCriteriaMatrix();
        double[] expectedCriteriaTotals = {1.676, 4.533, 9.333, 16.000};
        String[] criteriaNames = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        
        System.out.println("Criteria Matrix Column Totals:");
        for (int j = 0; j < 4; j++) {
            double sum = 0;
            for (int i = 0; i < 4; i++) {
                sum += criteriaMatrix[i][j];
            }
            boolean matches = Math.abs(sum - expectedCriteriaTotals[j]) < 0.01;
            System.out.printf("  %s: %.3f (expected: %.3f) %s%n", 
                criteriaNames[j], sum, expectedCriteriaTotals[j], matches ? "✓" : "✗");
        }
    }
    
    /**
     * Validate criteria weights against paper values
     */
    private void validateCriteriaWeights() {
        double[] weights = ahpCalc.getCriteriaWeights();
        double[] expectedWeights = {0.558, 0.263, 0.122, 0.057};
        String[] names = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        
        System.out.println("Criteria Weights Validation:");
        boolean allMatch = true;
        for (int i = 0; i < 4; i++) {
            double diff = Math.abs(weights[i] - expectedWeights[i]);
            boolean matches = diff < 0.01;
            System.out.printf("  %s: %.3f (expected: %.3f) diff: %.3f %s%n", 
                names[i], weights[i], expectedWeights[i], diff, matches ? "✓" : "✗");
            if (!matches) allMatch = false;
        }
        System.out.println("Overall criteria weights: " + (allMatch ? "PASSED" : "NEEDS ADJUSTMENT"));
    }
    
    /**
     * Validate subcriteria weights against paper values
     */
    private void validateSubcriteriaWeights() {
        double[][] weights = ahpCalc.getSubcriteriaWeights();
        double[][] expectedWeights = {
            {0.633, 0.260, 0.106}, // Harga
            {0.429, 0.429, 0.143}, // CC
            {0.643, 0.283, 0.074}, // Irit
            {0.633, 0.260, 0.106}  // Desain
        };
        
        String[] criteriaNames = {"HARGA", "MESIN/CC", "IRIT BENSIN", "DESAIN"};
        String[][] subNames = {
            {"EKONOMIS", "MENENGAH", "PREMIUM"},
            {"Kecil", "Sedang", "Besar"},
            {"Irit", "Sedang", "Boros"}, 
            {"Sporty", "Retro", "Futuristik"}
        };
        
        System.out.println("Subcriteria Weights Validation:");
        boolean allMatch = true;
        for (int i = 0; i < 4; i++) {
            System.out.println("  " + criteriaNames[i] + ":");
            for (int j = 0; j < 3; j++) {
                double diff = Math.abs(weights[i][j] - expectedWeights[i][j]);
                boolean matches = diff < 0.01;
                System.out.printf("    %s: %.3f (expected: %.3f) diff: %.3f %s%n",
                    subNames[i][j], weights[i][j], expectedWeights[i][j], diff, matches ? "✓" : "✗");
                if (!matches) allMatch = false;
            }
        }
        System.out.println("Overall subcriteria weights: " + (allMatch ? "PASSED" : "NEEDS ADJUSTMENT"));
    }

    // mendapatkan nama motor dari id yang dipilih
    private void cbIdMotorItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cbIdMotorItemStateChanged
        // Check if a valid motor is selected (not the default option)
        if (cbIdMotor.getSelectedIndex() == 0
                || cbIdMotor.getSelectedItem().toString().equals("-- Pilih Motor --")) {
            Motor.setText("");
            return;
        }

        String sql = "SELECT DISTINCT nama_motor FROM data_motor WHERE id_motor='"
                + cbIdMotor.getSelectedItem().toString() + "';";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String b = hasil.getString("nama_motor");
                Motor.setText(b);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }// GEN-LAST:event_cbIdMotorItemStateChanged
     // simpan data

    private void SimpanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_SimpanActionPerformed
        // TODO add your handling code here:
        if (noIdAlternatif == null || TotalNilai.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Silakan lakukan perhitungan terlebih dahulu!");
            return;
        }

        try {
            // Check if record already exists
            String checkSql = "SELECT COUNT(*) FROM seleksi WHERE id_motor = ?";
            PreparedStatement checkStat = conn.prepareStatement(checkSql);
            checkStat.setString(1, noIdAlternatif);
            ResultSet result = checkStat.executeQuery();

            boolean exists = false;
            if (result.next()) {
                exists = result.getInt(1) > 0;
            }

            if (exists) {
                // Update existing record
                String updateSql = "UPDATE seleksi SET nama_motor=?, merek=?, hasil_penilaian=? WHERE id_motor=?";
                PreparedStatement updateStat = conn.prepareStatement(updateSql);
                updateStat.setString(1, namaMotorAlternatif);
                updateStat.setString(2, merekAlternatif);
                updateStat.setDouble(3, Double.parseDouble(TotalNilai.getText().replace(",", ".")));
                updateStat.setString(4, noIdAlternatif);
                updateStat.executeUpdate();
                JOptionPane.showMessageDialog(null, "DATA berhasil diupdate");
            } else {
                // Get next ranking
                String maxRankSql = "SELECT COALESCE(MAX(ranking), 0) + 1 as next_rank FROM seleksi";
                PreparedStatement maxRankStat = conn.prepareStatement(maxRankSql);
                ResultSet maxRankResult = maxRankStat.executeQuery();
                int nextRank = 1;
                if (maxRankResult.next()) {
                    nextRank = maxRankResult.getInt("next_rank");
                }

                // Insert new record
                String insertSql = "INSERT INTO seleksi VALUES (?,?,?,?,?)";
                PreparedStatement insertStat = conn.prepareStatement(insertSql);
                insertStat.setString(1, noIdAlternatif);
                insertStat.setString(2, namaMotorAlternatif);
                insertStat.setString(3, merekAlternatif);
                insertStat.setDouble(4, Double.parseDouble(TotalNilai.getText().replace(",", ".")));
                insertStat.setInt(5, nextRank);
                insertStat.executeUpdate();
                JOptionPane.showMessageDialog(null, "DATA berhasil disimpan");
            }

            // Update rankings based on hasil_penilaian (highest score gets rank 1)
            java.sql.Statement updateRankStat = conn.createStatement();
            updateRankStat.execute("SET @rank = 0");
            updateRankStat.executeUpdate(
                    "UPDATE seleksi SET ranking = (@rank := @rank + 1) ORDER BY hasil_penilaian DESC");

            kosong();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal disimpan: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Format nilai tidak valid: " + e.getMessage());
        }
    }// GEN-LAST:event_SimpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                    .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogPerhitunganAHP.class.getName()).log(
                    java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogPerhitunganAHP.class.getName()).log(
                    java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogPerhitunganAHP.class.getName()).log(
                    java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogPerhitunganAHP.class.getName()).log(
                    java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogPerhitunganAHP dialog = new DialogPerhitunganAHP(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Motor;
    private javax.swing.JPanel Pane;
    private javax.swing.JPanel PanelPerhitungan;
    private javax.swing.JTextField Prior1;
    private javax.swing.JTextField Prior2;
    private javax.swing.JTextField Prior3;
    private javax.swing.JTextField Prior4;
    private javax.swing.JTextField PriorS11;
    private javax.swing.JTextField PriorS12;
    private javax.swing.JTextField PriorS13;
    private javax.swing.JTextField PriorS14;
    private javax.swing.JTextField PriorS21;
    private javax.swing.JTextField PriorS22;
    private javax.swing.JTextField PriorS23;
    private javax.swing.JTextField PriorS24;
    private javax.swing.JTextField PriorS31;
    private javax.swing.JTextField PriorS32;
    private javax.swing.JTextField PriorS33;
    private javax.swing.JTextField PriorS34;
    private javax.swing.JTextField PriorS41;
    private javax.swing.JTextField PriorS42;
    private javax.swing.JTextField PriorS43;
    private javax.swing.JTextField PriorS44;
    private javax.swing.JButton Simpan;
    private javax.swing.JTextField TotalNilai;
    private javax.swing.JComboBox<String> cbIdMotor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel judul;
    private javax.swing.JTextField k1k1;
    private javax.swing.JTextField k1k1N;
    private javax.swing.JTextField k1k2;
    private javax.swing.JTextField k1k2N;
    private javax.swing.JTextField k1k3;
    private javax.swing.JTextField k1k3N;
    private javax.swing.JTextField k1k4;
    private javax.swing.JTextField k1k4N;
    private javax.swing.JTextField k2k1;
    private javax.swing.JTextField k2k1N;
    private javax.swing.JTextField k2k2;
    private javax.swing.JTextField k2k2N;
    private javax.swing.JTextField k2k3;
    private javax.swing.JTextField k2k3N;
    private javax.swing.JTextField k2k4;
    private javax.swing.JTextField k2k4N;
    private javax.swing.JTextField k3k1;
    private javax.swing.JTextField k3k1N;
    private javax.swing.JTextField k3k2;
    private javax.swing.JTextField k3k2N;
    private javax.swing.JTextField k3k3;
    private javax.swing.JTextField k3k3N;
    private javax.swing.JTextField k3k4;
    private javax.swing.JTextField k3k4N;
    private javax.swing.JTextField k4k1;
    private javax.swing.JTextField k4k1N;
    private javax.swing.JTextField k4k2;
    private javax.swing.JTextField k4k2N;
    private javax.swing.JTextField k4k3;
    private javax.swing.JTextField k4k3N;
    private javax.swing.JTextField k4k4;
    private javax.swing.JTextField k4k4N;
    private javax.swing.JButton mulaiHitung;
    // End of variables declaration//GEN-END:variables

    void show(JRootPane rootPane) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods,
                                                                       // choose
                                                                       // Tools | Templates.
    }
}
