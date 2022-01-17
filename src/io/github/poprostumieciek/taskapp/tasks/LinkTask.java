package io.github.poprostumieciek.taskapp.tasks;

import java.io.IOException;

public class LinkTask implements Task {

    private final Runtime runtime = Runtime.getRuntime();

    private String link = "";
    private boolean isDone = false;

    public void setLink(String link) {
        this.link = link;
    }

    String getLink() {
        return link;
    }

    @Override
    public void unserialize(String line) {
        isDone = line.charAt(0) == 'T';
        link = line.substring(2);
    }

    @Override
    public String serialize() {
        char d = isDone ? 'T' : 'N';
        return d + "0" + link;
    }

    @Override
    public void check() {
        open();
        isDone = true;
    }

    @Override
    public void uncheck() {
        isDone = false;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        if (isDone)
            return "Open: " + link;
        else
            return "Open: " + link;
    }

    private void open() {
        try {
            runtime.exec("rundll32 link.dll, FileProtocolHandler " + link);
        } catch (Exception unused) {
        }

        try {
            runtime.exec("xdg-open " + link);
        } catch (Exception unused) {
        }
    }
}