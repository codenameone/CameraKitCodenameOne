![Camera Kit Codename One](https://raw.githubusercontent.com/codenameone/CameraKitCodenameOne/master/camera-kit-banner.jpg "Camera Kit Codename One")

Camera Kit is a cross platform API for low level camera access. It's based on https://github.com/CameraKit/camerakit-android/[Camera Kit Android] and uses it for the Android version of this API. In iOS the implementation is based directly on the native OS API's.

**Important** not all API's are implemented in a cross platform way at this time as this cn1lib is highly experimental. Basic things might not work...

## Basic Usage

````java
CameraKit ck = CameraKit.create();

// will happen in unsupported platforms such as the simulator!
if(ck != null) {
  Form hi = new Form("Native Camera", new LayeredLayout());
  hi.setScrollableY(false);
  ck.addCameraListener(new CameraListener() {
      @Override
      public void onError(CameraEvent ev) {
          // handle camera errors
      }

      @Override
      public void onImage(CameraEvent ev) {
          // Callback when a photo is captured
          byte[] jpegData = ev.getJpeg();
      }

      @Override
      public void onVideo(CameraEvent ev) {
          // Callback when a photo is captured
          String videoFile = ev.getFile();
      }
  });
  hi.add(ck.getView());
  Button grabPhoto = new Button("Picture");
  grabPhoto.addActionListener(e -> ck.captureImage());
  Button video = new Button("Video");
  video.addActionListener(e -> {
      Boolean b = (Boolean)video.getClientProperty("capturing");
      if(b == null) {
          video.putClientProperty("capturing", Boolean.TRUE);
          ck.captureVideo();
          video.setText("Stop");
      } else {
          video.putClientProperty("capturing", null);
          ck.stopVideo();
          video.setText("Video");
      }
  });
  Button toggleCamera = new Button("Toggle Camera");
  Button toggleFlash = new Button("Toggle Flash");
  toggleCamera.addActionListener(e -> ck.toggleFacing());
  toggleFlash.addActionListener(e -> ck.toggleFlash());
  Container buttons = BoxLayout.encloseX(grabPhoto, video, toggleCamera, toggleFlash);
  buttons.setScrollableX(true);
  hi.add(BorderLayout.south(buttons));
  hi.show();
}
````

## Caveats

If used in the first form of the application it is recommended that you have a splash screen at least for the first time the application loads. This is essential for iOS where [screenshots are generated for the splash screens](https://www.codenameone.com/manual/appendix-ios.html).
