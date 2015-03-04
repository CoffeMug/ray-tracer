package classes;

import java.util.Scanner;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.Color;

class TracerGUI extends Observable implements ActionListener {
    static private final String newline = "\n";
    static private JTextArea log;
    static private JPanel mainWindow;
    static private JPanel configPanel;
    static private JPanel drawPanel;
    static private JButton openXMLButton; 
    static private JFileChooser fc;  
    static private JComboBox zoomList;
    static private JLabel zoomLabel;
    static private JComboBox reflectionList;
    static private JLabel reflectionLabel;
    static private JComboBox depthList;
    static private JLabel depthLabel;
    static private JComboBox timerList;
    static private JLabel timerLabel;
    
    JScrollPane logScrollPane;
    String[] zoomValues = { "1", "2", "3", "4" };
    String[] reflectionValues = { "Yes", "No"};
    String[] depthValues = { "1", "2", "3", "4" };
    String[] timerValues = { "Yes", "No"};
 
    public TracerGUI() {
        fc = new JFileChooser();

        java.net.URL imageURL = this.getClass().getClassLoader().getResource("images/open.gif");
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            openXMLButton = new JButton("Open a scence xml file...", icon);
        } else {
            openXMLButton = new JButton("Open a scence xml file...");
        }

        openXMLButton.addActionListener(this);

        zoomLabel = new JLabel("Select camera zoom value:");
        zoomLabel.setMaximumSize(new Dimension(250, 20));
        zoomList = new JComboBox(zoomValues);
        zoomList.setMaximumSize(new Dimension(50, 20));
        zoomList.setAlignmentX(Component.LEFT_ALIGNMENT);
        zoomList.setSelectedIndex(0);
        zoomList.addActionListener(this);

        reflectionLabel = new JLabel("Use reflection:");
        reflectionLabel.setMaximumSize(new Dimension(200, 20));
        reflectionList = new JComboBox(reflectionValues);
        reflectionList.setMaximumSize(new Dimension(50, 20));
        reflectionList.setAlignmentX(Component.LEFT_ALIGNMENT);
        reflectionList.setSelectedIndex(0);
        reflectionList.addActionListener(this);

        depthLabel = new JLabel("Select depth:");
        depthLabel.setMaximumSize(new Dimension(150, 20));
        depthList = new JComboBox(depthValues);
        depthList.setMaximumSize(new Dimension(50, 20));
        depthList.setAlignmentX(Component.LEFT_ALIGNMENT);
        depthList.setSelectedIndex(0);
        depthList.addActionListener(this);

        timerLabel = new JLabel("Use timer:");
        timerLabel.setMaximumSize(new Dimension(100, 20));
        timerList = new JComboBox(timerValues);
        timerList.setMaximumSize(new Dimension(50, 20));
        timerList.setAlignmentX(Component.LEFT_ALIGNMENT);
        timerList.setSelectedIndex(0);
        timerList.addActionListener(this);

        configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setPreferredSize(new Dimension(640, 300));
        configPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        configPanel.add(openXMLButton);
        configPanel.add(Box.createRigidArea(new Dimension(0,10)));
        configPanel.add(zoomLabel);
        configPanel.add(zoomList);
        configPanel.add(Box.createRigidArea(new Dimension(0,10)));
        configPanel.add(reflectionLabel);
        configPanel.add(reflectionList);
        configPanel.add(Box.createRigidArea(new Dimension(0,10)));
        configPanel.add(depthLabel);
        configPanel.add(depthList);
        configPanel.add(Box.createRigidArea(new Dimension(0,10)));
        configPanel.add(timerLabel);
        configPanel.add(timerList);

        drawPanel = new JPanel();
        drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.X_AXIS));
        drawPanel.setPreferredSize(new Dimension(400, 400));
        drawPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        mainWindow = new JPanel(new BorderLayout());

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        logScrollPane = new JScrollPane(log);
 
        mainWindow.add(configPanel, BorderLayout.PAGE_START);
        mainWindow.add(drawPanel, BorderLayout.CENTER);
        mainWindow.add(logScrollPane, BorderLayout.PAGE_END);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openXMLButton) {
            int returnVal = fc.showOpenDialog(mainWindow);

            TracerParam data = new TracerParam();
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                log.append("Opening: " + file.getName() + "." + newline);
                data.setSceneFile(file.getName());
                changeData(data);

            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
 
        } else if (e.getSource() == zoomList) {
            JComboBox cb = (JComboBox)e.getSource();
            String zoomValue = (String)cb.getSelectedItem();
            log.append("Zoom value: " + zoomValue + newline);

        }

    }
    

    void changeData(Object data) {
        setChanged(); // the two methods of Observable class
        notifyObservers(data);
    }


    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TracerGUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
 
    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Raytracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainWindow);
        frame.pack();
        frame.setVisible(true);
    }
}
