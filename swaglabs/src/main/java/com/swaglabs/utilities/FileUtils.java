package com.swaglabs.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
public class FileUtils {

    public static byte[] convertFileToByteArray(File fileData) throws IOException {
        return Files.readAllBytes(fileData.toPath());
    }

    public static void getFileContent(InputStream responseStream) throws UnsupportedEncodingException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
        String responseLine = br.readLine();
        String tempResponseString = "";
        while (responseLine != null) {
            tempResponseString = tempResponseString.concat(responseLine) + System.getProperty("line.separator");
            responseLine = br.readLine();
        }
        br.close();
        if (tempResponseString.length() > 0) {
            System.out.println(tempResponseString);
        }
    }

}
