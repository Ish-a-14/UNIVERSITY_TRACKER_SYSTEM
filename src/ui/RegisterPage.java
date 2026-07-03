package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterPage extends JFrame {

    JTextField name, email;
    JPasswordField pass;
    JComboBox<String> deptBox;

    public RegisterPage() {

        setTitle("Register");
        setSize(350, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel l1 = new JLabel("Name:");
        l1.setBounds(50, 20, 100, 30);
        add(l1);

        name = new JTextField();
        name.setBounds(50, 50, 250, 30);
        add(name);

        JLabel l2 = new JLabel("Email:");
        l2.setBounds(50, 80, 100, 30);
        add(l2);

        email = new JTextField();
        email.setBounds(50, 110, 250, 30);
        add(email);

        JLabel l3 = new JLabel("Password:");
        l3.setBounds(50, 140, 100, 30);
        add(l3);

        pass = new JPasswordField();
        pass.setBounds(50, 170, 250, 30);
        add(pass);

        JLabel l4 = new JLabel("Department:");
        l4.setBounds(50, 200, 100, 30);
        add(l4);

        deptBox = new JComboBox<>(new String[]{
                "CSE", "IT", "ECE", "EEE", "ME", "CE"
        });
        deptBox.setBounds(50, 230, 250, 30);
        add(deptBox);

        JButton register = new JButton("Register");
        register.setBounds(50, 270, 250, 30);
        add(register);

        register.addActionListener(e -> {

            String nameText = name.getText().trim();
            String emailText = email.getText().trim();
            String passText = new String(pass.getPassword()).trim();
            String dept = deptBox.getSelectedItem().toString();

            if(nameText.isEmpty() || emailText.isEmpty() || passText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            if(!emailText.contains("@")) {
                JOptionPane.showMessageDialog(this, "Enter valid email");
                return;
            }

            User u = new User();
            u.setName(nameText);
            u.setEmail(emailText);
            u.setPassword(passText);
            u.setDepartment(dept);

            boolean ok = new UserDAO().register(u);

            if(ok) {
                JOptionPane.showMessageDialog(this, "Registered Successfully");
                new LoginPage();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error (Email may already exist)");
            }
        });

        setVisible(true);
    }
}