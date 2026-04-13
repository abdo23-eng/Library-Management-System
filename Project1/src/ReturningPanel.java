import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;

public class ReturningPanel extends JPanel {

    private JTable booksTable;
    private JTable usersTable;
    private DefaultTableModel booksModel;
    private DefaultTableModel usersModel;
    private JButton returnBtn = new JButton("Return");

    public ReturningPanel() {
        setLayout(new BorderLayout());

     
        booksModel = new DefaultTableModel(new String[]{"Title", "Author", "Year", "Available"}, 0);
        usersModel = new DefaultTableModel(new String[]{"Username", "Borrowed Books"}, 0);

       
        loadBooksFromFile();
        loadUsersFromFile();

      
        JLabel header = new JLabel("Returning Section", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(153,153,255));
        header.setOpaque(true);
        add(header, BorderLayout.NORTH);

       
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(getBackground());

        booksTable = new JTable(booksModel);
        booksTable.setBackground(new Color(153,153,255));
        styleTable(booksTable);
        JTableHeader tableHeader2 =booksTable .getTableHeader();
tableHeader2.setOpaque(true);
tableHeader2.setBackground( Color.WHITE); 
tableHeader2.setForeground(new Color(153,153,255));         
tableHeader2.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        JScrollPane booksScroll = new JScrollPane(booksTable);
        booksScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(153,153,255), 2),
                "Books", 0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(153,153,255)
        ));
        centerPanel.add(booksScroll);

        usersTable = new JTable(usersModel);
          usersTable.setBackground(new Color(153,153,255));
        styleTable(usersTable);
        JTableHeader tableHeader1 = usersTable.getTableHeader();
tableHeader1.setOpaque(true);
tableHeader1.setBackground( Color.WHITE); 
tableHeader1.setForeground(new Color(153,153,255));         
tableHeader1.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        JScrollPane usersScroll = new JScrollPane(usersTable);
        usersScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(153,153,255), 2),
                "Users", 0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(153,153,255)
        ));
        centerPanel.add(usersScroll);

        add(centerPanel, BorderLayout.CENTER);

        
        returnBtn.setBackground(new Color(153,153,255));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        returnBtn.setFocusPainted(false);
        returnBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(returnBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        returnBtn.addActionListener(e -> returnBook());
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(153,153,255));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(102, 0, 51));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        JTableHeader th = table.getTableHeader();
        th.setOpaque(true);
        th.setBackground(new Color(153,153,255));
        th.setForeground(Color.WHITE);
        th.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    private void returnBook() {
        int bookRow = booksTable.getSelectedRow();
        int userRow = usersTable.getSelectedRow();

        if (bookRow == -1 || userRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a book and a user.");
            return;
        }

        String available = (String) booksModel.getValueAt(bookRow, 3);
        if ("Yes".equalsIgnoreCase(available)) {
            JOptionPane.showMessageDialog(this, "This book is already available.");
            return;
        }

        int borrowed = Integer.parseInt((String) usersModel.getValueAt(userRow, 1));
        if (borrowed <= 0) {
            JOptionPane.showMessageDialog(this, "This user has no borrowed books.");
            return;
        }

        booksModel.setValueAt("Yes", bookRow, 3);
        usersModel.setValueAt(String.valueOf(borrowed - 1), userRow, 1);

        saveAllBooksToFile();
        saveAllUsersToFile();

        booksTable.revalidate();
        booksTable.repaint();
        usersTable.revalidate();
        usersTable.repaint();

        JOptionPane.showMessageDialog(this, "Book returned successfully.");
    }

    private void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                booksModel.addRow(line.split(","));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                usersModel.addRow(line.split(","));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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