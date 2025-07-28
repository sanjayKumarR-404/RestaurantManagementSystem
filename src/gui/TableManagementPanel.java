package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class TableManagementPanel extends JPanel {
    private TableManager tableManager;
    private JPanel tableListPanel;
    private JTextField searchField;
    private JPanel transparentWrapper;

    public TableManagementPanel(TableManager tableManager) {
        this.tableManager = tableManager;
        setLayout(new BorderLayout());

        setOpaque(false);

        Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color primaryColor = new Color(33, 150, 243);
        Color bgWhite = new Color(255, 255, 255);
        Color occupiedColor = new Color(244, 67, 54);
        Color availableColor = new Color(76, 175, 80);

        BackgroundPanel background = new BackgroundPanel("/resources/background.jpg");
        background.setLayout(new GridBagLayout());

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(900, 600));
        cardPanel.setBackground(bgWhite);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel heading = new JLabel("Table Management", JLabel.CENTER);
        heading.setFont(titleFont);
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(primaryColor);
        heading.setPreferredSize(new Dimension(100, 60));
        cardPanel.add(heading, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search Table:");
        searchLabel.setFont(labelFont);
        searchField = new JTextField(20);
        searchField.setFont(labelFont);
        searchField.addActionListener(e -> refreshTableList());
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        tableListPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        tableListPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(tableListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        cardPanel.add(contentPanel, BorderLayout.CENTER);
        background.add(cardPanel);

        setLayout(new BorderLayout());
        add(background, BorderLayout.CENTER);

        refreshTableList();
    }

    private void refreshTableList() {
        tableListPanel.removeAll();

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color occupiedColor = new Color(244, 67, 54);
        Color availableColor = new Color(76, 175, 80);

        String keyword = searchField.getText().trim().toLowerCase();
        List<Table> filtered = tableManager.getTables().stream()
                .filter(t -> keyword.isEmpty() || ("table " + t.getTableNumber()).contains(keyword))
                .collect(Collectors.toList());

        for (Table table : filtered) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panel.setBackground(Color.WHITE);
            panel.setPreferredSize(new Dimension(250, 60));

            JLabel label = new JLabel(table.toString());
            label.setFont(labelFont);
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton toggleButton = new JButton(table.isOccupied() ? "Free" : "Occupy");
            toggleButton.setFont(labelFont);
            toggleButton.setFocusPainted(false);
            toggleButton.setBackground(table.isOccupied() ? occupiedColor : availableColor);
            toggleButton.setForeground(Color.WHITE);
            toggleButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            toggleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    table.setOccupied(!table.isOccupied());
                    refreshTableList();
                }
            });

            panel.add(label, BorderLayout.CENTER);
            panel.add(toggleButton, BorderLayout.EAST);

            tableListPanel.add(panel);
        }

        tableListPanel.revalidate();
        tableListPanel.repaint();
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
}