package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CurrencyConverter extends JFrame {
    private JTextField inrField;
    private JComboBox<String> currencyBox;
    private JLabel resultLabel;

    public CurrencyConverter() {
        setTitle("Currency Converter");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Fonts & Colors
        Font titleFont = new Font("SansSerif", Font.BOLD, 22);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        Color primaryColor = new Color(33, 150, 243);
        Color accentColor = new Color(76, 175, 80);
        Color cardColor = new Color(255, 255, 255, 230); // semi-transparent white

        // Main panel with background image
        BackgroundPanel bgPanel = new BackgroundPanel("/resources/background.jpg");
        bgPanel.setLayout(new BorderLayout());

        // Header
        JLabel titleLabel = new JLabel("Currency Converter", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(primaryColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        bgPanel.add(titleLabel, BorderLayout.NORTH);

        // Center Panel (card overlay)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(cardColor); // visible over background
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        centerPanel.setMaximumSize(new Dimension(380, 300));

        JLabel inrLabel = new JLabel("Enter Amount in INR:");
        inrLabel.setFont(labelFont);
        inrField = new JTextField();
        inrField.setFont(labelFont);
        inrField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel currencyLabel = new JLabel("Select Currency:");
        currencyLabel.setFont(labelFont);
        currencyBox = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "AUD", "JPY"});
        currencyBox.setFont(labelFont);
        currencyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton convertBtn = new JButton("Convert");
        convertBtn.setFont(labelFont);
        convertBtn.setBackground(accentColor);
        convertBtn.setForeground(Color.WHITE);
        convertBtn.setFocusPainted(false);
        convertBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        convertBtn.setMaximumSize(new Dimension(150, 40));

        resultLabel = new JLabel("Converted Amount: ₹0.00", JLabel.CENTER);
        resultLabel.setFont(labelFont);
        resultLabel.setForeground(primaryColor);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Add components to card panel
        centerPanel.add(inrLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(inrField);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(currencyLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(currencyBox);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(convertBtn);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(resultLabel);

        // Wrap in container with flow layout to center the panel
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(centerPanel);

        bgPanel.add(wrapper, BorderLayout.CENTER);
        setContentPane(bgPanel);

        // Action listener
        convertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
    }

    private void convertCurrency() {
        try {
            double amountInINR = Double.parseDouble(inrField.getText().trim());
            String selectedCurrency = (String) currencyBox.getSelectedItem();
            double convertedAmount = 0;

            switch (selectedCurrency) {
                case "USD":
                    convertedAmount = amountInINR * 0.012;
                    break;
                case "EUR":
                    convertedAmount = amountInINR * 0.011;
                    break;
                case "GBP":
                    convertedAmount = amountInINR * 0.0098;
                    break;
                case "AUD":
                    convertedAmount = amountInINR * 0.018;
                    break;
                case "JPY":
                    convertedAmount = amountInINR * 1.75;
                    break;
            }

            resultLabel.setText(String.format("Converted Amount: %.2f %s", convertedAmount, selectedCurrency));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }
}
