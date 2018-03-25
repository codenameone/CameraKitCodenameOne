package com.codename1.camerakit.impl;

import com.codename1.camerakit.CameraEvent;
import com.codename1.camerakit.CameraKit;
import static com.codename1.ui.CN.*;

/**
 * @deprecated this is an internal implementation detail
 */
public class CameraCallbacks {
    public static void onError(String type, String message, String exceptionMessage) {
        callSerially(() -> {
            CameraKit ck = CameraKit.create();
            if(ck != null) {
                ck.fireCameraErrorEvent(new CameraEvent(type, message, exceptionMessage));
            }
        });
    }
    
    public static void onImage(byte[] jpeg) {
        callSerially(() -> {
            CameraKit ck = CameraKit.create();
            if(ck != null) {
                ck.fireCameraImageEvent(new CameraEvent(jpeg));
            }
        });
    }
    
    public static void onVideo(String file) {
        callSerially(() -> {
            CameraKit ck = CameraKit.create();
            if(ck != null) {
                ck.fireCameraVideoEvent(new CameraEvent(file));
            }
        });
    }
}
