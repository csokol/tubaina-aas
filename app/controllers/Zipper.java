package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    public static void main(String args[]) {
        String sourceDir = "C:/examples";
        String zipFile = "C:/FileIO/zipdemo.zip";
        Zipper zipper = new Zipper();

        try {
            // create object of FileOutputStream
            FileOutputStream fout = new FileOutputStream(zipFile);

            // create object of ZipOutputStream from FileOutputStream
            ZipOutputStream zout = new ZipOutputStream(fout);

            // create File object from source directory
            File fileSource = new File(sourceDir);

            zipper.addDirectory(zout, fileSource);

            // close the ZipOutputStream
            zout.close();

            System.out.println("Zip file has been created!");

        } catch (IOException ioe) {
            System.out.println("IOException :" + ioe);
        }

    }

    public void addDirectory(ZipOutputStream zout, File fileSource) {

        // get sub-folder/files list
        File[] files = fileSource.listFiles();

        System.out.println("Adding directory " + fileSource.getName());

        for (int i = 0; i < files.length; i++) {
            // if the file is directory, call the function recursively
            if (files[i].isDirectory()) {
                addDirectory(zout, files[i]);
                continue;
            }

            /*
             * we are here means, its file and not directory, so add it to the
             * zip file
             */

            try {
                System.out.println("Adding file " + files[i].getName());

                // create byte buffer
                byte[] buffer = new byte[1024];

                // create object of FileInputStream
                FileInputStream fin = new FileInputStream(files[i]);

                zout.putNextEntry(new ZipEntry(files[i].getName()));

                /*
                 * After creating entry in the zip file, actually write the
                 * file.
                 */
                int length;

                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }

                /*
                 * After writing the file to ZipOutputStream, use
                 * 
                 * void closeEntry() method of ZipOutputStream class to close
                 * the current entry and position the stream to write the next
                 * entry.
                 */

                zout.closeEntry();

                // close the InputStream
                fin.close();

            } catch (IOException ioe) {
                System.out.println("IOException :" + ioe);
            }
        }

    }

}
