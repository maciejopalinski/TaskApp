package io.github.poprostumieciek.taskapp.tasks;

public interface Task {
    void unserialize(String line);
    String serialize();

    void check();
    void uncheck();
    boolean isDone();

    String toString();

    void setContent(String content);
    String getContent();
}