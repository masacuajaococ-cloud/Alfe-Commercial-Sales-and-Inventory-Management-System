import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Admin
 */


public class POSterminal extends javax.swing.JFrame {
    
    private final java.util.ArrayList<String> saleProductIds =
        new java.util.ArrayList<>();
    
    private java.util.ArrayList<Product> productList =
        new java.util.ArrayList<>();

private javax.swing.JPopupMenu searchPopup;
private javax.swing.JList<String> suggestionList;
private javax.swing.DefaultListModel<String> suggestionModel;

private java.util.ArrayList<String> suggestionProductIds =
        new java.util.ArrayList<>();
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(POSterminal.class.getName());

    /**
     * Creates new form CashierSales
     */
    
public POSterminal() {
    initComponents();

    jLabel3.setText(Session.firstName + " " + Session.lastName);
    jLabel4.setText(Session.role);

    jTextField2.setText("1");
    jLabel18.setText("0 Products");

    setupSearchAutocomplete();
    setupKeyboardShortcuts();
    setupPOSKeyboardShortcuts();

    updateTotal();
    updateProductCount();

    // Maximize after initializing the GUI
    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
}

private void loadAutocompleteProducts() {
    productList.clear();

    String sql = "SELECT product_id, name, category, brand, variant, "
            + "size, specification, color, unit, price, stock, status "
            + "FROM products ORDER BY id DESC";

    try (Connection conn = new DatabaseConnection().connect();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            productList.add(new Product(
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
            ));
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Failed to load product suggestions:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

private boolean matches(String value, String keyword) {
    return value != null
            && value.toLowerCase().contains(keyword.toLowerCase());
}

private void showSuggestions() {
    if (searchPopup == null || !jTextField1.isShowing()) {
        return;
    }

    String keyword = jTextField1.getText().trim();

    suggestionModel.clear();
    suggestionProductIds.clear();

    if (keyword.isEmpty()) {
        searchPopup.setVisible(false);
        return;
    }

    int count = 0;

    for (Product product : productList) {
        if (matches(product.getProductId(), keyword)
                || matches(product.getName(), keyword)
                || matches(product.getCategory(), keyword)
                || matches(product.getBrand(), keyword)
                || matches(product.getVariant(), keyword)
                || matches(product.getSize(), keyword)
                || matches(product.getSpecification(), keyword)
                || matches(product.getColor(), keyword)
                || matches(product.getUnit(), keyword)) {

            String suggestion = product.getProductId()
                    + " | " + product.getName()
                    + " | " + product.getBrand()
                    + " | ₱" + String.format("%.2f", product.getPrice())
                    + " | Stock: " + product.getStock();

            suggestionModel.addElement(suggestion);
            suggestionProductIds.add(product.getProductId());

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

    if (!searchPopup.isVisible()) {
        searchPopup.show(
                jTextField1,
                0,
                jTextField1.getHeight()
        );
    }
}

private void setupSearchAutocomplete() {
    loadAutocompleteProducts();

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

    scrollPane.setPreferredSize(new java.awt.Dimension(600, 170));
    searchPopup.add(scrollPane);

    jTextField1.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {
        private void update() {
            javax.swing.SwingUtilities.invokeLater(
                    () -> showSuggestions()
            );
        }

        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            update();
        }
    });

    jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            if (!searchPopup.isVisible()) {
                return;
            }

            int selected = suggestionList.getSelectedIndex();

            if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                if (selected < suggestionModel.size() - 1) {
                    suggestionList.setSelectedIndex(selected + 1);
                    suggestionList.ensureIndexIsVisible(selected + 1);
                }
                e.consume();

            } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
                if (selected > 0) {
                    suggestionList.setSelectedIndex(selected - 1);
                    suggestionList.ensureIndexIsVisible(selected - 1);
                }
                e.consume();

            } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                selectSuggestion();
                e.consume();

            } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                   searchPopup.setVisible(false);
                e.consume();
}
        }
    });

    suggestionList.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int index = suggestionList.locationToIndex(e.getPoint());

        if (index >= 0) {
            java.awt.Rectangle bounds =
                    suggestionList.getCellBounds(index, index);

            if (bounds != null && bounds.contains(e.getPoint())) {
                suggestionList.setSelectedIndex(index);

                if (e.getClickCount() == 2) {
                    selectSuggestion();
                }
            }
        }
    }
});
}

private void selectSuggestion() {
    int selected = suggestionList.getSelectedIndex();

    if (selected < 0 || selected >= suggestionProductIds.size()) {
        return;
    }

    String productId = suggestionProductIds.get(selected);

    searchPopup.setVisible(false);
    jTextField1.setText(productId);
    addProductToSale();
}
    


