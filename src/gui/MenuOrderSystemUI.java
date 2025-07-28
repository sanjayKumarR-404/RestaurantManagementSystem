package gui;

import javax.swing.*;
import java.awt.*;

public class MenuOrderSystemUI extends JFrame {
    public MenuOrderSystemUI() {
        setTitle("Menu & Order System");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Background panel with image
        setContentPane(new BackgroundPanel("/resources/background.jpg"));
        setLayout(new GridBagLayout()); // to center the content panel

        // Center content panel (opaque white card)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setOpaque(true);
        contentPanel.setPreferredSize(new Dimension(900, 600));
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        contentPanel.setLayout(new BorderLayout());

        // Your UI components go here...
        JLabel title = new JLabel("Menu & Order System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBackground(new Color(33, 150, 243));
        title.setOpaque(true);
        title.setPreferredSize(new Dimension(100, 60));
        contentPanel.add(title, BorderLayout.NORTH);

        // Sample center area
        JPanel centerArea = new JPanel();
        centerArea.setOpaque(false);
        centerArea.setLayout(new GridLayout(1, 2, 20, 0)); // 2 tables
        centerArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTable menuTable = new JTable(new Object[][]{
                {"Pizza", 250}, {"Burger", 150}
        }, new String[]{"Item", "Price"});
        JTable orderTable = new JTable(new Object[][]{}, new String[]{"Item", "Qty", "Price"});

        centerArea.add(new JScrollPane(menuTable));
        centerArea.add(new JScrollPane(orderTable));

        contentPanel.add(centerArea, BorderLayout.CENTER);

        // Footer
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(new JButton("Add to Order"));
        bottomPanel.add(new JButton("Place Order"));
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main content panel to background
        add(contentPanel);
    }

    // BackgroundPanel class
    static class BackgroundPanel extends JPanel {
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
        SwingUtilities.invokeLater(() -> new MenuOrderSystemUI().setVisible(true));
    }
}
