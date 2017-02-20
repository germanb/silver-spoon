package com.gbringas.silver;

import com.squareup.spoon.DeviceResult;
import com.squareup.spoon.DeviceTest;
import com.squareup.spoon.DeviceTestResult;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class HtmlDeviceCompare {

    public static HtmlDeviceCompare from(DeviceResult original, DeviceResult newResult, Path output) {

        String html = "<html> <body>";

        System.out.println("Start compare...");

        for (Map.Entry<DeviceTest, DeviceTestResult> entry : original.getTestResults().entrySet()) {

            //System.out.println("Comparing " + entry.getKey() + " and " + newResult.getTestResults());

            DeviceTestResult originalTestResult = entry.getValue();
            DeviceTestResult newTestResult = newResult.getTestResults().get(entry.getKey());


            if (!original.getDeviceDetails().getName().equals(newResult.getDeviceDetails().getName())) {
                System.out.println("[ERROR] - No device matching .....");
                continue;
            }

            if (newTestResult == null) {
                System.out.println("[ERROR] - No test matching .....");
                continue;
            }

            List<File> originalScreenshots = originalTestResult.getScreenshots();
            List<File> newScreenshots = newTestResult.getScreenshots();

            if (originalScreenshots.size() != newScreenshots.size()) {
                System.out.println("[ERROR] - No size matching .....");
                continue;
            }

            for (int i = 0; i < originalScreenshots.size(); i++) {

                //TODO: devovler % de diff para completar testsPassed
                String name = originalScreenshots.get(i).getName();
                String testPath = originalScreenshots.get(i).getParent();
                testPath = testPath.substring(testPath.indexOf("image/") + 5);
                name = name.substring(name.indexOf("_") + 1);
                File outFile = new File(output.toAbsolutePath() + testPath + "/" + name);

                outFile.getParentFile().mkdirs();
                ImageCompareUtils.compare(originalScreenshots.get(i), newScreenshots.get(i), outFile);

                html += "<img src=\"" + outFile + "\" title=\"\" data-original-title=\"Screen 2\">";

            }
        }

        html += "</body></html>";

        File htmlFile = new File(output.toAbsolutePath() + "/html/index.html");


        try {
            FileUtils.writeStringToFile(htmlFile, html);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}