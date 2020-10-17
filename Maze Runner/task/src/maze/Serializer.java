package maze;

import java.io.*;

public class Serializer {
    static void save(Maze maze, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(maze);
            oos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static Object load(String fileName){
        Object maze = null;

        try (FileInputStream fis = new FileInputStream(fileName)) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            maze = ois.readObject();
            ois.close();
        } catch(IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return maze;
    }
}
