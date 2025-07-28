package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import db.OrderDAO;
import java.awt.*;
import java.util.List;

public class ManageMenuSystem extends JFrame {
    private DefaultTableModel menuModel;

    public ManageMenuSystem() {
        setTitle("Manage Menu");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fonts & Colors
        Font titleFont = new Font("SansSerif", Font.BOLD, 24);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        Color primaryColor = new Color(33, 150, 243);
        Color bgColor = new Color(250, 250, 250);
        Color hoverColor = new Color(25, 118, 210);
        Color deleteColor = new Color(244, 67, 54);
        Color deleteHover = new Color(211, 47, 47);

        // Logo Label
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        logoLabel.setIcon(new ImageIcon(logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        logoLabel.setText(" Manage Menu");
        logoLabel.setFont(titleFont);
        logoLabel.setIconTextGap(10);
        logoLabel.setOpaque(true);
        logoLabel.setBackground(primaryColor);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Menu Table
        menuModel = new DefaultTableModel(new String[]{"Item Name", "Price"}, 0);
        JTable menuTable = new JTable(menuModel);
        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuTable.setRowHeight(28);
        menuTable.setFont(labelFont);
        menuTable.getTableHeader().setFont(labelFont);
        menuTable.getTableHeader().setBackground(primaryColor);
        menuTable.getTableHeader().setForeground(Color.WHITE);
        menuTable.setSelectionBackground(new Color(200, 230, 201));

        // Enable visible grid lines
        menuTable.setShowHorizontalLines(true);
        menuTable.setShowVerticalLines(true);
        menuTable.setGridColor(Color.LIGHT_GRAY);
        menuTable.setIntercellSpacing(new Dimension(1, 1));

        // Center align price column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        menuTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        menuTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        // Load menu items
        List<MenuItem> menuItems = OrderDAO.getAllMenuItems();
        for (MenuItem item : menuItems) {
            menuModel.addRow(new Object[]{item.getName(), item.getPrice()});
        }

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        inputPanel.setBackground(bgColor);

        JTextField itemNameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");

        itemNameField.setFont(labelFont);
        priceField.setFont(labelFont);
        itemNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        priceField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        addBtn.setFont(labelFont);
        deleteBtn.setFont(labelFont);
        styleButton(addBtn, primaryColor, hoverColor);
        styleButton(deleteBtn, deleteColor, deleteHover);

        inputPanel.add(new JLabel("Item Name:", JLabel.RIGHT));
        inputPanel.add(itemNameField);
        inputPanel.add(new JLabel("Price:", JLabel.RIGHT));
        inputPanel.add(priceField);
        inputPanel.add(addBtn);
        inputPanel.add(deleteBtn);

        // Button Actions
        addBtn.addActionListener(e -> {
            String name = itemNameField.getText().trim();
            String priceText = priceField.getText().trim();
            if (name.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both item name and price.");
                return;
            }
            try {
                double price = Double.parseDouble(priceText);
                MenuItem newItem = new MenuItem(name, price);
                OrderDAO.addMenuItem(newItem);
                menuModel.addRow(new Object[]{newItem.getName(), newItem.getPrice()});
                itemNameField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format. Enter numbers only.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = menuTable.getSelectedRow();
            if (selectedRow != -1) {
                String name = (String) menuModel.getValueAt(selectedRow, 0);
                OrderDAO.deleteMenuItem(name);
                menuModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Select an item to delete.");
            }
        });

        // Background Panel with Image
        BackgroundPanel bgPanel = new BackgroundPanel("/resources/background.jpg");
        bgPanel.setLayout(new BorderLayout(10, 10));
        bgPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components
        bgPanel.add(logoLabel, BorderLayout.NORTH);
        bgPanel.add(menuScroll, BorderLayout.CENTER);
        bgPanel.add(inputPanel, BorderLayout.SOUTH);

        setContentPane(bgPanel);
    }

    // Button style helper
    private void styleButton(JButton button, Color baseColor, Color hoverColor) {
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }
}
