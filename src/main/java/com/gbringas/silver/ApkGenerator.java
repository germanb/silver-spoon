package com.gbringas.silver;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApkGenerator {

    //TODO: podría soportar que reciba un modulo en particular.
    public static boolean generateTestApk(String directory) {
        return generateTestApk(directory, null);
    }

    public static boolean generateTestApk(String directory, String module) {
        String command = "assembleAndroidTest";
        if(module != null){
            command = module + ":" + command;
        }

        return execute(directory, command);
    }

    public static boolean generateApk(String directory, String flavor) {
        flavor = flavor != null ? flavor : "";
        //TODO: revisar como llamo a esto
        // --debug for debug
        return execute(directory, " assemble" + StringUtils.capitalize(flavor) +"Debug ");
    }

    private static boolean execute(String directory, String command) {
        String cmd = directory + "/gradlew " + command + " -p " + directory;

        System.out.println(cmd);

        Runtime rt = Runtime.getRuntime();
        // final Process p = rt.exec("/Users/gbringas/dev/SilverSpoonTestApp/gradlew assembleAndroidTest
        // -p /Users/gbringas/dev/SilverSpoonTestApp");

        final Process p;
        try {
            p = rt.exec(cmd);
            new Thread(new Runnable() {
                public void run() {
                    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;

                    try {
                        while ((line = input.readLine()) != null)
                            System.out.println(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            p.waitFor();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
