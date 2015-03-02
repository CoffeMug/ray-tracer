package classes;

import java.util.Scanner;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GUIResponseHandler implements Observer {
    @Override
    public void update(Observable obj, Object arg) {
        if (arg instanceof String) {
            String resp = (String) arg;
            System.out.println("\nReceived response: " + resp);
        }
    }
}
