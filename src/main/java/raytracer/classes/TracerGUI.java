package classes;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TracerGUI extends JFrame implements ActionListener {

	public TracerGUI() {
		init();
	}

	private void init(){
		this.setSize(500, 300); 
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}


    public void actionPerformed(ActionEvent e) {
		// if (e.getActionCommand() == "start"){
		// 	board.start();
		// }
    }
	
}