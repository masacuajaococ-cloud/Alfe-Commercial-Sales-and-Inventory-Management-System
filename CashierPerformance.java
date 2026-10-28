
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class CashierPerformance extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CashierPerformance.class.getName());
    
    /**
     * Creates new form CashierPerformance
     */
    
public CashierPerformance() {
    initComponents();
    initializePerformanceUI();

    getContentPane().setLayout(new java.awt.BorderLayout());
    getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

    jTable1.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
    );

    jTable2.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS
    );

    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
}


        
private boolean isAdmin() {
    return Session.role != null
            && Session.role.trim().equalsIgnoreCase("admin");
}

private void initializePerformanceUI() {
    setTitle("Cashier Performance");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setMinimumSize(new java.awt.Dimension(925, 650));

    Calendar calendar = Calendar.getInstance();
    Date today = calendar.getTime();

    calendar.set(Calendar.DAY_OF_MONTH, 1);
    Date firstDay = calendar.getTime();

    jSpinner1.setModel(new SpinnerDateModel(
        firstDay, null, null, Calendar.DAY_OF_MONTH
    ));

    jSpinner2.setModel(new SpinnerDateModel(
        today, null, null, Calendar.DAY_OF_MONTH
    ));

    jSpinner1.setEditor(new javax.swing.JSpinner.DateEditor(
        jSpinner1, "yyyy-MM-dd"
    ));

    jSpinner2.setEditor(new javax.swing.JSpinner.DateEditor(
        jSpinner2, "yyyy-MM-dd"
    ));

    jLabel5.setText("TOTAL USERS");
    jLabel7.setText("TOTAL TRANSACTIONS");
    jLabel9.setText("COMBINED SALES");

    jLabel6.setText("0");
    jLabel8.setText("0");
    jLabel10.setText("₱0.00");

    jTextField1.setText("No cashier selected");
    jTextField1.setEditable(false);
    jTextField1.setForeground(Color.BLACK);
    jTextField1.setBackground(Color.WHITE);

    DefaultTableModel cashierModel = new DefaultTableModel(
        new Object[][] {},
        new String[] {
            "Cashier ID", "Cashier Name", "Transactions",
            "Total Sales", "Average Sale"
        }
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    jTable2.setModel(cashierModel);
    jTable2.setSelectionMode(
        javax.swing.ListSelectionModel.SINGLE_SELECTION
    );

    jTable2.getColumnModel().getColumn(0).setMinWidth(0);
    jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
    jTable2.getColumnModel().getColumn(0).setWidth(0);

    DefaultTableModel transactionModel = new DefaultTableModel(
        new Object[][] {},
        new String[] {
            "Transaction ID", "Cashier", "Date & Time",
            "Payment Method", "Total", "Payment", "Change"
        }
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    jTable1.setModel(transactionModel);

    jTable1.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_OFF
    );

    jButton1.setText("Back");
    jButton2.setText("Generate Report");
    jButton3.setText("View All Transactions");
    jButton4.setText("Export Report");
    
    jTable2.getSelectionModel().addListSelectionListener(
    this::cashierSelected
);
}

private LocalDate getStartDate() {
    Date date = (Date) jSpinner1.getValue();

    return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
}

private LocalDate getEndDate() {
    Date date = (Date) jSpinner2.getValue();

    return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
}

private Timestamp getStartTimestamp() {
    return Timestamp.valueOf(getStartDate().atStartOfDay());
}

private Timestamp getEndTimestampExclusive() {
    return Timestamp.valueOf(
        getEndDate().plusDays(1).atStartOfDay()
    );
}

private String formatMoney(double amount) {
    return String.format(
        java.util.Locale.US, "₱%,.2f", amount
    );
}

private void generateCashierReport() {
    if (!isAdmin()) {
        JOptionPane.showMessageDialog(
            this, "Administrator access is required."
        );
        return;
    }

    if (getStartDate().isAfter(getEndDate())) {
        JOptionPane.showMessageDialog(
            this,
            "The start date cannot be later than the end date.",
            "Invalid Date Range",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    DefaultTableModel model =
        (DefaultTableModel) jTable2.getModel();

    model.setRowCount(0);

    long totalTransactions = 0;
    double combinedSales = 0.0;

    String sql =
    "SELECT u.id AS cashier_id, " +
    "CONCAT(COALESCE(u.first_name, ''), ' ', " +
    "COALESCE(u.last_name, '')) AS cashier_name, " +
    "COUNT(s.id) AS transaction_count, " +
    "COALESCE(SUM(s.total_amount), 0) AS total_sales, " +
    "COALESCE(AVG(s.total_amount), 0) AS average_sale " +
    "FROM users u " +
    "LEFT JOIN sales s ON s.user_id = u.id " +
    "AND s.sale_date >= ? AND s.sale_date < ? " +
    "WHERE LOWER(TRIM(u.role)) IN ('cashier', 'admin') " +
    "GROUP BY u.id, u.first_name, u.last_name " +
    "ORDER BY total_sales DESC";

    try {
        DatabaseConnection db = new DatabaseConnection();

        try (Connection conn = db.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setTimestamp(1, getStartTimestamp());
            pst.setTimestamp(2, getEndTimestampExclusive());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int cashierId = rs.getInt("cashier_id");
                    String cashierName =
                        rs.getString("cashier_name");

                    long transactions =
                        rs.getLong("transaction_count");

                    double sales =
                        rs.getDouble("total_sales");

                    double average =
                        rs.getDouble("average_sale");

                    model.addRow(new Object[] {
                        cashierId,
                        cashierName,
                        transactions,
                        formatMoney(sales),
                        formatMoney(average)
                    });

                    totalTransactions += transactions;
                    combinedSales += sales;
                }
            }
        }

        jLabel6.setText(String.valueOf(model.getRowCount()));
        jLabel8.setText(String.valueOf(totalTransactions));
        jLabel10.setText(formatMoney(combinedSales));

        
jTable2.clearSelection();
clearTransactionHistory();

jTextField1.setText(
    model.getRowCount() > 0
        ? "Select a cashier/admin to view transactions"
        : "No matching users found"
);


    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Unable to generate the cashier report:\n"
                + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

private void cashierSelected(ListSelectionEvent evt) {
    if (evt.getValueIsAdjusting()) {
        return;
    }

    int selectedRow = jTable2.getSelectedRow();

    if (selectedRow < 0) {
        return;
    }

    int cashierId = Integer.parseInt(
        jTable2.getValueAt(selectedRow, 0).toString()
    );

    String cashierName =
        jTable2.getValueAt(selectedRow, 1).toString();

    jTextField1.setText("Selected cashier: " + cashierName);

    loadTransactions(cashierId);
}

private void clearTransactionHistory() {
    DefaultTableModel model =
        (DefaultTableModel) jTable1.getModel();

    model.setRowCount(0);
}

private void loadTransactions(Integer cashierId) {
    if (!isAdmin()) {
        JOptionPane.showMessageDialog(
            this, "Administrator access is required."
        );
        return;
    }

    if (getStartDate().isAfter(getEndDate())) {
        JOptionPane.showMessageDialog(
            this, "Please select a valid date range."
        );
        return;
    }

    DefaultTableModel model =
        (DefaultTableModel) jTable1.getModel();

    model.setRowCount(0);

    String sql =
        "SELECT s.transaction_id, " +
        "CONCAT(u.first_name, ' ', u.last_name) AS cashier_name, " +
        "s.sale_date, s.payment_method, s.total_amount, " +
        "s.payment, s.change_amount " +
        "FROM sales s " +
        "INNER JOIN users u ON s.user_id = u.id " +
        "WHERE LOWER(TRIM(u.role)) IN ('cashier', 'admin') " +
        "AND s.sale_date >= ? AND s.sale_date < ? ";

    if (cashierId != null) {
        sql += "AND s.user_id = ? ";
    }

    sql += "ORDER BY s.sale_date DESC";

    SimpleDateFormat dateFormat =
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    try {
        DatabaseConnection db = new DatabaseConnection();

        try (Connection conn = db.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setTimestamp(1, getStartTimestamp());
            pst.setTimestamp(2, getEndTimestampExclusive());

            if (cashierId != null) {
                pst.setInt(3, cashierId);
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Timestamp saleDate =
                        rs.getTimestamp("sale_date");

                    model.addRow(new Object[] {
                        rs.getString("transaction_id"),
                        rs.getString("cashier_name"),
                        dateFormat.format(saleDate),
                        rs.getString("payment_method"),
                        formatMoney(rs.getDouble("total_amount")),
                        formatMoney(rs.getDouble("payment")),
                        formatMoney(rs.getDouble("change_amount"))
                    });
                }
            }
        }

        if (cashierId == null) {
            jTextField1.setText("Showing all cashier transactions");
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Unable to load transaction history:\n"
                + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
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
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(java.awt.SystemColor.control);

        jPanel2.setBackground(new java.awt.Color(204, 0, 0));

        jButton1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton1.setText("Back");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CASHIER PERFORMANCE & TRANSACTION HISTORY ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel2))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel1.setText("Report Period");

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel3.setText("Start Date:");

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel4.setText("End Date:");

        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton2.setText("Generate Report");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jPanel3.setBackground(new java.awt.Color(204, 0, 0));

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("TOTAL CASHIERS");

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("0");

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("TOTAL TRANSACTION");

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("0");

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("COMBINED SALES");

        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel6))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 398, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel8)))
                .addGap(272, 272, 272)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(106, 106, 106))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(44, 44, 44))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Transaction ID", "Date & Time", "Payment Method", "Total", "Payment"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTextField1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel11.setText("TRANSACTION HISTORY ");

        jButton3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton3.setText("View All Transactions");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton4.setText("Export Report");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel12.setText("Selected Cashier");

        jTable2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cashier Name", "Transactions", "Total Sales", "Average Sales"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 232, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addContainerGap(232, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(317, 317, 317)
                                .addComponent(jLabel4)
                                .addGap(36, 36, 36)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(288, 288, 288)
                .addComponent(jButton3)
                .addGap(578, 578, 578)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 47, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(94, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!isAdmin()) {
        JOptionPane.showMessageDialog(
            this,
            "Administrator access is required."
        );
        dispose();
        return;
    }

    new SalesAndReports().setVisible(true);
    dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    if (getStartDate().isAfter(getEndDate())) {
        JOptionPane.showMessageDialog(
            this, "Please select a valid date range."
        );
        return;
    }

    jTable2.clearSelection();
    loadTransactions(null);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (!isAdmin()) {
        JOptionPane.showMessageDialog(
            this,
            "Administrator access is required."
        );
        return;
    }

    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Export Cashier Performance Report");
    chooser.setSelectedFile(
        new File("Cashier_Performance_Report.csv")
    );

    if (chooser.showSaveDialog(this)
            != JFileChooser.APPROVE_OPTION) {
        return;
    }

    File file = chooser.getSelectedFile();

    if (!file.getName().toLowerCase().endsWith(".csv")) {
        file = new File(file.getAbsolutePath() + ".csv");
    }

    if (file.exists()) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "This file already exists. Overwrite it?",
            "Confirm Export",
            JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
    }

    try (BufferedWriter writer = new BufferedWriter(
            new FileWriter(file))) {

        writer.write("CASHIER PERFORMANCE REPORT");
        writer.newLine();
        writer.write("Start Date," + csv(getStartDate().toString()));
        writer.newLine();
        writer.write("End Date," + csv(getEndDate().toString()));
        writer.newLine();
        writer.newLine();

        writer.write("Total Cashiers," + csv(jLabel6.getText()));
        writer.newLine();
        writer.write("Total Transactions," + csv(jLabel8.getText()));
        writer.newLine();
        writer.write("Combined Sales," + csv(jLabel10.getText()));
        writer.newLine();
        writer.newLine();

        writer.write("CASHIER PERFORMANCE");
        writer.newLine();

        DefaultTableModel cashierModel =
            (DefaultTableModel) jTable2.getModel();

        for (int col = 1;
                col < cashierModel.getColumnCount(); col++) {
            if (col > 1) {
                writer.write(",");
            }

            writer.write(csv(cashierModel.getColumnName(col)));
        }

        writer.newLine();

        for (int row = 0;
                row < cashierModel.getRowCount(); row++) {
            for (int col = 1;
                    col < cashierModel.getColumnCount(); col++) {
                if (col > 1) {
                    writer.write(",");
                }

                writer.write(csv(String.valueOf(
                    cashierModel.getValueAt(row, col)
                )));
            }

            writer.newLine();
        }

        writer.newLine();
        writer.write("TRANSACTION HISTORY");
        writer.newLine();

        DefaultTableModel transactionModel =
            (DefaultTableModel) jTable1.getModel();

        for (int col = 0;
                col < transactionModel.getColumnCount(); col++) {
            if (col > 0) {
                writer.write(",");
            }

            writer.write(csv(
                transactionModel.getColumnName(col)
            ));
        }

        writer.newLine();

        for (int row = 0;
                row < transactionModel.getRowCount(); row++) {
            for (int col = 0;
                    col < transactionModel.getColumnCount(); col++) {
                if (col > 0) {
                    writer.write(",");
                }

                writer.write(csv(String.valueOf(
                    transactionModel.getValueAt(row, col)
                )));
            }

            writer.newLine();
        }

        JOptionPane.showMessageDialog(
            this,
            "Report exported successfully:\n"
                + file.getAbsolutePath(),
            "Export Successful",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (IOException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Unable to export report:\n" + ex.getMessage(),
            "Export Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}



private String csv(String value) {
    if (value == null) {
        return "\"\"";
    }

    return "\"" + value.replace("\"", "\"\"") + "\"";
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        generateCashierReport();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new CashierPerformance().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}