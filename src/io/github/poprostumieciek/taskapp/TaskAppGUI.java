package io.github.poprostumieciek.taskapp;

import io.github.poprostumieciek.taskapp.gui.MainWindow;
import io.github.poprostumieciek.taskapp.tasks.Task;

import java.util.ArrayList;

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
    }
}
