package io.github.poprostumieciek.taskapp.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import io.github.poprostumieciek.taskapp.tasks.Task;

public class MainWindow extends JFrame implements TaskListener {

    private ArrayList<Task> tasks;
    private ArrayList<GUITask> guiTasks = new ArrayList<>();

    private JPanel tasks_panel;
    private JLabel info;

    private int selectedTaskIndex = -1;

    public MainWindow(ArrayList<Task> tasks) {

        super("TaskApp");

        this.tasks = tasks;

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setMinimumSize(new Dimension(800, 450));
        this.setLayout(new BorderLayout());

        JPanel navbar = new JPanel();
        navbar.setLayout(new GridLayout(1, 0));
        this.add(navbar, BorderLayout.NORTH);

        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        navbar.add(btnAdd);
        navbar.add(btnEdit);
        navbar.add(btnDelete);
        btnAdd.addActionListener(this::addTaskClicked);
        btnEdit.addActionListener(this::editTaskClicked);
        btnDelete.addActionListener(this::deleteTaskClicked);

        tasks_panel = new JPanel();
        tasks_panel.setLayout(new BoxLayout(tasks_panel, BoxLayout.PAGE_AXIS));

        JScrollPane tasks_scroll = new JScrollPane(tasks_panel);
        this.add(tasks_scroll, BorderLayout.CENTER);

        info = new JLabel();
        this.add(info, BorderLayout.SOUTH);

        update();
    }

    void update() {
        tasks_panel.removeAll();
        guiTasks.clear();

        int idx = 0;
        for (Task task : tasks) {
            GUITask guiTask = new GUITask(task, idx, this);
            guiTask.setAlignmentX(0);

            guiTasks.add(guiTask);
            tasks_panel.add(guiTask);

            idx++;
        }

        String info_text = guiTasks.size() + " tasks";
        info.setText(info_text);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void add_task(Task task) {
        tasks.add(task);
        update();
    }

    @Override
    public void onTaskSelected(int idx) {
        for (GUITask guiTask : guiTasks) {
            guiTask.setSelected(false);
        }
        selectedTaskIndex = idx;
        guiTasks.get(idx).setSelected(true);
    }

    private void addTaskClicked(ActionEvent e) {
        new AddTaskDialog(this);
    }

    private void editTaskClicked(ActionEvent e) {
        if (selectedTaskIndex != -1) {
            new EditTaskDialog(this, tasks.get(selectedTaskIndex));
            update();
        }
    }

    private void deleteTaskClicked(ActionEvent e) {
        if (selectedTaskIndex != -1) {
            tasks.remove(selectedTaskIndex);
            update();
        }
    }
}
