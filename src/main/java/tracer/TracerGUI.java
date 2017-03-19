package tracer;

import java.util.Observable;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.*;

public class TracerGUI extends Observable implements ActionListener {

    // Left config panel 
    static private JPanel configPanelLeft;
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

    // Right config panel
    static private JPanel configPanelRight;
    static private JComboBox shadowList;
    static private JLabel shadowLabel;
    static private JComboBox diffuseList;
    static private JLabel diffuseLabel;

    // Rest
    static private JFrame frame;
    static private JPanel drawPanel;
    static private JPanel configPanelTop;
    static private JLabel resultPic;    
    static private ImageIcon resultImage;
    static private TracerParam data;

    String[] zoomValues = { "100", "200", "300", "400" };
    String[] depthValues = { "1", "2", "3", "4" };
    String[] reflectionValues = { "True", "False"};
    String[] timerValues = { "True", "False"};
    String[] diffuseValues = { "True", "False"};
    String[] shadowValues = { "True", "False"};
 
    public TracerGUI() {
        data = new TracerParam();
        configPanelTop = createConfigPanel("top", configPanelTop);
        configPanelLeft = createConfigPanel("left", configPanelLeft);
        configPanelRight = createConfigPanel("right", configPanelRight);
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

        } else if (e.getSource() == trace) {
            if (data.getSceneFile() == null) {
                JOptionPane.showMessageDialog(null, "Please select a scene xml file first!");
            } 
            else {
                data.setZoom(Integer.parseInt(zoomList.getSelectedItem().toString()));
                data.setDepth(Integer.parseInt(depthList.getSelectedItem().toString()));
                data.setRenderReflection(Boolean.parseBoolean(reflectionList.getSelectedItem().toString()));
                data.setRenderDiffuse(Boolean.parseBoolean(diffuseList.getSelectedItem().toString()));
                data.setRenderShadows(Boolean.parseBoolean(shadowList.getSelectedItem().toString()));
                data.setEnableTimer(Boolean.parseBoolean(timerList.getSelectedItem().toString()));
                changeData(data);
                paintResults();
            }
        }
    }

    private void changeData(Object data) {
        setChanged();
        notifyObservers(data);
    }

    private void paintResults() {
        System.out.println("Painting the result!");

        if (resultImage.getImage() == null) {
            resultImage = createImageIcon("output.jpeg"); 
        }
        else {
            resultImage.getImage().flush();
            resultImage = createImageIcon("output.jpeg"); 
        }
        resultPic.setIcon(resultImage);
        drawPanel.add(resultPic);
        drawPanel.revalidate();
        drawPanel.repaint();
    }

    private ImageIcon createImageIcon(String path) {
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
        frame.add(configPanelTop, BorderLayout.NORTH);
        frame.add(configPanelLeft, BorderLayout.WEST);
        frame.add(configPanelRight, BorderLayout.EAST);
        frame.add(drawPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private JLabel createLabel(String title, JLabel label) {
        label = new JLabel(title);
        label.setMaximumSize(new Dimension(150, 20));
        return label;
    }

    private JComboBox createList(JComboBox list, String[] values) {
        list = new JComboBox(values);
        list.setMaximumSize(new Dimension(50, 20));
        list.setAlignmentX(Component.LEFT_ALIGNMENT);
        list.setSelectedIndex(0);
        return list;
    }

    private JPanel createConfigPanel(final String position, JPanel panel) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        switch (position) {

        case "top" :
            panel.setPreferredSize(new Dimension(200, 50));
            createFileOpen(); 
            panel.add(openXMLButton);
            break;
        case "left" :
            panel.setPreferredSize(new Dimension(200, 50));
            createTraceButton();
            zoomLabel = createLabel("Camera zoom value", zoomLabel);
            zoomList = createList(zoomList, zoomValues);
            reflectionLabel = createLabel("Trace reflection", reflectionLabel);
            reflectionList = createList(reflectionList, reflectionValues);
            depthLabel = createLabel("Trace depth", depthLabel);
            depthList = createList(depthList, depthValues);
            timerLabel = createLabel("Timer", timerLabel);
            timerList = createList(timerList, timerValues);
            panel.add(zoomLabel);
            panel.add(zoomList);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            panel.add(reflectionLabel);
            panel.add(reflectionList);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            panel.add(depthLabel);
            panel.add(depthList);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            panel.add(timerLabel);
            panel.add(timerList);
            panel.add(Box.createRigidArea(new Dimension(0,20)));
            panel.add(trace);
            break;
        case "right" :
            panel.setPreferredSize(new Dimension(200, 300));
            diffuseLabel = createLabel("Render diffuse", diffuseLabel);
            diffuseList = createList(diffuseList, diffuseValues);
            shadowLabel = createLabel("Render shadow", shadowLabel);
            shadowList = createList(shadowList, shadowValues);
            panel.add(diffuseLabel);
            panel.add(diffuseList);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            panel.add(shadowLabel);
            panel.add(shadowList);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            break;
        }
            return panel;
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
