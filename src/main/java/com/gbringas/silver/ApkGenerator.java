package com.gbringas.silver;

public class ApkGenerator {

    public static void generateTestApk(String directory) {
        GradleExecutor.execute(directory, " assembleAndroidTest");
    }

    public static void generateApk(String directory) {
        //TODO: revisar como llamo a esto
        GradleExecutor.execute(directory, " assembleDebug");
    }
}
