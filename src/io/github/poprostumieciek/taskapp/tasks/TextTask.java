package io.github.poprostumieciek.taskapp.tasks;

public class TextTask implements Task {

    private String text = "";
    private boolean isDone = false;
    private int checksLeft = 9;

    public void setText(String text) {
        this.text = text;
    }

    String getText() {
        return text;
    }

    @Override
    public void unserialize(String line) {
        isDone = line.charAt(0) == 'T';
        checksLeft = line.charAt(1) - '0';
        text = line.substring(2);
    }

    @Override
    public String serialize() {
        char d = isDone ? 'T' : 'N';
        return d + String.valueOf(checksLeft) + text;
    }

    @Override
    public void check() {
        checksLeft--;
        if (checksLeft <= 0)
            isDone = true;
    }

    @Override
    public void uncheck() {
        isDone = false;
        checksLeft = 9;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        if (isDone)
            return text + " (checked " + (9 - checksLeft) + " times)";
        else
            return text + " (checked " + (9 - checksLeft) + " times)";
    }
}