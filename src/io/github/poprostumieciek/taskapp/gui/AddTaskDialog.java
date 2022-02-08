package io.github.poprostumieciek.taskapp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddTaskDialog extends JDialog {

    private JLabel label;
    private JRadioButton text_task_button;
    private JRadioButton link_task_button;
    private JTextField text_field;
    private JPanel button_bar;
    private JButton add_button;
    private JButton cancel_button;

    public AddTaskDialog (JFrame owner){
        super(owner, "Add new task", ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(owner);
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

        radioButtonGroup.add(text_task_button);
        radioButtonGroup.add(link_task_button);

        margin.add(text_task_button);
        margin.add(link_task_button);

        text_field = new JTextField();
        text_field.setAlignmentX(Component.LEFT_ALIGNMENT);
        margin.add(text_field);

        button_bar = new JPanel();
        button_bar.setLayout(new GridLayout(1, 0));

        add_button = new JButton("Add");
        cancel_button = new JButton("Cancel");

        add_button.addActionListener(this::addClicked);
        cancel_button.addActionListener(this::cancelClicked);

        button_bar.add(add_button);
        button_bar.add(cancel_button);

        button_bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        margin.add(button_bar);

        pack();

        setResizable(false);
        setVisible(true);
    }

    private void addClicked(ActionEvent e){

    }

    private void cancelClicked(ActionEvent e){
        dispose();
    }
}
