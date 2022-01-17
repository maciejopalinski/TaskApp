package io.github.poprostumieciek.taskapp.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import io.github.poprostumieciek.taskapp.tasks.Task;

public class GUITask extends JPanel implements MouseInputListener, ActionListener {

    private JCheckBox checkBox;
    private JLabel taskTitle;
    private Task task;
    private int idx;
    private TaskListener taskListener;

    private boolean isFocused = false;
    private boolean isSelected = false;

    public GUITask(Task task, int idx, TaskListener taskListener) {
        this.checkBox = new JCheckBox();
        this.taskTitle = new JLabel();

        this.task = task;
        this.idx = idx;
        this.taskListener = taskListener;

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(checkBox);
        this.add(taskTitle);

        this.addMouseListener(this);

        this.checkBox.addActionListener(this);

        update();
    }

    void update() {
        taskTitle.setText(task.toString());
        checkBox.setSelected(task.isDone());
    }

    void setFocused(boolean selected) {
        this.isFocused = selected;
        updateBackground();
    }

    boolean getFocused() {
        return this.isFocused;
    }

    void setSelected(boolean selected) {
        this.isSelected = selected;
        updateBackground();
    }

    boolean getSelected() {
        return this.isSelected;
    }

    void updateBackground() {
        if (this.isSelected) {
            this.setBackground(Color.DARK_GRAY);
        } else if (this.isFocused) {
            this.setBackground(Color.GRAY);
        } else {
            this.setBackground(UIManager.getColor("Panel.background"));
        }
        this.checkBox.setBackground(this.getBackground());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        taskListener.onTaskSelected(this.idx);
        updateBackground();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setFocused(true);
        updateBackground();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setFocused(false);
        updateBackground();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkBox) {
            if (this.checkBox.isSelected()) {
                this.task.check();
            } else {
                this.task.uncheck();
            }
        }
        update();
    }
}
