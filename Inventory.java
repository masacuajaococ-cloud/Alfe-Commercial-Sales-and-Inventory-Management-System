/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class Inventory extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Inventory.class.getName());
    
    private java.util.ArrayList<Product> inventoryList = new java.util.ArrayList<>();
    
    private javax.swing.JPopupMenu searchPopup;
    private javax.swing.JList<String> suggestionList;
    private javax.swing.DefaultListModel<String> suggestionModel;
    private java.util.ArrayList<String> suggestionProductIds =
        new java.util.ArrayList<>();
    
public Inventory() {
    initComponents();

    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    setMinimumSize(new java.awt.Dimension(1000, 650));

    java.awt.Dimension cardSize =
            new java.awt.Dimension(200, 100);

    jPanel9.setPreferredSize(cardSize);
    jPanel10.setPreferredSize(cardSize);
    jPanel11.setPreferredSize(cardSize);

    centerCard(jPanel9, jLabel16, jLabel17);
    centerCard(jPanel10, jLabel18, jLabel19);
    centerCard(jPanel11, jLabel20, jLabel21);

    setupInventoryTable();

    jTextField1.setText("Search Product...");
    jTextField1.setForeground(java.awt.Color.GRAY);

    jTextField1.addFocusListener(
        new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (jTextField1.getText().equals("Search Product...")) {
                    jTextField1.setText("");
                    jTextField1.setForeground(java.awt.Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (jTextField1.getText().trim().isEmpty()) {
                    jTextField1.setText("Search Product...");
                    jTextField1.setForeground(java.awt.Color.GRAY);
                }
            }
        }
    );

    loadInventory();
    setupSearchAutocomplete();
}

