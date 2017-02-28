package com.gbringas.silver;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.squareup.spoon.html.HtmlRenderer;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class HtmlUtils {

    public static void generateDeviceHtml(DeviceCompareResult htmlDeviceCompare, File output) {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile("page/device-compare.html");

        renderMustacheToFile(mustache, htmlDeviceCompare, output);

    }


    private static void renderMustacheToFile(Mustache mustache, Object scope, File file) {
        FileWriter writer = null;
        try {
            file.getParentFile().mkdirs();
            writer = new FileWriter(file);
            mustache.execute(writer, scope);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static void copyStaticAssets(String output) {
        final String STATIC_DIRECTORY = "static";
        final String[] STATIC_ASSETS = {
                "bootstrap.min.css", "bootstrap-responsive.min.css", "bootstrap.min.js", "jquery.min.js",
                "jquery.nivo.slider.pack.js", "nivo-slider.css", "icon-animated.png", "icon-devices.png",
                "icon-log.png", "ceiling_android.png", "arrows.png", "bullets.png", "loading.gif", "spoon.css"
        };
        File statics = new File(output, STATIC_DIRECTORY);
        statics.mkdirs();
        for (String staticAsset : STATIC_ASSETS) {
            copyStaticToOutput(staticAsset, statics);
        }
    }

    private static void copyStaticToOutput(String resource, File output) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = HtmlRenderer.class.getResourceAsStream("/static/" + resource);
            os = new FileOutputStream(new File(output, resource));
            IOUtils.copy(is, os);
        } catch (IOException e) {
            throw new RuntimeException("Unable to copy static resource " + resource + " to " + output, e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }
}
