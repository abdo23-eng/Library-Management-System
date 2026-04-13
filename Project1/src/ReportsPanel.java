import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;

public class ReportsPanel extends JPanel {

    private JLabel totalBooksLbl;
    private JLabel availableBooksLbl;
    private JLabel borrowedBooksLbl;
    private JLabel totalUsersLbl;

    private JTable borrowedTable;
    private DefaultTableModel borrowedModel;

    private JButton refreshBtn = new JButton("Refresh");

    public ReportsPanel() {
        setLayout(new BorderLayout());

        
        JLabel header = new JLabel("Reports Section", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(153, 153, 255));   
        header.setOpaque(true);
        add(header, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(153, 153, 255), 2),
                "Statistics", 0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(153, 153, 255)
        ));

        totalBooksLbl = new JLabel("Total Books: 0");
        totalBooksLbl.setForeground(new Color(153, 153, 255));
        availableBooksLbl = new JLabel("Available Books: 0");
         availableBooksLbl.setForeground(new Color(153, 153, 255));
        borrowedBooksLbl = new JLabel("Borrowed Books: 0");
        borrowedBooksLbl.setForeground(new Color(153, 153, 255));
        totalUsersLbl = new JLabel("Total Users: 0");
        totalUsersLbl.setForeground(new Color(153, 153, 255));
        Font statsFont = new Font("Segoe UI", Font.BOLD, 16);
        totalBooksLbl.setFont(statsFont);
        availableBooksLbl.setFont(statsFont);
        borrowedBooksLbl.setFont(statsFont);
        totalUsersLbl.setFont(statsFont);

        statsPanel.add(totalBooksLbl);
        statsPanel.add(availableBooksLbl);
        statsPanel.add(borrowedBooksLbl);
        statsPanel.add(totalUsersLbl);

        add(statsPanel, BorderLayout.WEST);

        borrowedModel = new DefaultTableModel(new String[]{"Title", "Author", "Borrowed By"}, 0);
        borrowedTable = new JTable(borrowedModel);
        styleTable(borrowedTable);
JTableHeader tableHeader = borrowedTable.getTableHeader();
tableHeader.setOpaque(true);
tableHeader.setBackground( Color.WHITE); 
tableHeader.setForeground(new Color(153,153,255));         
tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
borrowedTable.setBackground(new Color(153,153,255));
borrowedTable.setForeground(Color.white);
        JScrollPane borrowedScroll = new JScrollPane(borrowedTable);
        borrowedScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(153, 153, 255), 2),
                "Borrowed Books List", 0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(153, 153, 255)
        ));

        add(borrowedScroll, BorderLayout.CENTER);

        refreshBtn.setBackground(new Color(153, 153, 255));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refreshBtn.setFocusPainted(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadReports());

        loadReports();
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(153, 153, 255));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(102, 102, 204)); // درجة أغمق للأزرق
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        JTableHeader th = table.getTableHeader();
        th.setOpaque(true);
        th.setBackground(new Color(153, 153, 255));
        th.setForeground(Color.WHITE);
        th.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    private void loadReports() {
        int totalBooks = 0;
        int availableBooks = 0;
        int borrowedBooks = 0;
        int totalUsers = 0;

        borrowedModel.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader("books.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                totalBooks++;
                if ("Yes".equalsIgnoreCase(data[3])) {
                    availableBooks++;
                } else {
                    borrowedBooks++;
                    borrowedModel.addRow(new Object[]{data[0], data[1], "Unknown"});
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                totalUsers++;
                String[] data = line.split(",");
                int borrowedCount = Integer.parseInt(data[1]);
                if (borrowedCount > 0) {
                    for (int i = 0; i < borrowedModel.getRowCount(); i++) {
                        if (borrowedModel.getValueAt(i, 2).equals("Unknown")) {
                            borrowedModel.setValueAt(data[0], i, 2);
                            borrowedCount--;
                            if (borrowedCount == 0) break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        totalBooksLbl.setText("Total Books: " + totalBooks);
        availableBooksLbl.setText("Available Books: " + availableBooks);
        borrowedBooksLbl.setText("Borrowed Books: " + borrowedBooks);
        totalUsersLbl.setText("Total Users: " + totalUsers);
    }
}