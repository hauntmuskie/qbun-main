
package menuutama;

import java.awt.Color;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JRootPane;
import koneksi.Koneksi;

/**
 *
 * @author Neubri
 */
public class DialogTambahData extends javax.swing.JDialog {
        private Connection conn = new Koneksi().connect();

        // private DefaultTableModel tabmode; // Removed - variable not used
        /**
         * Creates new form DialogTambahData
         */
        public DialogTambahData(java.awt.Frame parent, boolean modal) {
                super(parent, modal);
                initComponents();

                // add Panel, add panel(sebuah panel)
                Pane.add(PanelKandidat);
                Pane.repaint();
                Pane.revalidate();
        }

        public void setDataTabel(String id_motor, String nama_motor, String merek, String tahun_produksi,
                        String warna_motor,
                        String kategori_harga, String kategori_cc, String kategori_irit, String kategori_desain) {
                tNoId.setText(id_motor);
                tNoId.setEnabled(false); // Disable ID field when editing
                tMotor.setText(nama_motor);
                if (merek.equals("Yamaha")) {
                        rbYamaha.setSelected(true);
                } else if (merek.equals("Honda")) {
                        rbHonda.setSelected(true);
                }
                cbTahunProduksi.setSelectedItem(tahun_produksi);
                tWarnaMotor.setText(warna_motor);
                cbHarga.setSelectedItem(kategori_harga);
                cbmesin.setSelectedItem(kategori_cc);
                cbIritBensin.setSelectedItem(kategori_irit);
                cbDesain.setSelectedItem(kategori_desain);
        }

        protected void kosong() {
                // Generate incremental ID for new data
                generateNextId();
                tMotor.setText("");
                btnG.clearSelection();
                cbTahunProduksi.setSelectedIndex(0);
                tWarnaMotor.setText("");
                cbHarga.setSelectedIndex(0);
                cbmesin.setSelectedIndex(0);
                cbIritBensin.setSelectedIndex(0);
                cbDesain.setSelectedIndex(0);
                tNoId.setEnabled(true); // Enable ID field for new data
        }

        private void generateNextId() {
                try {
                        String sql = "SELECT MAX(CAST(SUBSTRING(id_motor, 2) AS UNSIGNED)) as max_id FROM data_motor";
                        java.sql.Statement stat = conn.createStatement();
                        ResultSet hasil = stat.executeQuery(sql);
                        int nextId = 1;
                        if (hasil.next()) {
                                String maxIdStr = hasil.getString("max_id");
                                if (maxIdStr != null) {
                                        nextId = Integer.parseInt(maxIdStr) + 1;
                                }
                        }
                        tNoId.setText("M" + String.format("%03d", nextId));
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error generating ID: " + e);
                        tNoId.setText("M001"); // Default fallback
                }
        }

        private void insertDataPaket() {
                String sql = "INSERT INTO data_motor VALUES (?,?,?,?,?,?,?,?,?)";
                try {
                        PreparedStatement stat = conn.prepareStatement(sql);
                        stat.setString(1, tNoId.getText());
                        stat.setString(2, tMotor.getText());

                        String merek = "";
                        if (rbYamaha.isSelected()) {
                                merek = "Yamaha";
                        } else if (rbHonda.isSelected()) {
                                merek = "Honda";
                        }
                        stat.setString(3, merek);
                        stat.setString(4, cbTahunProduksi.getSelectedItem().toString()); // tahun_produksi
                        stat.setString(5, tWarnaMotor.getText()); // warna_motor
                        stat.setString(6, cbHarga.getSelectedItem().toString()); // kategori_harga
                        stat.setString(7, cbmesin.getSelectedItem().toString()); // kategori_cc
                        stat.setString(8, cbIritBensin.getSelectedItem().toString()); // kategori_irit
                        stat.setString(9, cbDesain.getSelectedItem().toString()); // kategori_desain
                        stat.executeUpdate();
                        JOptionPane.showMessageDialog(null, "DATA Motor Berhasil Disimpan");
                        kosong();
                        tNoId.requestFocus();
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Data Gagal Disimpan " + e);
                }
        }

