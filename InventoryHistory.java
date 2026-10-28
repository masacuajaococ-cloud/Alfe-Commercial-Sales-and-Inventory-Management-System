/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class InventoryHistory extends javax.swing.JFrame {
    
    
    private javax.swing.JTable historyTable;
    private javax.swing.JScrollPane historyScrollPane;
    private javax.swing.table.DefaultTableModel historyModel;
    private javax.swing.JPopupMenu searchPopup;
    private javax.swing.JList<String> suggestionList;
    private javax.swing.DefaultListModel<String> suggestionModel;

    private java.util.ArrayList<String> suggestionProductIds =
            new java.util.ArrayList<>();

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InventoryHistory.class.getName());

    /**
     * Creates new form InventoryHistory
     */
    
    public InventoryHistory() {
    initComponents();
    setupHistoryTable();
    setupSearchAutocomplete();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setResizable(true);
    setMinimumSize(new java.awt.Dimension(900, 600));
    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

    javax.swing.SwingUtilities.invokeLater(() -> {
    jPanel1.revalidate();
    jPanel1.repaint();
    loadHistory();
    });
}

    private void setupSearchAutocomplete() {
    suggestionModel = new javax.swing.DefaultListModel<>();
    suggestionList = new javax.swing.JList<>(suggestionModel);

    suggestionList.setSelectionMode(
        javax.swing.ListSelectionModel.SINGLE_SELECTION
    );
    suggestionList.setFont(
        new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13)
    );

    searchPopup = new javax.swing.JPopupMenu();
    searchPopup.setFocusable(false);

    javax.swing.JScrollPane scrollPane =
        new javax.swing.JScrollPane(suggestionList);

    scrollPane.setPreferredSize(new java.awt.Dimension(500, 160));
    searchPopup.add(scrollPane);

    jTextField1.getDocument().addDocumentListener(
        new javax.swing.event.DocumentListener() {
            private void updateSuggestions() {
                javax.swing.SwingUtilities.invokeLater(
                    () -> showHistorySuggestions()
                );
            }

            @Override
            public void insertUpdate(
                    javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void removeUpdate(
                    javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void changedUpdate(
                    javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }
        }
    );
    
    jTextField1.addKeyListener(
    new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {

            int key = e.getKeyCode();

            if (key == java.awt.event.KeyEvent.VK_ESCAPE) {
                searchPopup.setVisible(false);
                e.consume();
                return;
            }

            if (!searchPopup.isVisible()) {
                if (key == java.awt.event.KeyEvent.VK_DOWN) {
                    showHistorySuggestions();
                    e.consume();
                } else if (key == java.awt.event.KeyEvent.VK_ENTER) {
                    searchHistory();
                    e.consume();
                }
                return;
            }

            int selected = suggestionList.getSelectedIndex();

            if (key == java.awt.event.KeyEvent.VK_DOWN) {

                if (selected < suggestionModel.getSize() - 1) {
                    suggestionList.setSelectedIndex(selected + 1);
                    suggestionList.ensureIndexIsVisible(selected + 1);
                }

                e.consume();

            } else if (key == java.awt.event.KeyEvent.VK_UP) {

                if (selected > 0) {
                    suggestionList.setSelectedIndex(selected - 1);
                    suggestionList.ensureIndexIsVisible(selected - 1);
                }

                e.consume();

            } else if (key == java.awt.event.KeyEvent.VK_ENTER) {

                if (suggestionList.getSelectedIndex() >= 0) {
                    selectHistorySuggestion();
                } else {
                    searchPopup.setVisible(false);
                    searchHistory();
                }

                e.consume();
            }
        }
    }
);

    suggestionList.addMouseListener(
        new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectHistorySuggestion();
                }
            }
        }
    );
}
    
