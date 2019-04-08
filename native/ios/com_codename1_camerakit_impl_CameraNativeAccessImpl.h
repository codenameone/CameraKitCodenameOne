#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>
#import "CameraKitView.h"

const int FACING_BACK = 0;
const int FACING_FRONT = 1;

const int FLASH_OFF = 0;
const int FLASH_ON = 1;
const int FLASH_AUTO = 2;
const int FLASH_TORCH = 3;

const int FOCUS_OFF = 0;
const int FOCUS_CONTINUOUS = 1;
const int FOCUS_TAP = 2;
const int FOCUS_TAP_WITH_MARKER = 3;

const int METHOD_STANDARD = 0;
const int METHOD_STILL = 1;

const int VIDEO_QUALITY_480P = 0;
const int VIDEO_QUALITY_720P = 1;
const int VIDEO_QUALITY_1080P = 2;
const int VIDEO_QUALITY_2160P = 3;
const int VIDEO_QUALITY_HIGHEST = 4;
const int VIDEO_QUALITY_LOWEST = 5;
const int VIDEO_QUALITY_QVGA = 6;

@interface com_codename1_camerakit_impl_CameraNativeAccessImpl : NSObject<AVCaptureFileOutputRecordingDelegate, AVCapturePhotoCaptureDelegate> {
    int direction;
    int flash;
    int focus;
    int method;
    int videoQuality;
    int jpegQuality;
    float zoom;
    BOOL authorized;
    BOOL capturingVideo;
    CameraKitView* container;
    AVCaptureDevice* device;
    AVCaptureSession* captureSession;
    AVCaptureVideoPreviewLayer* previewLayer;
    AVCaptureMovieFileOutput* movieOutput;
    AVCapturePhotoOutput* photoOutput;
    // pre-iOS 10 API
    AVCaptureStillImageOutput* stillImageOutput;
    dispatch_queue_t globalQueue;
    BOOL initialized;
}

-(void)start;
-(void)stop;
-(BOOL)isStarted;
-(void)setMethod:(int)param;
-(void*_Nullable)getView;
-(void)setPermissions:(int)param;
-(int)getFlash;
-(void)setZoom:(float)param;
-(void)captureVideoFile:(NSString*_Nullable)param;
-(void)setFocus:(int)param;
-(void)setFlash:(int)param;
-(float)getVerticalViewingAngle;
-(float)getHorizontalViewingAngle;
-(int)getFacing;
-(BOOL)isFacingFront;
-(BOOL)isFacingBack;
-(void)setFacing:(int)param;
-(void)setPinchToZoom:(BOOL)param;
-(void)setVideoQuality:(int)param;
-(void)setVideoBitRate:(int)param;
-(void)setLockVideoAspectRatio:(BOOL)param;
-(void)setJpegQuality:(int)param;
-(void)setCropOutput:(BOOL)param;
-(int)toggleFacing;
-(int)toggleFlash;
-(void)captureImage;
-(void)captureVideo;
-(void)stopVideo;
-(int)getPreviewWidth;
-(int)getPreviewHeight;
-(int)getCaptureWidth;
-(int)getCaptureHeight;
-(BOOL)isSupported;
- (void)captureOutput:(nonnull AVCaptureFileOutput *)output didFinishRecordingToOutputFileAtURL:(nonnull NSURL *)outputFileURL fromConnections:(nonnull NSArray<AVCaptureConnection *> *)connections error:(nullable NSError *)error;
-(BOOL)supportsFeature:(int)param;
@end
