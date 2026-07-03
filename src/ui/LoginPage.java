package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    JTextField email;
    JPasswordField pass;

    public LoginPage() {

        setTitle("Login");
        setSize(300, 250);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel l1 = new JLabel("Email:");
        l1.setBounds(50, 20, 100, 30);
        add(l1);

        email = new JTextField();
        email.setBounds(50, 50, 200, 30);
        add(email);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(50, 80, 100, 30);
        add(l2);

        pass = new JPasswordField();
        pass.setBounds(50, 110, 200, 30);
        add(pass);

        JButton login = new JButton("Login");
        login.setBounds(50, 150, 200, 30);
        add(login);

        JButton reg = new JButton("Register");
        reg.setBounds(50, 185, 200, 30);
        add(reg);

        reg.addActionListener(e -> {
            new RegisterPage();
            dispose();
        });

        login.addActionListener(e -> {

            String emailText = email.getText().trim();
            String passwordText = new String(pass.getPassword()).trim();

            if(emailText.isEmpty() || passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Enter email & password");
                return;
            }

            if(emailText.equals("admin@gmail.com") && passwordText.equals("admin123")) {
                JOptionPane.showMessageDialog(null, "Admin Login Successful");

                util.Session.userId = 0;
                util.Session.userName = "Admin";
                util.Session.email = "admin@gmail.com";

                new AdminDashboard();
                dispose();
                return;
            }

            User u = UserDAO.loginUser(emailText, passwordText);

            if(u != null) {

                JOptionPane.showMessageDialog(null, "Login Successful");

                util.Session.userId = u.getId();
                util.Session.userName = u.getName();
                util.Session.email = u.getEmail();

                new FacultyDashboard();
                dispose();

            } else {
                JOptionPane.showMessageDialog(null, "Invalid Login");
            }
        });
        setVisible(true);
    }
}