private void centerCard(javax.swing.JPanel card,
        javax.swing.JLabel titleLabel,
        javax.swing.JLabel valueLabel) {

    javax.swing.GroupLayout layout =
            (javax.swing.GroupLayout) card.getLayout();

    titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    layout.setHorizontalGroup(
        layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING
        )
        .addComponent(titleLabel, 0,
            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(valueLabel, 0,
            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
}

    private void setupSearchAutocomplete() {

    suggestionModel =
        new javax.swing.DefaultListModel<>();

    suggestionList =
        new javax.swing.JList<>(suggestionModel);


    suggestionList.setFocusable(false);

    suggestionList.setSelectionMode(
        javax.swing.ListSelectionModel.SINGLE_SELECTION
    );

    suggestionList.setFont(
        new java.awt.Font(
            "Segoe UI",
            java.awt.Font.PLAIN,
            13
        )
    );

    suggestionList.setVisibleRowCount(6);

    searchPopup =
        new javax.swing.JPopupMenu();

 
    searchPopup.setFocusable(false);

    searchPopup.setBorder(
        javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(200, 200, 200)
        )
    );

    javax.swing.JScrollPane scrollPane =
        new javax.swing.JScrollPane(suggestionList);

    scrollPane.setFocusable(false);

    searchPopup.add(scrollPane);

    

    jTextField1.getDocument().addDocumentListener(
        new javax.swing.event.DocumentListener() {

            private void updateSuggestions() {

                javax.swing.SwingUtilities.invokeLater(
                    () -> showSuggestions()
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

    
    suggestionList.addMouseListener(
        new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e) {

                if (e.getClickCount() == 2) {
                    selectSuggestion();
                }
            }
        }
    );

    
    jTextField1.getInputMap(
        javax.swing.JComponent.WHEN_FOCUSED
    ).put(
        javax.swing.KeyStroke.getKeyStroke("DOWN"),
        "nextSuggestion"
    );

    jTextField1.getActionMap().put(
        "nextSuggestion",
        new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(
                    java.awt.event.ActionEvent e) {

                if (!searchPopup.isVisible()
                        || suggestionModel.isEmpty()) {

                    return;
                }

                int index =
                    suggestionList.getSelectedIndex();

                if (index < suggestionModel.size() - 1) {

                    suggestionList.setSelectedIndex(
                        index + 1
                    );

                } else {

                    suggestionList.setSelectedIndex(0);
                }

                suggestionList.ensureIndexIsVisible(
                    suggestionList.getSelectedIndex()
                );

                jTextField1.requestFocusInWindow();
            }
        }
    );

    

    jTextField1.getInputMap(
        javax.swing.JComponent.WHEN_FOCUSED
    ).put(
        javax.swing.KeyStroke.getKeyStroke("UP"),
        "previousSuggestion"
    );

    jTextField1.getActionMap().put(
        "previousSuggestion",
        new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(
                    java.awt.event.ActionEvent e) {

                if (!searchPopup.isVisible()
                        || suggestionModel.isEmpty()) {

                    return;
                }

                int index =
                    suggestionList.getSelectedIndex();

                if (index > 0) {

                    suggestionList.setSelectedIndex(
                        index - 1
                    );

                } else {

                    suggestionList.setSelectedIndex(
                        suggestionModel.size() - 1
                    );
                }

                suggestionList.ensureIndexIsVisible(
                    suggestionList.getSelectedIndex()
                );

                jTextField1.requestFocusInWindow();
            }
        }
    );

    

    jTextField1.getInputMap(
        javax.swing.JComponent.WHEN_FOCUSED
    ).put(
        javax.swing.KeyStroke.getKeyStroke("ENTER"),
        "selectSuggestion"
    );

    jTextField1.getActionMap().put(
        "selectSuggestion",
        new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(
                    java.awt.event.ActionEvent e) {

                if (searchPopup.isVisible()
                        && suggestionList.getSelectedIndex() != -1) {

                    selectSuggestion();

                } else {

                    jButton7.doClick();
                }
            }
        }
    );

   

    jTextField1.getInputMap(
        javax.swing.JComponent.WHEN_FOCUSED
    ).put(
        javax.swing.KeyStroke.getKeyStroke("ESCAPE"),
        "closeSuggestions"
    );

    jTextField1.getActionMap().put(
        "closeSuggestions",
        new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(
                    java.awt.event.ActionEvent e) {

                searchPopup.setVisible(false);

                jTextField1.requestFocusInWindow();
            }
        }
    );
}
    