private void addProductToSale() {
    String search = jTextField1.getText().trim();

    if (search.isEmpty()) {
        jTextField1.requestFocusInWindow();
        return;
    }

    int requestedQuantity;

    try {
        requestedQuantity = Integer.parseInt(jTextField2.getText().trim());

        if (requestedQuantity <= 0) {
            throw new NumberFormatException();
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter a valid quantity greater than zero.",
                "Invalid Quantity",
                JOptionPane.WARNING_MESSAGE
        );
        jTextField2.requestFocusInWindow();
        jTextField2.selectAll();
        return;
    }

    String sql =
            "SELECT product_id, name, price, stock FROM products "
            + "WHERE product_id = ? OR name = ? OR name LIKE ? "
            + "ORDER BY CASE WHEN product_id = ? THEN 0 "
            + "WHEN name = ? THEN 1 ELSE 2 END LIMIT 1";

    try (Connection conn = new DatabaseConnection().connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, search);
        pst.setString(2, search);
        pst.setString(3, "%" + search + "%");
        pst.setString(4, search);
        pst.setString(5, search);

        try (ResultSet rs = pst.executeQuery()) {
            if (!rs.next()) {
                JOptionPane.showMessageDialog(
                        this, "Product not found.",
                        "Search Result", JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            String productId = rs.getString("product_id");
            String productName = rs.getString("name");
            double price = rs.getDouble("price");
            int stock = rs.getInt("stock");

            if (requestedQuantity > stock) {
                JOptionPane.showMessageDialog(
                        this,
                        "Only " + stock + " unit(s) available.",
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            DefaultTableModel model =
                    (DefaultTableModel) jTable1.getModel();

            if (saleProductIds.size() != model.getRowCount()) {
                JOptionPane.showMessageDialog(
                        this,
                        "The product IDs do not match the sale table.",
                        "POS Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                if (saleProductIds.get(i).equals(productId)) {
                    int currentQuantity = Integer.parseInt(
                            model.getValueAt(i, 1).toString()
                    );

                    int newQuantity = currentQuantity + requestedQuantity;

                    if (newQuantity > stock) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Only " + stock + " unit(s) are in stock.",
                                "Stock Limit",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    model.setValueAt(newQuantity, i, 1);
                    model.setValueAt(
                            String.format("₱%.2f", price), i, 2
                    );
                    model.setValueAt(
                            String.format("₱%.2f", newQuantity * price),
                            i, 3
                    );

                    finishAddingProduct();
                    return;
                }
            }

            model.addRow(new Object[]{
                productName,
                requestedQuantity,
                String.format("₱%.2f", price),
                String.format("₱%.2f", requestedQuantity * price)
            });

            saleProductIds.add(productId);
            finishAddingProduct();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Error searching product:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}


private int getStockForProduct(String productId) throws Exception {
    String sql = "SELECT stock FROM products WHERE product_id = ?";

    try (Connection conn = new DatabaseConnection().connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, productId);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("stock");
            }
        }
    }

    return -1;
}

private void finishAddingProduct() {
    updateTotal();
    updateProductCount();

    jTextField1.setText("");
    jTextField2.setText("1");

    jTextField1.requestFocusInWindow();
}
    
    private void updateTotal() {
    DefaultTableModel model =
            (DefaultTableModel) jTable1.getModel();

    double total = 0.0;

    for (int i = 0; i < model.getRowCount(); i++) {

        String subtotalText =
                model.getValueAt(i, 3).toString()
                     .replace("₱", "")
                     .replace(",", "")
                     .trim();

        total += Double.parseDouble(subtotalText);
    }

    jLabel11.setText(String.format("₱%.2f", total));
}
    
    
private void updateProductCount() {
    DefaultTableModel model =
            (DefaultTableModel) jTable1.getModel();

    int totalQuantity = 0;

    for (int i = 0; i < model.getRowCount(); i++) {
        totalQuantity += Integer.parseInt(
                model.getValueAt(i, 1).toString()
        );
    }

    jLabel18.setText(
            totalQuantity
            + (totalQuantity == 1 ? " Product" : " Products")
    );
}
    
private void removeSelectedItem() {
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select an item to remove.",
                "No Item Selected",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    DefaultTableModel model =
            (DefaultTableModel) jTable1.getModel();

    model.removeRow(selectedRow);

    if (selectedRow < saleProductIds.size()) {
        saleProductIds.remove(selectedRow);
    }

    updateTotal();
    updateProductCount();
}
    
private void setupKeyboardShortcuts() {
    javax.swing.JRootPane root = getRootPane();

    javax.swing.InputMap inputMap = root.getInputMap(
            javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
    );

    javax.swing.ActionMap actionMap = root.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelSearch");

    actionMap.put("cancelSearch", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (searchPopup != null && searchPopup.isVisible()) {
                searchPopup.setVisible(false);
            } else {
                jTextField1.setText("");
                jTextField2.setText("1");
                jTextField1.requestFocusInWindow();
            }
        }
    });
}
    
    
    
    

