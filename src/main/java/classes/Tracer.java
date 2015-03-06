package classes;

import java.util.Scanner;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class Tracer {

    static TracerParam param;
    final static TracerGUI gui = new TracerGUI();
    final static TracerGUIObserver guiObserver = new TracerGUIObserver();

    public static void main (final String args[]){
        try {
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        UIManager.put("swing.boldMetal", Boolean.FALSE);
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
