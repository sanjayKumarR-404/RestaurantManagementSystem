package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class InventoryManagementSystem extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField itemNameField, quantityField, thresholdField;
    private JButton addBtn, updateBtn, deleteBtn;

    public InventoryManagementSystem() {
        setTitle("Inventory Management");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set custom background
        BackgroundPanel background = new BackgroundPanel("/resources/background.jpg");
        setContentPane(background);
        background.setLayout(new GridBagLayout());

        Font font = new Font("Segoe UI", Font.PLAIN, 16);
        Color btnColor = new Color(33, 150, 243);
        Color textColor = Color.WHITE;

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(900, 600));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel heading = new JLabel("Inventory Management", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(33, 150, 243));
        heading.setPreferredSize(new Dimension(100, 60));
        cardPanel.add(heading, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Item", "Quantity", "Threshold"}, 0);
        table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                int quantity = Integer.parseInt(getValueAt(row, 1).toString());
                int threshold = Integer.parseInt(getValueAt(row, 2).toString());
                if (quantity <= threshold) {
                    c.setBackground(new Color(255, 204, 204));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        table.setFont(font);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(200, 230, 255));
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        cardPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        itemNameField = new JTextField(10);
        quantityField = new JTextField(5);
        thresholdField = new JTextField(5);

        itemNameField.setFont(font);
        quantityField.setFont(font);
        thresholdField.setFont(font);

        addBtn = createButton("Add", btnColor, textColor);
        updateBtn = createButton("Update", btnColor, textColor);
        deleteBtn = createButton("Delete", new Color(220, 53, 69), textColor);

        // Row 1: Item Label + Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(createLabel("Item:", font), gbc);
        gbc.gridx = 1;
        form.add(itemNameField, gbc);

        // Row 1: Qty Label + Field
        gbc.gridx = 2;
        form.add(createLabel("Qty:", font), gbc);
        gbc.gridx = 3;
        form.add(quantityField, gbc);

        // Row 2: Update button (below Qty field)
        gbc.gridx = 3;
        gbc.gridy = 1;
        form.add(updateBtn, gbc);

        // Row 1: Threshold Label + Field
        gbc.gridx = 4;
        gbc.gridy = 0;
        form.add(createLabel("Threshold:", font), gbc);
        gbc.gridx = 5;
        form.add(thresholdField, gbc);

        // Row 1: Add + Delete button
        gbc.gridx = 6;
        form.add(addBtn, gbc);
        gbc.gridx = 7;
        form.add(deleteBtn, gbc);

        cardPanel.add(form, BorderLayout.SOUTH);
        background.add(cardPanel);

        // Event listeners
        loadInventory();
        addBtn.addActionListener(e -> addItem());
        updateBtn.addActionListener(e -> updateQuantity());
        deleteBtn.addActionListener(e -> deleteItem());
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadInventory() {
        model.setRowCount(0);
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "Sa20082005!");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT item_name, quantity, threshold FROM inventory")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getInt("threshold")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading inventory.");
        }
    }

    private void addItem() {
        String name = itemNameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item name cannot be empty.");
            return;
        }

        try {
            int qty = Integer.parseInt(quantityField.getText());
            int threshold = Integer.parseInt(thresholdField.getText());

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "Sa20082005!");
                 PreparedStatement ps = con.prepareStatement("INSERT INTO inventory (item_name, quantity, threshold) VALUES (?, ?, ?)")) {

                ps.setString(1, name);
                ps.setInt(2, qty);
                ps.setInt(3, threshold);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Item added!");
                loadInventory();
                clearFields();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity and Threshold must be numbers.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding item.");
        }
    }

    private void updateQuantity() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to update.");
            return;
        }

        String itemName = (String) model.getValueAt(row, 0);

        try {
            int qty = Integer.parseInt(quantityField.getText());

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "Sa20082005!");
                 PreparedStatement ps = con.prepareStatement("UPDATE inventory SET quantity = ? WHERE item_name = ?")) {

                ps.setInt(1, qty);
                ps.setString(2, itemName);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Quantity updated!");
                loadInventory();
                clearFields();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating quantity.");
        }
    }

    private void deleteItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to delete.");
            return;
        }

        String itemName = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected item?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "Sa20082005!");
                 PreparedStatement ps = con.prepareStatement("DELETE FROM inventory WHERE item_name = ?")) {

                ps.setString(1, itemName);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Item deleted!");
                loadInventory();
                clearFields();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting item.");
            }
        }
    }

    private void clearFields() {
        itemNameField.setText("");
        quantityField.setText("");
        thresholdField.setText("");
    }

    class BackgroundPanel extends JPanel {
        private Image bgImage;

        public BackgroundPanel(String path) {
            try {
                bgImage = new ImageIcon(getClass().getResource(path)).getImage();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Background image not found.");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryManagementSystem().setVisible(true));
    }
}
