package com.devtech.java7;

import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipal;

import static org.junit.Assert.*;


/**
 * New in Java 7 is the nio package. Two big players here are the Path class, and the Files class.
 *
 * A Path in Java represents a filesystem path. In addition to having access to much more metadata about the
 * path than the older File class, a Path is aware of any symbolic links. Most operations that include a Path can
 * be provided various options on what to do when a symbolic link is encountered.
 *
 *
 * The motivation for a new way of dealing with files was many. Among them, as noted above, was the desire
 * to access more metadata. Also, the older library did not always throw errors, or useful errors. NIO2 added
 * the ability to walk a file tree, and watch directories.
 */
/**
 * Created by zmelnic on 12/16/2015.
 */
public class NewFileLibrary {
    private static final String USER_HOME = System.getProperty("user.home");

    @Test
    public void checkExistence() {
        final Path doesNotExist = Paths.get(System.getProperty("user.home"), "doesNotExist");
        final Path exists = Paths.get(System.getProperty("user.home"), "doesExist");

        assertFalse(Files.exists(doesNotExist)); //false can mean either "does not exist" or "unknown".
        assertTrue(Files.exists(exists));
    }












    @Test
    public void writeAndCreate() {
        String input = "input";
        final Path doesNotExist = Paths.get(System.getProperty("user.home"), "doesNotExist");
        final Path exists = Paths.get(System.getProperty("user.home"), "doesExist");
        try {
            assertTrue(Files.isWritable(exists));

            Files.write(doesNotExist, input.getBytes());

            //It now has bytes! It exists
            assertTrue(Files.size(doesNotExist) > 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }









    @Test
    public void delete() {
        final Path doesExist = Paths.get(System.getProperty("user.home"), "doesExist");
        final Path doesNotExist = Paths.get(System.getProperty("user.home"), "doesNotExist");

        try {
            Files.delete(doesNotExist);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(Files.notExists(doesNotExist));
        assertFalse(Files.notExists(doesExist)); //false can either mean it does not exist or unknown.

    }








    /**
     * In addition to the copy method shown below, there is also a form of
     * Files.copy(InputStream inputStream, Path target, CopyOptions...)
     * that will take in any inputStream and copy it to a target location.
     */
    @Test
    public void copy() {
        final Path doesExist = Paths.get(System.getProperty("user.home"), "doesExist");
        final Path copyLocation = Paths.get(System.getProperty("user.home"), "copyOfDoesExist");

        try {
            Files.copy(doesExist, copyLocation, StandardCopyOption.REPLACE_EXISTING);
            assertTrue(Files.size(doesExist) == Files.size(copyLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Path storeWebData  = Paths.get(System.getProperty("user.home"), "webPage");
        URI u = URI.create("http://xkcd.com/");
        try (InputStream in = u.toURL().openStream()) {
            Files.copy(in, storeWebData, StandardCopyOption.REPLACE_EXISTING);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @Test
    public void read() {
        final Path storeWebData = Paths.get(System.getProperty("user.home"), "webPage");

        try (BufferedReader bufferedReader = Files.newBufferedReader(storeWebData)) {
            StringBuilder stringBuilder = new StringBuilder();

            while(bufferedReader.ready()) {
                stringBuilder.append(bufferedReader.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }










    public void otherPotentiallyUsefulFeatures() {
        final Path path = Paths.get("/foo/bar");

        Files.isRegularFile(path);
        Files.isExecutable(path);
        Files.isReadable(path);

        Files.isDirectory(path);
        try {
            Files.isHidden(path);

            UserPrincipal userPrincipal = FileSystems.getDefault().getUserPrincipalLookupService()
                                                .lookupPrincipalByName("zmelnick");
            Files.setOwner(path, userPrincipal);
            Files.isExecutable(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void voidpathInfo() {

        final Path path = Paths.get(System.getProperty("user.home"));

        System.out.println("Number of Nodes:"+ path.getNameCount());

        System.out.println("File Name:"+ path.getFileName());

        System.out.println("File Root:"+ path.getRoot());

        System.out.println("File Parent:"+ path.getParent());

    }
}