        private void editDataPaket() {
                try {
                        String sql = "UPDATE data_motor set nama_motor=?, merek=?, tahun_produksi=?, warna_motor=?, kategori_harga=?, kategori_cc=?, kategori_irit=?, kategori_desain=? WHERE id_motor=?";
                        PreparedStatement stat = conn.prepareStatement(sql);
                        stat.setString(1, tMotor.getText());
                        String merek = "";
                        if (rbYamaha.isSelected()) {
                                merek = "Yamaha";
                        } else if (rbHonda.isSelected()) {
                                merek = "Honda";
                        }
                        stat.setString(2, merek);
                        stat.setString(3, cbTahunProduksi.getSelectedItem().toString()); // tahun_produksi
                        stat.setString(4, tWarnaMotor.getText()); // warna_motor
                        stat.setString(5, cbHarga.getSelectedItem().toString()); // kategori_harga
                        stat.setString(6, cbmesin.getSelectedItem().toString()); // kategori_cc
                        stat.setString(7, cbIritBensin.getSelectedItem().toString()); // kategori_irit
                        stat.setString(8, cbDesain.getSelectedItem().toString()); // kategori_desain
                        stat.setString(9, tNoId.getText()); // WHERE clause parameter

                        stat.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Data Motor Berhasil diUbah ");
                        kosong();
                        tNoId.requestFocus();
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

                btnG = new javax.swing.ButtonGroup();
                PanelKandidat = new javax.swing.JPanel();
                tombolEdit = new javax.swing.JLabel();
                judul = new javax.swing.JLabel();
                jPanel12 = new javax.swing.JPanel();
                jLabel24 = new javax.swing.JLabel();
                jLabel25 = new javax.swing.JLabel();
                jLabel26 = new javax.swing.JLabel();
                cbmesin = new javax.swing.JComboBox<>();
                cbIritBensin = new javax.swing.JComboBox<>();
                jLabel27 = new javax.swing.JLabel();
                cbDesain = new javax.swing.JComboBox<>();
                cbHarga = new javax.swing.JComboBox<>();
                jPanel11 = new javax.swing.JPanel();
                jLabel6 = new javax.swing.JLabel();
                jLabel7 = new javax.swing.JLabel();
                jLabel22 = new javax.swing.JLabel();
                tMotor = new javax.swing.JTextField();
                tWarnaMotor = new javax.swing.JTextField();
                rbYamaha = new javax.swing.JRadioButton();
                rbHonda = new javax.swing.JRadioButton();
                jLabel28 = new javax.swing.JLabel();
                jLabel9 = new javax.swing.JLabel();
                tNoId = new javax.swing.JTextField();
                cbTahunProduksi = new javax.swing.JComboBox<>();
                tombolTambah = new javax.swing.JLabel();
                Pane = new javax.swing.JPanel();

                PanelKandidat.setBackground(new java.awt.Color(204, 204, 204));

                tombolEdit.setBackground(new java.awt.Color(0, 0, 51));
                tombolEdit.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                tombolEdit.setForeground(new java.awt.Color(255, 255, 255));
                tombolEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                tombolEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image (11).png"))); // NOI18N
                tombolEdit.setText("UBAH");
                tombolEdit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                tombolEdit.setOpaque(true);
                tombolEdit.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                tombolEditMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                tombolEditMouseEntered(evt);
                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                                tombolEditMouseExited(evt);
                        }
                });

                judul.setBackground(new java.awt.Color(255, 0, 0));
                judul.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
                judul.setForeground(new java.awt.Color(255, 255, 255));
                judul.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                judul.setText("Tambah Data Motor");
                judul.setOpaque(true);

                jPanel12.setBackground(new java.awt.Color(51, 51, 51));
                jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Penilaian Bobot Motor",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12),
                                new java.awt.Color(255, 237, 192))); // NOI18N
                jPanel12.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

                jLabel24.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel24.setForeground(new java.awt.Color(255, 237, 192));
                jLabel24.setText("Harga ");

                jLabel25.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel25.setForeground(new java.awt.Color(255, 237, 192));
                jLabel25.setText("Mesin");

                jLabel26.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel26.setForeground(new java.awt.Color(255, 237, 192));
                jLabel26.setText("Irit bensin");

                cbmesin.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                cbmesin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
                                "- Pilih SubKriteria Mesin / CC -", "Kecil (Entry) 110 – 125 cc",
                                "Sedang (Mid-range) 150 – 160 cc", "Besar (Premium)  > 160 cc", " ", " " }));
                cbmesin.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                cbmesinActionPerformed(evt);
                        }
                });

                cbIritBensin.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                cbIritBensin.setModel(
                                new javax.swing.DefaultComboBoxModel<>(new String[] { "- SubKriteria Irit Bensin -",
                                                "Irit ≥ 50 km/l", "Sedang 40–49 km/l", "Boros < 40 km/l", " " }));

                jLabel27.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel27.setForeground(new java.awt.Color(255, 237, 192));
                jLabel27.setText("Desain");

                cbDesain.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                cbDesain.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih SubKriteria Desain -",
                                "Sporty/Agresif\t(Klasik, fashionable)", "Retro/Stylish \t(Aerodinamis, tajam)",
                                "Futuristik/Modern (Besar, elegan, fitur tinggi)", " " }));

                cbHarga.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                cbHarga.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih SubKriteria Harga -",
                                "EKONOMIS ≤ 22 juta", "MENENGAH 22 – 35 juta", "PREMIUM > 35 juta", " ", " " }));

                javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
                jPanel12.setLayout(jPanel12Layout);
                jPanel12Layout.setHorizontalGroup(
                                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel12Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel12Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel12Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel12Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jLabel24)
                                                                                                                .addComponent(jLabel25)
                                                                                                                .addComponent(jLabel26))
                                                                                                .addGap(23, 23, 23)
                                                                                                .addGroup(jPanel12Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(cbmesin,
                                                                                                                                0,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(cbIritBensin,
                                                                                                                                0,
                                                                                                                                341,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(cbHarga,
                                                                                                                                0,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)))
                                                                                .addGroup(jPanel12Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel27)
                                                                                                .addGap(23, 23, 23)
                                                                                                .addComponent(cbDesain,
                                                                                                                0, 341,
                                                                                                                Short.MAX_VALUE)))
                                                                .addContainerGap()));
                jPanel12Layout.setVerticalGroup(
                                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel12Layout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addGroup(jPanel12Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel24)
                                                                                .addComponent(cbHarga,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel12Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel25)
                                                                                .addComponent(cbmesin,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel12Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel26)
                                                                                .addComponent(cbIritBensin,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel12Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel27)
                                                                                .addComponent(cbDesain,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(111, Short.MAX_VALUE)));

                jPanel11.setBackground(new java.awt.Color(51, 51, 51));
                jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Motor",
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12),
                                new java.awt.Color(255, 237, 192))); // NOI18N
                jPanel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

                jLabel6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel6.setForeground(new java.awt.Color(255, 237, 192));
                jLabel6.setText("Nama Motor");

                jLabel7.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel7.setForeground(new java.awt.Color(255, 237, 192));
                jLabel7.setText("Merek Motor ");

                jLabel22.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel22.setForeground(new java.awt.Color(255, 237, 192));
                jLabel22.setText("Warna Motor");

                tMotor.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

                tWarnaMotor.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

                rbYamaha.setBackground(new java.awt.Color(51, 51, 51));
                btnG.add(rbYamaha);
                rbYamaha.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                rbYamaha.setForeground(new java.awt.Color(255, 237, 192));
                rbYamaha.setText("YAMAHA");
                rbYamaha.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                rbYamahaActionPerformed(evt);
                        }
                });

                rbHonda.setBackground(new java.awt.Color(51, 51, 51));
                btnG.add(rbHonda);
                rbHonda.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                rbHonda.setForeground(new java.awt.Color(255, 237, 192));
                rbHonda.setText("HONDA");

                jLabel28.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel28.setForeground(new java.awt.Color(255, 237, 192));
                jLabel28.setText("Tahun Produksi");

                jLabel9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                jLabel9.setForeground(new java.awt.Color(255, 237, 192));
                jLabel9.setText("ID Motor");

                tNoId.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

                cbTahunProduksi.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                cbTahunProduksi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
                                "- Pilih Tahun Produksi -", "2020", "2021", "2022", "2023", "2024", "2025" }));

                javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
                jPanel11.setLayout(jPanel11Layout);
                jPanel11Layout.setHorizontalGroup(
                                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel11Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel11Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel6)
                                                                                .addComponent(jLabel7)
                                                                                .addComponent(jLabel22)
                                                                                .addComponent(jLabel28)
                                                                                .addComponent(jLabel9))
                                                                .addGap(24, 24, 24)
                                                                .addGroup(jPanel11Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(tNoId)
                                                                                .addComponent(tMotor)
                                                                                .addComponent(tWarnaMotor)
                                                                                .addGroup(jPanel11Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(rbYamaha)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(rbHonda)
                                                                                                .addGap(0, 106, Short.MAX_VALUE))
                                                                                .addComponent(cbTahunProduksi, 0,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));
                jPanel11Layout.setVerticalGroup(
                                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel11Layout.createSequentialGroup()
                                                                .addGap(9, 9, 9)
                                                                .addGroup(jPanel11Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel9)
                                                                                .addComponent(tNoId,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel11Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel6)
                                                                                .addComponent(tMotor,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel11Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel7)
                                                                                .addComponent(rbYamaha)
                                                                                .addComponent(rbHonda))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel11Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel28)
                                                                                .addComponent(cbTahunProduksi,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel11Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(tWarnaMotor,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel22))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                tombolTambah.setBackground(new java.awt.Color(0, 0, 51));
                tombolTambah.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
                tombolTambah.setForeground(new java.awt.Color(255, 255, 255));
                tombolTambah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                tombolTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/image (5).png"))); // NOI18N
                tombolTambah.setText("TAMBAH");
                tombolTambah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                tombolTambah.setOpaque(true);
                tombolTambah.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                tombolTambahMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                tombolTambahMouseEntered(evt);
                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                                tombolTambahMouseExited(evt);
                        }
                });

                javax.swing.GroupLayout PanelKandidatLayout = new javax.swing.GroupLayout(PanelKandidat);
                PanelKandidat.setLayout(PanelKandidatLayout);
                PanelKandidatLayout.setHorizontalGroup(
                                PanelKandidatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(judul, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(PanelKandidatLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(PanelKandidatLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(PanelKandidatLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jPanel11,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(jPanel12,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addContainerGap(97,
                                                                                                                Short.MAX_VALUE))
                                                                                .addGroup(PanelKandidatLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(tombolTambah,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                100,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(tombolEdit,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                100,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 0, Short.MAX_VALUE)))));
                PanelKandidatLayout.setVerticalGroup(
                                PanelKandidatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(PanelKandidatLayout.createSequentialGroup()
                                                                .addComponent(judul,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                62,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(14, 14, 14)
                                                                .addGroup(PanelKandidatLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(tombolTambah,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                34,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(tombolEdit,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                34,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(PanelKandidatLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(jPanel12,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel11,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap(202, Short.MAX_VALUE)));

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setName("dialog1"); // NOI18N

                Pane.setLayout(new java.awt.CardLayout());

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(Pane, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(Pane, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

                setSize(new java.awt.Dimension(888, 577));
                setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

        private void tombolTambahMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tombolTambahMouseClicked
                // TODO add your handling code here:
                if (!tMotor.getText().equals("") && btnG.getSelection() != null
                                && cbTahunProduksi.getSelectedIndex() != 0
                                && !tWarnaMotor.getText().equals("") // Fields for motor data entry
                                && cbHarga.getSelectedIndex() != 0
                                && cbmesin.getSelectedIndex() != 0 && cbIritBensin.getSelectedIndex() != 0
                                && cbDesain.getSelectedIndex() != 0) {
                        insertDataPaket();
                        dispose();
                } else {
                        JOptionPane.showMessageDialog(rootPane, "Mohon isi semua kolom data motor pada form !", "Error",
                                        ERROR_MESSAGE);
                }
        }// GEN-LAST:event_tombolTambahMouseClicked

        private void tombolTambahMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tombolTambahMouseEntered
                // TODO add your handling code here:
                tombolTambah.setBackground(new Color(0, 51, 153));
                tombolTambah.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }// GEN-LAST:event_tombolTambahMouseEntered

        private void tombolTambahMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tombolTambahMouseExited
                // TODO add your handling code here:
                tombolTambah.setBackground(new Color(0, 51, 102));
        }// GEN-LAST:event_tombolTambahMouseExited

        private void tombolEditMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tombolEditMouseClicked
                // TODO add your handling code here:
                editDataPaket();
                dispose();
        }// GEN-LAST:event_tombolEditMouseClicked

        private void tombolEditMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tombolEditMouseEntered
                // TODO add your handling code here:
                tombolEdit.setBackground(new Color(0, 51, 153));
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }// GEN-LAST:event_tombolEditMouseEntered

        private void tombolEditMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tombolEditMouseExited
                // TODO add your handling code here:
                tombolEdit.setBackground(new Color(0, 51, 102));
        }// GEN-LAST:event_tombolEditMouseExited

        private void rbYamahaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_rbYamahaActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_rbYamahaActionPerformed

        private void cbmesinActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbmesinActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_cbmesinActionPerformed

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
                        java.util.logging.Logger.getLogger(DialogTambahData.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(DialogTambahData.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(DialogTambahData.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(DialogTambahData.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                }
                // </editor-fold>
                // </editor-fold>

                /* Create and display the dialog */
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                DialogTambahData dialog = new DialogTambahData(new javax.swing.JFrame(), true);
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
        private javax.swing.JPanel Pane;
        private javax.swing.JPanel PanelKandidat;
        private javax.swing.ButtonGroup btnG;
        private javax.swing.JComboBox<String> cbDesain;
        private javax.swing.JComboBox<String> cbHarga;
        private javax.swing.JComboBox<String> cbIritBensin;
        private javax.swing.JComboBox<String> cbTahunProduksi;
        private javax.swing.JComboBox<String> cbmesin;
        private javax.swing.JLabel jLabel22;
        private javax.swing.JLabel jLabel24;
        private javax.swing.JLabel jLabel25;
        private javax.swing.JLabel jLabel26;
        private javax.swing.JLabel jLabel27;
        private javax.swing.JLabel jLabel28;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel11;
        private javax.swing.JPanel jPanel12;
        private javax.swing.JLabel judul;
        private javax.swing.JRadioButton rbHonda;
        private javax.swing.JRadioButton rbYamaha;
        private javax.swing.JTextField tMotor;
        private javax.swing.JTextField tNoId;
        private javax.swing.JTextField tWarnaMotor;
        private javax.swing.JLabel tombolEdit;
        private javax.swing.JLabel tombolTambah;
        // End of variables declaration//GEN-END:variables

        void show(JRootPane rootPane) {
                throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods,
                                                                               // choose
                                                                               // Tools | Templates.
        }
}
