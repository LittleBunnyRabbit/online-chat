package face;

import javax.swing.*;    
import java.awt.*; 

import java.awt.event.ActionListener;  
import java.awt.event.ActionEvent;

public class LoggIn implements ActionListener { 
    private JFrame frame;
    private JLabel passLabel, usernameLabel;
    private JTextField usernameField;
    private JPasswordField passField;
    private JButton loggin;

    private String username, password;

    public LoggIn() {
        frame = new JFrame("Logg In");

        usernameLabel = new JLabel("USERNAME:"); 
        usernameField = new JTextField();

        passLabel = new JLabel("PASSWORD:"); 
        passField = new JPasswordField();

        loggin = new JButton("Logg In");

        usernameLabel.setBounds(25, 10, 200, 25);
        usernameField.setBounds(25, 35, 200, 25);

        passLabel.setBounds(25, 60, 200, 25);
        passField.setBounds(25, 85, 200, 25);

        loggin.setBounds(25, 120, 200, 25);

        loggin.addActionListener(this);

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(loggin);

        Dimension dimension = new Dimension(250, 200);
        frame.setMinimumSize(dimension);

        frame.setLocationRelativeTo(null);

        frame.setLayout(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }

    public void actionPerformed(ActionEvent e) { 
        if(e.getSource() == loggin){ 
            this.username = usernameField.getText();
            this.password = passField.getPassword().toString();
            
        }
    } 

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void resetInput() {
        usernameField.setText("");
        passField.setText("");
        username = null;
        password = null;
    }
}