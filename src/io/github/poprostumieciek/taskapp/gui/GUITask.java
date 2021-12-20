package io.github.poprostumieciek.taskapp.gui;

import javax.swing.*;

import io.github.poprostumieciek.taskapp.tasks.Task;

public class GUITask extends JPanel {

    Task task;
    JCheckBox checkBox;
    JLabel taskTitle;

    public GUITask(Task task) {
        this.task = task;
        this.checkBox = new JCheckBox();
        this.taskTitle = new JLabel();

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(checkBox);
        this.add(taskTitle);

        update();
    }

    void update() {
        taskTitle.setText(task.toString());
        checkBox.setSelected(task.isDone());
    }
}
