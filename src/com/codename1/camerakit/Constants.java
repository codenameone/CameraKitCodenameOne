package com.codename1.camerakit;

/**
 * The constants from the camera kit from here:
 * https://github.com/CameraKit/camerakit-android/blob/master/camerakit-core/src/main/java/com/wonderkiln/camerakit/CameraKit.java
 */
public interface Constants {
    public static final int PERMISSION_REQUEST_CAMERA = 16;

    public static final int FACING_BACK = 0;
    public static final int FACING_FRONT = 1;

    public static final int FLASH_OFF = 0;
    public static final int FLASH_ON = 1;
    public static final int FLASH_AUTO = 2;
    public static final int FLASH_TORCH = 3;

    public static final int FOCUS_OFF = 0;
    public static final int FOCUS_CONTINUOUS = 1;
    public static final int FOCUS_TAP = 2;
    public static final int FOCUS_TAP_WITH_MARKER = 3;

    public static final int METHOD_STANDARD = 0;
    public static final int METHOD_STILL = 1;

    public static final int PERMISSIONS_STRICT = 0;
    public static final int PERMISSIONS_LAZY = 1;
    public static final int PERMISSIONS_PICTURE = 2;

    public static final int VIDEO_QUALITY_480P = 0;
    public static final int VIDEO_QUALITY_720P = 1;
    public static final int VIDEO_QUALITY_1080P = 2;
    public static final int VIDEO_QUALITY_2160P = 3;
    public static final int VIDEO_QUALITY_HIGHEST = 4;
    public static final int VIDEO_QUALITY_LOWEST = 5;
    public static final int VIDEO_QUALITY_QVGA = 6;
}
