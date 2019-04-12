package utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class WriteReadUtils {

    public static void createDir(String dirPath) {
        try {
            if (!Files.exists(Paths.get(dirPath))) {
                Files.createDirectories(Paths.get(dirPath));
            }
        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
        }
    }

    public static void writeUsedMessageId(String path, int id) throws IOException {
        FileWriter fileWriter = new FileWriter(path, true);
        PrintWriter writer = new PrintWriter(fileWriter);
        writer.println(id);
        writer.close();
        fileWriter.close();
    }

    public static synchronized Boolean readMessageIdFile(String dirPath, String message_id) {
        try {


            if (!Files.exists(Paths.get(dirPath))) {
                Files.createFile(Paths.get(dirPath));
                writeUsedMessageId(dirPath,0);
            }

            Stream<String> stream = Files.lines(Paths.get(dirPath));
            return stream.anyMatch(x -> x.equals(message_id));

        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
        }

        return false;
    }

    public static synchronized String getMyMessageId(String path) {
        String res = "";
        try {

            if (!Files.exists(Paths.get(path))) {
                Files.createFile(Paths.get(path));
                writeUsedMessageId(path,0);
                return "0";
            }

            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                // line
                res = line;
                // read next line
                line = reader.readLine();
            }
            reader.close();
            return res;
        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
            return "";
        }
    }


}
