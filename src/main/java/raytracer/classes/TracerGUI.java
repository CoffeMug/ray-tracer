package classes;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TracerGUI extends JFrame implements ActionListener {

    JPanel compsPanel;
    JButton chooseFileButton; 

    public TracerGUI() {
        init_comps_panel();
        init_choose_file_button();
        this.setSize(500, 300); 
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void init_comps_panel() {
        compsPanel = new JPanel();
        this.add(compsPanel, BorderLayout.WEST);
    }

    private void init_choose_file_button() {
	chooseFileButton= new JButton("Choose a ppm file...");
	chooseFileButton.setVerticalTextPosition(AbstractButton.CENTER);
	chooseFileButton.setHorizontalTextPosition(AbstractButton.LEADING);
	chooseFileButton.setPreferredSize(new Dimension(100, 30));
        this.compsPanel.add(chooseFileButton);
    }

    public void actionPerformed(ActionEvent e) {
        // if (e.getActionCommand() == "start"){
        //  board.start();
        // }
    }
    
}