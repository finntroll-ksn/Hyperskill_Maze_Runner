package maze;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int height;
    private final int width;
    private int heightEdges;
    private int widthEdges;

    private int[][] adjacencyMatrix;
    private int[][] minSpanTree;
    private int[][] maze;

    private Random random = new Random();

    Maze(int[] fieldSizes) {
        height = fieldSizes[0];
        width = fieldSizes[1];

        buildMaze();
    }

    Maze(int fieldSizes) {
        height = fieldSizes;
        width = fieldSizes;

        buildMaze();
    }

    private void buildMaze() {
        buildAdjacencyMatrix();
        buildMinimumTree();
        fillMaze();
    }

    private void buildAdjacencyMatrix() {
        heightEdges = (height - 1) / 2;
        widthEdges = (width - 1) / 2;

        adjacencyMatrix = new int[heightEdges * widthEdges][heightEdges * widthEdges];

        int node = 0;
        int edgeWeight;

        //exclude right node
        for (int i = 0; i < adjacencyMatrix.length - 1; i++) {
            // Fill right path
            edgeWeight = getNextRandom();

            if (node + 1 < ((node / widthEdges) + 1) * widthEdges) {
                adjacencyMatrix[node][node + 1] = edgeWeight;
                adjacencyMatrix[node + 1][node] = edgeWeight;
            }

            // Fill lower path
            edgeWeight = getNextRandom();

            if (node + widthEdges < adjacencyMatrix.length) {
                adjacencyMatrix[node][node + widthEdges] = edgeWeight;
                adjacencyMatrix[node + widthEdges][node] = edgeWeight;
            }

            node++;
        }
    }

    private void buildMinimumTree() {
        // Minimum spanning tree Pims
        minSpanTree = new int[adjacencyMatrix.length][adjacencyMatrix.length];
        Set<Integer> addedNodes = new HashSet<>();
        addedNodes.add(0);
        int nextNode = 0;
        int currentNode = 0;

        while (addedNodes.size() < adjacencyMatrix.length) {
            int minValues = adjacencyMatrix.length * adjacencyMatrix.length;

            for (int eachNode : addedNodes) {
                for (int j = 0; j < adjacencyMatrix.length; j++) {
                    if (adjacencyMatrix[eachNode][j] < minValues && adjacencyMatrix[eachNode][j] > 0) {
                        if (!addedNodes.contains(j)) {
                            minValues = adjacencyMatrix[eachNode][j];
                            currentNode = eachNode;
                            nextNode = j;
                        }
                    }
                }
            }

            addedNodes.add(nextNode);
            minSpanTree[currentNode][nextNode] = 1;
            minSpanTree[nextNode][currentNode] = 1;
        }
    }

    private void fillMaze() {
        maze = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }

        maze[1][0] = 0;
        int currentEdge = 0;
        int mazeRow = 1;
        int mazeCol = 1;

        while (currentEdge < heightEdges * widthEdges) {
            if (currentEdge + 1 < (currentEdge / widthEdges + 1) * widthEdges && minSpanTree[currentEdge][currentEdge + 1] == 1) {
                maze[mazeRow][mazeCol] = 0;
                maze[mazeRow][mazeCol + 1] = 0;
                maze[mazeRow][mazeCol + 2] = 0;
            }
            if (currentEdge + widthEdges < heightEdges * widthEdges) {
                if (minSpanTree[currentEdge][currentEdge + widthEdges] == 1) {
                    maze[mazeRow][mazeCol] = 0;
                    maze[mazeRow + 1][mazeCol] = 0;
                    maze[mazeRow + 2][mazeCol] = 0;
                }
            }

            if (currentEdge + 1 <= (currentEdge / widthEdges + 1) * widthEdges - 1) {
                mazeCol += 2;
            } else {
                mazeRow += 2;
                mazeCol = 1;
            }

            currentEdge++;
            if (currentEdge == heightEdges * widthEdges) {
                maze[mazeRow - 2][width - 1] = 0;
            }
        }
    }

    private int getNextRandom() {
        return random.nextInt(height * width);
    }

    void display() {
        String WALL = "\u2588\u2588";
        String EMPTY = "  ";

        for (int[] line : maze) {
            for (int num : line) {
                String block = num == 1 ? WALL : EMPTY;
                System.out.printf("%s", block);
            }

            System.out.println();
        }
    }

    void displayWithPath() {
        String PASS = "//";
        String WALL = "\u2588\u2588";
        String EMPTY = "  ";

        int[][] pathMase = maze;

        for (int i = 0; i < height; i++) {
            pathMase[i] = Arrays.copyOf(maze[i], width);
        }

        char[] known = new char[minSpanTree.length];
        int[] cost = new int[minSpanTree.length];
        int[] path = new int[minSpanTree.length];

        Arrays.fill(cost, Integer.MAX_VALUE);
        Arrays.fill(path, Integer.MAX_VALUE);

        int index = 0;
        known[0] = 'T';
        cost[0] = 0;
        path[0] = -1;

        pathMase[height - 2][width - 1] = 2;
        pathMase[height - 2][width - 2] = 2;
        pathMase[1][0] = 2;
        pathMase[1][1] = 2;

        for (int j = 0; j < minSpanTree.length; j++) {
            for (int i = 1; i < minSpanTree.length; i++) {
                if (minSpanTree[index][i] > 0 && known[i] != 'T') {
                    if (cost[i] > cost[index] + minSpanTree[index][i]) {
                        cost[i] = cost[index] + minSpanTree[index][i];
                        path[i] = index;
                    }
                }
            }

            int min = Integer.MAX_VALUE;

            for (int i = 0; i < cost.length; i++) {
                if (known[i] != 'T' && cost[i] < min) {
                    min = cost[i];
                    index = i;
                }
            }
            known[index] = 'T';
        }

        index = path.length - 1;

        while (index != 0) {
            index = path[index];
            int i = (index / heightEdges * 2) + 1;
            int j = (index % heightEdges * 2) + 1;

            pathMase[i][j] = 2;

            if (j + 2 < width && pathMase[i][j + 2] == 2 && pathMase[i][j + 1] != 1) {
                pathMase[i][j + 1] = 2;
            } else if(j - 2 > 0 && pathMase[i][j - 2] == 2 && pathMase[i][j - 1] != 1) {
                pathMase[i][j - 1] = 2;
            } else if(i + 2 < height && pathMase[i + 2][j] == 2 && pathMase[i + 1][j] != 1) {
                pathMase[i + 1][j] = 2;
            } else {
                pathMase[i - 1][j] = 2;
            }
        }

        for (int[] line : pathMase) {
            for (int num : line) {
                String block = num == 1 ? WALL : num == 2 ? PASS : EMPTY;
                System.out.printf("%s", block);
            }

            System.out.println();
        }
    }
}
