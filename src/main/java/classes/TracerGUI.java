package classes;

import java.util.Observable;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TracerGUI extends JPanel implements ActionListener {
    static private final String newline = "\n";
    JTextArea log;
    JButton openButton, saveButton; 
    JFileChooser fc;  

    Integer[] zoomValues = { 1, 2, 3, 4 };
 
    public TracerGUI() {
        super(new BorderLayout());
        fc = new JFileChooser();
        openButton = new JButton("Open a ppm File...", 
                                 createImageIcon("images/Open.gif"));
        openButton.addActionListener(this);

        JComboBox zoomList = new JComboBox(zoomValues);
        zoomList.setSelectedIndex(1);
        zoomList.addActionListener(this);

        JPanel tracerPanel = new JPanel();
        tracerPanel.add(openButton);
        tracerPanel.add(zoomList);

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

 
        add(tracerPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(TracerGUI.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                log.append("Opening: " + file.getName() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
 
        } else if (e.getSource() == zoomValues) {
            JComboBox cb = (JComboBox)e.getSource();
            String zoomValue = (String)cb.getSelectedItem();
            log.append("Zoom value: " + zoomValue + newline);

        }

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
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {

        JFrame frame = new JFrame("Raytracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new TracerGUI());

        frame.pack();
        frame.setVisible(true);
    }
}
