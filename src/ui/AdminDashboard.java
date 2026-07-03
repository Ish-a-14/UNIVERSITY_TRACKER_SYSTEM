package ui;

import dao.ComplaintDAO;
import model.Complaint;
import util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;

    Color bg = new Color(30, 30, 30);
    Color panelBg = new Color(45, 45, 45);
    Color accent = new Color(0, 153, 255);

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Admin Panel - Complaint Manager");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(panelBg);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");

        styleButton(searchBtn);
        styleButton(clearBtn);

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(clearBtn);

        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID", "Name", "Department", "Category", "Description", "Priority", "Status", "Date"
        });

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(accent);
        table.getTableHeader().setForeground(Color.WHITE);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(accent);
                    c.setForeground(Color.WHITE);
                    return c;
                }

                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);

                String priority = table.getValueAt(row, 5).toString();
                String status = table.getValueAt(row, 6).toString();

                if ("Resolved".equalsIgnoreCase(status)) {
                    c.setBackground(new Color(144, 238, 144));
                } else if ("High".equalsIgnoreCase(priority)) {
                    c.setBackground(new Color(255, 182, 193));
                } else if ("Medium".equalsIgnoreCase(priority)) {
                    c.setBackground(new Color(255, 255, 153));
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(panelBg);

        JButton resolveBtn = new JButton("Resolve");
        JButton refreshBtn = new JButton("Refresh");
        JButton logoutBtn = new JButton("Logout");

        styleButton(resolveBtn);
        styleButton(refreshBtn);
        styleButton(logoutBtn);

        bottomPanel.add(resolveBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(logoutBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        loadData("");

        searchBtn.addActionListener(e -> loadData(searchField.getText().trim()));

        clearBtn.addActionListener(e -> {
            searchField.setText("");
            loadData("");
        });

        resolveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select complaint");
                return;
            }

            int id = Integer.parseInt(model.getValueAt(row, 0).toString());

            if (ComplaintDAO.resolveComplaint(id)) {
                JOptionPane.showMessageDialog(this, "Resolved");
                loadData("");
            } else {
                JOptionPane.showMessageDialog(this, "Error");
            }
        });

        refreshBtn.addActionListener(e -> loadData(""));

        logoutBtn.addActionListener(e -> {
            Session.clear();
            new LoginPage();
            dispose();
        });

        setVisible(true);
    }

    private void loadData(String keyword) {

        model.setRowCount(0);

        List<Complaint> list = ComplaintDAO.getAllComplaints();
        String key = keyword == null ? "" : keyword.toLowerCase();

        for (Complaint c : list) {

            String name = c.getUserName() == null ? "" : c.getUserName();
            String dept = c.getDepartment() == null ? "" : c.getDepartment();
            String category = c.getCategory() == null ? "" : c.getCategory();
            String description = c.getDescription() == null ? "" : c.getDescription();

            if (key.isEmpty()
                    || name.toLowerCase().contains(key)
                    || dept.toLowerCase().contains(key)
                    || category.toLowerCase().contains(key)
                    || description.toLowerCase().contains(key)) {

                model.addRow(new Object[]{
                    c.getId(),
                    name,
                    dept,
                    category,
                    description,
                    c.getPriority(),
                    c.getStatus(),
                    c.getDate(),
                });
            }
        }
    }

    private void styleButton(JButton btn) {
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}