private void changeSelectedQuantity(int change) {
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select a product first.",
                "No Item Selected",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    DefaultTableModel model =
            (DefaultTableModel) jTable1.getModel();

    try {
        int currentQuantity = Integer.parseInt(
                model.getValueAt(selectedRow, 1).toString()
        );

        int newQuantity = currentQuantity + change;

        if (newQuantity <= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove this product from the sale?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            model.removeRow(selectedRow);

            if (selectedRow < saleProductIds.size()) {
                saleProductIds.remove(selectedRow);
            }

        } else {
            // Check current stock before increasing quantity.
            if (change > 0) {
                if (selectedRow >= saleProductIds.size()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Unable to identify this product. "
                            + "Please remove it and add it again.",
                            "Product Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int stock = getStockForProduct(
                        saleProductIds.get(selectedRow)
                );

                if (stock < 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Product not found in the database.",
                            "Product Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (newQuantity > stock) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Only " + stock + " unit(s) are available.",
                            "Insufficient Stock",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }

            String priceText = model.getValueAt(selectedRow, 2)
                    .toString()
                    .replace("₱", "")
                    .replace(",", "")
                    .trim();

            double price = Double.parseDouble(priceText);

            model.setValueAt(newQuantity, selectedRow, 1);
            model.setValueAt(
                    String.format("₱%.2f", newQuantity * price),
                    selectedRow, 3
            );
        }

        updateTotal();
        updateProductCount();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Unable to update quantity:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    
    
private void setupPOSKeyboardShortcuts() {
    javax.swing.JRootPane root = getRootPane();
    javax.swing.InputMap inputMap =
            root.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
    javax.swing.ActionMap actionMap = root.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "focusSearch");
    actionMap.put("focusSearch", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jTextField1.requestFocusInWindow();
            jTextField1.selectAll();
        }
    });

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "openCheckout");
    actionMap.put("openCheckout", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openCheckout();
        }
    });

    javax.swing.InputMap tableInputMap = jTable1.getInputMap(
            javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    javax.swing.ActionMap tableActionMap = jTable1.getActionMap();

    tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "increaseQty");
    tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.SHIFT_DOWN_MASK), "increaseQty");
    tableActionMap.put("increaseQty", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeSelectedQuantity(1);
        }
    });

    tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "decreaseQty");
    tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "decreaseQty");
    tableActionMap.put("decreaseQty", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeSelectedQuantity(-1);
        }
    });

    javax.swing.Action removeAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeSelectedItem();
        }
    };

    tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "removeProduct");
    tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "removeProduct");
    tableActionMap.put("removeProduct", removeAction);
}

private void openCheckout() {
    jButton2ActionPerformed(null);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(java.awt.SystemColor.control);

        jPanel2.setBackground(new java.awt.Color(204, 0, 0));

        jButton1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton1.setText("Back");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Cashier");

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Welcome,");

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Name");

        jLabel19.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("STORE POS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(116, 116, 116)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(402, 402, 402))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel5.setText("Add Product");

        jTextField1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel6.setText("Current Sale");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Quantity", "Unit Price", "Subtotal"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(204, 0, 0));

        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Total Due");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel10)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel11.setText("₱0.00");

        jButton2.setBackground(new java.awt.Color(204, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("[ F12 PAY NOW ]");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("F5 Search · Enter Add · Delete Remove · ESC Cancel");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(26, 26, 26))
        );

        jTextField2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jLabel17.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel17.setText("Quantity");

        jLabel18.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel18.setText("0 Products");

        jButton3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton3.setText("Remove");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton4.setText("- Qty");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton5.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton5.setText("+ Qty");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jLabel20.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel20.setText("Product Name or ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(788, 788, 788))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(103, 103, 103)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(772, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel18))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1284, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(jButton4)
                .addGap(40, 40, 40)
                .addComponent(jButton5)
                .addGap(54, 54, 54)
                .addComponent(jButton3)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
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
                .addGap(0, 7, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        CashierDashboard dashboard = new CashierDashboard();
        dashboard.setLocationRelativeTo(this);
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        addProductToSale();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        addProductToSale();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultTableModel model =
        (DefaultTableModel) jTable1.getModel();

        // Check if there are products in the sale
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "There are no items in the current sale.",
                "Empty Sale",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Get total
        double total = 0.0;

        for (int i = 0; i < model.getRowCount(); i++) {

            String subtotalText =
            model.getValueAt(i, 3)
            .toString()
            .replace("₱", "")
            .replace(",", "")
            .trim();

            total += Double.parseDouble(subtotalText);
        }

        // Open existing Checkout window
        Checkout checkout = new Checkout(
        total,
        jTable1,
        saleProductIds
);

        checkout.setLocationRelativeTo(this);
        checkout.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        removeSelectedItem();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        changeSelectedQuantity(-1);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        changeSelectedQuantity(1);
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
        java.awt.EventQueue.invokeLater(() -> new POSterminal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
