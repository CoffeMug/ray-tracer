package classes;

import javax.swing.*;

public final class Tracer {

    final static TracerGUI gui = new TracerGUI();
    final static TracerGUIObserver guiObserver = new TracerGUIObserver();

    public static void main (final String args[]){
        try {
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        TracerGUI.createAndShowGUI();
                    }
                });
            gui.addObserver(guiObserver);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
