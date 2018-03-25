package com.codename1.camerakit.impl;

import com.wonderkiln.camerakit.*;
import com.codename1.impl.android.AndroidNativeUtil;
import com.codename1.impl.android.AndroidImplementation;
import java.io.File;

public class CameraNativeAccessImpl {
    private CameraView view;
    private CameraKitEventListener listener = new CameraKitEventListener() {
            public void onEvent(CameraKitEvent event) {
            }
            public void onError(CameraKitError error) {
                String errorMessage = "";
                if(error.getException() != null) {
                    errorMessage = error.getException().toString();
                    com.codename1.io.Log.e(error.getException());
                    com.codename1.io.Log.sendLogAsync();
                }
                CameraCallbacks.onError(error.getType(), error.getMessage(), errorMessage);
            }
            public void onImage(CameraKitImage image) {
                CameraCallbacks.onImage(image.getJpeg());                
            }
            public void onVideo(CameraKitVideo video) {
                CameraCallbacks.onVideo("file://" + video.getVideoFile().getAbsolutePath());                
            }
    };
    
    public void start() {
        AndroidImplementation.runOnUiThreadAndBlock(new Runnable() {
            @Override
            public void run() {
                if(view == null) {
                    view = new CameraView(AndroidNativeUtil.getContext());
                    view.addCameraKitListener(listener);
                }
                view.start();
            }
        });
    }

    public void stop() {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.stop();
            }
        });
    }

    public boolean isStarted() {
        if(view == null) {
            return false;
        }
        return view.isStarted();
    }

    public void setMethod(final int param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setMethod(param);
            }
        });
    }

    public android.view.View getView() {
        return view;
    }

    public void setPermissions(final int param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setPermissions(param);
            }
        });
    }

    public int getFlash() {
        return view.getFlash();
    }

    public void setZoom(final float param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setZoom(param);
            }
        });
    }

    public void captureVideoFile(final String param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                String f = param;
                if (param.startsWith("file://")) {
                   f = param.substring(7);
                }
                view.captureVideo(new File(f));
            }
        });
    }

    public void setFocus(final int param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setFocus(param);
            }
        });
    }

    public void setFlash(final int param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setFlash(param);
            }
        });
    }

    public float getVerticalViewingAngle() {
        return view.getCameraProperties().verticalViewingAngle;
    }

    public float getHorizontalViewingAngle() {
        return view.getCameraProperties().horizontalViewingAngle;
    }

    public int getFacing() {
        return view.getFacing();
    }

    public boolean isFacingFront() {
        return view.isFacingFront();
    }

    public boolean isFacingBack() {
        return view.isFacingBack();
    }

    public void setFacing(final int param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setFacing(param);
            }
        });
    }

    public void setPinchToZoom(final boolean param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setPinchToZoom(param);
            }
        });
    }

    public void setVideoQuality(final int param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setVideoQuality(param);
            }
        });
    }

    public void setVideoBitRate(final int param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setVideoBitRate(param);
            }
        });
    }

    public void setLockVideoAspectRatio(final boolean param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setLockVideoAspectRatio(param);
            }
        });
    }

    public void setJpegQuality(final int param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setJpegQuality(param);
            }
        });
    }

    public void setCropOutput(final boolean param) {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.setCropOutput(param);
            }
        });
    }

    public int toggleFacing() {
        return view.toggleFacing();
    }

    public int toggleFlash() {
        return view.toggleFlash();
    }

    public void captureImage() {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.captureImage();
            }
        });        
    }

    public void captureVideo() {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.captureVideo();
            }
        });        
    }

    public void stopVideo() {
        AndroidNativeUtil.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                view.stopVideo();
            }
        });        
    }

    public int getPreviewWidth() {
        return view.getPreviewSize().getWidth();
    }

    public int getPreviewHeight() {
        return view.getPreviewSize().getHeight();
    }

    public int getCaptureWidth() {
        return view.getCaptureSize().getWidth();
    }

    public int getCaptureHeight() {
        return view.getCaptureSize().getHeight();
    }

    public boolean isSupported() {
        return true;
    }

}
