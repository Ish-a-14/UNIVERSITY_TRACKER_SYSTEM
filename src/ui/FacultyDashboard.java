package ui;

import dao.ComplaintDAO;
import model.Complaint;
import util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacultyDashboard extends JFrame {

    JTable table;
    DefaultTableModel model;

    JComboBox<String> categoryBox, priorityBox;
    JTextArea descArea;
    JTextField customCategory;

    public FacultyDashboard() {

        setTitle("Faculty Dashboard - " + Session.userName);
        setSize(950, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3, 4, 10, 10));

        categoryBox = new JComboBox<>(new String[]{
                "Light Issue", "Fan Issue", "AC Problem",
                "Projector", "Computer Lab", "Internet/WiFi",
                "Water Supply", "Cleanliness", "Furniture",
                "Electrical", "Infrastructure", "Other"
        });

        priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});

        descArea = new JTextArea(2, 20);
        customCategory = new JTextField();
        customCategory.setVisible(false);

        JButton submitBtn = new JButton("Add Complaint");

        topPanel.add(new JLabel("Category"));
        topPanel.add(categoryBox);

        topPanel.add(new JLabel("Priority"));
        topPanel.add(priorityBox);

        topPanel.add(new JLabel("Description"));
        topPanel.add(new JScrollPane(descArea));

        topPanel.add(new JLabel("Custom (if Other)"));
        topPanel.add(customCategory);

        topPanel.add(new JLabel(""));
        topPanel.add(submitBtn);

        add(topPanel, BorderLayout.NORTH);

        // SHOW CUSTOM FIELD IF "Other"
        categoryBox.addActionListener(e -> {
            if (categoryBox.getSelectedItem().equals("Other")) {
                customCategory.setVisible(true);
            } else {
                customCategory.setVisible(false);
            }
        });

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID", "Category", "Description", "Priority", "Status", "Date"
        });

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton logoutBtn = new JButton("Logout");

        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);
        panel.add(logoutBtn);

        add(panel, BorderLayout.SOUTH);

        loadComplaints();

        submitBtn.addActionListener(e -> {

            String category = categoryBox.getSelectedItem().toString();
            String description = descArea.getText().trim();
            String priority = priorityBox.getSelectedItem().toString();

            if (category.equals("Other")) {
                category = customCategory.getText().trim();
            }

            if (category.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields");
                return;
            }

            boolean status = ComplaintDAO.addComplaint(
                    Session.userId,
                    category,
                    description,
                    priority
            );

            if (status) {
                JOptionPane.showMessageDialog(this, "Complaint Registered Successfully");
                descArea.setText("");
                customCategory.setText("");
                loadComplaints();
            } else {
                JOptionPane.showMessageDialog(this, "Error while adding complaint");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a complaint");
                return;
            }

            int id = Integer.parseInt(model.getValueAt(row, 0).toString());

            boolean deleted = ComplaintDAO.deleteComplaint(id, Session.userId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Deleted Successfully");
                loadComplaints();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot delete");
            }
        });

        editBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select complaint");
                return;
            }

            int id = Integer.parseInt(model.getValueAt(row, 0).toString());

            String oldCategory = model.getValueAt(row, 1).toString();
            String oldDescription = model.getValueAt(row, 2).toString();
            String oldPriority = model.getValueAt(row, 3).toString();

            String newCategory = JOptionPane.showInputDialog(this,
                    "Enter Category", oldCategory);

            String newDescription = JOptionPane.showInputDialog(this,
                    "Enter Description", oldDescription);

            String[] priorities = {"Low", "Medium", "High"};

            String newPriority = (String) JOptionPane.showInputDialog(
                    this,
                    "Select Priority",
                    "Priority",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    priorities,
                    oldPriority
            );

            if (newCategory == null || newDescription == null || newPriority == null) {
                return;
            }

            newCategory = newCategory.trim();
            newDescription = newDescription.trim();

            if (newCategory.isEmpty() || newDescription.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields");
                return;
            }

            boolean updated = ComplaintDAO.updateComplaint(
                    id,
                    Session.userId,
                    newCategory,
                    newDescription,
                    newPriority
            );

            if (updated) {
                JOptionPane.showMessageDialog(this, "Updated Successfully");
                loadComplaints();
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed");
            }
        });

        refreshBtn.addActionListener(e -> loadComplaints());

        logoutBtn.addActionListener(e -> {
            Session.clear();
            new LoginPage();
            dispose();
        });

        setVisible(true);
    }

    private void loadComplaints() {
        model.setRowCount(0);

        List<Complaint> list = ComplaintDAO.getUserComplaints(Session.userId);

        for (Complaint c : list) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getCategory(),
                    c.getDescription(),
                    c.getPriority(),
                    c.getStatus(),
                    c.getDate()
            });
        }
    }
}