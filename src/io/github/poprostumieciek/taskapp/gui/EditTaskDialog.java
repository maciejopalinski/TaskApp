package io.github.poprostumieciek.taskapp.gui;

import io.github.poprostumieciek.taskapp.tasks.Task;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditTaskDialog extends JDialog implements DocumentListener {

    private Task task;
    private JTextField text_field;
    private JPanel button_bar;
    private JButton edit_button;
    private JButton cancel_button;

    public EditTaskDialog(MainWindow owner, Task task) {
        super(owner, "Edit existing task", ModalityType.APPLICATION_MODAL);
        this.task = task;

        setLocationRelativeTo(owner);
        JPanel margin = new JPanel();
        margin.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        margin.setLayout(new BoxLayout(margin, BoxLayout.PAGE_AXIS));
        add(margin);

        edit_button = new JButton("Edit");
        cancel_button = new JButton("Cancel");

        text_field = new JTextField();
        text_field.setAlignmentX(Component.LEFT_ALIGNMENT);
        text_field.getDocument().addDocumentListener(this);
        text_field.setText(task.getContent());
        margin.add(text_field);

        button_bar = new JPanel();
        button_bar.setLayout(new GridLayout(1, 0));

        edit_button.addActionListener(this::editClicked);
        cancel_button.addActionListener(this::cancelClicked);

        button_bar.add(edit_button);
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

    private void textChanged() {
        String text = text_field.getText();
        edit_button.setEnabled(!text.isEmpty());
    }

    private void editClicked(ActionEvent e) {
        task.setContent(text_field.getText());
        dispose();
    }

    private void cancelClicked(ActionEvent e) {
        dispose();
    }
}
