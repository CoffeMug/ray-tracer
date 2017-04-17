package tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class TracerGUI extends Observable implements ActionListener {

    private final Logger logger = LoggerFactory.getLogger(TracerGUI.class);

    // Left config panel 
    static private JPanel configPanelLeft;
    static private JButton openXMLButton; 
    static private JButton trace;
    static private JFileChooser fileChooser;
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
    static private JComboBox threadList;
    static private JLabel threadLabel;

    // Rest
    static private JFrame frame;
    static private JPanel drawPanel;
    static private JPanel configPanelTop;
    static private JLabel resultPic;    
    static private ImageIcon resultImage;
    static private TracerParam tracerParam;

    String[] zoomValues = { "100", "200", "300", "400" };
    String[] depthValues = { "1", "2", "3", "4" };
    String[] reflectionValues = { "True", "False"};
    String[] timerValues = { "True", "False"};
    String[] diffuseValues = { "True", "False"};
    String[] shadowValues = { "True", "False"};
    String[] threadValues = {"1", "2", "3", "4", "5"};
 
    public TracerGUI(TracerParam tracerParam) {
        this.tracerParam = tracerParam;
        configPanelTop = createConfigPanel("top", configPanelTop);
        configPanelLeft = createConfigPanel("left", configPanelLeft);
        configPanelRight = createConfigPanel("right", configPanelRight);
        createDrawPanel();
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == openXMLButton) {
            int returnVal = fileChooser.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                tracerParam.setSceneFile(file.getAbsolutePath());
            } else {
                logger.info("Open command cancelled by user.");
            }

        } else if (e.getSource() == trace) {
            if (tracerParam.getSceneFile() == null) {
                JOptionPane.showMessageDialog(null, "Please select a scene xml file first!");
            } 
            else {
                tracerParam.setZoom(Integer.parseInt(zoomList.getSelectedItem().toString()));
                tracerParam.setDepth(Integer.parseInt(depthList.getSelectedItem().toString()));
                tracerParam.setRenderReflection(Boolean.parseBoolean(reflectionList.getSelectedItem().toString()));
                tracerParam.setRenderDiffuse(Boolean.parseBoolean(diffuseList.getSelectedItem().toString()));
                tracerParam.setRenderShadows(Boolean.parseBoolean(shadowList.getSelectedItem().toString()));
                tracerParam.setEnableTimer(Boolean.parseBoolean(timerList.getSelectedItem().toString()));
                tracerParam.setNoOfThreads(Integer.parseInt(threadList.getSelectedItem().toString()));
                changeData(tracerParam);
                paintResults();
            }
        }
    }

    private void changeData(Object data) {
        setChanged();
        notifyObservers(data);
    }

    private void paintResults() {

        logger.info("Painting the result!");

        if (resultImage.getImage() == null) {
            resultImage = new ImageIcon("output.jpeg");
        } else {
            resultImage.getImage().flush();
            resultImage = new ImageIcon("output.jpeg");
        }
        resultPic.setIcon(resultImage);
        drawPanel.add(resultPic);
        drawPanel.revalidate();
        drawPanel.repaint();
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
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
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
            threadLabel = createLabel("Number of threads", threadLabel);
            threadList = createList(threadList, threadValues);
            panel.add(diffuseLabel);
            panel.add(diffuseList);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            panel.add(shadowLabel);
            panel.add(shadowList);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            panel.add(threadLabel);
            panel.add(threadList);
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
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./src/main/resources/ppm"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.xml", "xml"));
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        java.net.URL imageURL = this.getClass().getClassLoader().getResource("images/open.gif");
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            openXMLButton = new JButton("Open a scene xml file...", icon);
        } else {
            openXMLButton = new JButton("Open a scene xml file...");
        }
        openXMLButton.addActionListener(this);
    }

}
