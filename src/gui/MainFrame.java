package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private final Color primaryColor = new Color(33, 150, 243);  // Blue
    private final Color secondaryColor = new Color(25, 118, 210); // Darker blue
    private final Font buttonFont = new Font("Segoe UI Semibold", Font.BOLD, 16);
    private final Font titleFont = new Font("Segoe UI Black", Font.BOLD, 28);
    private final TableManager tableManager = new TableManager(10);

    public MainFrame() {
        setTitle("Restaurant Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background Panel with image
        BackgroundPanel bgPanel = new BackgroundPanel("/resources/background.jpg");
        bgPanel.setLayout(new BorderLayout(15, 15));
        bgPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(bgPanel);

        // Logo at Top with gradient background
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogo));
        logoLabel.setText("  Restaurant Management System");
        logoLabel.setFont(titleFont);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setOpaque(true);
        // Gradient background for header
        logoLabel.setBackground(new Color(33, 150, 243));
        logoLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, secondaryColor),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        logoLabel.setIconTextGap(15);
        bgPanel.add(logoLabel, BorderLayout.NORTH);

        // Buttons Panel with semi-transparent background & rounded corners
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 25, 25)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Semi-transparent white with blur effect
                g2.setColor(new Color(255, 255, 255, 210));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        String[] labels = {
                "New Order", "Menu Management", "Billing",
                "Currency Converter", "Calculator", "Inventory",
                "Table Management", "Exit"
        };

        String[] iconPaths = {
                "/resources/order.png", "/resources/menu.png", "/resources/bill.png",
                "/resources/currency.png", "/resources/calc.png", "/resources/inventory.png",
                "/resources/table.png", "/resources/exit.png"
        };

        JButton[] buttons = new JButton[labels.length];
        for (int i = 0; i < labels.length; i++) {
            buttons[i] = createStyledButton(labels[i], iconPaths[i]);
            buttonPanel.add(buttons[i]);
        }

        bgPanel.add(buttonPanel, BorderLayout.CENTER);

        // Action Listeners
        buttons[0].addActionListener(e -> new MenuOrderSystem().setVisible(true));
        buttons[1].addActionListener(e -> new ManageMenuSystem().setVisible(true));
        buttons[2].addActionListener(e -> {
            JFrame billingFrame = new JFrame("Billing System");
            billingFrame.setSize(550, 550);
            billingFrame.setLocationRelativeTo(null);
            billingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            billingFrame.add(new BillingPanel());
            billingFrame.setVisible(true);
        });
        buttons[3].addActionListener(e -> new CurrencyConverter().setVisible(true));
        buttons[4].addActionListener(e -> new Calculator().setVisible(true));
        buttons[6].addActionListener(e -> {
            JFrame tableFrame = new JFrame("Table Management");
            tableFrame.setSize(550, 450);
            tableFrame.setLocationRelativeTo(null);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.add(new TableManagementPanel(tableManager));
            tableFrame.setVisible(true);
        });
        buttons[5].addActionListener(e -> new InventoryManagementSystem().setVisible(true));
        buttons[7].addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Load icon with getResource to avoid path issues
        ImageIcon icon = loadIcon(iconPath, 48, 48);
        if (icon != null) {
            button.setIcon(icon);
        }

        // Rounded corners & shadow
        button.setBackground(new Color(255, 255, 255, 230));
        button.setForeground(primaryColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryColor, 2, true),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setOpaque(true);

        // Add drop shadow effect on hover and subtle gradient
        addHoverEffect(button);

        return button;
    }

    private void addHoverEffect(JButton button) {
        final Color originalBg = button.getBackground();
        final Color hoverBg = new Color(240, 248, 255, 250); // Light blueish white
        final Color originalBorder = primaryColor;
        final Color hoverBorder = secondaryColor;

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(hoverBorder, 3, true),
                        BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                button.setBackground(hoverBg);
                button.setForeground(secondaryColor);
                button.setFont(buttonFont.deriveFont(Font.BOLD, 17f));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                // Slight shadow effect
                button.setFocusPainted(false);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(originalBorder, 2, true),
                        BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                button.setBackground(originalBg);
                button.setForeground(primaryColor);
                button.setFont(buttonFont);
                button.repaint();
            }
        });
    }

    private ImageIcon loadIcon(String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } else {
                System.err.println("Icon not found: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
