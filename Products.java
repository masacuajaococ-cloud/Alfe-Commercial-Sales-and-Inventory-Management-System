/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Admin
 */

public class Products extends javax.swing.JFrame {

    private final java.util.ArrayList<Product> productList =
            new java.util.ArrayList<>();

    private javax.swing.JPopupMenu searchPopup;
    private javax.swing.JList<String> suggestionList;
    private javax.swing.DefaultListModel<String> suggestionModel;

    private final java.util.ArrayList<String> suggestionProductIds =
            new java.util.ArrayList<>();

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Products.class.getName());


public Products() {
    initComponents();

    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

    jTable1.setRowHeight(35);

    jTextField1.setText("Search Product...");
    jTextField1.setForeground(java.awt.Color.GRAY);

    jTextField1.addFocusListener(
        new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if ("Search Product...".equals(jTextField1.getText())) {
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

    setupStatusRenderer();

    setupSearchAutocomplete();

    loadProducts();
}

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null
                && value.toLowerCase(java.util.Locale.ROOT)
                        .contains(keyword.toLowerCase(java.util.Locale.ROOT));
    }
    
    
    private String getStockStatus(int stock) {
        if (stock <= 0) {
            return "Out of Stock";
    } else if (stock <= 5) {
            return "Low Stock";
    } else {
            return "In Stock";
    }
}


    private Product linearSearch(String search) {
        String keyword = search.trim();

        if (keyword.isEmpty()) {
            return null;
        }

        for (Product product : productList) {
            if (containsIgnoreCase(product.getProductId(), keyword)
                    || containsIgnoreCase(product.getName(), keyword)
                    || containsIgnoreCase(product.getCategory(), keyword)
                    || containsIgnoreCase(product.getBrand(), keyword)
                    || containsIgnoreCase(product.getVariant(), keyword)
                    || containsIgnoreCase(product.getSize(), keyword)
                    || containsIgnoreCase(product.getSpecification(), keyword)
                    || containsIgnoreCase(product.getColor(), keyword)
                    || containsIgnoreCase(product.getUnit(), keyword)) {
                return product;
            }
        }

        return null;
    }

private void bubbleSort(String sortType) {
    java.util.ArrayList<Product> sortedList =
            new java.util.ArrayList<>(productList);

    int size = sortedList.size();

    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - i - 1; j++) {
            Product first = sortedList.get(j);
            Product second = sortedList.get(j + 1);

            boolean swap = false;

            switch (sortType) {
                case "Price: Low to High":
                    swap = first.getPrice() > second.getPrice();
                    break;

                case "Price: High to Low":
                    swap = first.getPrice() < second.getPrice();
                    break;

                case "Stock: Low to High":
                    swap = first.getStock() > second.getStock();
                    break;

                case "Stock: High to Low":
                    swap = first.getStock() < second.getStock();
                    break;
            }

            if (swap) {
                sortedList.set(j, second);
                sortedList.set(j + 1, first);
            }
        }
    }

    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) jTable1.getModel();

    model.setRowCount(0);

    for (Product product : sortedList) {
        model.addRow(new Object[]{
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
            getStockStatus(product.getStock())
        });
    }
}

    private void loadProducts() {
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) jTable1.getModel();

        model.setRowCount(0);
        productList.clear();

        boolean showingArchived =
                "Archieved Products".equals(jComboBox3.getSelectedItem());

        Object selectedCategory = jComboBox1.getSelectedItem();

        String category = selectedCategory == null
                ? "All Categories"
                : selectedCategory.toString();

        String sql =
                "SELECT product_id, name, category, brand, variant, size, "
                + "specification, color, unit, price, stock, status "
                + "FROM products WHERE "
                + (showingArchived
                    ? "status = 'ARCHIVED' "
                    : "(status IS NULL OR status <> 'ARCHIVED') ")
                + (!"All Categories".equals(category)
                    ? "AND category = ? "
                    : "")
                + "ORDER BY id DESC";

        try (
            java.sql.Connection conn =
                    new DatabaseConnection().connect();
            java.sql.PreparedStatement pst =
                    conn.prepareStatement(sql)
        ) {
            if (!"All Categories".equals(category)) {
                pst.setString(1, category);
            }

            try (java.sql.ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
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
                            rs.getInt("stock"),
                            rs.getString("status")
                    );

                    productList.add(product);

                    model.addRow(new Object[]{
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
                        getStockStatus(product.getStock())
                    });
                }
            }
        } catch (java.sql.SQLException e) {
            logger.log(
                    java.util.logging.Level.SEVERE,
                    "Failed to load products",
                    e
            );

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to load products:\n" + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }

        jButton15.setText(showingArchived ? "Restore" : "Archive");
        jButton11.setEnabled(!showingArchived);
        jButton8.setEnabled(!showingArchived);
    }

    private void displayProducts() {
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) jTable1.getModel();

        model.setRowCount(0);

        for (Product product : productList) {
            model.addRow(new Object[]{
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
                getStockStatus(product.getStock())
            });
        }
    }

