/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class InventoryCashier extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(InventoryCashier.class.getName());

    private javax.swing.JPopupMenu searchPopup;
    private javax.swing.JList<String> suggestionList;
    private javax.swing.DefaultListModel<String> suggestionModel;
    private java.util.ArrayList<Product> productList =
            new java.util.ArrayList<>();
    private java.util.ArrayList<String> suggestionProductIds =
            new java.util.ArrayList<>();
    private String currentSearch = "";
    private String currentCategory = "All Categories";
    private String currentSort = "Sort By ▼";
    
    
public InventoryCashier() {
    initComponents();

    // Keep your existing UI and allow it to fill the maximized window.
    getContentPane().setLayout(new java.awt.BorderLayout());
    getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

    // Restore search suggestions and stock-status colors.
    setupSearchAutocomplete();
    setupStatusRenderer();

    jTable1.getColumnModel()
            .getColumn(10)
            .setCellRenderer(new StockCellRenderer());

    jTable1.setAutoResizeMode(
            javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
    );

    // Restore the product data and text inside the table.
    loadProducts();

    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
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

            } else if ("Out of Stock".equalsIgnoreCase(status)) {
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

    jTable1.getColumnModel()
            .getColumn(11)
            .setCellRenderer(renderer);
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

    jTextField2.setText(productId);

    jButton5.doClick();

    javax.swing.SwingUtilities.invokeLater(() -> {

        jTextField2.requestFocusInWindow();

        jTextField2.setCaretPosition(
                jTextField2.getText().length()
        );
    });
}
    
    private void showSuggestions() {

    String keyword = jTextField2.getText().trim();

    if (keyword.isEmpty()
            || keyword.equals("Search Product...")) {

        searchPopup.setVisible(false);
        return;
    }

    keyword = keyword.toLowerCase();

    suggestionModel.clear();
    suggestionProductIds.clear();

    int count = 0;

    for (Product product : productList) {
        if (!currentCategory.equals("All Categories")
        && !currentCategory.equalsIgnoreCase(product.getCategory())) {
    continue;
}

        if (containsIgnoreCase(product.getProductId(), keyword)
                || containsIgnoreCase(product.getName(), keyword)
                || containsIgnoreCase(product.getCategory(), keyword)
                || containsIgnoreCase(product.getBrand(), keyword)
                || containsIgnoreCase(product.getVariant(), keyword)
                || containsIgnoreCase(product.getSize(), keyword)
                || containsIgnoreCase(product.getSpecification(), keyword)
                || containsIgnoreCase(product.getColor(), keyword)
                || containsIgnoreCase(product.getUnit(), keyword)) {

            
            String suggestion =
                    product.getName()
                    + " | "
                    + product.getBrand()
                    + " | "
                    + product.getVariant()
                    + " | "
                    + product.getSize()
                    + " | "
                    + product.getSpecification()
                    + " | "
                    + product.getColor()
                    + " | "
                    + product.getUnit()
                    + " | ₱"
                    + String.format("%.2f", product.getPrice())
                    + " | Stock: "
                    + product.getStock();

            suggestionModel.addElement(suggestion);

            suggestionProductIds.add(
                    product.getProductId()
            );

            count++;

            if (count >= 8) {
                break;
            }
        }
    }

    if (suggestionModel.isEmpty()) {

        searchPopup.setVisible(false);
        return;
    }

    suggestionList.setSelectedIndex(0);

    searchPopup.show(
            jTextField2,
            0,
            jTextField2.getHeight()
    );
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

    jTextField2.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {

        private void updateSuggestions() {
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (jTextField2.isShowing()) {
                    showSuggestions();
                }
            });
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

    suggestionList.addMouseListener(
            new java.awt.event.MouseAdapter() {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() == 2) {
                selectSuggestion();
            }
        }
    });

    jTextField2.addKeyListener(
            new java.awt.event.KeyAdapter() {

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {

            if (e.getKeyCode()
                    == java.awt.event.KeyEvent.VK_ESCAPE) {
                searchPopup.setVisible(false);
                e.consume();
                return;
            }

            if (!searchPopup.isVisible()) {
                // Enter still performs a normal search.
                if (e.getKeyCode()
                        == java.awt.event.KeyEvent.VK_ENTER) {
                    jButton5.doClick();
                    e.consume();
                }
                return;
            }

            int selected = suggestionList.getSelectedIndex();
            int lastIndex = suggestionModel.getSize() - 1;

            if (e.getKeyCode()
                    == java.awt.event.KeyEvent.VK_DOWN) {

                if (selected < lastIndex) {
                    suggestionList.setSelectedIndex(selected + 1);
                    suggestionList.ensureIndexIsVisible(selected + 1);
                }

                e.consume();

            } else if (e.getKeyCode()
                    == java.awt.event.KeyEvent.VK_UP) {

                if (selected > 0) {
                    suggestionList.setSelectedIndex(selected - 1);
                    suggestionList.ensureIndexIsVisible(selected - 1);
                }

                e.consume();

            } else if (e.getKeyCode()
                    == java.awt.event.KeyEvent.VK_ENTER) {

                if (selected >= 0
                        && selected < suggestionProductIds.size()) {
                    selectSuggestion();
                } else {
                    searchPopup.setVisible(false);
                    jButton5.doClick();
                }

                e.consume();
            }
        }
    });
}

