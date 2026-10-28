/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class Dashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dashboard.class.getName());
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    /**
     * Creates new form Dashboard
    */
    
    
public Dashboard() {
    initComponents();
    
    jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    jPanel15 = new javax.swing.JPanel();
    jPanel16 = new javax.swing.JPanel();

    setupDashboardLists();
    loadDashboardData();

    setupResponsiveDashboard();

    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    setLocationRelativeTo(null);
}



private void setupResponsiveDashboard() {
    java.awt.Color background = new java.awt.Color(245, 246, 250);
    java.awt.Color white = java.awt.Color.WHITE;
    java.awt.Color borderColor = new java.awt.Color(225, 228, 235);

    jPanel1.removeAll();
    jPanel1.setLayout(new java.awt.BorderLayout(0, 0));
    jPanel1.setBackground(background);

    // Sidebar and header
    jPanel2.setPreferredSize(new java.awt.Dimension(272, 0));
    jPanel2.setMinimumSize(new java.awt.Dimension(250, 0));
    jPanel2.setBackground(white);
    jPanel3.setPreferredSize(new java.awt.Dimension(0, 90));

    // Configure the four metric cards
    centerMetricCard(jPanel9, jLabel16, jLabel17, jLabel25);
    centerMetricCard(jPanel7, jLabel12, jLabel13, jLabel27);
    centerMetricCard(jPanel8, jLabel14, jLabel15, jLabel24);
    centerMetricCard(jPanel6, jLabel10, jLabel11, jLabel26);

    // Two rows of metric cards
    javax.swing.JPanel cardsPanel = new javax.swing.JPanel(
        new java.awt.GridLayout(2, 2, 18, 14)
    );
    cardsPanel.setOpaque(false);

    cardsPanel.add(jPanel9); // Total Products
    cardsPanel.add(jPanel7); // Total Sales
    cardsPanel.add(jPanel8); // Total Stock
    cardsPanel.add(jPanel6); // Transactions

    // Table sections
    javax.swing.JPanel lowStockPanel = createTableSection(
        jLabel18, jScrollPane1, white, borderColor
    );

    javax.swing.JPanel transactionsPanel = createTableSection(
        jLabel21, jScrollPane2, white, borderColor
    );

    javax.swing.JPanel tablesPanel = new javax.swing.JPanel(
        new java.awt.GridLayout(1, 2, 20, 0)
    );
    tablesPanel.setOpaque(false);
    tablesPanel.add(lowStockPanel);
    tablesPanel.add(transactionsPanel);

    // Main responsive content
    javax.swing.JPanel contentPanel = new javax.swing.JPanel(
        new java.awt.GridBagLayout()
    );
    contentPanel.setBackground(background);
    contentPanel.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(20, 24, 30, 24)
    );

    java.awt.GridBagConstraints cardsGbc =
        new java.awt.GridBagConstraints();

    cardsGbc.gridx = 0;
    cardsGbc.gridy = 0;
    cardsGbc.weightx = 1.0;
    cardsGbc.weighty = 0.0;
    cardsGbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    cardsGbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
    cardsGbc.insets = new java.awt.Insets(0, 0, 24, 0);

    contentPanel.add(cardsPanel, cardsGbc);

    java.awt.GridBagConstraints tablesGbc =
        new java.awt.GridBagConstraints();

    tablesGbc.gridx = 0;
    tablesGbc.gridy = 1;
    tablesGbc.weightx = 1.0;
    tablesGbc.weighty = 1.0;
    tablesGbc.fill = java.awt.GridBagConstraints.BOTH;
    tablesGbc.anchor = java.awt.GridBagConstraints.NORTHWEST;

    contentPanel.add(tablesPanel, tablesGbc);

    // Assemble the dashboard
    jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);
    jPanel1.add(jPanel2, java.awt.BorderLayout.WEST);
    jPanel1.add(contentPanel, java.awt.BorderLayout.CENTER);

    getContentPane().removeAll();
    getContentPane().setLayout(new java.awt.BorderLayout());
    getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

    getContentPane().revalidate();
    getContentPane().repaint();
    jPanel1.revalidate();
    jPanel1.repaint();
} // IMPORTANT: setupResponsiveDashboard ends here


