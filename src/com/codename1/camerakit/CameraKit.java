package com.codename1.camerakit;

import com.codename1.camerakit.impl.CameraCallbacks;
import com.codename1.camerakit.impl.CameraNativeAccess;
import com.codename1.system.NativeLookup;
import com.codename1.ui.PeerComponent;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import java.util.ArrayList;
import java.util.Map;

/**
 * This is the public camera API
 *
 * @author Shai Almog
 */
public class CameraKit implements Constants {
    private CameraNativeAccess cma;
    private static CameraKit instance;
    private ArrayList<CameraListener> listeners = new ArrayList<>();
    static boolean thisIsForIOS;
    
    /**
     * Will return null on platforms where Camera Kit isn't supported
     * @return instance of the class
     */
    public static CameraKit create() {
        if(instance == null) {
            if(isSimulator()) {
                Map<String, String> buildHints = Display.getInstance().getProjectBuildHints();
                if(!buildHints.containsKey("ios.NSCameraUsageDescription")) {
                    Display.getInstance().setProjectBuildHint("ios.NSCameraUsageDescription", "We need camera access to grab pictures and videos");
                }
                if(thisIsForIOS) {
                    CameraCallbacks.onError(null, null, null);
                    CameraCallbacks.onImage(null);
                    CameraCallbacks.onVideo(null);
                }
                return null;
            }
            instance = new CameraKit();
            instance.cma = NativeLookup.create(CameraNativeAccess.class);
            if(instance.cma != null && instance.cma.isSupported()) {
                return instance;
            }
            instance = null;
        }
        return instance;
    }

    /**
     * The component representing the camera view. Notice that start() should be invoked first
     */
    public PeerComponent getView() {
        return cma.getView();
    }
    
    /**
     * Returns true if the camera is started, notice that start() is asynchronous so this might not be accurate
     */
    public boolean isStarted() {
        return cma.isStarted();
    }

    /**
     * Must be invoked before using the camera
     */
    public void start() {
        cma.start();
    }
    
    public void stop() {
        cma.stop();
    }

    public float getVerticalViewingAngle() {
        return cma.getVerticalViewingAngle();
    }

    public float getHorizontalViewingAngle() {
        return cma.getHorizontalViewingAngle();        
    }

    public int getFacing() {
        return cma.getFacing();
    }
    
    public boolean isFacingFront() {
        return cma.isFacingFront();        
    }
    
    public boolean isFacingBack() {
        return cma.isFacingBack();        
    }

    public void setFacing(int facing) {
        cma.setFacing(facing);
    }
    
    public void setFlash(int flash) {
        cma.setFlash(flash);
    }
    
    public int getFlash() {
        return cma.getFlash();
    }
    
    public void setFocus(int focus) {
        cma.setFocus(focus);
    }
    
    public void setMethod(int method) {
        cma.setMethod(method);
    }
    
    public void setPinchToZoom(boolean zoom) {
        cma.setPinchToZoom(zoom);
    }
    
    public void setZoom(float zoom) {
        cma.setZoom(zoom);
    }
    
    public void setPermissions(int permissions) {
        cma.setPermissions(permissions);
    }
    
    public void setVideoQuality(int videoQuality) {
        cma.setVideoBitRate(videoQuality);
    }
    
    public void setVideoBitRate(int videoBirRate) {
        cma.setVideoBitRate(videoBirRate);
    }
    
    public void setLockVideoAspectRatio(boolean lockVideoAspectRatio) {
        cma.setLockVideoAspectRatio(lockVideoAspectRatio);
    }
    public void setJpegQuality(int jpegQuality) {
        cma.setJpegQuality(jpegQuality);
    }
    
    public void setCropOutput(boolean cropOutput) {
        cma.setCropOutput(cropOutput);
    }
    
    public int toggleFacing() {
        if(getFacing() == FACING_BACK) {
            setFacing(FACING_FRONT);
            return FACING_FRONT;
        } 
        setFacing(FACING_BACK);
        return FACING_BACK;
    }
    
    public int toggleFlash() {
        switch (getFlash()) {
            case FLASH_OFF:
                setFlash(FLASH_ON);
                break;

            case FLASH_ON:
                setFlash(FLASH_AUTO);
                break;

            case FLASH_AUTO:
            case FLASH_TORCH:
                setFlash(FLASH_OFF);
                break;
        }    
        return getFlash();
    }
    
    public void captureImage() {
        cma.captureImage();
    }

    public void captureVideo() {
        cma.captureVideo();
    }

    public void captureVideo(String videoFile) {
        cma.captureVideoFile(videoFile);
    }

    public void stopVideo() {
        cma.stopVideo();
    }

    public int getPreviewWidth() {
        return cma.getPreviewWidth();
    }
    
    public int getPreviewHeight() {
        return cma.getPreviewHeight();
    }

    public int getCaptureWidth() {
        return cma.getCaptureWidth();
    }
    
    public int getCaptureHeight() {
        return cma.getCaptureHeight();
    }

    public void addCameraListener(CameraListener listener) {
        listeners.add(listener);
    }
    
    public void removeCameraListener(CameraListener listener) {
        listeners.remove(listener);
    }
    
    public void fireCameraErrorEvent(final CameraEvent ev) {
        if(!isEdt()) {
            callSerially(() -> fireCameraErrorEvent(ev));
            return;
        }
        for(CameraListener l : listeners) {
            l.onError(ev);
        }
    }

    public void fireCameraImageEvent(final CameraEvent ev) {
        if(!isEdt()) {
            callSerially(() -> fireCameraImageEvent(ev));
            return;
        }
        for(CameraListener l : listeners) {
            l.onImage(ev);
        }
    }

    public void fireCameraVideoEvent(final CameraEvent ev) {
        if(!isEdt()) {
            callSerially(() -> fireCameraVideoEvent(ev));
            return;
        }
        for(CameraListener l : listeners) {
            l.onVideo(ev);
        }
    }
}
