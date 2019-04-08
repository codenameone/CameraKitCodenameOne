package com.codename1.camerakit.impl;
import com.codename1.ui.Display;
import com.codename1.ui.PeerComponent;
import com.codename1.ui.util.ImageIO;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class CameraNativeAccessImpl implements com.codename1.camerakit.impl.CameraNativeAccess{
    private Webcam webcam;
    private WebcamPanel view;
    private PeerComponent peerView;
    private int jpegQuality = 70;
    
     public static final String TYPE_ERROR = "CameraKitError";

    public static final String TYPE_CAMERA_OPEN = "CKCameraOpenedEvent";
    public static final String TYPE_CAMERA_CLOSE = "CKCameraStoppedEvent";

    public static final String TYPE_FACING_CHANGED = "CKFacingChangedEvent";
    public static final String TYPE_FLASH_CHANGED = "CKFlashChangedEvent";

    public static final String TYPE_IMAGE_CAPTURED = "CKImageCapturedEvent";
    public static final String TYPE_VIDEO_CAPTURED = "CKVideoCapturedEvent";

    public static final String TYPE_FOCUS_MOVED = "CKFocusMovedEvent";

    public static final String TYPE_TEXT_DETECTED = "CKTextDetectedEvent";
    
    public CameraNativeAccessImpl() {
        /*
        EventQueue.invokeLater(()->{
            Webcam webcam = Webcam.getDefault();
            webcam.setViewSize(WebcamResolution.VGA.getSize());

            WebcamPanel panel = new WebcamPanel(webcam);
            panel.setFPSDisplayed(true);
            panel.setDisplayDebugInfo(true);
            panel.setImageSizeDisplayed(true);
            panel.setMirrored(true);
            
            JFrame f = new JFrame("Hello");
            f.setLayout(new BorderLayout());
            f.add(panel, BorderLayout.CENTER);
            f.pack();
            f.setVisible(true);
        });
        */
    }
    private boolean started;
    private boolean starting;
    private boolean stopping;
    
    private synchronized Webcam getWebcam() {
        if (webcam == null) {
            webcam = Webcam.getDefault();
            webcam.addWebcamListener(new WebcamListener() {
                @Override
                public void webcamOpen(WebcamEvent we) {
                    starting = false;
                    started = true;
                }

                @Override
                public void webcamClosed(WebcamEvent we) {
                    started = false;
                    stopping = false;
                    System.out.println("Webcam closed");
                }

                @Override
                public void webcamDisposed(WebcamEvent we) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void webcamImageObtained(WebcamEvent we) {

                }

            });
        }
        return webcam;
    }
    
    public void start() {
        if (starting || started) {
            return;
        }
        checkCameraUsageDescription();
        checkMicrophoneUsageDescription();
        getWebcam().setViewSize(WebcamResolution.VGA.getSize());
        starting = true;
        webcam.open(true);

    }

    public void stop() {
        System.out.println("in stop");
        if (!started || stopping) {
            return;
        }
        System.out.println("Closing webcam");
        webcam.close();
        
    }

    public void setCropOutput(boolean param) {
    }

    public int getPreviewHeight() {
        return getWebcam().getViewSize().height;
    }
    
    public int getPreviewWidth() {
        return getWebcam().getViewSize().width;
    }
    
    public int getCaptureHeight() {
        return getWebcam().getDevice().getResolution().height;
    }
    
    public int getCaptureWidth() {
        return getWebcam().getDevice().getResolution().width;
    }

    public int getFlash() {
        return 0;
    }

    public float getVerticalViewingAngle() {
        return 0;
    }

    public boolean isFacingBack() {
        return false;
    }

    public float getHorizontalViewingAngle() {
        return 0;
    }

    public void setFlash(int param) {
    }

    public void captureVideo() {
    }

    public boolean isStarted() {
        return started;
    }

    public void setMethod(int param) {
    }

    
    private WebcamPanel getViewInternal() {
        
        if (view == null) {
            
            //Webcam webcam = Webcam.getDefault();
            
            WebcamPanel panel = new WebcamPanel(getWebcam()) {
                @Override
                protected void paintComponent(Graphics g) {
                   if (painting) {
                        //System.out.println("Painting");
                        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
                    } else {
                        //System.out.println("Skipping");
                        if (getParent() != null) {
                            getParent().repaint();
                        }
                    }
                    
                }
                private boolean painting;
                @Override
                public void paint(Graphics g) {
                    painting = true;
                    try {
                        paintComponent(g);
                    } finally {
                        painting = false;
                    }
                }
                
                
                
            };
            panel.setOpaque(false);
            panel.setBackground(Color.red);
            //panel.setFPSDisplayed(true);
            //panel.setDisplayDebugInfo(true);
            //panel.setImageSizeDisplayed(true);
            panel.setMirrored(true);
            view = panel;
        }
        return view;
    }
    
    public PeerComponent getView() {
        if (!started) {
            
        }
        if (peerView == null) {
            System.out.println("About to get peerView");
            try {
                getViewInternal();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            
            peerView = PeerComponent.create(view);//com.codename1.impl.javase.JavaSEPort.instance.createHeavyPeer(view);
           
        }
        return peerView;
    }

    public void setPermissions(int param) {
    }

    /**
     * public static final int VIDEO_QUALITY_480P = 0;
    public static final int VIDEO_QUALITY_720P = 1;
    public static final int VIDEO_QUALITY_1080P = 2;
    public static final int VIDEO_QUALITY_2160P = 3;
    public static final int VIDEO_QUALITY_HIGHEST = 4;
    public static final int VIDEO_QUALITY_LOWEST = 5;
    public static final int VIDEO_QUALITY_QVGA = 6;
     */
    public void setVideoQuality(int param) {
        Dimension res;
        switch (param) {
            case 0:
                res = WebcamResolution.VGA.getSize();
                break;
            case 1:
                res = WebcamResolution.HD.getSize();
                break;
            case 2:
                res = WebcamResolution.FHD.getSize();
                break;
            case 3:
                res = WebcamResolution.UHD4K.getSize();
                break;
            case 4:
                res = findHighestQuality(getWebcam());
                break;
            case 5:
                res = findLowestQuality(getWebcam());
                break;
            case 6:
                res = WebcamResolution.QVGA.getSize();
                break;
                
            default:
                res = WebcamResolution.VGA.getSize();    
                        
                
        }
        getWebcam().getDevice().setResolution(res);
        
    }

    private int countPixels(Dimension dim) {
        return dim.height * dim.width;
    }
    
    private Dimension findLowestQuality(Webcam webcam) {
        Dimension min = null;
        for (Dimension res : webcam.getDevice().getResolutions()) {
            if (min == null || countPixels(res) < countPixels(min)) {
                min = res;
            }
        }
        return min;
    }
    
    private Dimension findHighestQuality(Webcam webcam) {
        Dimension max = null;
        for (Dimension res : webcam.getDevice().getResolutions()) {
            if (max == null || countPixels(res) > countPixels(max)) {
                max = res;
            }
        }
        return max;
    }

    public void setFacing(int param) {
    }

    public void setLockVideoAspectRatio(boolean param) {
        
    }

    public void setZoom(float param) {
        
    }

    public void captureVideoFile(String param) {
        
    }

    public void setVideoBitRate(int param) {
    }

    public int toggleFacing() {
        return 0;
    }

    

    public boolean isFacingFront() {
        return true;
    }

    public void setFocus(int param) {
    }

    public void captureImage() {
        BufferedImage image = webcam.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(image, ImageIO.FORMAT_JPEG, baos);
            CameraCallbacks.onImage(baos.toByteArray());
        } catch (Exception ex) {
            com.codename1.io.Log.e(ex);
            CameraCallbacks.onError(TYPE_ERROR, "Failed to capture image", ex.getMessage());
        }
        
        
    }

    public void setJpegQuality(int param) {
        jpegQuality = param;
    }

    public int getFacing() {
        return 0;
    }

    

    public void setPinchToZoom(boolean param) {
    }

    public int toggleFlash() {
        return 0;
    }

    public void stopVideo() {
    }

    public boolean isSupported() {
        return true;
    }

     /**
     * CaptureImage=0,
    CaptureVideo=1,
    Zoom=2,
    Focus=3,
    Flash=4,
    Crop=5
    HorizontalViewingAngle=6
    VerticalViewingAngle=7
    ToggleFacing=8
     */
    public boolean supportsFeature(int feature) {
        switch (feature) {
            case 0: 
                return true;
            
        }
        return false;
    }
    
    
    private static boolean cameraUsageDescriptionChecked;
    
    private static void checkCameraUsageDescription() {
        if (!cameraUsageDescriptionChecked) {
            cameraUsageDescriptionChecked = true;
            
            Map<String, String> m = Display.getInstance().getProjectBuildHints();
            if(m != null) {
                if(!m.containsKey("ios.NSCameraUsageDescription")) {
                    Display.getInstance().setProjectBuildHint("ios.NSCameraUsageDescription", "Some functionality of the application requires your camera");
                }
            }
        }
    }
    
    private static boolean microphoneUsageDescriptionChecked;
    
    private static void checkMicrophoneUsageDescription() {
        if (!microphoneUsageDescriptionChecked) {
            microphoneUsageDescriptionChecked = true;
            
            Map<String, String> m = Display.getInstance().getProjectBuildHints();
            if(m != null) {
                if(!m.containsKey("ios.NSMicrophoneUsageDescription")) {
                    Display.getInstance().setProjectBuildHint("ios.NSMicrophoneUsageDescription", "Some functionality of the application requires your microphone");
                }
            }
        }
    }
}
