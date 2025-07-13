package com.backend.hrms.helpers.utils;

import java.io.File;

public class PathUtils {
    public static String getTempFolderPath() {
        return new File(System.getProperty("user.dir"), "uploads/temp").getAbsolutePath();
    }

    public static String getUploadFolderPath() {
        return new File(System.getProperty("user.dir"), "uploads/media").getAbsolutePath();
    }

}