private void showHistorySuggestions() {
    if (searchPopup == null) {
        return;
    }

    String keyword = jTextField1.getText().trim();

    if (keyword.equalsIgnoreCase(
        "Search product, staff, or reason...")) {
    keyword = "";
    }

    suggestionModel.clear();
    suggestionProductIds.clear();

    String sql =
        "SELECT DISTINCT h.product_id, p.name AS product_name, " +
        "CONCAT(u.first_name, ' ', u.last_name) AS performed_by, " +
        "h.reason " +
        "FROM stock_history h " +
        "LEFT JOIN products p ON h.product_id = p.product_id " +
        "LEFT JOIN users u ON h.user_id = u.id " +
        "WHERE h.product_id LIKE ? " +
        "OR p.name LIKE ? " +
        "OR CONCAT(u.first_name, ' ', u.last_name) LIKE ? " +
        "OR h.reason LIKE ? " +
        "ORDER BY p.name ASC LIMIT 8";

    try (
        java.sql.Connection con = new DatabaseConnection().connect();
        java.sql.PreparedStatement pst = con.prepareStatement(sql)
    ) {
        String search = "%" + keyword + "%";

        pst.setString(1, search);
        pst.setString(2, search);
        pst.setString(3, search);
        pst.setString(4, search);

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String productId = rs.getString("product_id");
                String productName = rs.getString("product_name");
                String performedBy = rs.getString("performed_by");
                String reason = rs.getString("reason");

                if (productName == null || productName.trim().isEmpty()) {
                    productName = productId;
                }

                if (performedBy == null || performedBy.trim().isEmpty()) {
                    performedBy = "Unknown";
                }

                String suggestion =
                    productName + " (" + productId + ")"
                    + " | Staff: " + performedBy
                    + " | Reason: "
                    + (reason == null || reason.trim().isEmpty()
                        ? "-" : reason);

                suggestionModel.addElement(suggestion);
                suggestionProductIds.add(productId);
            }
        }

    } catch (java.sql.SQLException e) {
        System.err.println(
            "Failed to load search suggestions: " + e.getMessage()
        );
    }

    if (suggestionModel.isEmpty()
            || !jTextField1.isFocusOwner()) {
        searchPopup.setVisible(false);
        return;
    }

    suggestionList.setSelectedIndex(0);

    if (searchPopup.isVisible()) {
        searchPopup.setVisible(false);
    }

    searchPopup.show(
        jTextField1,
        0,
        jTextField1.getHeight()
    );
}

private void selectHistorySuggestion() {
    int selected = suggestionList.getSelectedIndex();

    if (selected < 0 || selected >= suggestionProductIds.size()) {
        return;
    }

    String productId = suggestionProductIds.get(selected);

    searchPopup.setVisible(false);

    // Search the selected product's complete history.
    jTextField1.setText(productId);
    jTextField1.setForeground(java.awt.Color.BLACK);

    searchHistory();
}



