package com.gbringas.silver;

import com.squareup.spoon.SpoonRunner;

import java.util.Arrays;

public class TestExecutor {

    public static void record(String apkPath, String testApkPath, String output) {

        String[] args = {"--apk", apkPath, "--test-apk", testApkPath, "--output", output, "--no-animations"};

        System.out.println("[INFO] - Args " + Arrays.toString(args) );

        SpoonRunner.main(args);


    }
}