private javax.swing.JPanel createTableSection(
        javax.swing.JLabel title,
        javax.swing.JScrollPane scrollPane,
        java.awt.Color white,
        java.awt.Color borderColor) {

    javax.swing.JPanel panel = new javax.swing.JPanel(
        new java.awt.BorderLayout(0, 12)
    );

    panel.setBackground(white);
    panel.setBorder(
        javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(borderColor),
            javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16)
        )
    );

    title.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    title.setFont(
        title.getFont().deriveFont(java.awt.Font.BOLD, 16f)
    );

    scrollPane.setBorder(
        javax.swing.BorderFactory.createLineBorder(borderColor)
    );
    scrollPane.setBackground(white);
    scrollPane.getViewport().setBackground(white);

    panel.add(title, java.awt.BorderLayout.NORTH);
    panel.add(scrollPane, java.awt.BorderLayout.CENTER);

    return panel;
}

private void centerMetricCard(
        javax.swing.JPanel card,
        javax.swing.JLabel title,
        javax.swing.JLabel value,
        javax.swing.JLabel subtitle) {

    card.removeAll();
    card.setLayout(
        new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS)
    );

    card.setBackground(new java.awt.Color(204, 0, 0));
    card.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)
    );

    // Title
    title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    title.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    title.setFont(
        new java.awt.Font("Segoe UI Black", java.awt.Font.BOLD, 18)
    );
    title.setForeground(java.awt.Color.WHITE);

    // Large number
    value.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    value.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    value.setFont(
        new java.awt.Font("Segoe UI Black", java.awt.Font.BOLD, 26)
    );
    value.setForeground(java.awt.Color.WHITE);

    // Smaller descriptive subtitle
    subtitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    subtitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    subtitle.setFont(
        new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13)
    );
    subtitle.setForeground(java.awt.Color.WHITE);

    // Vertically center the three labels inside each card
    card.add(javax.swing.Box.createVerticalGlue());
    card.add(title);
    card.add(javax.swing.Box.createVerticalStrut(8));
    card.add(value);
    card.add(javax.swing.Box.createVerticalStrut(5));
    card.add(subtitle);
    card.add(javax.swing.Box.createVerticalGlue());

    card.setMinimumSize(new java.awt.Dimension(200, 120));
    card.setPreferredSize(new java.awt.Dimension(300, 150));

    card.revalidate();
    card.repaint();
}

    
private void setupDashboardLists() {
    java.awt.Color headerRed = new java.awt.Color(204, 0, 0);
    java.awt.Font headerFont = new java.awt.Font(
        "Segoe UI Black",
        java.awt.Font.PLAIN,
        14
    );

    jPanel15.setLayout(new java.awt.GridBagLayout());
    jPanel16.setLayout(new java.awt.GridBagLayout());

    jPanel15.setBackground(java.awt.Color.WHITE);
    jPanel16.setBackground(java.awt.Color.WHITE);

    jPanel13.removeAll();
    jPanel13.setLayout(new java.awt.GridBagLayout());
    jPanel13.setBackground(headerRed);
    jPanel13.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15)
    );
    jPanel13.setPreferredSize(new java.awt.Dimension(600, 35));

    jLabel19.setText("Item");
    jLabel19.setForeground(java.awt.Color.WHITE);
    jLabel19.setFont(headerFont);
    jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

    jLabel20.setText("Stock Left");
    jLabel20.setForeground(java.awt.Color.WHITE);
    jLabel20.setFont(headerFont);
    jLabel20.setHorizontalAlignment(
    javax.swing.SwingConstants.CENTER
    );

    