private boolean containsIgnoreCase(
        String text,
        String keyword) {

    if (text == null) {
        return false;
    }

    return text.toLowerCase().contains(keyword);
}

private static class StockCellRenderer
        extends javax.swing.table.DefaultTableCellRenderer {

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
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );

        if (isSelected) {

            component.setForeground(
                    table.getSelectionForeground()
            );

        } else if (value != null) {

            try {
                int stock = Integer.parseInt(value.toString());

                if (stock <= 0) {
                    component.setForeground(java.awt.Color.RED);

                } else if (stock <= 5) {
                    component.setForeground(
                            new java.awt.Color(230, 150, 0)
                    );

                } else {
                    component.setForeground(
                            new java.awt.Color(0, 150, 0)
                    );
                }

            } catch (NumberFormatException e) {
                component.setForeground(java.awt.Color.BLACK);
            }

        } else {
            component.setForeground(java.awt.Color.BLACK);
        }

        setHorizontalAlignment(
                javax.swing.SwingConstants.CENTER
        );

        return component;
    }
}

private void loadProducts() {

    productList.clear();

    String sql =
    "SELECT product_id, name, category, brand, variant, size, " +
    "specification, color, unit, price, stock, status " +
    "FROM products " +
    "ORDER BY id DESC";

    DatabaseConnection db = new DatabaseConnection();

    try (
        java.sql.Connection conn = db.connect();
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery()
    ) {

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
        }

        updateInventoryTable();

    } catch (java.sql.SQLException e) {

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Failed to load products:\n" + e.getMessage(),
            "Database Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}

    
    private void filterByCategory(String category) {

    if (category.equals("All Categories")) {
        loadProducts();
        return;
    }

    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel)
                    jTable1.getModel();

    model.setRowCount(0);
    productList.clear();

    String sql =
            "SELECT product_id, name, category, brand, variant, size, " +
            "specification, color, unit, price, stock, status " +
            "FROM products " +
            "WHERE category = ? AND status = 'ACTIVE' " +
            "ORDER BY id DESC";

    DatabaseConnection db = new DatabaseConnection();

    try (
        java.sql.Connection conn = db.connect();
        java.sql.PreparedStatement pst =
                conn.prepareStatement(sql)
    ) {

        pst.setString(1, category);

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
                    getStockStatus(product.getStock())
                });
            }
        }

    } catch (java.sql.SQLException e) {

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Failed to filter products:\n" + e.getMessage(),
            "Database Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    private void bubbleSort(String sortType) {

    int n = productList.size();

    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {

            Product current = productList.get(j);
            Product next = productList.get(j + 1);

            boolean swap = false;

            if (sortType.equals("Price: Low to High")) {

                if (current.getPrice() > next.getPrice()) {
                    swap = true;
                }

            } else if (sortType.equals("Price: High to Low")) {

                if (current.getPrice() < next.getPrice()) {
                    swap = true;
                }

            } else if (sortType.equals("Stock: Low to High")) {

                if (current.getStock() > next.getStock()) {
                    swap = true;
                }

            } else if (sortType.equals("Stock: High to Low")) {

                if (current.getStock() < next.getStock()) {
                    swap = true;
                }
            }

            if (swap) {

                productList.set(j, next);
                productList.set(j + 1, current);
            }
        }
    }

    displayProducts();
}

