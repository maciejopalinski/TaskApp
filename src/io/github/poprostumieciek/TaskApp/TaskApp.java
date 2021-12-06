package io.github.poprostumieciek.TaskApp;

import java.io.InvalidClassException;
import java.io.PrintWriter;
import java.net.ProtocolException;
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
            if (isDone) return "[x] " + text + " (checked " + (9 - checksLeft) + " times)";
            else return "[ ] " + text + " (checked " + (9 - checksLeft) + " times)";
        }
    }

    public static class LinkTask implements Task {

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
            if (isDone) return "[x] Open: " + link;
            else return "[ ] Open: " + link;
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
                    LinkTask task = new LinkTask();
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

    public static void saveToFile(ArrayList<Task> tasks, String filename) {
        try {
            File file = new File(filename);
            PrintWriter writer = new PrintWriter(file);

/*
            for (int i = 0; i < tasks.size(); i++) {
                final Task task = tasks.get(i);
                writer.println(task.serialize());
            }

            the same as
*/

            for (Task task : tasks) {
                writer.println(task.serialize());
            }

            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        String command_args;

        boolean isExited = false;
        while (!isExited) {
            System.out.print("> ");

//            expr: "a b cd".split(" ")
//            contents: { "a", "b", "cd" }

            String[] input = scanner.nextLine().split(" ", 2);
            command = input[0];
            command_args = input.length > 1 ? input[1] : "";

//            System.out.printf("[DEBUG] command='%s', command_args='%s'\n", command, command_args);

            switch (command) {
                case "exit":
                case "quit":
                    isExited = true;
                    break;

                case "list":
                    for (int i = 0; i < tasks.size(); i++) {
                        final Task task = tasks.get(i);
                        System.out.printf("%3d. %s\n", i + 1, task.toString());
                    }
                    break;

                case "add_text": {
                    TextTask task = new TextTask();
                    task.text = command_args;
                    tasks.add(task);
                    break;
                }

                case "add_link": {
                    LinkTask task = new LinkTask();

                    try {
                        if (command_args.contains("://")) {
                            task.link = command_args;
                            tasks.add(task);
                        }
                        else {
                            throw new ProtocolException("String that you provided is not a link!");
                        }
                    }
                    catch (Exception e) {
                        System.out.printf("[ERROR] %s\n", e.getMessage());
                    }
                    break;
                }

                case "remove_task": {
                    try {
                        int idx = Integer.parseUnsignedInt(command_args);
                        tasks.remove(idx - 1);
                        System.out.printf("[INFO] Successfully removed task #%d\n", idx);
                    }
                    catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    }
                    catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    }
                    break;
                }

                case "edit_text_task": {
                    try {
//                        test this monstrosity
                        String[] cas = command_args.split(" ", 2);
                        String index = cas[0];
                        String content = cas.length > 1 ? cas[1] : "";

                        int idx = Integer.parseUnsignedInt(index);

                        Task task = tasks.get(idx - 1);

                        if (task instanceof TextTask) {
                            ((TextTask) task).text = content;
                        }
                        else {
                            throw new InvalidClassException("Invalid task type!");
                        }

                        System.out.printf("[INFO] Successfully edited TextTask #%d\n", idx);
                    }
                    catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    }
                    catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    }
                    catch (Exception e) {
                        System.out.printf("[ERROR] %s\n", e.getMessage());
                    }
                    break;
                }

                case "edit_link_task": {
                    try {
//                        test this monstrosity
                        String[] cas = command_args.split(" ", 2);
                        String index = cas[0];
                        String content = cas.length > 1 ? cas[1] : "";

                        int idx = Integer.parseUnsignedInt(index);

                        Task task = tasks.get(idx - 1);

                        if (task instanceof LinkTask) {
                            if (content.contains("://")) {
                                ((LinkTask) task).link = content;
                            }
                            else {
                                throw new ProtocolException("String that you provided is not a link!");
                            }
                        }
                        else {
                            throw new InvalidClassException("Invalid task type!");
                        }

                        System.out.printf("[INFO] Successfully edited LinkTask #%d\n", idx);
                    }
                    catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    }
                    catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    }
                    catch (Exception e) {
                        System.out.printf("[ERROR] %s\n", e.getMessage());
                    }
                    break;
                }

                case "check": {
                    try {
                        int idx = Integer.parseUnsignedInt(command_args);
                        tasks.get(idx - 1).check();
                        System.out.printf("[INFO] Successfully checked task #%d\n", idx);
                    }
                    catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    }
                    catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    }
                    break;
                }

                case "uncheck": {
                    try {
                        int idx = Integer.parseUnsignedInt(command_args);
                        tasks.get(idx - 1).uncheck();
                        System.out.printf("[INFO] Successfully unchecked task #%d\n", idx);
                    }
                    catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    }
                    catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    }
                    break;
                }

                default: {
                    System.out.printf("[ERROR] Invalid command '%s'!\n", command);
                    break;
                }
            }

            saveToFile(tasks, filename);
        }
    }
}