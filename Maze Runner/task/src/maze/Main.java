package maze;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static private Scanner scanner = new Scanner(System.in);
    static private boolean hasMaze = false;
    static private Maze maze;
    static private String fileName;

    public static void main(String[] args) {
        while (true) {
            printMenu();

            int action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    System.out.println("Please, enter the size of a maze");

                    int[] fieldSize = Arrays
                            .stream(scanner.nextLine().split("\\s+"))
                            .mapToInt(Integer::parseInt)
                            .toArray();

                    if (fieldSize.length < 2) {
                        maze = new Maze(fieldSize[0]);
                    } else {
                        maze = new Maze(fieldSize);
                    }

                    hasMaze = true;
                    maze.display();
                    break;
                case 2:
                    System.out.println("Enter the file name:");
                    fileName = scanner.nextLine();

                    try {
                        maze = (Maze) Serializer.load(fileName);
                        hasMaze = true;
                        System.out.println("Load was successful!");
                    } catch (Exception e) {
                        System.out.println("Cannot load the maze. It has an invalid format");
                    }
                    break;
                case 3:
                    if (!hasMaze) {
                        System.out.println("Incorrect option. Please try again");
                    } else {
                        System.out.println("Enter the file name:");
                        fileName = scanner.nextLine();

                        try {
                            Serializer.save(maze, fileName);
                            System.out.println("Save was successful!");
                        } catch (Exception e) {
                            System.out.println("ERROR! Save NOT successful. " + e);
                        }
                    }
                    break;
                case 4:
                    if (hasMaze) {
                        maze.display();
                    }
                    break;
                case 5:
                    if (hasMaze) {
                        maze.displayWithPath();
                    }
                    break;
                case 0:
                    System.out.println("Bye !");
                    System.exit(0);
                default:
                    System.out.println("Incorrect option. Please try again");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("=== Menu ===\n" +
                "1. Generate a new maze\n" +
                "2. Load a maze");

        if (hasMaze) {
            System.out.println("3. Save the maze\n" +
                    "4. Display the maze\n" +
                    "5. Find the escape.");
        }

        System.out.println("0. Exit");
    }
}
