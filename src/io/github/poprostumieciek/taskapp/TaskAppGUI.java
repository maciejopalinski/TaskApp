package io.github.poprostumieciek.taskapp;

import io.github.poprostumieciek.taskapp.gui.MainWindow;
import io.github.poprostumieciek.taskapp.tasks.Task;

import java.util.ArrayList;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TaskAppGUI {

    public static void main(String[] args) {

        String filename;

        if (args.length < 1 || args[0].isEmpty()) {
            filename = Utils.DEFAULT_FILENAME;
        } else {
            filename = args[0];
        }

        ArrayList<Task> tasks = Utils.loadFromFile(filename);

        MainWindow window = new MainWindow(tasks);
        window.setVisible(true);

        window.addWindowListener(new WindowListener() {
            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Utils.saveToFile(tasks, filename);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowOpened(WindowEvent e) {
            }
        });
    }
}