private void displayProducts() {

    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel)
                    jTable1.getModel();

    model.setRowCount(0);

    for (Product product : productList) {

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
            getStockStatus(product.getStock())
        });
    }
}

private void makeTableFillSpace() {
    // Let the main panel fill the maximized JFrame.
    getContentPane().setLayout(new java.awt.BorderLayout());
    getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

    javax.swing.GroupLayout layout =
            (javax.swing.GroupLayout) jPanel1.getLayout();

    

layout.setHorizontalGroup(
    layout.createParallelGroup(
        javax.swing.GroupLayout.Alignment.LEADING
    )
    .addComponent(
        jPanel2,
        javax.swing.GroupLayout.DEFAULT_SIZE,
        javax.swing.GroupLayout.DEFAULT_SIZE,
        Short.MAX_VALUE
    )
    .addGroup(
        javax.swing.GroupLayout.Alignment.CENTER,
        layout.createSequentialGroup()
            .addComponent(jScrollPane1, 1181, 1181, 1181)
    )
    .addGroup(
        javax.swing.GroupLayout.Alignment.CENTER,
        layout.createSequentialGroup()
            .addGroup(
                layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING
                )
                .addComponent(jLabel2)
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent(jTextField2, 712, 712, 712)
                        .addGap(40)
                        .addComponent(jButton5)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED
                        )
                        .addComponent(jButton4)
                )
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent(jComboBox1)
                        .addGap(220)
                        .addComponent(jComboBox2)
                        .addGap(182)
                        .addComponent(jButton3)
                )
            )
    )
);


    // Keep the original vertical spacing and let the table expand.
    layout.setVerticalGroup(
        layout.createSequentialGroup()
            .addComponent(
                jPanel2,
                javax.swing.GroupLayout.PREFERRED_SIZE,
                javax.swing.GroupLayout.DEFAULT_SIZE,
                javax.swing.GroupLayout.PREFERRED_SIZE
            )
            .addGap(33)
            .addComponent(
                jLabel2,
                javax.swing.GroupLayout.PREFERRED_SIZE,
                41,
                javax.swing.GroupLayout.PREFERRED_SIZE
            )
            .addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED
            )
            .addGroup(
                layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.BASELINE
                )
                .addComponent(
                    jTextField2,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    41,
                    javax.swing.GroupLayout.PREFERRED_SIZE
                )
                .addComponent(
                    jButton4,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    41,
                    javax.swing.GroupLayout.PREFERRED_SIZE
                )
                .addComponent(
                    jButton5,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    41,
                    javax.swing.GroupLayout.PREFERRED_SIZE
                )
            )
            .addGap(32)
            .addGroup(
                layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.BASELINE
                )
                .addComponent(jComboBox1)
                .addComponent(jComboBox2)
                .addComponent(
                    jButton3,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    41,
                    javax.swing.GroupLayout.PREFERRED_SIZE
                )
            )
            .addGap(30)
            .addComponent(
                jScrollPane1,
                0,
                528,
                Short.MAX_VALUE
            )
            .addGap(20)
    );

    jTable1.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
    );

    jPanel1.revalidate();
    jPanel1.repaint();
}

