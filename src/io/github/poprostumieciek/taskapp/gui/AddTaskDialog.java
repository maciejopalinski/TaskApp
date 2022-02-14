package io.github.poprostumieciek.taskapp.gui;

import io.github.poprostumieciek.taskapp.tasks.LinkTask;
import io.github.poprostumieciek.taskapp.tasks.TextTask;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddTaskDialog extends JDialog implements DocumentListener {

    private JLabel label;
    private JRadioButton text_task_button;
    private JRadioButton link_task_button;
    private JTextField text_field;
    private JPanel button_bar;
    private JButton add_button;
    private JButton cancel_button;
    private MainWindow owner;

    public AddTaskDialog(MainWindow owner) {
        super(owner, "Add new task", ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(owner);
        this.owner = owner;
        JPanel margin = new JPanel();
        margin.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        margin.setLayout(new BoxLayout(margin, BoxLayout.PAGE_AXIS));
        add(margin);

        label = new JLabel("Select task type:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        margin.add(label);

        ButtonGroup radioButtonGroup = new ButtonGroup();
        text_task_button = new JRadioButton("Text task");
        link_task_button = new JRadioButton("Link task");

        text_task_button.setAlignmentX(Component.LEFT_ALIGNMENT);
        link_task_button.setAlignmentX(Component.LEFT_ALIGNMENT);

        text_task_button.setSelected(true);

        text_task_button.addActionListener(this::taskTypeChanged);
        link_task_button.addActionListener(this::taskTypeChanged);

        radioButtonGroup.add(text_task_button);
        radioButtonGroup.add(link_task_button);

        margin.add(text_task_button);
        margin.add(link_task_button);

        text_field = new JTextField();
        text_field.setAlignmentX(Component.LEFT_ALIGNMENT);
        text_field.getDocument().addDocumentListener(this);
        margin.add(text_field);

        button_bar = new JPanel();
        button_bar.setLayout(new GridLayout(1, 0));

        add_button = new JButton("Add");
        cancel_button = new JButton("Cancel");

        add_button.addActionListener(this::addClicked);
        cancel_button.addActionListener(this::cancelClicked);

        add_button.setEnabled(false);

        button_bar.add(add_button);
        button_bar.add(cancel_button);

        button_bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        margin.add(button_bar);

        pack();

        setResizable(false);
        setVisible(true);
    }

    public void insertUpdate(DocumentEvent e) {
        textChanged();
    }
    public void removeUpdate(DocumentEvent e) {
        textChanged();
    }
    public void changedUpdate(DocumentEvent e) {}

    private void taskTypeChanged(ActionEvent e) {
        textChanged();
    }

    private void textChanged() {
        String text = text_field.getText();
        if (text.isEmpty() || (link_task_button.isSelected() && !text.contains("://"))) {
            add_button.setEnabled(false);
        } else {
            add_button.setEnabled(true);
        }
    }

    private void addClicked(ActionEvent e) {
        if (text_task_button.isSelected()) {
            TextTask task = new TextTask();
            task.setText(text_field.getText());
            owner.add_task(task);
        } else {
            LinkTask task = new LinkTask();
            task.setLink(text_field.getText());
            owner.add_task(task);
        }
        dispose();
    }

    private void cancelClicked(ActionEvent e) {
        dispose();
    }
}
