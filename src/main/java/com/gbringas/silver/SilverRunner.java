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

        ApkGenerator.generateTestApk(parsedArgs.baseDir.toString());

        ApkGenerator.generateApk(parsedArgs.baseDir.toString());

        String filesOutput = "/app/build/outputs/";
        String referenceDirectory = parsedArgs.baseDir + filesOutput + "reports/reference/";
        String testDirectory = parsedArgs.baseDir + filesOutput + "reports/test/";

        if ("reference".equalsIgnoreCase(parsedArgs.mode)) {
            TestExecutor.record(parsedArgs.baseDir + filesOutput + "apk/app-debug.apk",
                    parsedArgs.baseDir + filesOutput + "apk/app-debug-androidTest.apk",
                    referenceDirectory);
            System.exit(0);
        } else if ("test".equalsIgnoreCase(parsedArgs.mode)) {
            TestExecutor.record(parsedArgs.baseDir + filesOutput + "apk/app-debug.apk",
                    parsedArgs.baseDir + filesOutput + "apk/app-debug-androidTest.apk",
                    testDirectory);
        } else if ("compare".equalsIgnoreCase(parsedArgs.mode)) {
            System.out.println("Running compare");
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

    }


    static class CommandLineArgs {

        @Parameter(names = {"--mode"}, description = "execution mode", required = true)
        public String mode;

        @Parameter(names = {"--baseDir"}, description = "Base dir", converter = PathConverter.class, required = true)
        //
        public Path baseDir;

    }


}
