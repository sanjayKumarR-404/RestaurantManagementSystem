package gui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BillingPanel extends JPanel {
    private JTextArea billArea;
    private JLabel totalLabel;
    private JButton printButton;
    private List<OrderItem> orderedItems;
    private double subtotal = 0;
    private final double TAX_RATE = 0.18; // 18% GST
    private double taxAmount = 0;
    private double grandTotal = 0;

    public BillingPanel() {
        this(new ArrayList<>());
    }

    public BillingPanel(List<OrderItem> orderedItems) {
        this.orderedItems = orderedItems;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel headerLabel = new JLabel("Billing Summary");
        headerLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24));
        headerLabel.setForeground(new Color(44, 62, 80));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        billArea = new JTextArea(15, 30);
        billArea.setEditable(false);
        billArea.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
        billArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        billArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(billArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        totalLabel.setForeground(new Color(39, 174, 96));

        printButton = new JButton("Print Receipt (PDF)");
        printButton.setBackground(new Color(41, 128, 185));
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        printButton.setPreferredSize(new Dimension(200, 35));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(totalLabel);
        bottomPanel.add(printButton);

        add(headerLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadBillDetails();

        printButton.addActionListener(e -> generatePDFReceipt());
    }

    private void loadBillDetails() {
        StringBuilder billText = new StringBuilder();
        for (OrderItem item : orderedItems) {
            billText.append(String.format("%-20s x%-3d  ₹%.2f%n", item.getName(), item.getQuantity(), item.getTotal()));
            subtotal += item.getTotal();
        }

        taxAmount = subtotal * TAX_RATE;
        grandTotal = subtotal + taxAmount;

        billText.append("\n-------------------------------\n");
        billText.append(String.format("Subtotal:%27s₹%.2f%n", "", subtotal));
        billText.append(String.format("GST (18%%):%25s₹%.2f%n", "", taxAmount));
        billText.append(String.format("Total Amount:%22s₹%.2f%n", "", grandTotal));

        billArea.setText(billText.toString());
        totalLabel.setText("Total: ₹" + String.format("%.2f", grandTotal));
    }

    private void generatePDFReceipt() {
        try {
            Document document = new Document(PageSize.A5, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream("Bill_Receipt.pdf"));
            document.open();

            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            com.itextpdf.text.Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY);
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            com.itextpdf.text.Font itemFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            com.itextpdf.text.Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            com.itextpdf.text.Font thankYouFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 11, BaseColor.DARK_GRAY);

            Paragraph restaurantName = new Paragraph("MALBOUFFE AU CAFÉ", titleFont);
            restaurantName.setAlignment(Element.ALIGN_CENTER);
            document.add(restaurantName);

            Paragraph sub = new Paragraph("Fine Dining & Culinary Excellence\n", subTitleFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            document.add(sub);

            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            Paragraph address = new Paragraph("Church Street, Bangalore - 400001\nPhone: +91 95358 70525 \n", itemFont);
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            Paragraph meta = new Paragraph("Invoice #: #" + (int) (Math.random() * 1000000) +
                    "\nDate: " + java.time.LocalDate.now() +
                    "\nTime: " + java.time.LocalTime.now().withNano(0) + "\n\n", itemFont);
            meta.setAlignment(Element.ALIGN_RIGHT);
            document.add(meta);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2});

            table.addCell(getStyledCell("Item", headerFont));
            table.addCell(getStyledCell("Qty", headerFont));
            table.addCell(getStyledCell("Amount", headerFont));

            for (OrderItem item : orderedItems) {
                table.addCell(getStyledCell(item.getName(), itemFont));
                table.addCell(getStyledCell(String.valueOf(item.getQuantity()), itemFont));
                table.addCell(getStyledCell("₹" + String.format("%.2f", item.getTotal()), itemFont));
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            Paragraph subT = new Paragraph("Subtotal: ₹" + String.format("%.2f", subtotal), itemFont);
            subT.setAlignment(Element.ALIGN_RIGHT);
            document.add(subT);

            Paragraph taxT = new Paragraph("GST (18%): ₹" + String.format("%.2f", taxAmount), itemFont);
            taxT.setAlignment(Element.ALIGN_RIGHT);
            document.add(taxT);

            Paragraph total = new Paragraph("Total: ₹" + String.format("%.2f", grandTotal), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            Paragraph thankYou = new Paragraph("\nThank you for dining with us!\nWe look forward to serving you again! 🍽", thankYouFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();
            JOptionPane.showMessageDialog(this, "Receipt saved as PDF!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to generate receipt.");
        }
    }

    private PdfPCell getStyledCell(String text, com.itextpdf.text.Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(6);
        return cell;
    }
}

// OrderItem class for item details
class OrderItem {
    private String name;
    private int quantity;
    private double total;

    public OrderItem(String name, int quantity, double total) {
        this.name = name;
        this.quantity = quantity;
        this.total = total;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
}
