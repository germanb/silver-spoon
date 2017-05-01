package com.gbringas.silver;

import org.im4java.core.CommandException;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListErrorConsumer;
import org.im4java.process.ArrayListOutputConsumer;

import java.io.File;

public class ImageCompareUtils {
    public static boolean compare(File aFile, File bFile, File outFile) {

        CompareCmd compareCmd = new CompareCmd();

        ArrayListOutputConsumer arrayListOutputConsumer = new ArrayListOutputConsumer();
        ArrayListErrorConsumer arrayListErrorConsumer = new ArrayListErrorConsumer();
        compareCmd.setErrorConsumer(arrayListErrorConsumer);
        compareCmd.setOutputConsumer(arrayListOutputConsumer);

        IMOperation imOperation = new IMOperation();
        imOperation.metric("AE");
        //TODO: imOperation.fuzz((double) 50);
        imOperation.addImage(aFile.getAbsolutePath(), bFile.getAbsolutePath(), outFile.getAbsolutePath());

        try {

            compareCmd.run(imOperation);
            System.out.println("[OK] - " + aFile.getName() + " match with compare version ");
            return true;
        } catch (CommandException e) {
            if (!arrayListOutputConsumer.getOutput().isEmpty()) {
                System.out.println("[ERROR] - " + arrayListOutputConsumer.getOutput());
            } else if (!arrayListErrorConsumer.getOutput().isEmpty()) {
                System.out.println("[ERROR] - " + aFile.getName() + " differs by " + arrayListErrorConsumer.getOutput());
            }

            if (e.getReturnCode() != -1) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] - " + e.getMessage());
        }
        return false;
    }
}
