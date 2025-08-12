
package menuutama;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author Neubri
 */
public class PengaturanSubKriteria extends javax.swing.JPanel {
        private Connection conn = new Koneksi().connect();
        private DefaultTableModel tabmode;

        /**
         * Creates new form Pengaturan
         */
        public PengaturanSubKriteria() {
                initComponents();
                updateDataTabel();
        }

        protected void kosong() {
                cbSubIritBensin1.setSelectedIndex(0);
                cbSubritBensin2.setSelectedIndex(0);
                cbSubritBensin3.setSelectedIndex(0);
                // cbSubritBensin4.setSelectedIndex(0); // Component not defined in form
                cbSubMesin1.setSelectedIndex(0);
                cbSubMesin2.setSelectedIndex(0);
                cbSubMesin3.setSelectedIndex(0);
                cbSubDesain1.setSelectedIndex(0);
                cbSubDesain2.setSelectedIndex(0);
                cbSubDesain3.setSelectedIndex(0);
                cbSubharga1.setSelectedIndex(0);
                cbSubHarga2.setSelectedIndex(0);
                cbSubHarga3.setSelectedIndex(0);

        }

        protected void updateDataTabel() {
                Object[] Baris = {
                                "Kode Kriteria",
                                "Nama Kriteria",
                                "Nama SubKriteria",
                                "Kepentingan SubKriteria"
                };
                tabmode = new DefaultTableModel(null, Baris);
                tabelSubKriteria.setModel(tabmode);
                String sql = "SELECT * FROM sub_kriteria ORDER BY kd_kriteria, no_sub";
                try {
                        java.sql.Statement stat = conn.createStatement();
                        ResultSet hasil = stat.executeQuery(sql);
                        while (hasil.next()) {
                                String a = hasil.getString("kd_kriteria");
                                String b = hasil.getString("nama_kriteria");
                                String c = hasil.getString("nama_sub_kriteria");
                                String d = hasil.getString("prioritas_kepentingan");

                                String[] data = { a, b, c, d };
                                tabmode.addRow(data);
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                }
        }

        protected void getDataTabel() {
                String sql = "SELECT nama_sub_kriteria FROM sub_kriteria ORDER BY kd_kriteria, no_sub";
                int n = 1;
                try {
                        java.sql.Statement stat = conn.createStatement();
                        ResultSet hasil = stat.executeQuery(sql);
                        while (hasil.next()) {
                                String a = hasil.getString("nama_sub_kriteria");
                                if (n == 1) {
                                        cbSubIritBensin1.setSelectedItem(a);
                                } else if (n == 2) {
                                        cbSubritBensin2.setSelectedItem(a);
                                } else if (n == 3) {
                                        cbSubritBensin3.setSelectedItem(a);
                                } else if (n == 4) {
                                        // cbSubritBensin4.setSelectedItem(a); // Component not defined in form
                                } else if (n == 5) {
                                        cbSubMesin1.setSelectedItem(a);
                                } else if (n == 6) {
                                        cbSubMesin2.setSelectedItem(a);
                                } else if (n == 7) {
                                        cbSubMesin3.setSelectedItem(a);
                                } else if (n == 8) {
                                        cbSubDesain1.setSelectedItem(a);
                                } else if (n == 9) {
                                        cbSubDesain2.setSelectedItem(a);
                                } else if (n == 10) {
                                        cbSubDesain3.setSelectedItem(a);
                                } else if (n == 11) {
                                        cbSubharga1.setSelectedItem(a);
                                } else if (n == 12) {
                                        cbSubHarga2.setSelectedItem(a);
                                } else if (n == 13) {
                                        cbSubHarga3.setSelectedItem(a);
                                }
                                n++;
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                }
        }

        // masukan data subkriteria
        protected void insertDataSubKriteria() {
                try {
                        // Clear existing subcriteria data first
                        String deleteSql = "DELETE FROM sub_kriteria";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                        deleteStmt.executeUpdate();

                        int subId = 1;
                        String sql = "INSERT INTO sub_kriteria VALUES (?,?,?,?,?)";

                        // Insert Harga subcriteria (3x3)
                        String sqlHarga = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Harga'";
                        java.sql.Statement statHarga = conn.createStatement();
                        ResultSet hasilHarga = statHarga.executeQuery(sqlHarga);
                        if (hasilHarga.next()) {
                                String kdKriteria = hasilHarga.getString("kd_kriteria");
                                String namaKriteria = hasilHarga.getString("nama_kriteria");

                                // Harga subkriteria 1
                                if (cbSubharga1.getSelectedIndex() != 0) {
                                        PreparedStatement stat1 = conn.prepareStatement(sql);
                                        stat1.setString(1, Integer.toString(subId++));
                                        stat1.setString(2, kdKriteria);
                                        stat1.setString(3, namaKriteria);
                                        stat1.setString(4, cbSubharga1.getSelectedItem().toString());
                                        stat1.setString(5, "Sangat Penting ke-1");
                                        stat1.executeUpdate();
                                }

                                // Harga subkriteria 2
                                if (cbSubHarga2.getSelectedIndex() != 0) {
                                        PreparedStatement stat2 = conn.prepareStatement(sql);
                                        stat2.setString(1, Integer.toString(subId++));
                                        stat2.setString(2, kdKriteria);
                                        stat2.setString(3, namaKriteria);
                                        stat2.setString(4, cbSubHarga2.getSelectedItem().toString());
                                        stat2.setString(5, "Penting ke-2");
                                        stat2.executeUpdate();
                                }

                                // Harga subkriteria 3
                                if (cbSubHarga3.getSelectedIndex() != 0) {
                                        PreparedStatement stat3 = conn.prepareStatement(sql);
                                        stat3.setString(1, Integer.toString(subId++));
                                        stat3.setString(2, kdKriteria);
                                        stat3.setString(3, namaKriteria);
                                        stat3.setString(4, cbSubHarga3.getSelectedItem().toString());
                                        stat3.setString(5, "Cukup Penting ke-3");
                                        stat3.executeUpdate();
                                }
                        }

                        // Insert Kapasitas Mesin subcriteria (3x3)
                        String sqlMesin = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Kapasitas Mesin'";
                        java.sql.Statement statMesin = conn.createStatement();
                        ResultSet hasilMesin = statMesin.executeQuery(sqlMesin);
                        if (hasilMesin.next()) {
                                String kdKriteria = hasilMesin.getString("kd_kriteria");
                                String namaKriteria = hasilMesin.getString("nama_kriteria");

                                // Mesin subkriteria 1
                                if (cbSubMesin1.getSelectedIndex() != 0) {
                                        PreparedStatement stat1 = conn.prepareStatement(sql);
                                        stat1.setString(1, Integer.toString(subId++));
                                        stat1.setString(2, kdKriteria);
                                        stat1.setString(3, namaKriteria);
                                        stat1.setString(4, cbSubMesin1.getSelectedItem().toString());
                                        stat1.setString(5, "Sangat Penting ke-1");
                                        stat1.executeUpdate();
                                }

                                // Mesin subkriteria 2
                                if (cbSubMesin2.getSelectedIndex() != 0) {
                                        PreparedStatement stat2 = conn.prepareStatement(sql);
                                        stat2.setString(1, Integer.toString(subId++));
                                        stat2.setString(2, kdKriteria);
                                        stat2.setString(3, namaKriteria);
                                        stat2.setString(4, cbSubMesin2.getSelectedItem().toString());
                                        stat2.setString(5, "Penting ke-2");
                                        stat2.executeUpdate();
                                }

                                // Mesin subkriteria 3
                                if (cbSubMesin3.getSelectedIndex() != 0) {
                                        PreparedStatement stat3 = conn.prepareStatement(sql);
                                        stat3.setString(1, Integer.toString(subId++));
                                        stat3.setString(2, kdKriteria);
                                        stat3.setString(3, namaKriteria);
                                        stat3.setString(4, cbSubMesin3.getSelectedItem().toString());
                                        stat3.setString(5, "Cukup Penting ke-3");
                                        stat3.executeUpdate();
                                }
                        }

                        // Insert Irit Bahan Bakar subcriteria (3x3)
                        String sqlIrit = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Irit Bahan Bakar'";
                        java.sql.Statement statIrit = conn.createStatement();
                        ResultSet hasilIrit = statIrit.executeQuery(sqlIrit);
                        if (hasilIrit.next()) {
                                String kdKriteria = hasilIrit.getString("kd_kriteria");
                                String namaKriteria = hasilIrit.getString("nama_kriteria");

                                // Irit subkriteria 1
                                if (cbSubIritBensin1.getSelectedIndex() != 0) {
                                        PreparedStatement stat1 = conn.prepareStatement(sql);
                                        stat1.setString(1, Integer.toString(subId++));
                                        stat1.setString(2, kdKriteria);
                                        stat1.setString(3, namaKriteria);
                                        stat1.setString(4, cbSubIritBensin1.getSelectedItem().toString());
                                        stat1.setString(5, "Sangat Penting ke-1");
                                        stat1.executeUpdate();
                                }

                                // Irit subkriteria 2
                                if (cbSubritBensin2.getSelectedIndex() != 0) {
                                        PreparedStatement stat2 = conn.prepareStatement(sql);
                                        stat2.setString(1, Integer.toString(subId++));
                                        stat2.setString(2, kdKriteria);
                                        stat2.setString(3, namaKriteria);
                                        stat2.setString(4, cbSubritBensin2.getSelectedItem().toString());
                                        stat2.setString(5, "Penting ke-2");
                                        stat2.executeUpdate();
                                }

                                // Irit subkriteria 3
                                if (cbSubritBensin3.getSelectedIndex() != 0) {
                                        PreparedStatement stat3 = conn.prepareStatement(sql);
                                        stat3.setString(1, Integer.toString(subId++));
                                        stat3.setString(2, kdKriteria);
                                        stat3.setString(3, namaKriteria);
                                        stat3.setString(4, cbSubritBensin3.getSelectedItem().toString());
                                        stat3.setString(5, "Cukup Penting ke-3");
                                        stat3.executeUpdate();
                                }
                        }

                        // Insert Desain subcriteria (3x3)
                        String sqlDesain = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Desain'";
                        java.sql.Statement statDesain = conn.createStatement();
                        ResultSet hasilDesain = statDesain.executeQuery(sqlDesain);
                        if (hasilDesain.next()) {
                                String kdKriteria = hasilDesain.getString("kd_kriteria");
                                String namaKriteria = hasilDesain.getString("nama_kriteria");

                                // Desain subkriteria 1
                                if (cbSubDesain1.getSelectedIndex() != 0) {
                                        PreparedStatement stat1 = conn.prepareStatement(sql);
                                        stat1.setString(1, Integer.toString(subId++));
                                        stat1.setString(2, kdKriteria);
                                        stat1.setString(3, namaKriteria);
                                        stat1.setString(4, cbSubDesain1.getSelectedItem().toString());
                                        stat1.setString(5, "Sangat Penting ke-1");
                                        stat1.executeUpdate();
                                }

                                // Desain subkriteria 2
                                if (cbSubDesain2.getSelectedIndex() != 0) {
                                        PreparedStatement stat2 = conn.prepareStatement(sql);
                                        stat2.setString(1, Integer.toString(subId++));
                                        stat2.setString(2, kdKriteria);
                                        stat2.setString(3, namaKriteria);
                                        stat2.setString(4, cbSubDesain2.getSelectedItem().toString());
                                        stat2.setString(5, "Penting ke-2");
                                        stat2.executeUpdate();
                                }

                                // Desain subkriteria 3
                                if (cbSubDesain3.getSelectedIndex() != 0) {
                                        PreparedStatement stat3 = conn.prepareStatement(sql);
                                        stat3.setString(1, Integer.toString(subId++));
                                        stat3.setString(2, kdKriteria);
                                        stat3.setString(3, namaKriteria);
                                        stat3.setString(4, cbSubDesain3.getSelectedItem().toString());
                                        stat3.setString(5, "Cukup Penting ke-3");
                                        stat3.executeUpdate();
                                }
                        }

                        JOptionPane.showMessageDialog(null, "Data Sub Kriteria Berhasil Disimpan");
                        updateDataTabel();

                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
        }

        protected void hapusDataSubKriteria() {
                int ok = JOptionPane.showConfirmDialog(null, "hapus", "Konfirmasi Dialog", JOptionPane.YES_NO_OPTION);
                if (ok == 0) {
                        String sql = "DELETE FROM sub_kriteria";
                        try {
                                PreparedStatement stat = conn.prepareStatement(sql);

                                stat.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Data Berhasil diHapus ");
                                kosong();
                                updateDataTabel();
                        } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "Data Gagal diHapus " + e);
                        }
                }
        }

        protected void editDataSubKriteria() {
                try {
                        int n = 1, nTingkatVegetarian = 1, nHargaPaket = 1, nJumlahMenu = 1, nTingkatPopularitas = 1,
                                        i = 1;
                        do {
                                String kepentingan = ""; // Initialize with default value
                                String sql = "UPDATE sub_kriteria set kd_kriteria=?, nama_kriteria=?, nama_sub_kriteria=?, prioritas_kepentingan=? WHERE no_sub=?";
                                PreparedStatement stat = conn.prepareStatement(sql);
                                java.sql.Statement statCek = conn.createStatement();
                                String sqlSub = "SELECT kd_kriteria, nama_kriteria FROM kriteria";
                                String sqlTingkatVegetarian = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Harga'";
                                String sqlHargaPaket = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Kapasitas Mesin'";
                                String sqlJumlahMenu = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Irit Bahan Bakar'";
                                String sqlTingkatPopularitas = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Desain'";
                                ResultSet hasil = statCek.executeQuery(sqlSub);
                                if (n == 1) {
                                        hasil = statCek.executeQuery(sqlTingkatVegetarian);
                                        stat.setString(5, Integer.toString(i));
                                        i++;
                                        while (hasil.next()) {
                                                String a = hasil.getString("kd_kriteria");
                                                String b = hasil.getString("nama_kriteria");
                                                stat.setString(1, a);
                                                stat.setString(2, b);
                                        }
                                        if (nTingkatVegetarian == 1) {
                                                stat.setString(3, cbSubIritBensin1.getSelectedItem().toString());
                                                kepentingan = "Sangat Penting ke-1";
                                        } else if (nTingkatVegetarian == 2) {
                                                stat.setString(3, cbSubritBensin2.getSelectedItem().toString());
                                                kepentingan = "Penting ke-2";
                                        } else if (nTingkatVegetarian == 3) {
                                                stat.setString(3, cbSubritBensin3.getSelectedItem().toString());
                                                kepentingan = "Cukup Penting ke-3";
                                        } else {
                                                stat.setString(3, ""); // cbSubritBensin4.getSelectedItem().toString() -
                                                                       // Component not defined
                                                                       // in form
                                                kepentingan = "Biasa ke-4";
                                                n++;
                                        }
                                        stat.setString(4, kepentingan);
                                        stat.executeUpdate();
                                        nTingkatVegetarian++;
                                } else if (n == 2) {
                                        hasil = statCek.executeQuery(sqlHargaPaket);
                                        stat.setString(5, Integer.toString(i));
                                        i++;
                                        while (hasil.next()) {
                                                String a = hasil.getString("kd_kriteria");
                                                String b = hasil.getString("nama_kriteria");
                                                stat.setString(1, a);
                                                stat.setString(2, b);
                                        }
                                        if (nHargaPaket == 1) {
                                                stat.setString(3, cbSubMesin1.getSelectedItem().toString());
                                                kepentingan = "Sangat Penting ke-1";
                                        } else if (nHargaPaket == 2) {
                                                stat.setString(3, cbSubMesin2.getSelectedItem().toString());
                                                kepentingan = "Cukup Penting ke-2";
                                        } else {
                                                stat.setString(3, cbSubMesin3.getSelectedItem().toString());
                                                kepentingan = "Biasa ke-3";
                                                n++;
                                        }
                                        stat.setString(4, kepentingan);
                                        stat.executeUpdate();
                                        nHargaPaket++;
                                } else if (n == 3) {
                                        hasil = statCek.executeQuery(sqlJumlahMenu);
                                        stat.setString(5, Integer.toString(i));
                                        i++;
                                        while (hasil.next()) {
                                                String a = hasil.getString("kd_kriteria");
                                                String b = hasil.getString("nama_kriteria");
                                                stat.setString(1, a);
                                                stat.setString(2, b);
                                        }
                                        if (nJumlahMenu == 1) {
                                                stat.setString(3, cbSubDesain1.getSelectedItem().toString());
                                                kepentingan = "Sangat Penting ke-1";
                                        } else if (nJumlahMenu == 2) {
                                                stat.setString(3, cbSubDesain2.getSelectedItem().toString());
                                                kepentingan = "Cukup Penting ke-2";
                                        } else {
                                                stat.setString(3, cbSubDesain3.getSelectedItem().toString());
                                                kepentingan = "Biasa ke-3";
                                                n++;
                                        }
                                        stat.setString(4, kepentingan);
                                        stat.executeUpdate();
                                        nJumlahMenu++;
                                } else {
                                        hasil = statCek.executeQuery(sqlTingkatPopularitas);
                                        stat.setString(5, Integer.toString(i));
                                        i++;
                                        while (hasil.next()) {
                                                String a = hasil.getString("kd_kriteria");
                                                String b = hasil.getString("nama_kriteria");
                                                stat.setString(1, a);
                                                stat.setString(2, b);
                                        }
                                        if (nTingkatPopularitas == 1) {
                                                stat.setString(3, cbSubharga1.getSelectedItem().toString());
                                                kepentingan = "Sangat Penting ke-1";
                                        } else if (nTingkatPopularitas == 2) {
                                                stat.setString(3, cbSubHarga2.getSelectedItem().toString());
                                                kepentingan = "Penting ke-2";
                                        } else if (nTingkatPopularitas == 3) {
                                                stat.setString(3, cbSubHarga3.getSelectedItem().toString());
                                                kepentingan = "Cukup Penting ke-3";

                                        }
                                        stat.setString(4, kepentingan); // Fixed: should be parameter 4, not 3
                                        stat.executeUpdate();
                                        nTingkatPopularitas++;
                                }
                        } while (n <= 4);
                        JOptionPane.showMessageDialog(null, "DATA Berhasil DiUbah");
                        kosong();
                        updateDataTabel();
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Data Gagal DiUbah " + e);
                }
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        // @SuppressWarnings("unchecked") // Removed - unnecessary
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                judul = new javax.swing.JLabel();
                jPanel1 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                tabelSubKriteria = new javax.swing.JTable();
                jPanel3 = new javax.swing.JPanel();
                jLabel17 = new javax.swing.JLabel();
                jLabel18 = new javax.swing.JLabel();
                jLabel19 = new javax.swing.JLabel();
                cbSubHarga3 = new javax.swing.JComboBox<>();
                cbSubHarga2 = new javax.swing.JComboBox<>();
                cbSubharga1 = new javax.swing.JComboBox<>();
                jPanel5 = new javax.swing.JPanel();
                jLabel21 = new javax.swing.JLabel();
                jLabel22 = new javax.swing.JLabel();
                jLabel23 = new javax.swing.JLabel();
                cbSubritBensin3 = new javax.swing.JComboBox<>();
                cbSubritBensin2 = new javax.swing.JComboBox<>();
                cbSubIritBensin1 = new javax.swing.JComboBox<>();
                jPanel4 = new javax.swing.JPanel();
                jLabel9 = new javax.swing.JLabel();
                jLabel15 = new javax.swing.JLabel();
                jLabel16 = new javax.swing.JLabel();
                cbSubMesin3 = new javax.swing.JComboBox<>();
                cbSubMesin2 = new javax.swing.JComboBox<>();
                cbSubMesin1 = new javax.swing.JComboBox<>();
                jPanel6 = new javax.swing.JPanel();
                jLabel10 = new javax.swing.JLabel();
                jLabel25 = new javax.swing.JLabel();
                jLabel26 = new javax.swing.JLabel();
                cbSubDesain3 = new javax.swing.JComboBox<>();
                cbSubDesain2 = new javax.swing.JComboBox<>();
                cbSubDesain1 = new javax.swing.JComboBox<>();
                tombolSimpan = new javax.swing.JButton();
                tombolEdit = new javax.swing.JButton();
                tombolHapus = new javax.swing.JButton();
                jLabel2 = new javax.swing.JLabel();

                setBackground(new java.awt.Color(255, 237, 192));
                setPreferredSize(new java.awt.Dimension(990, 700));

                judul.setBackground(new java.awt.Color(255, 0, 0));
                judul.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
                judul.setForeground(new java.awt.Color(255, 255, 255));
                judul.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                judul.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image (8).png"))); // NOI18N
                judul.setText("  Pengaturan Bobot Kepentingan Subkriteria");
                judul.setOpaque(true);

                jPanel1.setBackground(new java.awt.Color(204, 204, 204));
                jPanel1.setPreferredSize(new java.awt.Dimension(990, 620));

                tabelSubKriteria.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                tabelSubKriteria.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null }
                                },
                                new String[] {
                                                "Kode Kriteria", "Nama Kriteria", "Nama SubKriteria",
                                                "Kepentingan SubKriteria"
                                }));
                tabelSubKriteria.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                tabelSubKriteriaMouseClicked(evt);
                        }
                });
                jScrollPane1.setViewportView(tabelSubKriteria);

                jPanel3.setBackground(new java.awt.Color(22, 65, 53));
                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kepentingan Harga",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12),
                                new java.awt.Color(255, 237, 192))); // NOI18N
                jPanel3.setForeground(new java.awt.Color(255, 237, 192));
                jPanel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jPanel3.setName("Tingkat"); // NOI18N

                jLabel17.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel17.setForeground(new java.awt.Color(255, 237, 192));
                jLabel17.setText("Harga Yang Sangat Penting ke-1");

                jLabel18.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel18.setForeground(new java.awt.Color(255, 237, 192));
                jLabel18.setText("Harga Penting ke-2");

                jLabel19.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel19.setForeground(new java.awt.Color(255, 237, 192));
                jLabel19.setText("Harga Cukup Penting ke-3");

                cbSubHarga3.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "- Pilih SubKriteria Harga -", "EKONOMIS ≤ 22 juta",
                                                "MENENGAH 22 – 35 juta", "PREMIUM > 35 juta", " " }));

                cbSubHarga2.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "- Pilih SubKriteria Harga -", "EKONOMIS ≤ 22 juta",
                                                "MENENGAH 22 – 35 juta", "PREMIUM > 35 juta", " ", " " }));

                cbSubharga1.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "- Pilih SubKriteria Harga -", "EKONOMIS ≤ 22 juta",
                                                "MENENGAH 22 – 35 juta", "PREMIUM > 35 juta", " ", " " }));

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addGroup(jPanel3Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel19)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(cbSubHarga3,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                197,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel3Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel3Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jLabel17)
                                                                                                                .addComponent(jLabel18))
                                                                                                .addGap(10, 10, 10)
                                                                                                .addGroup(jPanel3Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                false)
                                                                                                                .addComponent(cbSubHarga2,
                                                                                                                                0,
                                                                                                                                197,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(cbSubharga1,
                                                                                                                                0,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE))))
                                                                .addContainerGap()));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel17)
                                                                                .addComponent(cbSubharga1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel18)
                                                                                .addComponent(cbSubHarga2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel3Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel19)
                                                                                .addComponent(cbSubHarga3,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(31, Short.MAX_VALUE)));

                jPanel5.setBackground(new java.awt.Color(22, 65, 53));
                jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kepentingan Tingkat Irit Bensin",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12),
                                new java.awt.Color(255, 237, 192))); // NOI18N

                jLabel21.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel21.setForeground(new java.awt.Color(255, 237, 192));
                jLabel21.setText("Irit Bensin Yang Sangat Penting ke-1");

                jLabel22.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel22.setForeground(new java.awt.Color(255, 237, 192));
                jLabel22.setText("Irit Bensin Penting ke-2");

                jLabel23.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel23.setForeground(new java.awt.Color(255, 237, 192));
                jLabel23.setText("Irit Bensin Cukup Penting ke-3");

                cbSubritBensin3.setModel(
                                new javax.swing.DefaultComboBoxModel<>(new String[] { "- SubKriteria Irit Bensin -",
                                                "Irit ≥ 50 km/l", "Sedang 40–49 km/l", "Boros < 40 km/l" }));

                cbSubritBensin2.setModel(
                                new javax.swing.DefaultComboBoxModel<>(new String[] { "- SubKriteria Irit Bensin -",
                                                "Irit ≥ 50 km/l", "Sedang 40–49 km/l", "Boros < 40 km/l" }));

                cbSubIritBensin1.setModel(
                                new javax.swing.DefaultComboBoxModel<>(new String[] { "- SubKriteria Irit Bensin -",
                                                "Irit ≥ 50 km/l", "Sedang 40–49 km/l", "Boros < 40 km/l", " " }));

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel21)
                                                                                .addComponent(jLabel22)
                                                                                .addComponent(jLabel23))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(cbSubIritBensin1,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                0, 212, Short.MAX_VALUE)
                                                                                .addComponent(cbSubritBensin2,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(cbSubritBensin3,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));
                jPanel5Layout.setVerticalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel21)
                                                                                .addComponent(cbSubIritBensin1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel22)
                                                                                .addComponent(cbSubritBensin2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel23)
                                                                                .addComponent(cbSubritBensin3,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(22, 22, 22)));

                jPanel4.setBackground(new java.awt.Color(22, 65, 53));
                jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kepentingan Mesin / CC",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12),
                                new java.awt.Color(255, 237, 192))); // NOI18N

                jLabel9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel9.setForeground(new java.awt.Color(255, 237, 192));
                jLabel9.setText("Mesin Yang Sangat Penting ke-1");

                jLabel15.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel15.setForeground(new java.awt.Color(255, 237, 192));
                jLabel15.setText("Mesin Cukup Penting ke-2");

                jLabel16.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel16.setForeground(new java.awt.Color(255, 237, 192));
                jLabel16.setText("Mesin biasa ke-3");

                cbSubMesin3.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "- Pilih SubKriteria Mesin / CC -", "Kecil (Entry) 110 – 125 cc",
                                                "Sedang (Mid-range) 150 – 160 cc", "Besar (Premium)  > 160 cc" }));

                cbSubMesin2.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "- Pilih SubKriteria Mesin / CC -", "Kecil (Entry) 110 – 125 cc",
                                                "Sedang (Mid-range) 150 – 160 cc", "Besar (Premium)  > 160 cc" }));

                cbSubMesin1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
                                "- Pilih SubKriteria Mesin / CC -", "Kecil (Entry) 110 – 125 cc",
                                "Sedang (Mid-range) 150 – 160 cc", "Besar (Premium)  > 160 cc", " ", " " }));

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel9)
                                                                                .addComponent(jLabel15)
                                                                                .addComponent(jLabel16))
                                                                .addGap(44, 44, 44)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(cbSubMesin2,
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(cbSubMesin3, 0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(cbSubMesin1, 0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));
                jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel9)
                                                                                .addComponent(cbSubMesin1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel15)
                                                                                .addComponent(cbSubMesin2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel16)
                                                                                .addComponent(cbSubMesin3,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))));

                jPanel6.setBackground(new java.awt.Color(22, 65, 53));
                jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kepentingan Desain",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12),
                                new java.awt.Color(255, 237, 192))); // NOI18N
                jPanel6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

                jLabel10.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel10.setForeground(new java.awt.Color(255, 237, 192));
                jLabel10.setText("Desain Yang Sangat Penting ke-1");

                jLabel25.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel25.setForeground(new java.awt.Color(255, 237, 192));
                jLabel25.setText("Desain Cukup Penting ke-2");

                jLabel26.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel26.setForeground(new java.awt.Color(255, 237, 192));
                jLabel26.setText("Desain biasa ke-3");

                cbSubDesain3.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "- Pilih SubKriteria Desain -", "Sporty/Agresif\t(Klasik, fashionable)",
                                                "Retro/Stylish \t(Aerodinamis, tajam)",
                                                "Futuristik/Modern (Besar, elegan, fitur tinggi)", " " }));
                cbSubDesain3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                cbSubDesain3ActionPerformed(evt);
                        }
                });

                cbSubDesain2.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "- Pilih SubKriteria Desain -", "Sporty/Agresif\t(Klasik, fashionable)",
                                                "Retro/Stylish \t(Aerodinamis, tajam)",
                                                "Futuristik/Modern (Besar, elegan, fitur tinggi)", " " }));

                cbSubDesain1.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "- Pilih SubKriteria Desain -", "Sporty/Agresif\t(Klasik, fashionable)",
                                                "Retro/Stylish \t(Aerodinamis, tajam)",
                                                "Futuristik/Modern (Besar, elegan, fitur tinggi)", " " }));

                javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
                jPanel6.setLayout(jPanel6Layout);
                jPanel6Layout.setHorizontalGroup(
                                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel6Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel10)
                                                                                .addComponent(jLabel25)
                                                                                .addComponent(jLabel26))
                                                                .addGap(18, 18, Short.MAX_VALUE)
                                                                .addGroup(jPanel6Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(cbSubDesain3, 0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(cbSubDesain2, 0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(cbSubDesain1, 0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));
                jPanel6Layout.setVerticalGroup(
                                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel6Layout.createSequentialGroup()
                                                                .addGroup(jPanel6Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel10)
                                                                                .addComponent(cbSubDesain1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel6Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(cbSubDesain2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel25))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel6Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(cbSubDesain3,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel26))));

                tombolSimpan.setBackground(new java.awt.Color(0, 0, 51));
                tombolSimpan.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                tombolSimpan.setForeground(new java.awt.Color(255, 255, 255));
                tombolSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image (4).png"))); // NOI18N
                tombolSimpan.setText("SIMPAN");
                tombolSimpan.setBorder(null);
                tombolSimpan.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                tombolSimpanActionPerformed(evt);
                        }
                });

                tombolEdit.setBackground(new java.awt.Color(0, 0, 51));
                tombolEdit.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                tombolEdit.setForeground(new java.awt.Color(255, 255, 255));
                tombolEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image (11).png"))); // NOI18N
                tombolEdit.setText("UBAH");
                tombolEdit.setBorder(null);
                tombolEdit.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                tombolEditActionPerformed(evt);
                        }
                });

                tombolHapus.setBackground(new java.awt.Color(0, 0, 51));
                tombolHapus.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                tombolHapus.setForeground(new java.awt.Color(255, 255, 255));
                tombolHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image (11).png"))); // NOI18N
                tombolHapus.setText("HAPUS");
                tombolHapus.setBorder(null);
                tombolHapus.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                tombolHapusActionPerformed(evt);
                        }
                });

                jLabel2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
                jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                jLabel2.setText("Catatan : Edit data, klik data pada tabel terlebih dahulu");

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                false)
                                                                                                .addComponent(jScrollPane1)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(tombolSimpan,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                120,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGap(18, 18, 18)
                                                                                                                .addComponent(tombolEdit,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                120,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGap(18, 18, 18)
                                                                                                                .addComponent(tombolHapus,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                120,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addComponent(jPanel6,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE))
                                                                                .addComponent(jLabel2))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(jPanel3,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel4,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel5,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jPanel3,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jPanel4,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jPanel5,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                .addComponent(tombolSimpan,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                35,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(tombolEdit,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                35,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(tombolHapus,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                35,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jScrollPane1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                260,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel2)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jPanel6,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(184, Short.MAX_VALUE)));

                jPanel3.getAccessibleContext().setAccessibleName("");

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(judul, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                1002, Short.MAX_VALUE)
                                                                .addContainerGap()));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addComponent(judul,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                74,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, 0)
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                626, Short.MAX_VALUE)));
        }// </editor-fold>//GEN-END:initComponents

        private void tombolSimpanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tombolSimpanActionPerformed
                insertDataSubKriteria();
        }// GEN-LAST:event_tombolSimpanActionPerformed

        private void tombolEditActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tombolEditActionPerformed
                editDataSubKriteria();
        }// GEN-LAST:event_tombolEditActionPerformed

        private void tombolHapusActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tombolHapusActionPerformed
                hapusDataSubKriteria();
        }// GEN-LAST:event_tombolHapusActionPerformed

        private void tabelSubKriteriaMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tabelSubKriteriaMouseClicked
                getDataTabel();
        }// GEN-LAST:event_tabelSubKriteriaMouseClicked

        private void cbSubDesain3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbSubDesain3ActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cbSubDesain3ActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JComboBox<String> cbSubDesain1;
        private javax.swing.JComboBox<String> cbSubDesain2;
        private javax.swing.JComboBox<String> cbSubDesain3;
        private javax.swing.JComboBox<String> cbSubHarga2;
        private javax.swing.JComboBox<String> cbSubHarga3;
        private javax.swing.JComboBox<String> cbSubIritBensin1;
        private javax.swing.JComboBox<String> cbSubMesin1;
        private javax.swing.JComboBox<String> cbSubMesin2;
        private javax.swing.JComboBox<String> cbSubMesin3;
        private javax.swing.JComboBox<String> cbSubharga1;
        private javax.swing.JComboBox<String> cbSubritBensin2;
        private javax.swing.JComboBox<String> cbSubritBensin3;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel15;
        private javax.swing.JLabel jLabel16;
        private javax.swing.JLabel jLabel17;
        private javax.swing.JLabel jLabel18;
        private javax.swing.JLabel jLabel19;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel21;
        private javax.swing.JLabel jLabel22;
        private javax.swing.JLabel jLabel23;
        private javax.swing.JLabel jLabel25;
        private javax.swing.JLabel jLabel26;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JLabel judul;
        private javax.swing.JTable tabelSubKriteria;
        private javax.swing.JButton tombolEdit;
        private javax.swing.JButton tombolHapus;
        private javax.swing.JButton tombolSimpan;
        // End of variables declaration//GEN-END:variables
}
