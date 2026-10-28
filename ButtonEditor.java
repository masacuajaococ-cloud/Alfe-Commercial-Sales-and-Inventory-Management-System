import java.awt.*;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private final JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 2, 0)
    );

    private final JButton viewButton = new JButton("View");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");

    private JTable table;
    private int row;

    public ButtonEditor() {

        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);

        // VIEW
        viewButton.addActionListener(e -> {

    int modelRow = table.convertRowIndexToModel(row);

    String productId = String.valueOf(
            table.getValueAt(modelRow, 0)
    );

    String productName = String.valueOf(
            table.getValueAt(modelRow, 1)
    );

    String category = String.valueOf(
            table.getValueAt(modelRow, 2)
    );

    String price = String.valueOf(
            table.getValueAt(modelRow, 3)
    );

    String stock = String.valueOf(
            table.getValueAt(modelRow, 4)
    );

    String status = String.valueOf(
            table.getValueAt(modelRow, 5)
    );

    JOptionPane.showMessageDialog(
            table,
            "Product ID: " + productId +
            "\nProduct Name: " + productName +
            "\nCategory: " + category +
            "\nPrice: ₱" + price +
            "\nStock: " + stock +
            "\nStatus: " + status,
            "View Product",
            JOptionPane.INFORMATION_MESSAGE
    );

    fireEditingStopped();
});

        // EDIT
        editButton.addActionListener(e -> {

    int modelRow = table.convertRowIndexToModel(row);

    String productId = String.valueOf(
            table.getValueAt(modelRow, 0)
    );

    String productName = String.valueOf(
            table.getValueAt(modelRow, 1)
    );

    String category = String.valueOf(
            table.getValueAt(modelRow, 2)
    );

    String price = String.valueOf(
            table.getValueAt(modelRow, 3)
    );

    String stock = String.valueOf(
            table.getValueAt(modelRow, 4)
    );

    String status = String.valueOf(
            table.getValueAt(modelRow, 5)
    );

    String newName = JOptionPane.showInputDialog(
            table,
            "Product Name:",
            productName
    );

    if (newName == null || newName.trim().isEmpty()) {
        return;
    }

    String newPrice = JOptionPane.showInputDialog(
            table,
            "Price:",
            price
    );

    if (newPrice == null || newPrice.trim().isEmpty()) {
        return;
    }

    String newStock = JOptionPane.showInputDialog(
            table,
            "Stock:",
            stock
    );

    if (newStock == null || newStock.trim().isEmpty()) {
        return;
    }

    try {

        double priceValue = Double.parseDouble(newPrice);
        int stockValue = Integer.parseInt(newStock);

        if (priceValue < 0 || stockValue < 0) {
            throw new NumberFormatException();
        }

        String newStatus =
                stockValue > 0 ? "In Stock" : "Out of Stock";

        String sql =
                "UPDATE products " +
                "SET name = ?, price = ?, stock = ?, status = ? " +
                "WHERE product_id = ?";

        DatabaseConnection db = new DatabaseConnection();

        try (
            java.sql.Connection conn = db.connect();
            java.sql.PreparedStatement pst =
                    conn.prepareStatement(sql)
        ) {

            pst.setString(1, newName.trim());
            pst.setDouble(2, priceValue);
            pst.setInt(3, stockValue);
            pst.setString(4, newStatus);
            pst.setString(5, productId);

            pst.executeUpdate();

            table.setValueAt(newName.trim(), modelRow, 1);
            table.setValueAt(priceValue, modelRow, 3);
            table.setValueAt(stockValue, modelRow, 4);
            table.setValueAt(newStatus, modelRow, 5);

            JOptionPane.showMessageDialog(
                    table,
                    "Product updated successfully!",
                    "Edit Product",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                table,
                "Please enter valid numbers for price and stock.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
        );

    } catch (java.sql.SQLException ex) {

        JOptionPane.showMessageDialog(
                table,
                "Failed to update product:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    fireEditingStopped();
    });

        // DELETE
        deleteButton.addActionListener(e -> {

    int modelRow = table.convertRowIndexToModel(row);

    String productId = String.valueOf(
            table.getValueAt(modelRow, 0)
    );

    String productName = String.valueOf(
            table.getValueAt(modelRow, 1)
    );

    int confirm = JOptionPane.showConfirmDialog(
            table,
            "Are you sure you want to delete:\n\n"
            + productName + " (" + productId + ")?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    );

    if (confirm != JOptionPane.YES_OPTION) {
        fireEditingStopped();
        return;
    }

    String sql =
            "DELETE FROM products WHERE product_id = ?";

    DatabaseConnection db = new DatabaseConnection();

    try (
        java.sql.Connection conn = db.connect();
        java.sql.PreparedStatement pst =
                conn.prepareStatement(sql)
    ) {

        pst.setString(1, productId);

        int rowsDeleted = pst.executeUpdate();

        if (rowsDeleted > 0) {

            javax.swing.table.DefaultTableModel model =
                    (javax.swing.table.DefaultTableModel)
                    table.getModel();

            model.removeRow(modelRow);

            JOptionPane.showMessageDialog(
                    table,
                    "Product deleted successfully!",
                    "Delete Product",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    table,
                    "Product was not found in the database.",
                    "Delete Product",
                    JOptionPane.WARNING_MESSAGE
            );
        }

    } catch (java.sql.SQLException ex) {

        JOptionPane.showMessageDialog(
                table,
                "Failed to delete product:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    fireEditingStopped();
    });
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        this.table = table;
        this.row = row;

        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
}