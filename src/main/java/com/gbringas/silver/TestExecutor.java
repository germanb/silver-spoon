package com.gbringas.silver;

import com.squareup.spoon.SpoonRunner;

public class TestExecutor {

    public static void record(String apkPath, String testApkPath) {

        String[] args = {"--apk", apkPath, "--test-apk", testApkPath};

        SpoonRunner.main(args);


    }
}
