package io.github.poprostumieciek.taskapp.gui;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

import io.github.poprostumieciek.taskapp.tasks.Task;

public class MainWindow extends JFrame {

    ArrayList<Task> tasks;

    JPanel tasks_panel;

    public MainWindow(ArrayList<Task> tasks) {

        super("TaskApp");

        this.tasks = tasks;

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setMinimumSize(new Dimension(800, 450));
        this.setLayout(new BorderLayout());

        JPanel layout = new JPanel();
        layout.setLayout(new GridLayout(1, 0));
        this.add(layout, BorderLayout.NORTH);

        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        layout.add(btnAdd);
        layout.add(btnEdit);
        layout.add(btnDelete);

        tasks_panel = new JPanel();
        tasks_panel.setLayout(new BoxLayout(tasks_panel, BoxLayout.PAGE_AXIS));
        update();

        JScrollPane tasks_scroll = new JScrollPane(tasks_panel);
        this.add(tasks_scroll, BorderLayout.CENTER);

        JLabel info = new JLabel("Loading...");
        this.add(info, BorderLayout.SOUTH);
    }

    void update() {
        tasks_panel.removeAll();

        for (Task task : tasks) {
            GUITask guiTask = new GUITask(task);
            guiTask.setAlignmentX(0);

            tasks_panel.add(guiTask);
        }
    }
}
