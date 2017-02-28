package com.gbringas.silver;

import com.squareup.spoon.DeviceResult;
import com.squareup.spoon.DeviceTest;
import com.squareup.spoon.DeviceTestResult;
import com.squareup.spoon.SpoonSummary;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class HtmlDeviceCompare {

    public static HtmlDeviceCompare from(SpoonSummary spoonSummaryBase, SpoonSummary spoonSummaryCompare, Path output) {

        for (Map.Entry<String, DeviceResult> entry : spoonSummaryBase.getResults().entrySet()) {
            DeviceResult newResult = spoonSummaryCompare.getResults().get(entry.getKey());
            if (spoonSummaryCompare == null) {
                System.out.println("[ERROR] - No matching compare version of device " + entry.getKey());
                continue;
            }
            DeviceResult original = entry.getValue();


            System.out.println("Start compare...");

            File htmlFile = new File(output.toAbsolutePath() + "/" + entry.getKey()
                    + "/html/index.html");
            List<DeviceCompareResult> deviceCompareResults = new ArrayList<DeviceCompareResult>();

            for (Map.Entry<DeviceTest, DeviceTestResult> entry1 : original.getTestResults().entrySet()) {


                //System.out.println("Comparing " + entry.getKey() + " and " + newResult.getTestResults());

                DeviceTestResult originalTestResult = entry1.getValue();
                DeviceTestResult newTestResult = newResult.getTestResults().get(entry1.getKey());


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
                    DeviceCompareResult deviceCompareResult = new DeviceCompareResult();

                    //TODO: devovler % de diff para completar testsPassed
                    String name = originalScreenshots.get(i).getName();
                    String testPath = originalScreenshots.get(i).getParent();
                    testPath = testPath.substring(testPath.indexOf("image/") + 5);
                    name = name.substring(name.indexOf("_") + 1);
                    File outFile = new File(output.toAbsolutePath() + testPath + "/" + name);

                    outFile.getParentFile().mkdirs();
                    ImageCompareUtils.compare(originalScreenshots.get(i), newScreenshots.get(i), outFile);

                    deviceCompareResult.originalScreenShoot = originalScreenshots.get(i).getAbsolutePath();
                    deviceCompareResult.testScreenShoot = newScreenshots.get(i).getAbsolutePath();
                    deviceCompareResult.compareScreenShoot = outFile.getAbsolutePath();
                    deviceCompareResults.add(deviceCompareResult);


                }


            }

            HtmlUtils.generateDeviceHtml(deviceCompareResults, htmlFile);

        }
        return null;

    }
}