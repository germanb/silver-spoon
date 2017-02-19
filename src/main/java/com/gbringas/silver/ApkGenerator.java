package com.gbringas.silver;

public class ApkGenerator {

    //TODO: podría soportar que reciba un modulo en particular.
    public static void generateTestApk(String directory) {
        GradleExecutor.execute(directory, " assembleAndroidTest");
    }

    public static void generateApk(String directory) {
        //TODO: revisar como llamo a esto
        GradleExecutor.execute(directory, " assembleDebug");
    }
}