java.awt.GridBagConstraints stockHeader =
    new java.awt.GridBagConstraints();

    stockHeader.gridy = 0;
    stockHeader.fill = java.awt.GridBagConstraints.HORIZONTAL;
    stockHeader.weighty = 1;
    stockHeader.insets = new java.awt.Insets(0, 0, 0, 0);

    stockHeader.gridx = 0;
    stockHeader.weightx = 0.60;
    jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jPanel13.add(jLabel19, stockHeader);

    stockHeader.gridx = 1;
    stockHeader.weightx = 0.40;
    jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jPanel13.add(jLabel20, stockHeader);

    jPanel14.removeAll();
    jPanel14.setLayout(new java.awt.GridBagLayout());
    jPanel14.setBackground(headerRed);
    jPanel14.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15)
    );
    jPanel14.setPreferredSize(new java.awt.Dimension(600, 35));

    jLabel22.setText("Transaction");
    jLabel22.setForeground(java.awt.Color.WHITE);
    jLabel22.setFont(headerFont);
    jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

    jLabel23.setText("Amount");
    jLabel23.setForeground(java.awt.Color.WHITE);
    jLabel23.setFont(headerFont);
    jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    
java.awt.GridBagConstraints transactionHeader =
    new java.awt.GridBagConstraints();

    transactionHeader.gridy = 0;
    transactionHeader.fill = java.awt.GridBagConstraints.HORIZONTAL;
    transactionHeader.weighty = 1;
    transactionHeader.insets = new java.awt.Insets(0, 0, 0, 0);

    transactionHeader.gridx = 0;
    transactionHeader.weightx = 0.60;
    jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jPanel14.add(jLabel22, transactionHeader);

    transactionHeader.gridx = 1;
    transactionHeader.weightx = 0.40;
    jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jPanel14.add(jLabel23, transactionHeader);
 

    jScrollPane1.setViewportView(jPanel15);
    jScrollPane2.setViewportView(jPanel16);

    jScrollPane1.setColumnHeaderView(jPanel13);
    jScrollPane2.setColumnHeaderView(jPanel14);

    jScrollPane1.setBorder(null);
    jScrollPane2.setBorder(null);

    jScrollPane1.setHorizontalScrollBarPolicy(
        javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    jScrollPane2.setHorizontalScrollBarPolicy(
        javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    jScrollPane1.setVerticalScrollBarPolicy(
        javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    jScrollPane2.setVerticalScrollBarPolicy(
        javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );

    jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
    jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);

    jScrollPane1.getViewport().setBackground(java.awt.Color.WHITE);
    jScrollPane2.getViewport().setBackground(java.awt.Color.WHITE);

    jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel13.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

    jPanel13.revalidate();
    jPanel13.repaint();
    jPanel14.revalidate();
    jPanel14.repaint();
}

private void loadDashboardData() {
    if (jPanel15 == null || jPanel16 == null) {
        return;
    }

    jPanel15.removeAll();
    jPanel16.removeAll();

    jPanel15.setLayout(new java.awt.GridBagLayout());
    jPanel16.setLayout(new java.awt.GridBagLayout());

    jPanel15.setBackground(java.awt.Color.WHITE);
    jPanel16.setBackground(java.awt.Color.WHITE);

    java.awt.Font rowFont =
        new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13);

    try (java.sql.Connection conn =
             DatabaseConnection.getConnection()) {

        // =====================================
        // 1. TOTAL SALES
        // =====================================
        String totalSalesSql =
            "SELECT COALESCE(SUM(total_amount), 0) AS total_sales " +
            "FROM sales";

        try (java.sql.PreparedStatement pst =
                 conn.prepareStatement(totalSalesSql);
             java.sql.ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                java.math.BigDecimal totalSales =
                    rs.getBigDecimal("total_sales");

                if (totalSales == null) {
                    totalSales = java.math.BigDecimal.ZERO;
                }

                jLabel13.setText(
                    "₱" + String.format(
                        java.util.Locale.US,
                        "%,.2f",
                        totalSales
                    )
                );
            }
        }

        // =====================================
        // 2. LOW-STOCK PRODUCTS
        // =====================================
        String stockSql =
            "SELECT name, stock FROM products " +
            "WHERE stock <= 10 " +
            "ORDER BY stock ASC, name ASC LIMIT 15";

        try (java.sql.PreparedStatement pst =
                 conn.prepareStatement(stockSql);
             java.sql.ResultSet rs = pst.executeQuery()) {

            int row = 0;

            while (rs.next()) {
                String productName = rs.getString("name");
                int stock = rs.getInt("stock");

                javax.swing.JLabel nameLabel =
                    new javax.swing.JLabel(
                        productName == null
                            ? "Unnamed Product"
                            : productName
                    );

                javax.swing.JLabel stockLabel =
                    new javax.swing.JLabel(String.valueOf(stock));

                nameLabel.setFont(rowFont);
                nameLabel.setForeground(java.awt.Color.BLACK);
                nameLabel.setHorizontalAlignment(
                    javax.swing.SwingConstants.LEFT
                );

                stockLabel.setFont(rowFont);
                stockLabel.setForeground(
                    stock == 0
                        ? java.awt.Color.RED
                        : java.awt.Color.BLACK
                );
                stockLabel.setHorizontalAlignment(
                    javax.swing.SwingConstants.CENTER
                );

                // Product name column: 60%
                java.awt.GridBagConstraints left =
                    new java.awt.GridBagConstraints();

                left.gridx = 0;
                left.gridy = row;
                left.weightx = 0.60;
                left.fill = java.awt.GridBagConstraints.HORIZONTAL;
                left.anchor = java.awt.GridBagConstraints.WEST;
                left.insets = new java.awt.Insets(8, 15, 8, 8);

                // Stock column: 40%
                java.awt.GridBagConstraints right =
                    new java.awt.GridBagConstraints();

                right.gridx = 1;
                right.gridy = row;
                right.weightx = 0.40;
                right.fill = java.awt.GridBagConstraints.HORIZONTAL;
                right.anchor = java.awt.GridBagConstraints.CENTER;
                right.insets = new java.awt.Insets(8, 0, 8, 15);

                // Prevent preferred label widths from distorting columns.
                nameLabel.setPreferredSize(
                    new java.awt.Dimension(
                        0, nameLabel.getPreferredSize().height
                    )
                );

                stockLabel.setPreferredSize(
                    new java.awt.Dimension(
                        0, stockLabel.getPreferredSize().height
                    )
                );

                jPanel15.add(nameLabel, left);
                jPanel15.add(stockLabel, right);

                row++;
            }

            if (row == 0) {
                javax.swing.JLabel emptyLabel =
                    new javax.swing.JLabel("No low-stock products");

                emptyLabel.setFont(rowFont);
                emptyLabel.setForeground(java.awt.Color.GRAY);

                java.awt.GridBagConstraints empty =
                    new java.awt.GridBagConstraints();

                empty.gridx = 0;
                empty.gridy = 0;
                empty.gridwidth = 2;
                empty.weightx = 1.0;
                empty.fill = java.awt.GridBagConstraints.HORIZONTAL;
                empty.anchor = java.awt.GridBagConstraints.WEST;
                empty.insets = new java.awt.Insets(12, 15, 12, 15);

                jPanel15.add(emptyLabel, empty);
                row++;
            }

            java.awt.GridBagConstraints filler =
                new java.awt.GridBagConstraints();

            filler.gridx = 0;
            filler.gridy = row;
            filler.gridwidth = 2;
            filler.weightx = 1.0;
            filler.weighty = 1.0;
            filler.fill = java.awt.GridBagConstraints.BOTH;

            jPanel15.add(javax.swing.Box.createGlue(), filler);
        }

        // =====================================
        // 3. RECENT TRANSACTIONS
        // =====================================
        String salesSql =
            "SELECT transaction_id, total_amount " +
            "FROM sales ORDER BY sale_date DESC LIMIT 10";

        try (java.sql.PreparedStatement pst =
                 conn.prepareStatement(salesSql);
             java.sql.ResultSet rs = pst.executeQuery()) {

            int row = 0;

            while (rs.next()) {
                String transactionId =
                    rs.getString("transaction_id");

                java.math.BigDecimal amount =
                    rs.getBigDecimal("total_amount");

                String formattedAmount =
                    "₱" + String.format(
                        java.util.Locale.US,
                        "%,.2f",
                        amount == null
                            ? java.math.BigDecimal.ZERO
                            : amount
                    );

                javax.swing.JLabel transactionLabel =
                    new javax.swing.JLabel(
                        transactionId == null
                            ? "Unknown Transaction"
                            : transactionId
                    );

                javax.swing.JLabel amountLabel =
                    new javax.swing.JLabel(formattedAmount);

                transactionLabel.setFont(rowFont);
                transactionLabel.setForeground(java.awt.Color.BLACK);
                transactionLabel.setHorizontalAlignment(
                    javax.swing.SwingConstants.LEFT
                );

                amountLabel.setFont(rowFont);
                amountLabel.setForeground(java.awt.Color.BLACK);
                amountLabel.setHorizontalAlignment(
                    javax.swing.SwingConstants.CENTER
                );

                // Transaction column: 60%
                java.awt.GridBagConstraints left =
                    new java.awt.GridBagConstraints();

                left.gridx = 0;
                left.gridy = row;
                left.weightx = 0.60;
                left.fill = java.awt.GridBagConstraints.HORIZONTAL;
                left.anchor = java.awt.GridBagConstraints.WEST;
                left.insets = new java.awt.Insets(8, 15, 8, 8);

                // Amount column: 40%
                java.awt.GridBagConstraints right =
                    new java.awt.GridBagConstraints();

                right.gridx = 1;
                right.gridy = row;
                right.weightx = 0.40;
                right.fill = java.awt.GridBagConstraints.HORIZONTAL;
                right.anchor = java.awt.GridBagConstraints.CENTER;
                right.insets = new java.awt.Insets(8, 0, 8, 15);

                // Ensure the transaction ID length does not change
                // the intended column proportions.
                transactionLabel.setPreferredSize(
                    new java.awt.Dimension(
                        0, transactionLabel.getPreferredSize().height
                    )
                );

                amountLabel.setPreferredSize(
                    new java.awt.Dimension(
                        0, amountLabel.getPreferredSize().height
                    )
                );

                jPanel16.add(transactionLabel, left);
                jPanel16.add(amountLabel, right);

                row++;
            }

            if (row == 0) {
                javax.swing.JLabel emptyLabel =
                    new javax.swing.JLabel("No transactions yet");

                emptyLabel.setFont(rowFont);
                emptyLabel.setForeground(java.awt.Color.GRAY);

                java.awt.GridBagConstraints empty =
                    new java.awt.GridBagConstraints();

                empty.gridx = 0;
                empty.gridy = 0;
                empty.gridwidth = 2;
                empty.weightx = 1.0;
                empty.fill = java.awt.GridBagConstraints.HORIZONTAL;
                empty.anchor = java.awt.GridBagConstraints.WEST;
                empty.insets = new java.awt.Insets(12, 15, 12, 15);

                jPanel16.add(emptyLabel, empty);
                row++;
            }

            java.awt.GridBagConstraints filler =
                new java.awt.GridBagConstraints();

            filler.gridx = 0;
            filler.gridy = row;
            filler.gridwidth = 2;
            filler.weightx = 1.0;
            filler.weighty = 1.0;
            filler.fill = java.awt.GridBagConstraints.BOTH;

            jPanel16.add(javax.swing.Box.createGlue(), filler);
        }

    } catch (java.sql.SQLException e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Could not load dashboard data.\n\n" +
            "Check that MySQL is running and that your " +
            "products and sales tables contain the expected columns.\n\n" +
            "Error: " + e.getMessage(),
            "Database Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }

    // Refresh both dashboard sections.
    jPanel15.revalidate();
    jPanel15.repaint();

    jPanel16.revalidate();
    jPanel16.repaint();

    if (jScrollPane1 != null) {
        jScrollPane1.revalidate();
        jScrollPane1.repaint();
    }

    if (jScrollPane2 != null) {
        jScrollPane2.revalidate();
        jScrollPane2.repaint();
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
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel14 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(java.awt.SystemColor.control);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 153, 153));
        jButton1.setText("Products");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setBackground(new java.awt.Color(204, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Dashboard");

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

        jButton7.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(153, 153, 153));
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
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );

        jPanel6.setBackground(new java.awt.Color(204, 0, 0));

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("0");

        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("TRANSACTIONS");

        jLabel26.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Today");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jLabel11))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(jLabel26))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(204, 0, 0));

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("100,000");

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("TOTAL SALES");

        jLabel27.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Sales revenue");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addGap(79, 79, 79))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jLabel12))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel13)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(204, 0, 0));

        jLabel15.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("0");

        jLabel14.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("TOTAL STOCK");

        jLabel24.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Units Available");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(jLabel15))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel14))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel24)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addGap(12, 12, 12)
                .addComponent(jLabel24)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(204, 0, 0));

        jLabel17.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("0");

        jLabel16.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("TOTAL PRODUCTS");

        jLabel25.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Registered Items");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addGap(75, 75, 75))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(jLabel17))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel16)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel25)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel13.setBackground(new java.awt.Color(204, 0, 0));

        jLabel19.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Item");

        jLabel20.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Status");

        jLabel29.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("In Stock");

        jLabel31.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Paper");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
                        .addComponent(jLabel29)
                        .addGap(112, 112, 112))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20)
                        .addGap(48, 48, 48))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel31))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel13);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel14.setBackground(new java.awt.Color(204, 0, 0));

        jLabel22.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Transaction");

        jLabel23.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Number");

        jLabel28.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("1000");

        jLabel30.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("TRX");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel30))
                .addGap(229, 229, 229)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(jLabel23))
                .addContainerGap(258, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel30))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel14);

        jLabel18.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel18.setText("Low Stock Products");

        jLabel21.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel21.setText("Recent Transaction");

        jPanel3.setBackground(new java.awt.Color(204, 0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AlfeLogoRedHorizontal (1).png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Dashboard");

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Welcome Back!");

        jButton8.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton8.setText("Cashier Dashboard");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(75, 75, 75)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1223, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addContainerGap(13, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel21))
                            .addComponent(jLabel18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Transactions transactions = new Transactions();
        transactions.setLocationRelativeTo(this);
        transactions.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Inventory inventory = new Inventory();
        inventory.setLocationRelativeTo(this);
        inventory.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Products products = new Products();
        products.setLocationRelativeTo(this);
        products.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        SalesAndReports salesAndReports = new SalesAndReports();
        salesAndReports.setLocationRelativeTo(this);
        salesAndReports.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        InventoryHistory history = new InventoryHistory();
        history.setLocationRelativeTo(this);
        history.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (Session.role != null &&
        Session.role.trim().equalsIgnoreCase("admin")) {

        CashierDashboard cashier = new CashierDashboard();
        cashier.setLocationRelativeTo(this);
        cashier.setVisible(true);
        this.dispose();
    }
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
        java.awt.EventQueue.invokeLater(() -> new Dashboard().setVisible(true));
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}