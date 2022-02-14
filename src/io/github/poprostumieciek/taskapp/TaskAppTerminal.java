package io.github.poprostumieciek.taskapp;

import io.github.poprostumieciek.taskapp.tasks.*;

import java.io.InvalidClassException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskAppTerminal {

    public static void main(String[] args) {

        String filename;

        if (args.length < 1 || args[0].isEmpty()) {
            filename = Utils.DEFAULT_FILENAME;
        } else {
            filename = args[0];
        }

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = Utils.loadFromFile(filename);

        String command;
        String command_args;

        boolean isExited = false;
        while (!isExited) {
            System.out.print("> ");

            String[] input = scanner.nextLine().split(" ", 2);
            command = input[0];
            command_args = input.length > 1 ? input[1] : "";

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
                    task.setText(command_args);
                    tasks.add(task);
                    break;
                }

                case "add_link": {
                    LinkTask task = new LinkTask();

                    try {
                        if (command_args.contains("://")) {
                            task.setLink(command_args);
                            tasks.add(task);
                        } else {
                            throw new ProtocolException("String that you provided is not a link!");
                        }
                    } catch (Exception e) {
                        System.out.printf("[ERROR] %s\n", e.getMessage());
                    }
                    break;
                }

                case "remove_task": {
                    try {
                        int idx = Integer.parseUnsignedInt(command_args);
                        tasks.remove(idx - 1);
                        System.out.printf("[INFO] Successfully removed task #%d\n", idx);
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    }
                    break;
                }

                case "edit_text_task": {
                    try {
                        String[] cas = command_args.split(" ", 2);
                        String index = cas[0];
                        String content = cas.length > 1 ? cas[1] : "";

                        int idx = Integer.parseUnsignedInt(index);

                        Task task = tasks.get(idx - 1);

                        if (task instanceof TextTask) {
                            ((TextTask) task).setText(content);
                        } else {
                            throw new InvalidClassException("Invalid task type!");
                        }

                        System.out.printf("[INFO] Successfully edited TextTask #%d\n", idx);
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    } catch (Exception e) {
                        System.out.printf("[ERROR] %s\n", e.getMessage());
                    }
                    break;
                }

                case "edit_link_task": {
                    try {
                        String[] cas = command_args.split(" ", 2);
                        String index = cas[0];
                        String content = cas.length > 1 ? cas[1] : "";

                        int idx = Integer.parseUnsignedInt(index);

                        Task task = tasks.get(idx - 1);

                        if (task instanceof LinkTask) {
                            if (content.contains("://")) {
                                ((LinkTask) task).setLink(content);
                            } else {
                                throw new ProtocolException("String that you provided is not a link!");
                            }
                        } else {
                            throw new InvalidClassException("Invalid task type!");
                        }

                        System.out.printf("[INFO] Successfully edited LinkTask #%d\n", idx);
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    } catch (Exception e) {
                        System.out.printf("[ERROR] %s\n", e.getMessage());
                    }
                    break;
                }

                case "check": {
                    try {
                        int idx = Integer.parseUnsignedInt(command_args);
                        tasks.get(idx - 1).check();
                        System.out.printf("[INFO] Successfully checked task #%d\n", idx);
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    }
                    break;
                }

                case "uncheck": {
                    try {
                        int idx = Integer.parseUnsignedInt(command_args);
                        tasks.get(idx - 1).uncheck();
                        System.out.printf("[INFO] Successfully unchecked task #%d\n", idx);
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Provided argument is not a number!");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("[ERROR] Index out of bounds!");
                    }
                    break;
                }

                default: {
                    System.out.printf("[ERROR] Invalid command '%s'!\n", command);
                    break;
                }
            }

            Utils.saveToFile(tasks, filename);
        }

        scanner.close();
    }
}