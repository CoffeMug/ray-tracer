package main;

import tracer.TracerGUI;
import tracer.TracerGUIObserver;
import tracer.TracerParam;

import javax.swing.*;

public final class RayTracerApplication {

    final static TracerGUI gui = new TracerGUI(new TracerParam());
    final static TracerGUIObserver guiObserver = new TracerGUIObserver();

    public static void main (final String args[]){
        try {
            SwingUtilities.invokeLater(() -> TracerGUI.createAndShowGUI());
            gui.addObserver(guiObserver);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