private void updateInventoryTable() {

    java.util.ArrayList<Product> filteredProducts =
            new java.util.ArrayList<>();

    for (Product product : productList) {

        boolean matchesCategory =
                currentCategory.equals("All Categories")
                || currentCategory.equalsIgnoreCase(
                        product.getCategory());

        boolean matchesSearch =
                currentSearch.isEmpty()
                || containsIgnoreCase(product.getProductId(), currentSearch)
                || containsIgnoreCase(product.getName(), currentSearch)
                || containsIgnoreCase(product.getCategory(), currentSearch)
                || containsIgnoreCase(product.getBrand(), currentSearch)
                || containsIgnoreCase(product.getVariant(), currentSearch)
                || containsIgnoreCase(product.getSize(), currentSearch)
                || containsIgnoreCase(product.getSpecification(), currentSearch)
                || containsIgnoreCase(product.getColor(), currentSearch)
                || containsIgnoreCase(product.getUnit(), currentSearch);

        if (matchesCategory && matchesSearch) {
            filteredProducts.add(product);
        }
    }

    switch (currentSort) {

        case "Price: Low to High":
            filteredProducts.sort(
                java.util.Comparator.comparingDouble(
                    Product::getPrice));
            break;

        case "Price: High to Low":
            filteredProducts.sort(
                java.util.Comparator.comparingDouble(
                    Product::getPrice).reversed());
            break;

        case "Stock: Low to High":
            filteredProducts.sort(
                java.util.Comparator.comparingInt(
                    Product::getStock));
            break;

        case "Stock: High to Low":
            filteredProducts.sort(
                java.util.Comparator.comparingInt(
                    Product::getStock).reversed());
            break;
    }

    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel)
                    jTable1.getModel();

    model.setRowCount(0);

    for (Product product : filteredProducts) {
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
            getStockStatus(product.getStock())
        });
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
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setForeground(java.awt.SystemColor.control);

        jPanel2.setBackground(new java.awt.Color(204, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inventory");

        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton2.setText("Back");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AlfeLogoRedHorizontal (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(36, 36, 36))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel2.setText("Search Products");

        jButton3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton3.setText("Refresh");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton4.setText("Clear");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jComboBox1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Categories", "Electronics", "Office Supplies", "School Supplies" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jComboBox2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort By ▼", "Price: Low to High", "Price: High to Low", "Stock: Low to High", "Stock: High to Low" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Name", "Category", "Brand", "Variant", "Size", "Specification", "Color", "Unit", "Price", "Stock", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton5.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton5.setText("Search");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 712, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jButton5)
                        .addGap(62, 62, 62)
                        .addComponent(jButton4)
                        .addGap(60, 60, 60)
                        .addComponent(jButton3))
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(120, 120, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 229, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        CashierDashboard dashboard =
        new CashierDashboard();

        dashboard.setLocationRelativeTo(this);
        dashboard.setVisible(true);

        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        jButton5.doClick();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        loadProducts();

    javax.swing.JOptionPane.showMessageDialog(
        this,
        "Inventory refreshed successfully.",
        "Refresh",
        javax.swing.JOptionPane.INFORMATION_MESSAGE
    );
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    currentSearch = "";
    currentCategory = "All Categories";
    currentSort = "Sort By ▼";

    jTextField2.setText("Search Product...");
    jTextField2.setForeground(java.awt.Color.GRAY);

    jComboBox1.setSelectedIndex(0);
    jComboBox2.setSelectedIndex(0);

    if (searchPopup != null) {
        searchPopup.setVisible(false);
    }

    updateInventoryTable();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        currentSort =
        jComboBox2.getSelectedItem().toString();

    updateInventoryTable();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        currentCategory =
        jComboBox1.getSelectedItem().toString();
    updateInventoryTable();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String search = jTextField2.getText().trim();

    if (search.equalsIgnoreCase("Search Product...")) {
        search = "";
    }

    currentSearch = search;

    if (searchPopup != null) {
        searchPopup.setVisible(false);
    }

    updateInventoryTable();

    jTextField2.requestFocusInWindow();
    jTextField2.setCaretPosition(jTextField2.getText().length());

    }//GEN-LAST:event_jButton5ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new InventoryCashier().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
