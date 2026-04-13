
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author windows_OS
 */
public class BooksPanel extends JPanel {
    BookService service = new BookService();
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add New Book", true);



    private JTable table;
    private DefaultTableModel model;
  private  JButton saveBtn = new JButton("Save");
   private   JButton cancelBtn = new JButton("Cancel");
      private  JButton addBtn = new JButton("Add Book");
         private JButton deleteBtn = new JButton("Delete Book");
            private File file = new File("books.csv");
            private void saveBookToFile(String title, String author, String year, String available) {
    try (PrintWriter pw = new PrintWriter(new FileWriter("books.csv", true))) {
        pw.println(title + "," + author + "," + year + "," + available);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
            private void saveAllBooksToFile() {
    try (PrintWriter pw = new PrintWriter(new FileWriter("books.csv"))) {
        for (int i = 0; i < model.getRowCount(); i++) {
            pw.println(model.getValueAt(i, 0) + "," +
                       model.getValueAt(i, 1) + "," +
                       model.getValueAt(i, 2) + "," +
                       model.getValueAt(i, 3));
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
private void loadBooksFromFile() {
    File file = new File("books.csv");
    if (!file.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length == 4) {
                model.addRow(data);
            }
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}


         public BooksPanel() {
    setLayout(new BorderLayout());
    setBackground(new Color(128, 0, 64));

    
    JLabel title = new JLabel("Books Management", SwingConstants.CENTER);
    title.setFont(new Font("Segoe UI", Font.BOLD, 22));
    title.setForeground(Color.WHITE);
    add(title, BorderLayout.NORTH);

   
    String[] columns = {"Title", "Author", "Year", "Available"};
    model = new DefaultTableModel(columns, 0);
    table = new JTable(model);
    table.setRowHeight(25);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    table.setGridColor(Color.BLACK);
    table.setBackground(new Color(153, 153, 255));
    table.setSelectionBackground(new Color(128, 0, 64));
    table.setSelectionForeground(Color.WHITE);

    JTableHeader header = table.getTableHeader();
    header.setBackground(Color.BLACK);
    header.setForeground(new Color(153, 153, 255));
    header.setFont(new Font("Segoe UI", Font.BOLD, 16));

    JScrollPane scroll = new JScrollPane(table);
    add(scroll, BorderLayout.CENTER);

 
    JPanel buttonsPanel = new JPanel();
    
    addBtn.setBackground(new Color(153, 153, 255));
    deleteBtn.setBackground(new Color(153, 153, 255));
    deleteBtn.setForeground(Color.WHITE);
   addBtn.setForeground(Color.WHITE);
    buttonsPanel.setBackground(Color.WHITE);
   addBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
   deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
    buttonsPanel.add(addBtn);
    buttonsPanel.add(deleteBtn);
    add(buttonsPanel, BorderLayout.SOUTH);

   loadBooksFromFile();

    addBtn.addActionListener(e -> {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add New Book", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JComboBox<String> availableBox = new JComboBox<>(new String[]{"Yes", "No"});

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author:"));
        dialog.add(authorField);
        dialog.add(new JLabel("Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel("Available:"));
        dialog.add(availableBox);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        dialog.add(saveBtn);
        dialog.add(cancelBtn);

        saveBtn.addActionListener(ev -> {
            String ttle = titleField.getText().trim();
            String author = authorField.getText().trim();
            String year = yearField.getText().trim();
            String available = (String) availableBox.getSelectedItem();

            if (!ttle.isEmpty() && !author.isEmpty() && !year.isEmpty()) {
                model.addRow(new Object[]{ttle, author, year, available});
                saveBookToFile(ttle, author, year, available);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelBtn.addActionListener(ev -> dialog.dispose());

        dialog.setVisible(true);
    });


    deleteBtn.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row != -1) {
            model.removeRow(row);
              saveAllBooksToFile();
        }
    });
}
}