private void changeStock(int direction) {
    int selectedRow = jTable2.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please select a product first.",
            "No Product Selected",
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    selectedRow = jTable2.convertRowIndexToModel(selectedRow);

    String productId = jTable2.getValueAt(selectedRow, 0).toString();
    String productName = jTable2.getValueAt(selectedRow, 1).toString();
    String action = direction > 0 ? "Stock In" : "Stock Out";

    String input = javax.swing.JOptionPane.showInputDialog(
        this,
        "Product: " + productName + "\nEnter quantity to " +
        (direction > 0 ? "add:" : "remove:"),
        action,
        javax.swing.JOptionPane.QUESTION_MESSAGE
    );

    if (input == null) {
        return;
    }

    int quantity;

    try {
        quantity = Integer.parseInt(input.trim());

        if (quantity <= 0) {
            throw new NumberFormatException();
        }
    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please enter a valid positive whole number.",
            "Invalid Quantity",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    String selectSQL =
        "SELECT stock FROM products WHERE product_id = ? FOR UPDATE";

    String updateSQL =
        "UPDATE products SET stock = ?, status = ? WHERE product_id = ?";

    try (java.sql.Connection con =
            new DatabaseConnection().connect()) {

        con.setAutoCommit(false);

        try {
            int currentStock;

            try (java.sql.PreparedStatement pst =
                    con.prepareStatement(selectSQL)) {

                pst.setString(1, productId);

                try (java.sql.ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        throw new java.sql.SQLException("Product not found.");
                    }

                    currentStock = rs.getInt("stock");
                }
            }

            if (direction < 0 && quantity > currentStock) {
                con.rollback();

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Insufficient stock. Current stock: " + currentStock,
                    "Stock Out Failed",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            long calculatedStock =
                (long) currentStock + (long) direction * quantity;

            if (calculatedStock < 0 ||
                    calculatedStock > Integer.MAX_VALUE) {
                con.rollback();

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "The resulting stock quantity is invalid.",
                    "Invalid Quantity",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int newStock = (int) calculatedStock;

            String status;

            if (newStock == 0) {
                status = "Out of Stock";
            } else if (newStock <= 10) {
                status = "Low Stock";
            } else {
                status = "In Stock";
            }

            try (java.sql.PreparedStatement pst =
                    con.prepareStatement(updateSQL)) {

                pst.setInt(1, newStock);
                pst.setString(2, status);
                pst.setString(3, productId);

                if (pst.executeUpdate() == 0) {
                    throw new java.sql.SQLException(
                        "The stock was not updated."
                    );
                }
            }

            insertStockHistory(
                con,
                productId,
                direction > 0 ? "STOCK IN" : "STOCK OUT",
                direction * quantity,
                currentStock,
                newStock,
                direction > 0 ? "Manual stock in" : "Manual stock out"
            );

            con.commit();

            loadInventory();

            javax.swing.JOptionPane.showMessageDialog(
                this,
                action + " successful!\nProduct: " + productName
                + "\nPrevious stock: " + currentStock
                + "\nQuantity: " + quantity
                + "\nNew stock: " + newStock,
                "Success",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        } catch (java.sql.SQLException e) {
            try {
                con.rollback();
            } catch (java.sql.SQLException rollbackError) {
                e.addSuppressed(rollbackError);
            }
            throw e;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (java.sql.SQLException ignored) {
            }
        }

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Database error: " + e.getMessage(),
            "Database Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}

    
    private void selectSuggestion() {

    int selectedIndex =
        suggestionList.getSelectedIndex();

    if (selectedIndex == -1
            || selectedIndex >= suggestionProductIds.size()) {

        return;
    }

    String productId =
        suggestionProductIds.get(selectedIndex);

    searchPopup.setVisible(false);

    jTextField1.setText(productId);

    jButton7.doClick();

    jTextField1.requestFocusInWindow();
}
    
    private void showSuggestions() {

    String search =
        jTextField1.getText().trim();

    if (search.isEmpty()
            || search.equals("Search Product...")) {

        searchPopup.setVisible(false);
        return;
    }

    suggestionModel.clear();
    suggestionProductIds.clear();

    String sql =
        "SELECT product_id, name, brand, variant, size, " +
        "specification, color, unit, price, stock " +
        "FROM products " +
        "WHERE product_id LIKE ? " +
        "OR name LIKE ? " +
        "OR brand LIKE ? " +
        "OR variant LIKE ? " +
        "OR size LIKE ? " +
        "OR specification LIKE ? " +
        "OR color LIKE ? " +
        "OR unit LIKE ? " +
        "ORDER BY name ASC " +
        "LIMIT 8";

    try (
        java.sql.Connection conn =
            new DatabaseConnection().connect();

        java.sql.PreparedStatement pst =
            conn.prepareStatement(sql)
    ) {

        String keyword =
            "%" + search + "%";

        for (int i = 1; i <= 8; i++) {
            pst.setString(i, keyword);
        }

        try (java.sql.ResultSet rs =
                pst.executeQuery()) {

            while (rs.next()) {

                String productId =
                    rs.getString("product_id");

                String name =
                    rs.getString("name");

                String brand =
                    rs.getString("brand");

                String variant =
                    rs.getString("variant");

                String size =
                    rs.getString("size");

                String specification =
                    rs.getString("specification");

                String color =
                    rs.getString("color");

                double price =
                    rs.getDouble("price");

                int stock =
                    rs.getInt("stock");

                StringBuilder suggestion =
                    new StringBuilder(name);

                if (brand != null
                        && !brand.trim().isEmpty()) {

                    suggestion.append(" — ")
                              .append(brand);
                }

                if (variant != null
                        && !variant.trim().isEmpty()) {

                    suggestion.append(" — ")
                              .append(variant);
                }

                if (size != null
                        && !size.trim().isEmpty()) {

                    suggestion.append(" — ")
                              .append(size);
                }

                if (specification != null
                        && !specification.trim().isEmpty()) {

                    suggestion.append(" — ")
                              .append(specification);
                }

                if (color != null
                        && !color.trim().isEmpty()) {

                    suggestion.append(" — ")
                              .append(color);
                }

                suggestion.append(
                    String.format(
                        "    |    ₱%.2f    |    Stock: %d",
                        price,
                        stock
                    )
                );

                suggestionModel.addElement(
                    suggestion.toString()
                );

                suggestionProductIds.add(
                    productId
                );
            }
        }

        if (!suggestionModel.isEmpty()) {

            suggestionList.setSelectedIndex(0);

            searchPopup.setPopupSize(
                jTextField1.getWidth(),
                Math.min(
                    6 * 42,
                    suggestionModel.size() * 42
                )
            );

            searchPopup.show(
                jTextField1,
                0,
                jTextField1.getHeight()
            );

            // IMPORTANT:
            // Give keyboard focus back to the search field
            // after the popup appears.
            javax.swing.SwingUtilities.invokeLater(
                () -> {

                    jTextField1.requestFocusInWindow();

                    jTextField1.setCaretPosition(
                        jTextField1.getText().length()
                    );
                }
            );

        } else {

            searchPopup.setVisible(false);
        }

    } catch (java.sql.SQLException e) {

        searchPopup.setVisible(false);
    }
    
    
    
}


private void displayInventory() {

    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable2.getModel();

    model.setRowCount(0);

    for (Product product : inventoryList) {

        model.addRow(new Object[] {
            product.getProductId(),
            product.getName(),
            product.getCategory(),
            product.getBrand(),
            product.getVariant(),
            product.getSize(),
            product.getSpecification(),
            product.getColor(),
            product.getUnit(),
            product.getPrice(),
            product.getStock(),
            product.getStatus()
        });
    }
}
    
    private Product linearSearch(String keyword) {

    keyword = keyword.toLowerCase().trim();

    for (Product product : inventoryList) {

        if (containsIgnoreCase(
                product.getProductId(), keyword)
                || containsIgnoreCase(
                product.getName(), keyword)
                || containsIgnoreCase(
                product.getCategory(), keyword)
                || containsIgnoreCase(
                product.getBrand(), keyword)
                || containsIgnoreCase(
                product.getVariant(), keyword)
                || containsIgnoreCase(
                product.getSize(), keyword)
                || containsIgnoreCase(
                product.getSpecification(), keyword)
                || containsIgnoreCase(
                product.getColor(), keyword)
                || containsIgnoreCase(
                product.getUnit(), keyword)) {

            return product;
        }
    }

    return null;
}

private boolean containsIgnoreCase(
        String text,
        String keyword) {

    if (text == null) {
        return false;
    }

    return text.toLowerCase().contains(keyword);
}
    
    private void loadInventory() {

    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable2.getModel();

    model.setRowCount(0);
    inventoryList.clear();

    String sql =
        "SELECT product_id, name, category, brand, variant, size, " +
        "specification, color, unit, price, stock, status " +
        "FROM products ORDER BY product_id DESC";

    DatabaseConnection db = new DatabaseConnection();

    try (
        java.sql.Connection conn = db.connect();
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery()
    ) {

        int totalStock = 0;
        int lowStock = 0;
        int outOfStock = 0;

        while (rs.next()) {

            int stock = rs.getInt("stock");

            String status;

            if (stock == 0) {

                status = "Out of Stock";
                outOfStock++;

            } else if (stock <= 10) {

                status = "Low Stock";
                lowStock++;

            } else {

                status = "In Stock";
            }

            Product product = new Product(
                rs.getString("product_id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("brand"),
                rs.getString("variant"),
                rs.getString("size"),
                rs.getString("specification"),
                rs.getString("color"),
                rs.getString("unit"),
                rs.getDouble("price"),
                stock,
                status
            );

            inventoryList.add(product);

            totalStock += stock;

            model.addRow(new Object[] {
                product.getProductId(),
                product.getName(),
                product.getCategory(),
                product.getBrand(),
                product.getVariant(),
                product.getSize(),
                product.getSpecification(),
                product.getColor(),
                product.getUnit(),
                product.getPrice(),
                product.getStock(),
                product.getStatus()
            });
        }

        jLabel17.setText(String.valueOf(totalStock));
        jLabel19.setText(String.valueOf(lowStock));
        jLabel21.setText(String.valueOf(outOfStock));

    } catch (java.sql.SQLException e) {

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Failed to load inventory:\n" + e.getMessage(),
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
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

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

        jButton3.setBackground(new java.awt.Color(204, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Inventory");

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

        jButton12.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(153, 153, 153));
        jButton12.setText("Inventory History");
        jButton12.addActionListener(this::jButton12ActionPerformed);

        jButton13.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(153, 153, 153));
        jButton13.setText("Account Settings");
        jButton13.addActionListener(this::jButton13ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(72, 72, 72)
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
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel9.setBackground(new java.awt.Color(204, 0, 0));
        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel17.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("0");

        jLabel16.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Total Stock");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(19, 19, 19))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(204, 0, 0));
        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel19.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("0");

        jLabel18.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Low Stock");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addGap(20, 20, 20))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(204, 0, 0));
        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel21.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("0");

        jLabel20.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Out of Stock");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(64, 64, 64))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton7.setText("Search");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton8.setText("Refresh");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort By ▼", "Stock: Low to High", "Stock: High to Low", "Name: A to Z", "Name: Z to A" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "In Stock", "Low Stock", "Out of Stock" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton9.setText("Adjust Stock");
        jButton9.addActionListener(this::jButton9ActionPerformed);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID ", "Name", "Category", "Brand", "Variant", "Size", "Specification", "Color ", "Unit", "Price", "Stock", "Status"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton10.setText("Stock In");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton11.setText("Stock Out");
        jButton11.addActionListener(this::jButton11ActionPerformed);

        jPanel3.setBackground(new java.awt.Color(204, 0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AlfeLogoRedHorizontal (1).png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Inventory Management");

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Inventory");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(47, 47, 47)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addContainerGap(1482, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(323, 323, 323)
                        .addComponent(jButton9)
                        .addGap(185, 185, 185)
                        .addComponent(jButton11)
                        .addGap(222, 222, 222)
                        .addComponent(jButton10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(792, 792, 792)
                        .addComponent(jButton8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(193, 193, 193)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(230, 230, 230)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(69, 69, 69)
                                .addComponent(jButton7)
                                .addGap(161, 161, 161)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1196, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String search = jTextField1.getText().trim();

    if (search.equals("Search Product...")) {
    search = "";
}

    if (search.isEmpty()) {
    loadInventory();
    return;
}

    // DSA: Linear Search
    Product product = linearSearch(search);

    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable2.getModel();

    model.setRowCount(0);

    if (product != null) {

        model.addRow(new Object[] {
    product.getProductId(),
    product.getName(),
    product.getCategory(),
    product.getBrand(),
    product.getVariant(),
    product.getSize(),
    product.getSpecification(),
    product.getColor(),
    product.getUnit(),
    product.getPrice(),
    product.getStock(),
    product.getStatus()
});

    } else {

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "No product found.",
            "Search",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );

        loadInventory();
    }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        loadInventory();
    jTextField1.setText("Search Product...");
    jTextField1.setForeground(java.awt.Color.GRAY);
    jComboBox1.setSelectedItem("All");
    jComboBox2.setSelectedItem("Sort By ▼");

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        String sortType = jComboBox2.getSelectedItem().toString();

        if (sortType.equals("Sort By ▼")) {
            return;
        }

        bubbleSort(sortType);
    }//GEN-LAST:event_jComboBox2ActionPerformed

    



private void setupInventoryTable() {
    jTable2.setDefaultEditor(Object.class, null);
    jTable2.setRowHeight(28);
    jTable2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
    jTable2.setForeground(java.awt.Color.BLACK);
    jTable2.setBackground(java.awt.Color.WHITE);
    jTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jTable2.setFillsViewportHeight(true);
    jTable2.setShowGrid(true);
    jTable2.setGridColor(new java.awt.Color(230, 230, 230));
    jTable2.setIntercellSpacing(new java.awt.Dimension(1, 1));
    jTable2.setSelectionBackground(new java.awt.Color(255, 204, 128));
    jTable2.setSelectionForeground(java.awt.Color.BLACK);

    javax.swing.table.DefaultTableCellRenderer normalRenderer =
        new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                java.awt.Component component =
                    super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                    );

                component.setBackground(isSelected
                    ? new java.awt.Color(255, 204, 128)
                    : java.awt.Color.WHITE);

                component.setForeground(java.awt.Color.BLACK);
                return component;
            }
        };

    javax.swing.table.DefaultTableCellRenderer statusRenderer =
        new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );

                String status = value == null ? "" : value.toString().trim();

                if (isSelected) {
                    setBackground(new java.awt.Color(255, 204, 128));
                    setForeground(java.awt.Color.BLACK);
                } else if (status.equalsIgnoreCase("In Stock")) {
                    setBackground(new java.awt.Color(209, 250, 229));
                    setForeground(new java.awt.Color(22, 101, 52));
                } else if (status.equalsIgnoreCase("Low Stock")) {
                    setBackground(new java.awt.Color(255, 237, 213));
                    setForeground(new java.awt.Color(154, 52, 18));
                } else if (status.equalsIgnoreCase("Out of Stock")) {
                    setBackground(new java.awt.Color(254, 226, 226));
                    setForeground(new java.awt.Color(153, 27, 27));
                } else {
                    setBackground(java.awt.Color.WHITE);
                    setForeground(java.awt.Color.BLACK);
                }

                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                return this;
            }
        };

    for (int i = 0; i < jTable2.getColumnCount(); i++) {
        jTable2.getColumnModel().getColumn(i)
            .setCellRenderer(normalRenderer);
    }

    // Status column
    jTable2.getColumnModel().getColumn(11)
        .setCellRenderer(statusRenderer);

    jTable2.getTableHeader().setReorderingAllowed(false);
    jTable2.getTableHeader().setResizingAllowed(true);
    jTable2.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 32));
    jTable2.getTableHeader().setFont(
        new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)
    );
    jTable2.getTableHeader().setBackground(java.awt.Color.WHITE);
    jTable2.getTableHeader().setForeground(java.awt.Color.BLACK);
    jTable2.getTableHeader().setOpaque(true);

    jTable2.getTableHeader().setDefaultRenderer(
        new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                javax.swing.JLabel label = (javax.swing.JLabel)
                    super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                    );

                label.setBackground(java.awt.Color.WHITE);
                label.setForeground(java.awt.Color.BLACK);
                label.setFont(
                    new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)
                );
                label.setOpaque(true);
                label.setHorizontalAlignment(
                    javax.swing.SwingConstants.CENTER
                );

                return label;
            }
        }
    );

    int[] columnWidths = {
        90, 160, 110, 100, 100, 75,
        180, 90, 75, 90, 75, 110
    };

    for (int i = 0; i < Math.min(
            columnWidths.length, jTable2.getColumnCount()); i++) {
        jTable2.getColumnModel().getColumn(i)
            .setPreferredWidth(columnWidths[i]);
    }

    jScrollPane2.setHorizontalScrollBarPolicy(
        javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    jScrollPane2.setVerticalScrollBarPolicy(
        javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    jScrollPane2.getViewport().setBackground(java.awt.Color.WHITE);
    jScrollPane2.getVerticalScrollBar().setUnitIncrement(20);
    jScrollPane2.getHorizontalScrollBar().setUnitIncrement(20);
}
    
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String filter = jComboBox1.getSelectedItem().toString();

    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable2.getModel();

    model.setRowCount(0);

    for (Product product : inventoryList) {

        boolean show = false;

        if (filter.equals("All")) {

            show = true;

        } else if (filter.equals("In Stock")) {

            if (product.getStock() > 10) {
                show = true;
            }

        } else if (filter.equals("Low Stock")) {

            if (product.getStock() > 0 && product.getStock() <= 10) {
                show = true;
            }

        } else if (filter.equals("Out of Stock")) {

            if (product.getStock() == 0) {
                show = true;
            }
        }

        if (show) {

            model.addRow(new Object[] {
    product.getProductId(),
    product.getName(),
    product.getCategory(),
    product.getBrand(),
    product.getVariant(),
    product.getSize(),
    product.getSpecification(),
    product.getColor(),
    product.getUnit(),
    product.getPrice(),
    product.getStock(),
    product.getStatus()
});
        }
    }
    
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
         int selectedRow = jTable2.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please select a product first.",
            "Adjust Stock",
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    selectedRow = jTable2.convertRowIndexToModel(selectedRow);

    javax.swing.table.TableModel model = jTable2.getModel();

    String productId = model.getValueAt(selectedRow, 0).toString();
    String productName = model.getValueAt(selectedRow, 1).toString();

    String input = javax.swing.JOptionPane.showInputDialog(
        this,
        "Product: " + productName +
        "\n\nEnter new stock quantity:",
        "Adjust Stock",
        javax.swing.JOptionPane.QUESTION_MESSAGE
    );

    if (input == null || input.trim().isEmpty()) {
        return;
    }

    int newStock;

    try {
        newStock = Integer.parseInt(input.trim());

        if (newStock < 0) {
            throw new NumberFormatException();
        }
    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please enter a valid non-negative whole number.",
            "Invalid Stock",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    String selectSQL =
        "SELECT stock FROM products WHERE product_id = ? FOR UPDATE";

    String updateSQL =
        "UPDATE products SET stock = ?, status = ? WHERE product_id = ?";

    try (java.sql.Connection conn =
            new DatabaseConnection().connect()) {

        conn.setAutoCommit(false);

        try {
            int currentStock;

            try (java.sql.PreparedStatement pst =
                    conn.prepareStatement(selectSQL)) {

                pst.setString(1, productId);

                try (java.sql.ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        throw new java.sql.SQLException("Product not found.");
                    }

                    currentStock = rs.getInt("stock");
                }
            }

            if (newStock == currentStock) {
                conn.rollback();

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "The new stock is the same as the current stock.",
                    "No Changes",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            String status;

            if (newStock == 0) {
                status = "Out of Stock";
            } else if (newStock <= 10) {
                status = "Low Stock";
            } else {
                status = "In Stock";
            }

            try (java.sql.PreparedStatement pst =
                    conn.prepareStatement(updateSQL)) {

                pst.setInt(1, newStock);
                pst.setString(2, status);
                pst.setString(3, productId);

                if (pst.executeUpdate() == 0) {
                    throw new java.sql.SQLException(
                        "Failed to update product stock."
                    );
                }
            }

            insertStockHistory(
                conn,
                productId,
                "ADJUSTMENT",
                newStock - currentStock,
                currentStock,
                newStock,
                "Manual stock adjustment"
            );

            conn.commit();

            loadInventory();

            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Stock adjusted successfully!\n\n" +
                "Product: " + productName +
                "\nPrevious Stock: " + currentStock +
                "\nNew Stock: " + newStock +
                "\nChange: " + (newStock - currentStock),
                "Adjust Stock",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        } catch (java.sql.SQLException e) {
            try {
                conn.rollback();
            } catch (java.sql.SQLException rollbackError) {
                e.addSuppressed(rollbackError);
            }
            throw e;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (java.sql.SQLException ignored) {
            }
        }

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Failed to adjust stock:\n" + e.getMessage(),
            "Database Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }

    }
    
    
    private void insertStockHistory(
        java.sql.Connection con,
        String productId,
        String actionType,
        int quantityChanged,
        int stockBefore,
        int stockAfter,
        String reason) throws java.sql.SQLException {

    if (Session.userId <= 0) {
        throw new java.sql.SQLException(
            "No logged-in user found. Please log in again."
        );
    }

    String sql =
        "INSERT INTO stock_history " +
        "(product_id, user_id, action_type, quantity_changed, " +
        "stock_before, stock_after, reason) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (java.sql.PreparedStatement pst =
            con.prepareStatement(sql)) {

        pst.setString(1, productId);
        pst.setInt(2, Session.userId);
        pst.setString(3, actionType);
        pst.setInt(4, quantityChanged);
        pst.setInt(5, stockBefore);
        pst.setInt(6, stockAfter);
        pst.setString(7, reason);

        pst.executeUpdate();
    }
}
    
   private void bubbleSort(String sortType) {

    int n = inventoryList.size();

    
    for (int i = 0; i < n - 1; i++) {

        for (int j = 0; j < n - i - 1; j++) {

            Product current = inventoryList.get(j);
            Product next = inventoryList.get(j + 1);

            boolean swap = false;

            if (sortType.equals("Stock: Low to High")) {

                if (current.getStock() > next.getStock()) {
                    swap = true;
                }

            } else if (sortType.equals("Stock: High to Low")) {

                if (current.getStock() < next.getStock()) {
                    swap = true;
                }

            } else if (sortType.equals("Name: A to Z")) {

                if (current.getName()
                        .compareToIgnoreCase(next.getName()) > 0) {

                    swap = true;
                }

            } else if (sortType.equals("Name: Z to A")) {

                if (current.getName()
                        .compareToIgnoreCase(next.getName()) < 0) {

                    swap = true;
                }
            }

            if (swap) {

                inventoryList.set(j, next);
                inventoryList.set(j + 1, current);
            }
        }
    }

    
    String filter = jComboBox1.getSelectedItem().toString();

    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable2.getModel();

    model.setRowCount(0);

    for (Product product : inventoryList) {

        boolean show = false;

        if (filter.equals("All")) {

            show = true;

        } else if (filter.equals("In Stock")) {

            if (product.getStock() > 10) {
                show = true;
            }

        } else if (filter.equals("Low Stock")) {

            if (product.getStock() > 0
                    && product.getStock() <= 10) {

                show = true;
            }

        } else if (filter.equals("Out of Stock")) {

            if (product.getStock() == 0) {
                show = true;
            }
        }

        if (show) {

            model.addRow(new Object[] {
    product.getProductId(),
    product.getName(),
    product.getCategory(),
    product.getBrand(),
    product.getVariant(),
    product.getSize(),
    product.getSpecification(),
    product.getColor(),
    product.getUnit(),
    product.getPrice(),
    product.getStock(),
    product.getStatus()
});
        }
    }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        changeStock(1);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        changeStock(-1);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        AccountSettings settings = new AccountSettings();
        settings.setLocationRelativeTo(this);
        settings.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        InventoryHistory history = new InventoryHistory();
        history.setLocationRelativeTo(this);
        history.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        SalesAndReports salesAndReports = new SalesAndReports();
        salesAndReports.setLocationRelativeTo(this);
        salesAndReports.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Dashboard dashboard = new Dashboard();
        dashboard.setLocationRelativeTo(this);
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Products products = new Products();
        products.setLocationRelativeTo(this);
        products.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Inventory().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}