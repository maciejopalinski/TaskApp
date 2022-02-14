package io.github.poprostumieciek.taskapp;

import io.github.poprostumieciek.taskapp.tasks.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

    public static final String DEFAULT_FILENAME = "test/example.tasks";

    public static ArrayList<Task> loadFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            ArrayList<Task> tasks = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("://")) {
                    // http://, https://, ftp://, file://
                    LinkTask task = new LinkTask();
                    task.unserialize(line);
                    tasks.add(task);
                } else {
                    // "hello world"
                    TextTask task = new TextTask();
                    task.unserialize(line);
                    tasks.add(task);
                }
            }

            scanner.close();
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static void saveToFile(ArrayList<Task> tasks, String filename) {
        try {
            File file = new File(filename);
            PrintWriter writer = new PrintWriter(file);

            for (Task task : tasks) {
                writer.println(task.serialize());
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
