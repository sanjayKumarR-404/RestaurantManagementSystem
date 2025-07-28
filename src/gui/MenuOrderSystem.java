package gui;

import db.OrderDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuOrderSystem extends JFrame {
    private JTable menuTable;
    private JTable orderTable;
    private DefaultTableModel menuModel, orderModel;
    private JLabel totalLabel;
    private double total = 0;
    private TableManager tableManager;
    private JComboBox<String> tableComboBox;

    public MenuOrderSystem() {
        tableManager = new TableManager(10);

        setTitle("Menu & Order System");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fonts and colors
        Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font buttonFont = new Font("Segoe UI Semibold", Font.PLAIN, 15);

        Color primaryColor = new Color(33, 150, 243); // Blue
        Color bgColor = new Color(250, 250, 250);

        // Logo and Title
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        logoLabel.setIcon(new ImageIcon(logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        logoLabel.setText(" Menu & Order System");
        logoLabel.setFont(titleFont);
        logoLabel.setIconTextGap(10);
        logoLabel.setOpaque(true);
        logoLabel.setBackground(primaryColor);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Menu Table
        menuModel = new DefaultTableModel(new String[]{"Item", "Price (INR)"}, 0);
        loadMenuItems();
        menuTable = new JTable(menuModel);
        menuTable.setFont(fieldFont);
        menuTable.setRowHeight(25);
        menuTable.getTableHeader().setFont(labelFont);
        menuTable.getTableHeader().setBackground(primaryColor);
        menuTable.getTableHeader().setForeground(Color.WHITE);
        menuTable.setSelectionBackground(new Color(200, 230, 201));
        menuTable.setShowHorizontalLines(true);
        menuTable.setShowVerticalLines(true);
        menuTable.setGridColor(Color.LIGHT_GRAY);
        menuTable.setIntercellSpacing(new Dimension(1, 1));
        menuTable.setFillsViewportHeight(true);

        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Center-align price
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        menuTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        menuTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        // Order Table
        orderModel = new DefaultTableModel(new String[]{"Item", "Qty", "Price"}, 0);
        orderTable = new JTable(orderModel);
        orderTable.setFont(fieldFont);
        orderTable.setRowHeight(25);
        orderTable.getTableHeader().setFont(labelFont);
        orderTable.getTableHeader().setBackground(primaryColor);
        orderTable.getTableHeader().setForeground(Color.WHITE);
        orderTable.setSelectionBackground(new Color(200, 230, 201));
        orderTable.setShowHorizontalLines(true);
        orderTable.setShowVerticalLines(true);
        orderTable.setGridColor(Color.LIGHT_GRAY);
        orderTable.setIntercellSpacing(new Dimension(1, 1));
        orderTable.setFillsViewportHeight(true);

        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Bottom Panel
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        bottomPanel.setBackground(bgColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField qtyField = new JTextField();
        qtyField.setFont(fieldFont);
        qtyField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton addBtn = new JButton("Add to Order");
        JButton placeOrderBtn = new JButton("Place Order");

        addBtn.setFont(buttonFont);
        placeOrderBtn.setFont(buttonFont);
        addBtn.setBackground(primaryColor);
        addBtn.setForeground(Color.WHITE);
        placeOrderBtn.setBackground(new Color(76, 175, 80));
        placeOrderBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        placeOrderBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        placeOrderBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addHoverEffect(addBtn, new Color(30, 136, 229));
        addHoverEffect(placeOrderBtn, new Color(56, 142, 60));

        totalLabel = new JLabel("Total: ₹0.00", SwingConstants.CENTER);
        totalLabel.setFont(labelFont);
        totalLabel.setOpaque(true);
        totalLabel.setBackground(new Color(200, 230, 201));
        totalLabel.setForeground(Color.DARK_GRAY);
        totalLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        bottomPanel.add(new JLabel("Select Table:", SwingConstants.RIGHT));
        tableComboBox = new JComboBox<>();
        for (Table table : tableManager.getTables()) {
            tableComboBox.addItem(table.toString());
        }
        tableComboBox.setFont(fieldFont);
        tableComboBox.setBackground(Color.WHITE);
        tableComboBox.setFocusable(false);

        bottomPanel.add(tableComboBox);
        bottomPanel.add(new JLabel("Quantity:", SwingConstants.RIGHT));
        bottomPanel.add(qtyField);
        bottomPanel.add(placeOrderBtn);  // <- Swapped position
        bottomPanel.add(addBtn);         // <- Swapped position
        bottomPanel.add(new JLabel());   // Empty spacer
        bottomPanel.add(totalLabel);


        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablePanel.setOpaque(false);
        tablePanel.add(menuScroll);
        tablePanel.add(orderScroll);

        BackgroundPanel bgPanel = new BackgroundPanel("/resources/background.jpg");
        bgPanel.setLayout(new BorderLayout(10, 10));
        bgPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bgPanel.add(logoLabel, BorderLayout.NORTH);
        bgPanel.add(tablePanel, BorderLayout.CENTER);
        bgPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(bgPanel);

        addBtn.addActionListener(e -> addToOrder(qtyField));
        placeOrderBtn.addActionListener(e -> openBillingPanel());
    }

    private void addToOrder(JTextField qtyField) {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) return;

        String item = (String) menuModel.getValueAt(selectedRow, 0);
        double price = (double) menuModel.getValueAt(selectedRow, 1);
        try {
            int qty = Integer.parseInt(qtyField.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
            double itemTotal = price * qty;
            orderModel.addRow(new Object[]{item, qty, itemTotal});
            total += itemTotal;
            totalLabel.setText("Total: ₹" + String.format("%.2f", total));
            qtyField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive quantity.");
        }
    }

    private void openBillingPanel() {
        int selectedIndex = tableComboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a table.");
            return;
        }

        Table selectedTable = tableManager.getTables().get(selectedIndex);
        if (selectedTable.isOccupied()) {
            JOptionPane.showMessageDialog(this, "This table is already occupied!");
            return;
        }

        selectedTable.setOccupied(true);
        tableComboBox.removeItemAt(selectedIndex);
        tableComboBox.insertItemAt(selectedTable.toString(), selectedIndex);
        tableComboBox.setSelectedIndex(selectedIndex);

        List<OrderItem> orderedItems = new ArrayList<>();
        for (int i = 0; i < orderModel.getRowCount(); i++) {
            String itemName = (String) orderModel.getValueAt(i, 0);
            int qty = (int) orderModel.getValueAt(i, 1);
            double total = (double) orderModel.getValueAt(i, 2);
            orderedItems.add(new OrderItem(itemName, qty, total));
        }

        JFrame billingFrame = new JFrame("Billing - Table " + selectedTable.getTableNumber());
        billingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        billingFrame.setSize(500, 500);
        billingFrame.setLocationRelativeTo(null);
        billingFrame.add(new BillingPanel(orderedItems));
        billingFrame.setVisible(true);

        billingFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                selectedTable.setOccupied(false);
                tableComboBox.removeItemAt(selectedIndex);
                tableComboBox.insertItemAt(selectedTable.toString(), selectedIndex);
            }
        });

        OrderDAO.clearOrdersTable();
        orderModel.setRowCount(0);
        total = 0;
        totalLabel.setText("Total: ₹0.00");
    }

    private void loadMenuItems() {
        menuModel.setRowCount(0);
        List<MenuItem> items = OrderDAO.getAllMenuItems();
        for (MenuItem item : items) {
            menuModel.addRow(new Object[]{item.getName(), item.getPrice()});
        }
    }

    private void addHoverEffect(JButton button, Color hoverColor) {
        Color original = button.getBackground();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(original);
            }
        });
    }
}
