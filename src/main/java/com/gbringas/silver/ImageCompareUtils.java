package com.gbringas.silver;

import org.im4java.core.CommandException;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListErrorConsumer;
import org.im4java.process.ArrayListOutputConsumer;

import java.io.File;

public class ImageCompareUtils {
    public static void compare(File aFile, File bFile, File outFile) {

        CompareCmd compareCmd = new CompareCmd();

        ArrayListOutputConsumer arrayListOutputConsumer = new ArrayListOutputConsumer();
        ArrayListErrorConsumer arrayListErrorConsumer = new ArrayListErrorConsumer();
        compareCmd.setErrorConsumer(arrayListErrorConsumer);
        compareCmd.setOutputConsumer(arrayListOutputConsumer);

        IMOperation imOperation = new IMOperation();
        imOperation.metric("AE");
        imOperation.addImage(aFile.getAbsolutePath(), bFile.getAbsolutePath(), outFile.getAbsolutePath());

        try {
            compareCmd.run(imOperation);

        } catch (CommandException e) {
            System.out.println("[OUT] - " + arrayListOutputConsumer.getOutput());
            System.out.println("[OUT] - " + arrayListErrorConsumer.getOutput());

            if (e.getReturnCode() != -1) {
                e.printStackTrace();
            } else {
                System.out.println("differs");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
