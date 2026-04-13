
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
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

public class UsersPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addBtn = new JButton("Add User");
    private JButton deleteBtn = new JButton("Delete User");
    private File file = new File("users.csv");

    public UsersPanel() {
        
        setLayout(new BorderLayout());
JLabel header = new JLabel("Users Management", SwingConstants.CENTER);
header.setFont(new Font("Segoe UI", Font.BOLD, 22));
header.setForeground(Color.WHITE);     
header.setBackground(new Color(153,153,255));             
header.setOpaque(true);                            
add(header, BorderLayout.NORTH);
        String[] columns = {"Username", "Borrowed Books"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
table.setBackground(new Color(153,153,255));
table.setForeground(Color.WHITE);
table.setSelectionBackground(new Color(102, 0, 51));
JTableHeader tableHeader = table.getTableHeader();
tableHeader.setOpaque(true);
tableHeader.setBackground( Color.WHITE); 
tableHeader.setForeground(new Color(153,153,255));         
tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
         addBtn.setBackground(new Color(153, 153, 255));
    deleteBtn.setBackground(new Color(153, 153, 255));
    addBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
   deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
   addBtn.setForeground(Color.WHITE);
    deleteBtn.setForeground(Color.WHITE);
        buttonsPanel.add(addBtn);
        buttonsPanel.add(deleteBtn);
        add(buttonsPanel, BorderLayout.SOUTH);
    buttonsPanel.setBackground(Color.WHITE);

        loadUsersFromFile();

        addBtn.addActionListener(e -> {
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add User", true);
            dialog.setSize(300, 150);
            dialog.setLayout(new GridLayout(3, 2, 10, 10));
            dialog.setLocationRelativeTo(this);

            JTextField nameField = new JTextField();
            JTextField borrowedField = new JTextField();

            dialog.add(new JLabel("Username:"));
            dialog.add(nameField);
            dialog.add(new JLabel("Borrowed Books:"));
            dialog.add(borrowedField);

            JButton saveBtn = new JButton("Save");
            JButton cancelBtn = new JButton("Cancel");
            dialog.add(saveBtn);
            dialog.add(cancelBtn);

            saveBtn.addActionListener(ev -> {
                String name = nameField.getText().trim();
                String borrowed = borrowedField.getText().trim();

                if (!name.isEmpty() && !borrowed.isEmpty()) {
                    model.addRow(new Object[]{name, borrowed});
                    saveUserToFile(name, borrowed);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields");
                }
            });

            cancelBtn.addActionListener(ev -> dialog.dispose());
            dialog.setVisible(true);
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.removeRow(row);
                saveAllUsersToFile(); // ✅ تحديث الملف بعد الحذف
            }
        });
    }

    private void saveUserToFile(String name, String borrowed) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.println(name + "," + borrowed);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadUsersFromFile() {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    model.addRow(data);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveAllUsersToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                pw.println(model.getValueAt(i, 0) + "," + model.getValueAt(i, 1));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