private void setupSearchAutocomplete() {
    suggestionModel = new javax.swing.DefaultListModel<>();
    suggestionList = new javax.swing.JList<>(suggestionModel);

    suggestionList.setFocusable(false);
    suggestionList.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION
    );
    suggestionList.setFont(
            new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13)
    );

    searchPopup = new javax.swing.JPopupMenu();
    searchPopup.setFocusable(false);
    searchPopup.setBorder(
            javax.swing.BorderFactory.createLineBorder(
                    new java.awt.Color(200, 200, 200)
            )
    );

    javax.swing.JScrollPane scrollPane =
            new javax.swing.JScrollPane(suggestionList);

    scrollPane.setPreferredSize(new java.awt.Dimension(450, 180));
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
    });

    jTextField1.addKeyListener(
            new java.awt.event.KeyAdapter() {

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            if (!searchPopup.isVisible()
                    || suggestionModel.isEmpty()) {
                return;
            }

            int selected = suggestionList.getSelectedIndex();
            int lastIndex = suggestionModel.getSize() - 1;

            switch (e.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_DOWN:
                    if (selected < 0 || selected >= lastIndex) {
                        suggestionList.setSelectedIndex(0);
                    } else {
                        suggestionList.setSelectedIndex(selected + 1);
                    }

                    suggestionList.ensureIndexIsVisible(
                            suggestionList.getSelectedIndex()
                    );
                    e.consume();
                    break;

                case java.awt.event.KeyEvent.VK_UP:
                    if (selected <= 0) {
                        suggestionList.setSelectedIndex(lastIndex);
                    } else {
                        suggestionList.setSelectedIndex(selected - 1);
                    }

                    suggestionList.ensureIndexIsVisible(
                            suggestionList.getSelectedIndex()
                    );
                    e.consume();
                    break;

                case java.awt.event.KeyEvent.VK_ENTER:
                    if (selected >= 0) {
                    selectSuggestion();
                           e.consume();
                    } else {
                    searchPopup.setVisible(false);
        
    }
    break;

                case java.awt.event.KeyEvent.VK_ESCAPE:
                    searchPopup.setVisible(false);
                    suggestionList.clearSelection();
                    e.consume();
                    break;
            }
        }
    });

    suggestionList.addMouseListener(
            new java.awt.event.MouseAdapter() {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() == 2) {
                selectSuggestion();
            }
        }
    });
}

public void refreshProducts() {
    loadProducts();

    if (searchPopup != null) {
        searchPopup.setVisible(false);
    }

    if (suggestionModel != null) {
        suggestionModel.clear();
    }

    if (suggestionProductIds != null) {
        suggestionProductIds.clear();
    }
}

private void showSuggestions() {
    if (searchPopup == null
            || suggestionModel == null
            || suggestionList == null) {
        return;
    }

    String keyword = jTextField1.getText().trim();

    suggestionModel.clear();
    suggestionProductIds.clear();

    if (keyword.isEmpty()
            || keyword.equalsIgnoreCase("Search Product...")) {
        searchPopup.setVisible(false);
        return;
    }

    for (Product product : productList) {
        if (containsIgnoreCase(product.getProductId(), keyword)
                || containsIgnoreCase(product.getName(), keyword)
                || containsIgnoreCase(product.getCategory(), keyword)
                || containsIgnoreCase(product.getBrand(), keyword)
                || containsIgnoreCase(product.getVariant(), keyword)
                || containsIgnoreCase(product.getSize(), keyword)
                || containsIgnoreCase(
                        product.getSpecification(), keyword)
                || containsIgnoreCase(product.getColor(), keyword)
                || containsIgnoreCase(product.getUnit(), keyword)) {

            suggestionModel.addElement(
                    product.getProductId() + " - " + product.getName()
            );

            suggestionProductIds.add(product.getProductId());

            if (suggestionModel.getSize() >= 10) {
                break;
            }
        }
    }

    if (suggestionModel.isEmpty() || !jTextField1.isShowing()) {
        searchPopup.setVisible(false);
        return;
    }

    suggestionList.clearSelection();

    searchPopup.setPopupSize(
            jTextField1.getWidth(),
            Math.min(180, suggestionModel.getSize() * 30 + 5)
    );

    if (!searchPopup.isVisible()) {
        searchPopup.show(
                jTextField1,
                0,
                jTextField1.getHeight()
        );
    }
}
    
    private void selectSuggestion() {
        int selectedIndex = suggestionList.getSelectedIndex();

        if (selectedIndex < 0
                || selectedIndex >= suggestionProductIds.size()) {
            return;
        }

        String productId = suggestionProductIds.get(selectedIndex);

        searchPopup.setVisible(false);
        jTextField1.setText(productId);
        jButton7.doClick();

        javax.swing.SwingUtilities.invokeLater(() -> {
            jTextField1.requestFocusInWindow();
            jTextField1.setCaretPosition(jTextField1.getText().length());
        });
    }
    
