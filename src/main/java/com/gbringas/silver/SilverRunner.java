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

        output = new File(parsedArgs.outputResultFile.toUri());

        SpoonSummary spoonSummaryBase = JsonUtils.GSON.fromJson(FileUtils.readFileToString(
                FileUtils.getFile(parsedArgs.baseResultFile.toString() + "/result.json")), SpoonSummary.class);

        SpoonSummary spoonSummaryCompare = JsonUtils.GSON.fromJson(FileUtils.readFileToString(
                FileUtils.getFile(parsedArgs.revisionResultFile.toString() + "/result.json")), SpoonSummary.class);


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
        @Parameter(names = {"--base"}, description = "Base result json", converter = PathConverter.class, required = true)
        //
        public Path baseResultFile;

        @Parameter(names = {"--revision"}, description = "Compare result json", converter = PathConverter.class, required = true)
        //
        public Path revisionResultFile;

        @Parameter(names = {"--output"}, description = "Compare result json", converter = PathConverter.class, required = true)
        //
        public Path outputResultFile;
    }

}