private void setupHistoryTable() {
    historyModel = new javax.swing.table.DefaultTableModel(
        new Object[]{
            "DATE & TIME",
            "PRODUCT",
            "ACTION",
            "PERFORMED BY",
            "QUANTITY",
            "STOCK BEFORE",
            "STOCK AFTER",
            "REASON"
        }, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    historyTable = jTable1;
    historyTable.setModel(historyModel);

    historyTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
    historyTable.setRowHeight(34);
    historyTable.setSelectionMode(
        javax.swing.ListSelectionModel.SINGLE_SELECTION
    );
    historyTable.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
    );
    historyTable.setFillsViewportHeight(true);
    historyTable.setShowGrid(true);
    historyTable.setGridColor(new java.awt.Color(230, 230, 230));
    historyTable.setBackground(java.awt.Color.WHITE);
    historyTable.setForeground(new java.awt.Color(40, 40, 40));
    historyTable.setSelectionBackground(new java.awt.Color(255, 220, 220));
    historyTable.setSelectionForeground(java.awt.Color.BLACK);

    historyTable.getTableHeader().setFont(
        new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)
    );
    historyTable.getTableHeader().setReorderingAllowed(false);
    historyTable.getTableHeader().setPreferredSize(
        new java.awt.Dimension(0, 35)
    );

    historyScrollPane = jScrollPane1;
    historyScrollPane.setViewportView(historyTable);
    historyScrollPane.setHorizontalScrollBarPolicy(
        javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    historyScrollPane.setVerticalScrollBarPolicy(
        javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    historyScrollPane.getViewport().setBackground(java.awt.Color.WHITE);
    
}

    
     
private void loadHistory() {
    String sql =
        "SELECT h.created_at, h.product_id, " +
        "p.name AS product_name, h.action_type, " +
        "CONCAT(u.first_name, ' ', u.last_name) AS performed_by, " +
        "h.quantity_changed, h.stock_before, h.stock_after, h.reason " +
        "FROM stock_history h " +
        "LEFT JOIN products p ON h.product_id = p.product_id " +
        "LEFT JOIN users u ON h.user_id = u.id " +
        "ORDER BY h.created_at DESC, h.id DESC";

    try (
        java.sql.Connection con = new DatabaseConnection().connect();
        java.sql.PreparedStatement pst = con.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery()
    ) {
        historyModel.setRowCount(0);

        while (rs.next()) {
            String productName = rs.getString("product_name");
            String productId = rs.getString("product_id");
            String performedBy = rs.getString("performed_by");
            String reason = rs.getString("reason");

            if (productName == null || productName.trim().isEmpty()) {
                productName = productId;
            }

            if (performedBy == null || performedBy.trim().isEmpty()) {
                performedBy = "Unknown";
            }

            if (reason == null || reason.trim().isEmpty()) {
                reason = "-";
            }

            historyModel.addRow(new Object[]{
                rs.getTimestamp("created_at"),
                productName + " (" + productId + ")",
                rs.getString("action_type"),
                performedBy,
                rs.getInt("quantity_changed"),
                rs.getInt("stock_before"),
                rs.getInt("stock_after"),
                reason
            });
        }

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Failed to load inventory history:\n" + e.getMessage(),
            "Database Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}


private void searchHistory() {
    if (historyModel == null) {
        return;
    }

    String keyword = jTextField1.getText().trim();

if (keyword.equals("Search product, staff, or reason...")) {
    keyword = "";
}
    String action = (String) jComboBox1.getSelectedItem();

    if (action == null) {
        action = "All Actions";
    }

    String sql =
        "SELECT h.created_at, h.product_id, " +
        "p.name AS product_name, h.action_type, " +
        "CONCAT(u.first_name, ' ', u.last_name) AS performed_by, " +
        "h.quantity_changed, h.stock_before, h.stock_after, h.reason " +
        "FROM stock_history h " +
        "LEFT JOIN products p ON h.product_id = p.product_id " +
        "LEFT JOIN users u ON h.user_id = u.id " +
        "WHERE (? = '' OR h.product_id LIKE ? " +
        "OR p.name LIKE ? " +
        "OR CONCAT(u.first_name, ' ', u.last_name) LIKE ? " +
        "OR h.reason LIKE ?) " +
        "AND (? = 'All Actions' OR h.action_type = ?) " +
        "ORDER BY h.created_at DESC, h.id DESC";

    try (
        java.sql.Connection con = new DatabaseConnection().connect();
        java.sql.PreparedStatement pst = con.prepareStatement(sql)
    ) {
        String search = "%" + keyword + "%";

        pst.setString(1, keyword);
        pst.setString(2, search);
        pst.setString(3, search);
        pst.setString(4, search);
        pst.setString(5, search);
        pst.setString(6, action);
        pst.setString(7, action);

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            historyModel.setRowCount(0);

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String productId = rs.getString("product_id");
                String performedBy = rs.getString("performed_by");
                String reason = rs.getString("reason");

                if (productName == null || productName.trim().isEmpty()) {
                    productName = productId;
                }

                if (performedBy == null || performedBy.trim().isEmpty()) {
                    performedBy = "Unknown";
                }

                if (reason == null || reason.trim().isEmpty()) {
                    reason = "-";
                }

                historyModel.addRow(new Object[]{
                    rs.getTimestamp("created_at"),
                    productName + " (" + productId + ")",
                    rs.getString("action_type"),
                    performedBy,
                    rs.getInt("quantity_changed"),
                    rs.getInt("stock_before"),
                    rs.getInt("stock_after"),
                    reason
                });
            }
        }

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Search failed:\n" + e.getMessage(),
            "Database Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton8 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(java.awt.SystemColor.control);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 153, 153));
        jButton1.setText("Products");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(153, 153, 153));
        jButton2.setText("Dashboard");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(153, 153, 153));
        jButton3.setText("Inventory");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton5.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(153, 153, 153));
        jButton5.setText("Transactions");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setBackground(new java.awt.Color(255, 153, 153));
        jButton6.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Logout");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton4.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(153, 153, 153));
        jButton4.setText("Sales & Reports");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton7.setBackground(new java.awt.Color(204, 0, 0));
        jButton7.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Inventory History");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton10.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(153, 153, 153));
        jButton10.setText("Account Settings");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel4.setText("Search");

        jTextField1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N

        jComboBox1.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Actions", "STOCK IN", "STOCK OUT", "ADJUSTMENT", "SALE" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jButton8.setBackground(new java.awt.Color(204, 0, 0));
        jButton8.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Search");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel5.setText("History Records");

        jPanel6.setBackground(new java.awt.Color(204, 0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AlfeLogoRedHorizontal (1).png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Inventory History");

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Monitor stock movement and staff accountability.");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(1318, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "History ID", "Product Name", "Action", "Description", "Quantity", "Previous Stock", "New Stock", "Date & Time"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jButton8)
                        .addGap(34, 34, 34)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel4)
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Products products = new Products();
        products.setLocationRelativeTo(this);
        products.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Inventory inventory = new Inventory();
        inventory.setLocationRelativeTo(this);
        inventory.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Transactions transactions = new Transactions();
        transactions.setLocationRelativeTo(this);
        transactions.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int choice = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (choice == javax.swing.JOptionPane.YES_OPTION) {

            Login login = new Login();
            login.setLocationRelativeTo(this);
            login.setVisible(true);

            this.dispose();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        SalesAndReports salesAndReports = new SalesAndReports();
        salesAndReports.setLocationRelativeTo(this);
        salesAndReports.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Dashboard dashboard = new Dashboard();
    dashboard.setLocationRelativeTo(this);
    dashboard.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (historyModel != null) {
        searchHistory();
    }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (searchPopup != null) {
        searchPopup.setVisible(false);
    }
        searchHistory();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        AccountSettings settings = new AccountSettings();
        settings.setLocationRelativeTo(this);
        settings.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
        InventoryHistory history = new InventoryHistory();
        history.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        history.setVisible(true);
});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
