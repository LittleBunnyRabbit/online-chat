import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import javax.swing.text.*;

import javax.swing.*;  
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class Interface implements ActionListener {
    private Client client;

    private JFrame frame;
    private JTextArea chatLog, onlineLog;
    private JScrollPane scrollChat, scrollOnline;
    private JTextField fileNameField, fileLocationField, chatInput;
    private JLabel fileNameLabel, fileLocationLabel, fileNameLabel2;
    private JButton sendChat, sendFile, getFile, updateList;
    private JComboBox<String> fileListBox, sendingFileListBox;
    private JScrollBar chatVertical;
    private JMenuBar menuBar;
    private JMenu fonts, theme;
    private JMenuItem mono, times, helv, light, dark;

    private Color discord = new Color(54, 57, 62);

    public Interface(Client client) {
        this.client = client;
        frame = new JFrame("Chat Room");
        createComponents();
        addActionListeners();
        setBoundsComponents();
        addMenu();
        setFonts("Monospaced");
        addColors();
        setBorders();
        addWrap();
        addComponents();

        configureFrame();

        updateSendingFiles();

    }

    public void createComponents() {
        fileNameLabel = new JLabel("File Name:");
        sendingFileListBox = new JComboBox<>(new String[]{});
        fileNameField = new JTextField();

        fileLocationLabel = new JLabel("File Location:");
        fileLocationField = new JTextField();

        updateList = new JButton("Update");
        sendFile = new JButton("Send");

        fileNameLabel2 = new JLabel("File Name:");
        fileListBox = new JComboBox<>(new String[]{});
        getFile = new JButton("Get");

        chatLog = new JTextArea();
        scrollChat = new JScrollPane(chatLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatVertical = scrollChat.getVerticalScrollBar();
        chatInput = new JTextField();
        sendChat = new JButton("Send");

        onlineLog = new JTextArea();
        scrollOnline = new JScrollPane(onlineLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void addActionListeners() {
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChat();
            }
        };
        chatInput.addActionListener(action); 

        sendFile.addActionListener(this); 
        sendChat.addActionListener(this);
        getFile.addActionListener(this);
        updateList.addActionListener(this);
    }

    public void setBoundsComponents() {
        fileLocationLabel.setBounds(25, 25, 100, 25);
        fileLocationField.setBounds(25, 50, 100, 25);

        fileNameLabel.setBounds(25, 75, 100, 25);
        //fileNameField.setBounds(25, 50, 100, 25);
        sendingFileListBox.setBounds(25, 100, 100, 25);

        updateList.setBounds(25, 135, 100, 25);
        sendFile.setBounds(25, 159, 100, 25);

        fileNameLabel2.setBounds(25, 225, 100, 25);
        fileListBox.setBounds(25, 250, 100, 25);
        getFile.setBounds(25, 285, 100, 25);


        scrollChat.setBounds(150, 25, 500, 501);
        chatInput.setBounds(150, 525, 400, 25);
        sendChat.setBounds(549, 525, 101, 25);

        scrollOnline.setBounds(649, 25, 200, 525);
    }

    public void setFonts(String font) {
        chatLog.setFont(new Font(font, Font.BOLD, 14));
        onlineLog.setFont(new Font(font, Font.BOLD, 14));
        chatInput.setFont(new Font(font, Font.BOLD, 14));
        fileLocationField.setFont(new Font(font, Font.BOLD, 14));
        menuBar.setFont(new Font(font, Font.BOLD, 14));
        fonts.setFont(new Font(font, Font.BOLD, 14));
        theme.setFont(new Font(font, Font.BOLD, 14));
    }

    public void addColors() {
        frame.getContentPane().setBackground(new Color(44, 47, 51));

        chatLog.setForeground(Color.LIGHT_GRAY);
        chatLog.setBackground(discord);

        onlineLog.setForeground(Color.LIGHT_GRAY);
        onlineLog.setBackground(discord);

        fileLocationField.setForeground(Color.LIGHT_GRAY);
        fileLocationField.setBackground(discord);
        fileLocationField.setCaretColor(Color.LIGHT_GRAY);
                
        sendingFileListBox.setForeground(Color.LIGHT_GRAY);
        sendingFileListBox.setBackground(discord);

        chatInput.setForeground(Color.LIGHT_GRAY);
        chatInput.setBackground(discord);
        chatInput.setCaretColor(Color.LIGHT_GRAY);

        fileListBox.setForeground(Color.LIGHT_GRAY);
        fileListBox.setBackground(discord);

        fileNameLabel.setForeground(Color.LIGHT_GRAY);
        fileLocationLabel.setForeground(Color.LIGHT_GRAY);
        fileNameLabel2.setForeground(Color.LIGHT_GRAY);

        sendChat.setForeground(Color.LIGHT_GRAY);
        sendChat.setBackground(discord);

        getFile.setForeground(Color.LIGHT_GRAY);
        getFile.setBackground(discord);

        sendFile.setForeground(Color.LIGHT_GRAY);
        sendFile.setBackground(discord);

        updateList.setForeground(Color.LIGHT_GRAY);
        updateList.setBackground(discord);

        menuBar.setForeground(Color.LIGHT_GRAY);
        menuBar.setBackground(discord);

        fonts.setForeground(Color.LIGHT_GRAY);
        fonts.setBackground(discord);

        theme.setForeground(Color.LIGHT_GRAY);
        theme.setBackground(discord);

        fileListBox.setUI(new BasicComboBoxUI());
                
        sendingFileListBox.setUI(new BasicComboBoxUI());
    }

    public void removeColors() {
        frame.getContentPane().setBackground(null);

        chatLog.setForeground(null);
        chatLog.setBackground(null);

        onlineLog.setForeground(null);
        onlineLog.setBackground(null);

        fileLocationField.setForeground(null);
        fileLocationField.setBackground(null);
        fileLocationField.setCaretColor(null);
                
        sendingFileListBox.setForeground(null);
        sendingFileListBox.setBackground(null);

        chatInput.setForeground(null);
        chatInput.setBackground(null);
        chatInput.setCaretColor(null);

        fileListBox.setForeground(null);
        fileListBox.setBackground(null);

        fileNameLabel.setForeground(null);
        fileLocationLabel.setForeground(null);
        fileNameLabel2.setForeground(null);

        sendChat.setForeground(null);
        sendChat.setBackground(null);

        getFile.setForeground(null);
        getFile.setBackground(null);

        sendFile.setForeground(null);
        sendFile.setBackground(null);

        updateList.setForeground(null);
        updateList.setBackground(null);

        menuBar.setForeground(null);
        menuBar.setBackground(null);

        fonts.setForeground(null);
        fonts.setBackground(null);

        theme.setForeground(null);
        theme.setBackground(null);

        fileListBox.setUI(null);
                
        sendingFileListBox.setUI(null);
    }

    public void setBorders() {
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        //fileNameField.setBorder(border);
        sendingFileListBox.setBorder(border);
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
        updateList.setBorder(border);
        menuBar.setBorder(border);
        fonts.setBorder(null);
        theme.setBorder(null);

    }

    public void addWrap() {
        chatLog.setLineWrap(true);
        chatLog.setWrapStyleWord(true);
        chatLog.setEditable(false);

        onlineLog.setLineWrap(true);
        onlineLog.setWrapStyleWord(true);
        onlineLog.setEditable(false);
    }

    public void addComponents() {
        frame.add(scrollChat);
        //frame.add(fileNameField);
        frame.add(sendingFileListBox);
        frame.add(fileLocationField);
        frame.add(chatInput);
        frame.add(updateList);
        frame.add(sendChat);
        frame.add(fileNameLabel);
        frame.add(fileLocationLabel);
        frame.add(sendFile);
        frame.add(fileNameLabel2);
        frame.add(fileListBox);
        frame.add(getFile);
        frame.add(scrollOnline);
    }

    public void addMenu() {
        String[] fon = new String[]{"TimesRoman", "Serif", "Helvetica", "SansSerif", "Courier", "Monospaced", "Dialog", "DialogInput"};
        menuBar = new JMenuBar();

        fonts = new JMenu("Font");
        theme = new JMenu("Theme");

        mono = new JMenuItem("Monospaced");
        times = new JMenuItem("TimesRoman");
        helv = new JMenuItem("Helvetica");

        light = new JMenuItem("Light");
        dark = new JMenuItem("Dark");


        fonts.add(mono);
        fonts.add(times);
        fonts.add(helv);

        theme.add(light);
        theme.add(dark);

        menuBar.add(fonts);
        menuBar.add(theme);

        frame.add(menuBar);
        frame.setJMenuBar(menuBar);

        mono.addActionListener(this); 
        times.addActionListener(this); 
        helv.addActionListener(this); 

        light.addActionListener(this); 
        dark.addActionListener(this); 
    }

    public void configureFrame() {
        frame.setSize(910, 600);    
        Dimension dimension = new Dimension(910, 600);
        frame.setMinimumSize(dimension);

        frame.setLocationRelativeTo(null);

        frame.setLayout(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) { 
        if(e.getSource() == sendChat) { sendChat(); }
        else if(e.getSource() == sendFile){ sendFile(); } 
        else if(e.getSource() == getFile) { getFile(); } 
        else if(e.getSource() == updateList) { updateSendingFiles(); }
        else if(e.getSource() == mono) { setFonts("Monospaced"); }
        else if(e.getSource() == times) { setFonts("TimesRoman"); }
        else if(e.getSource() == helv) { setFonts("Helvetica"); }
        else if(e.getSource() == light) { removeColors(); }
        else if(e.getSource() == dark) { addColors(); }
    }
 
    private void sendChat() {
        String input = chatInput.getText();
        chatInput.setText("");

        client.sendMessage(input);
    }

    private void sendFile() {
        String location = fileLocationField.getText(); 
        String fileName = sendingFileListBox.getSelectedItem().toString();

        if(!fileName.equals("")) {
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

    public void updateSendingFiles() {
        sendingFileListBox.removeAllItems();
        try {
            File folder = new File("../sending/");
            File[] listOfFiles = folder.listFiles();
            String files = "";
            for (File f : listOfFiles) {
                String fileName = f.getName();
                sendingFileListBox.addItem(fileName);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}