package classes;

import java.util.Scanner;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import javax.imageio.*;

class TracerGUI extends Observable implements ActionListener {

    static private JFrame frame;
    static private JPanel configPanel;
    static private JPanel drawPanel;
    static private JButton openXMLButton; 
    static private JButton trace;
    static private JFileChooser fc;  
    static private JComboBox zoomList;
    static private JLabel zoomLabel;
    static private JComboBox reflectionList;
    static private JLabel reflectionLabel;
    static private JComboBox depthList;
    static private JLabel depthLabel;
    static private JComboBox timerList;
    static private JLabel timerLabel;
    static private JLabel resultPic;    
    static private ImageIcon resultImage;
    static private TracerParam data;

    String[] zoomValues = { "1", "2", "3", "4" };
    String[] reflectionValues = { "Yes", "No"};
    String[] depthValues = { "1", "2", "3", "4" };
    String[] timerValues = { "Yes", "No"};
 
    public TracerGUI() {
        data = new TracerParam();
        createFileOpen(); 
        createTraceButton();
        createZoomOption("Select camera zoom value:");
        createReflectionOption("Use reflection:");
        createDepthOption("Select depth:"); 
        createTimerOption("Use timer:");
        createConfigPanel();
        createDrawPanel();
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == openXMLButton) {
            int returnVal = fc.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                data.setSceneFile(file.getAbsolutePath());
            } else {
                System.out.println("Open command cancelled by user.");
            }

        } else if (e.getSource() == zoomList) {
            JComboBox cb = (JComboBox)e.getSource();
            String zoomValue = (String)cb.getSelectedItem();
            data.setZoom(Integer.parseInt(zoomValue));

        } else if (e.getSource() == trace) {
            if (data.getSceneFile() == null) {
                JOptionPane.showMessageDialog(null, "Please select a scene xml file first!");
            } 
            else {
                changeData(data);
                paintResults();
            }
        }
    }

    private void changeData(Object data) {
        setChanged();
        notifyObservers(data);
    }

    private static void paintResults() {
        System.out.println("Painting the result!");

        if (resultImage.getImage() == null) {
            resultImage = createImageIconResult("output.jpg"); 
        }
        else {
            resultImage.getImage().flush();
            resultImage = createImageIconResult("output.jpg"); 
        }

        resultPic.setIcon(resultImage);
        drawPanel.add(resultPic);
        drawPanel.revalidate();
        drawPanel.repaint();
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


    protected static ImageIcon createImageIconResult(String path) {
        if (path != null) {
            return new ImageIcon(path);
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
        frame = new JFrame("Raytracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(configPanel, BorderLayout.NORTH);
        frame.add(drawPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void createZoomOption(String label) {
        zoomLabel = new JLabel(label);
        zoomLabel.setMaximumSize(new Dimension(150, 20));
        zoomList = new JComboBox(zoomValues);
        zoomList.setMaximumSize(new Dimension(50, 20));
        zoomList.setAlignmentX(Component.LEFT_ALIGNMENT);
        zoomList.setSelectedIndex(0);
        zoomList.addActionListener(this);
    }

    private void createReflectionOption(String label) {
        reflectionLabel = new JLabel(label);
        reflectionLabel.setMaximumSize(new Dimension(200, 20));
        reflectionList = new JComboBox(reflectionValues);
        reflectionList.setMaximumSize(new Dimension(50, 20));
        reflectionList.setAlignmentX(Component.LEFT_ALIGNMENT);
        reflectionList.setSelectedIndex(0);
        reflectionList.addActionListener(this);
    }

    private void createDepthOption(String label) {
        depthLabel = new JLabel(label);
        depthLabel.setMaximumSize(new Dimension(150, 20));
        depthList = new JComboBox(depthValues);
        depthList.setMaximumSize(new Dimension(50, 20));
        depthList.setAlignmentX(Component.LEFT_ALIGNMENT);
        depthList.setSelectedIndex(0);
        depthList.addActionListener(this);
    }

    private void createTimerOption(String label) {
        timerLabel = new JLabel(label);
        timerLabel.setMaximumSize(new Dimension(100, 20));
        timerList = new JComboBox(timerValues);
        timerList.setMaximumSize(new Dimension(50, 20));
        timerList.setAlignmentX(Component.LEFT_ALIGNMENT);
        timerList.setSelectedIndex(0);
        timerList.addActionListener(this);
    }

    private void createConfigPanel() {
        configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setPreferredSize(new Dimension(400, 300));
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
        configPanel.add(Box.createRigidArea(new Dimension(0,20)));
        configPanel.add(trace);
    }

    private void createDrawPanel() {
        drawPanel = new JPanel();
        resultPic = new JLabel();
        resultImage = new ImageIcon();
        drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.X_AXIS));
        drawPanel.setPreferredSize(new Dimension(400, 400));
        drawPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    private void createTraceButton() {
        trace = new JButton("Trace the scene");
        trace.addActionListener(this);
     }

    private void createFileOpen() {
        fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./src/main/"));
        java.net.URL imageURL = this.getClass().getClassLoader().getResource("images/open.gif");
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            openXMLButton = new JButton("Open a scence xml file...", icon);
        } else {
            openXMLButton = new JButton("Open a scence xml file...");
        }
        openXMLButton.addActionListener(this);
    }

}
