package com.gbringas.silver;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;
import com.squareup.spoon.SpoonSummary;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SilverRunner {

    static File output;

    public static void main(String... args) throws IOException {
        CommandLineArgs parsedArgs = new CommandLineArgs();
        JCommander jc = new JCommander(parsedArgs);

        jc.parse(args);

        if ("reference".equalsIgnoreCase(parsedArgs.mode) ||
                "test".equalsIgnoreCase(parsedArgs.mode)) {

            ApkGenerator.generateTestApk(parsedArgs.baseDir.toString(), parsedArgs.module);

            ApkGenerator.generateApk(parsedArgs.baseDir.toString(), parsedArgs.flavor);
        }

        String filesOutput = "/app/build/outputs/";
        String referenceDirectory = parsedArgs.baseDir + filesOutput + "reports/reference/";
        String testDirectory = parsedArgs.baseDir + filesOutput + "reports/test/";

        String testApk = parsedArgs.baseDir + filesOutput + "apk/app-debug-androidTest.apk";
        testApk = parsedArgs.module != null ? testApk.replaceAll("app", parsedArgs.module) : testApk;
        String apk = parsedArgs.baseDir + filesOutput + "apk/app" +
                (parsedArgs.flavor != null ? "-" + parsedArgs.flavor.toLowerCase() : "")
                + "-debug.apk";

        if ("reference".equalsIgnoreCase(parsedArgs.mode)) {

            TestExecutor.record(apk,
                    testApk,
                    referenceDirectory);
            System.exit(0);
        } else if ("test".equalsIgnoreCase(parsedArgs.mode)) {
            TestExecutor.record(apk,
                    testApk,
                    testDirectory);
        } else if ("compare".equalsIgnoreCase(parsedArgs.mode)) {
            System.out.println("Running compare");
        } else if ("replaceBase".equalsIgnoreCase(parsedArgs.mode)) {
            FileUtils.deleteDirectory(FileUtils.getFile(referenceDirectory));
            FileUtils.copyDirectory(FileUtils.getFile(testDirectory), FileUtils.getFile(referenceDirectory));
        } else {
            System.out.println("[ERROR] - Invalid option.");
            System.exit(1);
        }

        output = new File(parsedArgs.baseDir + filesOutput + "reports/output/");


        SpoonSummary spoonSummaryBase = JsonUtils.GSON.fromJson(FileUtils.readFileToString(
                FileUtils.getFile(referenceDirectory + "/result.json")), SpoonSummary.class);

        SpoonSummary spoonSummaryCompare = JsonUtils.GSON.fromJson(FileUtils.readFileToString(
                FileUtils.getFile(testDirectory + "result.json")), SpoonSummary.class);


        if (output.exists() || output.mkdirs()) {
            HtmlDeviceCompare.from(spoonSummaryBase, spoonSummaryCompare, output.toPath());
        } else {
            System.out.println("[ERROR] - Failed to create output dir.");
        }


        //TODO: agregar si tiene diferencias si desea pisar o no el archivo
        //TODO: soportar flavour
    }


    static class CommandLineArgs {

        @Parameter(names = {"--mode"}, description = "execution mode", required = true)
        public String mode;

        @Parameter(names = {"--baseDir"}, description = "Base dir", converter = PathConverter.class, required = true)
        //
        public Path baseDir;

        @Parameter(names = {"--module"}, description = "Module",  required = false)
        //
        public String module;

        @Parameter(names = {"--flavor"}, description = "Flavor",  required = false)
        //
        public String flavor;
    }


}
