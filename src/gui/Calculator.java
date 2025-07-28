package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Calculator extends JFrame {
    private JTextField display;

    public Calculator() {
        setTitle("Calculator");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background image label with scaled image
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/resources/background.jpg")); // Use getResource to load from JAR or src/resources
        JLabel background = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundIcon != null) {
                    Image img = backgroundIcon.getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        background.setLayout(new GridBagLayout()); // Use GridBagLayout for center alignment with insets
        setContentPane(background);

        // Panel that holds the calculator UI with transparency
        JPanel calcPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Semi-transparent white background with rounded corners
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 220)); // 220 alpha for transparency
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        calcPanel.setOpaque(false);
        calcPanel.setPreferredSize(new Dimension(350, 500));
        calcPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Fonts and colors
        Font displayFont = new Font("Segoe UI", Font.BOLD, 28);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 20);
        Color primaryColor = new Color(33, 150, 243);
        Color accentColor = new Color(76, 175, 80);
        Color buttonTextColor = Color.WHITE;

        // Header label
        JLabel header = new JLabel("Calculator", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setOpaque(false);
        header.setForeground(primaryColor);

        // Display field
        display = new JTextField();
        display.setFont(displayFont);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(new Color(245, 245, 245));
        display.setForeground(Color.BLACK);
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Buttons panel with grid and spacing
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 12, 12));
        buttonPanel.setOpaque(false);

        // Buttons array including clear "C" at last row properly aligned
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "", "", ""
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            if (text.isEmpty()) {
                button.setEnabled(false);
                button.setVisible(false);
                buttonPanel.add(button);
                continue;
            }

            button.setFont(buttonFont);
            button.setFocusPainted(false);
            if (text.equals("=")) {
                button.setBackground(accentColor);
            } else if (text.equals("C")) {
                button.setBackground(new Color(244, 67, 54)); // red color for clear button
            } else {
                button.setBackground(primaryColor);
            }
            button.setForeground(buttonTextColor);
            button.setBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 2));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setOpaque(true);

            // Rounded corners for buttons
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(button.getBackground());
                    g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                    g2.setColor(button.getForeground());
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (c.getWidth() - fm.stringWidth(button.getText())) / 2;
                    int y = (c.getHeight() + fm.getAscent()) / 2 - 2;
                    g2.drawString(button.getText(), x, y);
                    g2.dispose();
                }
            });

            button.addActionListener(this::onButtonClick);
            buttonPanel.add(button);
        }

        // Add components to calcPanel
        calcPanel.add(header, BorderLayout.NORTH);
        calcPanel.add(display, BorderLayout.CENTER);
        calcPanel.add(buttonPanel, BorderLayout.SOUTH);

        background.add(calcPanel, new GridBagConstraints());
    }

    private void onButtonClick(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();

        if (command.equals("C")) {
            display.setText("");
        } else if (command.equals("=")) {
            try {
                double result = evaluateExpression(display.getText());
                display.setText(String.valueOf(result));
            } catch (Exception ex) {
                display.setText("Error");
            }
        } else {
            display.setText(display.getText() + command);
        }
    }

    // Simple infix expression evaluator (no ScriptEngine)
    private double evaluateExpression(String expr) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;

                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }
}