private void setupStatusRenderer() {
    javax.swing.table.DefaultTableCellRenderer renderer =
        new javax.swing.table.DefaultTableCellRenderer() {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            java.awt.Component component =
                super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );

            String status = value == null
                    ? ""
                    : value.toString().trim();

            if (isSelected) {
                component.setBackground(table.getSelectionBackground());
                component.setForeground(table.getSelectionForeground());
            } else if ("In Stock".equalsIgnoreCase(status)) {
                component.setBackground(new java.awt.Color(209, 250, 229));
                component.setForeground(new java.awt.Color(22, 101, 52));
            } else if ("Low Stock".equalsIgnoreCase(status)) {
                component.setBackground(new java.awt.Color(255, 237, 213));
                component.setForeground(new java.awt.Color(154, 52, 18));
            } else if ("Out of Stock".equalsIgnoreCase(status)
                    || "No Stock".equalsIgnoreCase(status)) {
                component.setBackground(new java.awt.Color(254, 226, 226));
                component.setForeground(new java.awt.Color(153, 27, 27));
            } else {
                component.setBackground(java.awt.Color.WHITE);
                component.setForeground(java.awt.Color.BLACK);
            }

            setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

            return component;
        }
    };

    // Change 11 to the actual Status column index if different.
    if (jTable1.getColumnCount() > 11) {
        jTable1.getColumnModel().getColumn(11)
                .setCellRenderer(renderer);
    }
}


    
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
        jButton14 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jButton8 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton11 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(java.awt.SystemColor.control);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(204, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
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

        jButton12.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(153, 153, 153));
        jButton12.setText("Inventory History");
        jButton12.addActionListener(this::jButton12ActionPerformed);

        jButton14.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton14.setForeground(new java.awt.Color(153, 153, 153));
        jButton14.setText("Account Settings");
        jButton14.addActionListener(this::jButton14ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(55, 55, 55)
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
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton7.setText("Search");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton8.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton8.setText("Add Product");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Categories", "Electronics", "Office Supplies", "School Supplies" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jButton9.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton9.setText("Refresh");
        jButton9.addActionListener(this::jButton9ActionPerformed);

        jButton10.setText("Clear Search");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort By ▼", "Price: Low to High", "Price: High to Low", "Stock: Low to High", "Stock: High to Low" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);

        jButton11.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton11.setText("Edit Product");
        jButton11.addActionListener(this::jButton11ActionPerformed);

        jButton15.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton15.setText("Archieve");
        jButton15.addActionListener(this::jButton15ActionPerformed);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "On Stock Products", "Archieved Products" }));
        jComboBox3.addActionListener(this::jComboBox3ActionPerformed);

        jPanel3.setBackground(new java.awt.Color(204, 0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AlfeLogoRedHorizontal (1).png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Products");

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Manage Your Products");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(56, 56, 56)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(1478, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Name", "Category", "Brand", "Variant", "Size", "Specification", "Color", "Unit", "Price", "Stock", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(jButton11)
                        .addGap(202, 202, 202)
                        .addComponent(jButton8)
                        .addGap(172, 172, 172)
                        .addComponent(jButton9)
                        .addGap(147, 147, 147)
                        .addComponent(jButton15)
                        .addGap(438, 438, 438))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7)
                                .addGap(18, 18, 18)
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(321, 321, 321)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1553, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(111, 111, 111))))
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

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
         loadProducts();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
         String search = jTextField1.getText().trim();

    if (search.equals("Search Product...")) {
        search = "";
    }

    if (search.isEmpty()) {
        loadProducts();
        return;
    }

    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) jTable1.getModel();

    model.setRowCount(0);

    boolean found = false;

    for (Product product : productList) {
        if (containsIgnoreCase(product.getProductId(), search)
                || containsIgnoreCase(product.getName(), search)
                || containsIgnoreCase(product.getCategory(), search)
                || containsIgnoreCase(product.getBrand(), search)
                || containsIgnoreCase(product.getVariant(), search)
                || containsIgnoreCase(product.getSize(), search)
                || containsIgnoreCase(product.getSpecification(), search)
                || containsIgnoreCase(product.getColor(), search)
                || containsIgnoreCase(product.getUnit(), search)) {

            model.addRow(new Object[]{
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
                getStockStatus(product.getStock())
            });

            found = true;
        }
    }

    if (!found) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "No matching products found.",
                "Search",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        AddProduct addProduct = new AddProduct();
        addProduct.setLocationRelativeTo(this);
        addProduct.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
         loadProducts();

    javax.swing.JOptionPane.showMessageDialog(
        this,
        "Products refreshed successfully.",
        "Refresh",
        javax.swing.JOptionPane.INFORMATION_MESSAGE
    );
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        jTextField1.setText("Search Product...");
        jTextField1.setForeground(java.awt.Color.GRAY);

        jComboBox1.setSelectedIndex(0);

        jComboBox2.setSelectedIndex(0);

    loadProducts();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        String sortType = jComboBox2.getSelectedItem().toString();

    if ("Sort By ▼".equals(sortType)) {
        return;
    }

    bubbleSort(sortType);
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Dashboard dashboard = new Dashboard();
        dashboard.setLocationRelativeTo(this);
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Inventory inventory = new Inventory();
        inventory.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
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

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please select a product to edit.",
            "Edit Product",
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // Convert the selected row in case the table is sorted.
    int modelRow = jTable1.convertRowIndexToModel(selectedRow);

    String productId =
        jTable1.getModel().getValueAt(modelRow, 0).toString();

    Product selectedProduct = null;

    for (Product product : productList) {
        if (product.getProductId().equals(productId)) {
            selectedProduct = product;
            break;
        }
    }

    if (selectedProduct == null) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Product could not be found.",
            "Edit Product",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    EditProduct editProduct =
        new EditProduct(selectedProduct, this);

    editProduct.setLocationRelativeTo(this);
    editProduct.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

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

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        AccountSettings settings = new AccountSettings();
        settings.setLocationRelativeTo(this);
        settings.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        int selectedRow = jTable1.getSelectedRow();

    boolean showingArchived =
        "Archieved Products".equals(jComboBox3.getSelectedItem());

    String action = showingArchived ? "restore" : "archive";

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please select a product to " + action + ".",
            "Product " + (showingArchived ? "Restore" : "Archive"),
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int modelRow = jTable1.convertRowIndexToModel(selectedRow);

    String productId = jTable1.getModel()
        .getValueAt(modelRow, 0).toString();

    String productName = jTable1.getModel()
        .getValueAt(modelRow, 1).toString();

    int choice = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to " + action + " product \""
            + productName + "\" (" + productId + ")?",
        "Confirm " + (showingArchived ? "Restore" : "Archive"),
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.QUESTION_MESSAGE
    );

    if (choice != javax.swing.JOptionPane.YES_OPTION) {
        return;
    }

    String sql = showingArchived
        ? "UPDATE products SET status = 'ACTIVE' "
            + "WHERE product_id = ? AND status = 'ARCHIVED'"
        : "UPDATE products SET status = 'ARCHIVED' "
            + "WHERE product_id = ? "
            + "AND (status IS NULL OR status <> 'ARCHIVED')";

    try (
        java.sql.Connection conn = new DatabaseConnection().connect();
        java.sql.PreparedStatement pst = conn.prepareStatement(sql)
    ) {
        pst.setString(1, productId);

        int updated = pst.executeUpdate();

        if (updated == 1) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                showingArchived
                    ? "Product restored successfully."
                    : "Product archived successfully.",
                "Success",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            loadProducts();
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "No product was updated.",
                "Notice",
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
        }

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Failed to " + action + " product:\n" + e.getMessage(),
            "Database Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        loadProducts();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Products().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
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
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
  }
