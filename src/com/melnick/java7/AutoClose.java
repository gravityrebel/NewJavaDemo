package com.melnick.java7;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by zmelnic on 12/16/2015.
 */
public class AutoClose {

    /**
     * All this class is trying to do is open a file and read from it. Look how nested,
     * and how we need to declare the resource outside the try/catch block.
     */
    public void withJava6() {
        InputStream inputStream = null;
        BufferedWriter writer = null;
        try {
            inputStream = new FileInputStream("/Some/Path");
            inputStream.read();

            writer  = new BufferedWriter(new FileWriter("/Other/Path"));
            writer.write("text to write");
        } catch (IOException e) {
            System.out.println("Exception Caught");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("COULD NOT CLOSE");
                }
            }
            if (writer != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("COULD NOT CLOSE");
                }
            }
        }
    }
















    /**
     * In Java 7 many resources implement @AutoCloseable, which will automatically run close code
     * upon leaving the try block.
     * <p>
     * Notice how the resource is declared inside the try() statement itself.
     * <p>
     * If you need access to the suppressed exception for whatever reason, it is possible to
     * obtain it.
     * @see InputStream
     * @see java.io.Closeable
     * @see AutoCloseable
     */
    public void withJava7() {
        String print = "Print me";
        try (InputStream inputStream = new FileInputStream("Some/Path");
             BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(System.getProperty("user.home")))
        ) {
            inputStream.read();
            writer.write(print);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
