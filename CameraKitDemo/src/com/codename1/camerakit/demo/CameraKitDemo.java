package com.codename1.camerakit.demo;

import com.codename1.camerakit.CameraEvent;
import com.codename1.camerakit.CameraKit;
import com.codename1.camerakit.CameraListener;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;
import com.codename1.ui.Toolbar;
import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.LayeredLayout;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename
 * One</a> for the purpose of building native mobile applications using Java.
 */
public class CameraKitDemo {
private Form current;
    private Resources theme;
    private CameraKit ck;
    
    public void init(Object context) {
        ck = CameraKit.create();

        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

    }
    
    public void start() {
        if(ck != null && !ck.isStarted()) {
            ck.start();
        }
        if(current != null){
            current.show();
            return;
        }
        Form hi = new Form("Native Camera", new LayeredLayout());
        hi.setScrollableY(false);
        if(ck != null) {
            ck.addCameraListener(new CameraListener() {
                @Override
                public void onError(CameraEvent ev) {
                    // We currently get some errors on Android
                    Log.p(ev.getMessage() + " : " + ev.getExceptionMessage());
                }

                @Override
                public void onImage(CameraEvent ev) {
                    ToastBar.showInfoMessage("Captured image bytes");
                }

                @Override
                public void onVideo(CameraEvent ev) {
                    ToastBar.showInfoMessage("Captured video: " + ev.getFile());
                }
            });
            hi.add(ck.getView());
            Button video = new Button();
            FontImage.setMaterialIcon(video, FontImage.MATERIAL_VIDEOCAM);
            video.addActionListener(e -> {
                Boolean b = (Boolean)video.getClientProperty("capturing");
                if(b == null) {
                    video.putClientProperty("capturing", Boolean.TRUE);
                    ck.captureVideo();
                    FontImage.setMaterialIcon(video, FontImage.MATERIAL_VIDEOCAM_OFF);
                } else {
                    video.putClientProperty("capturing", null);
                    ck.stopVideo();
                    FontImage.setMaterialIcon(video, FontImage.MATERIAL_VIDEOCAM);
                }
            });
            FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_CAMERA);
            fab.bindFabToContainer(hi, CENTER, BOTTOM);
            fab.addActionListener(e -> ck.captureImage());
            
            Button toggleCamera = new Button();
            FontImage.setMaterialIcon(toggleCamera, FontImage.MATERIAL_CAMERA_FRONT);
            Button toggleFlash = new Button();
            FontImage.setMaterialIcon(toggleFlash, FontImage.MATERIAL_FLASH_ON);
            toggleCamera.addActionListener(e -> ck.toggleFacing());
            toggleFlash.addActionListener(e -> ck.toggleFlash());
            Container buttons = BoxLayout.encloseY(video, toggleCamera, toggleFlash);
            buttons.setScrollableY(true);
            hi.add(BorderLayout.east(buttons));
        } else {
            hi.add(BorderLayout.north(new SpanLabel("Loading native camera view")));
        }
        hi.show();
    }

    public void stop() {
        if(ck.isStarted()) {
            ck.stop();
        }
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }
}
