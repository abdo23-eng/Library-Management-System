import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;

public class BorrowingPanel extends JPanel {

    private JTable booksTable;
    private JTable usersTable;
    private DefaultTableModel booksModel;
    private DefaultTableModel usersModel;
    private JButton borrowBtn = new JButton("Borrow");

    public BorrowingPanel() {
        setLayout(new BorderLayout());

       
        JLabel header = new JLabel("Borrowing Section", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(153,153,255));
        header.setOpaque(true);
        add(header, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(getBackground());

        
        String[] bookCols = {"Title", "Author", "Year", "Available"};
        booksModel = new DefaultTableModel(bookCols, 0);
        booksTable = new JTable(booksModel);
        styleTable(booksTable);
JTableHeader tableHeader1 = booksTable.getTableHeader();
tableHeader1.setOpaque(true);
tableHeader1.setBackground( Color.WHITE); 
tableHeader1.setForeground(new Color(153,153,255));         
tableHeader1.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
 booksTable.setBackground(new Color(153,153,255));
        JScrollPane booksScroll = new JScrollPane(booksTable);
        booksScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(153,153,255), 2),
                "Books",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(153,153,255)
        ));
        centerPanel.add(booksScroll);

       
        String[] userCols = {"Username", "Borrowed Books"};
        usersModel = new DefaultTableModel(userCols, 0);
        usersTable = new JTable(usersModel);
        styleTable(usersTable);
JTableHeader tableHeader2 = usersTable.getTableHeader();
tableHeader2.setOpaque(true);
tableHeader2.setBackground( Color.WHITE); 
tableHeader2.setForeground(new Color(153,153,255));         
tableHeader2.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        JScrollPane usersScroll = new JScrollPane(usersTable);
        usersTable .setBackground(new Color(153,153,255));

        usersScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(153,153,255), 2),
                "Users",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(153,153,255)
        ));
        centerPanel.add(usersScroll);

        add(centerPanel, BorderLayout.CENTER);

        
        borrowBtn.setBackground(new Color(153,153,255));
        borrowBtn.setForeground(Color.WHITE);
        borrowBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        borrowBtn.setFocusPainted(false);
        borrowBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.WHITE);

        bottomPanel.add(borrowBtn);
        add(bottomPanel, BorderLayout.SOUTH);

   
        loadBooksFromFile();
        loadUsersFromFile();

       
        borrowBtn.addActionListener(e -> borrowBook(
        ));
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(128, 0, 64));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(102, 0, 51));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        JTableHeader th = table.getTableHeader();
        th.setBackground(new Color(153, 0, 76));
        th.setForeground(Color.WHITE);
        th.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    private void loadBooksFromFile() {
        File file = new File("books.csv");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) booksModel.addRow(data);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadUsersFromFile() {
        File file = new File("users.csv");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) usersModel.addRow(data);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void borrowBook() {
        int bookRow = booksTable.getSelectedRow();
        int userRow = usersTable.getSelectedRow();

        if (bookRow == -1 || userRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a book and a user.");
            return;
        }

        String available = (String) booksModel.getValueAt(bookRow, 3);
        if ("No".equalsIgnoreCase(available)) {
            JOptionPane.showMessageDialog(this, "This book is not available.");
            return;
        }

       
        booksModel.setValueAt("No", bookRow, 3);

       
        String borrowedStr = (String) usersModel.getValueAt(userRow, 1);
        int borrowed = 0;
        try {
            borrowed = Integer.parseInt(borrowedStr);
        } catch (NumberFormatException ex) {
            borrowed = 0;
        }
        borrowed++;
        usersModel.setValueAt(String.valueOf(borrowed), userRow, 1);

        
        saveAllBooksToFile();
        saveAllUsersToFile();

        JOptionPane.showMessageDialog(this, "Book borrowed successfully.");
       
    }

    private void saveAllBooksToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("books.csv"))) {
            for (int i = 0; i < booksModel.getRowCount(); i++) {
                pw.println(
                        booksModel.getValueAt(i, 0) + "," +
                        booksModel.getValueAt(i, 1) + "," +
                        booksModel.getValueAt(i, 2) + "," +
                        booksModel.getValueAt(i, 3)
                );
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveAllUsersToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("users.csv"))) {
            for (int i = 0; i < usersModel.getRowCount(); i++) {
                pw.println(
                        usersModel.getValueAt(i, 0) + "," +
                        usersModel.getValueAt(i, 1)
                );
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}