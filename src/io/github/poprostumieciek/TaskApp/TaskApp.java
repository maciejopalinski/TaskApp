package io.github.poprostumieciek.TaskApp;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class TaskApp {

    interface Task {
        void unserialize(String line);
        String serialize();

        void check();
        void uncheck();
        boolean isDone();

        String toString();
    }

    public static class TextTask implements Task {

        private String text = "";
        private boolean isDone = false;
        private int checksLeft = 9;

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
            if (checksLeft <= 0) isDone = true;
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
            if (isDone) return "- [x] " + text;
            else return "- [ ] " + text;
        }
    }

    public static class LinksTask implements Task {

        private final Runtime runtime = Runtime.getRuntime();

        private String link = "";
        private boolean isDone = false;

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

        public String toString() {
            if (isDone) return "- [x] Open: " + link;
            else return "- [ ] Open: " + link;
        }

        private void open() {
            try {
                runtime.exec("rundll32 link.dll, FileProtocolHandler " + link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Task> loadFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            ArrayList<Task> tasks = new ArrayList<>();

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("://")) {
                    // http://, https://, ftp://, file://
                    LinksTask task = new LinksTask();
                    task.unserialize(line);
                    tasks.add(task);
                }
                else {
                    // "hello world"
                    TextTask task = new TextTask();
                    task.unserialize(line);
                    tasks.add(task);
                }
            }

            scanner.close();
            return tasks;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static void main(String[] args) {

        String filename;

        if (args.length < 1 || args[0].isEmpty()) {
            filename = "test/example.tasks";
        } else {
            filename = args[0];
        }

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = loadFromFile(filename);

        String command;

        boolean isExited = false;
        while (!isExited) {
            System.out.print("> ");
            command = scanner.nextLine();

            switch (command) {
                case "exit":
                case "quit":
                    isExited = true;
                    break;

                case "list":
                    for (Task task : tasks) {
                        System.out.println(task.toString());
                    }
                    break;
            }
        }
    }
}