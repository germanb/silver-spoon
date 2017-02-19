package com.gbringas.silver;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;
import com.squareup.spoon.DeviceResult;
import com.squareup.spoon.SpoonSummary;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class SilverRunner {

    static File output;

    public static void main(String... args) throws IOException {
        CommandLineArgs parsedArgs = new CommandLineArgs();
        JCommander jc = new JCommander(parsedArgs);

        jc.parse(args);

        ApkGenerator.generateTestApk(parsedArgs.baseDir.toString());

        ApkGenerator.generateApk(parsedArgs.baseDir.toString());

        //build test apk
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

            for (Map.Entry<String, DeviceResult> entry : spoonSummaryBase.getResults().entrySet()) {
                DeviceResult deviceResultCompare = spoonSummaryCompare.getResults().get(entry.getKey());
                if (deviceResultCompare == null) {
                    System.out.println("[ERROR] - No matching compare version of device " + entry.getKey());
                    continue;
                }

                HtmlDeviceCompare.from(entry.getValue(), deviceResultCompare, output.toPath());
            }

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
