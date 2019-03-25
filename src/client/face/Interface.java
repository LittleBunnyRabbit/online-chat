package face;

import client.*;

import javax.swing.*;  
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.lang.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class Interface implements ActionListener {
    private Client client;

    private JFrame frame;
    private JTextArea chatLog, onlineLog;
    private JScrollPane scrollChat, scrollOnline;
    private JTextField fileNameField, fileLocationField, chatInput;
    private JLabel fileNameLabel, fileLocationLabel, fileNameLabel2, onlineLabel;
    private JButton sendChat, sendFile, getFile;
    private JComboBox<String> fileListBox;
    private JScrollBar chatVertical;

    private Color discord = new Color(54, 57, 62);

    public Interface(Client client) {
        this.client = client;

        frame = new JFrame("Chat Room");

        fileNameLabel = new JLabel("File Name:");
        fileNameField = new JTextField();

        fileLocationLabel = new JLabel("File Location:");
        fileLocationField = new JTextField();

        sendFile = new JButton("Send");

        fileNameLabel2 = new JLabel("File Name:");
        fileListBox = new JComboBox<>(new String[]{});
        getFile = new JButton("Get");

        chatLog = new JTextArea();
        scrollChat = new JScrollPane(chatLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatVertical = scrollChat.getVerticalScrollBar();
        chatInput = new JTextField();
        sendChat = new JButton("Send");

        onlineLabel = new JLabel("Online:");
        onlineLog = new JTextArea();
        scrollOnline = new JScrollPane(onlineLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        fileNameLabel.setBounds(25, 100, 100, 25);
        fileNameField.setBounds(25, 125, 100, 25);

        fileLocationLabel.setBounds(25, 150, 100, 25);
        fileLocationField.setBounds(25, 175, 100, 25);

        sendFile.setBounds(25, 215, 100, 25);

        fileNameLabel2.setBounds(25, 275, 100, 25);
        fileListBox.setBounds(25, 300, 100, 25);
        getFile.setBounds(25, 340, 100, 25);


        scrollChat.setBounds(150, 100, 500, 400);
        chatInput.setBounds(150, 525, 400, 25);
        sendChat.setBounds(550, 525, 100, 25);

        onlineLabel.setBounds(700, 0, 200, 25);
        scrollOnline.setBounds(700, 25, 200, 525);

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sendChat();
            }
        };


        sendFile.addActionListener(this); 
        sendChat.addActionListener(this);
        getFile.addActionListener(this);

        chatInput.addActionListener(action); 

        

        chatLog.setFont(new Font("Monospaced", Font.BOLD, 14));
        onlineLog.setFont(new Font("Monospaced", Font.BOLD, 14));
        chatInput.setFont(new Font("Monospaced", Font.BOLD, 14));
        fileLocationField.setFont(new Font("Monospaced", Font.BOLD, 14));
        fileNameField.setFont(new Font("Monospaced", Font.BOLD, 14));
        
        frame.getContentPane().setBackground(new Color(44, 47, 51));

        chatLog.setForeground(Color.LIGHT_GRAY);
        chatLog.setBackground(discord);

        onlineLog.setForeground(Color.LIGHT_GRAY);
        onlineLog.setBackground(discord);

        fileLocationField.setForeground(Color.LIGHT_GRAY);
        fileLocationField.setBackground(discord);
        fileLocationField.setCaretColor(Color.LIGHT_GRAY);

        fileNameField.setForeground(Color.LIGHT_GRAY);
        fileNameField.setBackground(discord);
        fileNameField.setCaretColor(Color.LIGHT_GRAY);

        chatInput.setForeground(Color.LIGHT_GRAY);
        chatInput.setBackground(discord);
        chatInput.setCaretColor(Color.LIGHT_GRAY);

        fileListBox.setForeground(Color.LIGHT_GRAY);
        fileListBox.setBackground(discord);

        fileNameLabel.setForeground(Color.LIGHT_GRAY);
        fileLocationLabel.setForeground(Color.LIGHT_GRAY);
        fileNameLabel2.setForeground(Color.LIGHT_GRAY);
        onlineLabel.setForeground(Color.LIGHT_GRAY);

        sendChat.setForeground(Color.LIGHT_GRAY);
        sendChat.setBackground(discord);

        getFile.setForeground(Color.LIGHT_GRAY);
        getFile.setBackground(discord);

        sendFile.setForeground(Color.LIGHT_GRAY);
        sendFile.setBackground(discord);
        Border border = BorderFactory.createLineBorder(Color.BLACK);


        fileNameField.setBorder(border);
        fileLocationField.setBorder(border);
        sendFile.setBorder(border);
        fileListBox.setBorder(border);
        getFile.setBorder(border);
        chatLog.setBorder(null);
        scrollChat.setBorder(border);
        chatVertical.setBorder(border);
        chatInput.setBorder(border);
        sendChat.setBorder(border);
        onlineLog.setBorder(null);
        scrollOnline.setBorder(border);

        fileListBox.setUI(new BasicComboBoxUI());


        chatLog.setLineWrap(true);
        chatLog.setWrapStyleWord(true);
        chatLog.setEditable(false);

        onlineLog.setLineWrap(true);
        onlineLog.setWrapStyleWord(true);
        onlineLog.setEditable(false);
        
        frame.add(scrollChat);
        frame.add(fileNameField);
        frame.add(fileLocationField);
        frame.add(chatInput);
        frame.add(sendChat);
        frame.add(fileNameLabel);
        frame.add(fileLocationLabel);
        frame.add(sendFile);
        frame.add(fileNameLabel2);
        frame.add(fileListBox);
        frame.add(getFile);
        frame.add(scrollOnline);
        frame.add(onlineLabel);


        frame.setSize(910, 600);    
        Dimension dimension = new Dimension(910, 600);
        frame.setMinimumSize(dimension);

        frame.setLocationRelativeTo(null);

        frame.setLayout(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) { 
        if(e.getSource() == sendChat){ sendChat(); }
        else if(e.getSource() == sendFile){ sendFile(); } 
        else if(e.getSource() == getFile) { getFile(); }   
    }  

    private void sendChat() {
        String input = chatInput.getText();
        chatInput.setText("");

        client.sendMessage(input);

        //appendChat("[Bunny]: " + input);
    }

    private void sendFile() {
        String fileName = fileNameField.getText();
        String location = fileLocationField.getText(); 
        if(fileName.equals("")) {
            fileNameField.setText("");
            fileLocationField.setText("");
    
            client.sendFile(fileName);
        }
    }

    private void getFile() {
        String fileSelected = fileListBox.getSelectedItem().toString();
        client.downloadFile(fileSelected);
    }

    public void appendChat(String msg) { 
        chatLog.append(msg + "\n"); 
        chatVertical.setValue(chatVertical.getMaximum());
    }

    public void updateOnlineList(String unsortedUsers) {
        List<String> userList = createList(unsortedUsers);
        
        onlineLog.setText("");
        for (String user : userList) {
            onlineLog.append("- " + user + "\n");
        }
    }

    public void updateFileList(String unsortedFiles) {
        List<String> fileList = createList(unsortedFiles);
        
        fileListBox.removeAllItems();
        for(String s : fileList){
            fileListBox.addItem(s);
        }
    }

    public String[] splitInput(String data) {
        String firstWord = "";
        int dataLength = data.length();
        for (int i = 0; i < dataLength; i++) {
            char c = data.charAt(i);
            if(c == ' ') {
                data = data.substring(i + 1);
                break;
            } else {
                firstWord = firstWord + c;
            }
        }
        return new String[]{firstWord, data};
    }

    public List<String> createList(String data) {
        List<String> list = new ArrayList<>();

        while(data != "" || data != null) {
            String[] spliting = splitInput(data);
            list.add(spliting[0]);
            if(data.equals(spliting[1])) {
                break;
            }
            data = spliting[1];
        }
        return list;
    }
}