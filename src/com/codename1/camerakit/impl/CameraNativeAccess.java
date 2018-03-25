package com.codename1.camerakit.impl;

import com.codename1.system.NativeInterface;
import com.codename1.ui.PeerComponent;

/**
 * @deprecated use the {@link com.codename1.camerakit.CameraKit} API this is an implementation detail!
 */
public interface CameraNativeAccess extends NativeInterface {
    public PeerComponent getView();
    
    public boolean isStarted();

    public void start();
    public void stop();

    public float getVerticalViewingAngle();
    public float getHorizontalViewingAngle();

    public int getFacing();
    public boolean isFacingFront();
    public boolean isFacingBack();

    public void setFacing(int facing);
    public void setFlash(int flash);
    public int getFlash();
    public void setFocus(int focus);
    public void setMethod(int method);
    public void setPinchToZoom(boolean zoom);
    public void setZoom(float zoom);
    public void setPermissions(int permissions);
    public void setVideoQuality(int videoQuality);
    public void setVideoBitRate(int videoBirRate);
    public void setLockVideoAspectRatio(boolean lockVideoAspectRatio);
    public void setJpegQuality(int jpegQuality);
    public void setCropOutput(boolean cropOutput);
    public int toggleFacing();
    public int toggleFlash();
    public void captureImage();

    public void captureVideo();

    public void captureVideoFile(String videoFile);

    public void stopVideo();

    public int getPreviewWidth();
    public int getPreviewHeight();

    public int getCaptureWidth();
    public int getCaptureHeight();

    
    //public boolean setTextDetectionListener(final CameraKitEventCallback<CameraKitTextDetect> callback) 
    //public void captureImage(final CameraKitEventCallback<CameraKitImage> callback)
    //public void captureVideo(CameraKitEventCallback<CameraKitVideo> callback)
    //public void captureVideo(File videoFile, final CameraKitEventCallback<CameraKitVideo> callback) {
    //public void addCameraKitListener(CameraKitEventListener CameraKitEventListener) {
    //public void bindCameraKitListener(Object object) {

}