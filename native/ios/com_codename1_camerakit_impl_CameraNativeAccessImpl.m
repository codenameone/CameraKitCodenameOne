#import "com_codename1_camerakit_impl_CameraNativeAccessImpl.h"
#include "com_codename1_camerakit_impl_CameraCallbacks.h"

BOOL firstTimeCameraKitLaunch = YES;
extern JAVA_OBJECT nsDataToByteArr(NSData *data);

@implementation com_codename1_camerakit_impl_CameraNativeAccessImpl

-(void)start{
    if(firstTimeCameraKitLaunch) {
        direction = FACING_BACK;
        flash = FLASH_OFF;
        focus = FOCUS_CONTINUOUS;
        method = METHOD_STANDARD;
        videoQuality = VIDEO_QUALITY_480P;
        previewLayer = nil;
        device = nil;
        photoOutput = nil;
        captureSession = nil;
        stillImageOutput = nil;
        firstTimeCameraKitLaunch = NO;
        zoom = 1;
        jpegQuality = 70;
        globalQueue =  dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0);
        [self lazyInit];
        while (!initialized) {
            usleep(20000); // 20 milliseconds
        }
    } else {
        
        dispatch_sync(globalQueue, ^{
            [captureSession startRunning];
        });
    }
}

-(void)stop{
    dispatch_sync(dispatch_get_main_queue(), ^{
        [captureSession stopRunning];
        previewLayer = nil;
    });
}

-(BOOL)isStarted{
    return previewLayer != nil;
}

-(void)setMethod:(int)param{
    self.method = param;
}
-(AVCaptureDevice*)cameraWithPosition {
    AVCaptureDevice* dev;
    if ([AVCaptureDeviceDiscoverySession class]) {
        if(direction == FACING_FRONT) {
            dev = [AVCaptureDevice defaultDeviceWithDeviceType:AVCaptureDeviceTypeBuiltInWideAngleCamera mediaType:AVMediaTypeVideo position:AVCaptureDevicePositionFront];
        } else {
            dev = [AVCaptureDevice defaultDeviceWithDeviceType:AVCaptureDeviceTypeBuiltInWideAngleCamera mediaType:AVMediaTypeVideo position:AVCaptureDevicePositionBack];
        }
    } else {
        // Pre iOS 10 code
        if(direction == FACING_FRONT) {
            for(AVCaptureDevice* d in [AVCaptureDevice devices]) {
                if(d.position == AVCaptureDevicePositionFront) {
                    dev = d;
                    break;
                }
            }
        } else {
            for(AVCaptureDevice* d in [AVCaptureDevice devices]) {
                if(d.position == AVCaptureDevicePositionBack) {
                    dev = d;
                    break;
                }
            }
        }
    }
    return dev;
}

-(void)updateDirection {
    //[previewLayer removeFromSuperlayer];
    //[container setLayer:nil];
    //[captureSession stopRunning];
    
    //[previewLayer release];
    //previewLayer = nil;
    //[captureSession release];
    //captureSession = nil;
    //[self lazyInitPostAuthorization];
    AVCaptureInput *currInput = captureSession.inputs.firstObject;
    if (currInput != nil) {
        [captureSession removeInput:currInput];
    }
    AVCaptureDevice *newCamera = [self cameraWithPosition];
    device = newCamera;
    NSError *error = nil;
    AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:newCamera error:&error];
    [captureSession addInput:input];
    [captureSession commitConfiguration];
    
}

-(void)updateVideoQuality {
    if(device == nil) {
        return;
    }
    [device lockForConfiguration:nil];
    switch(videoQuality) {
        case VIDEO_QUALITY_480P:
            captureSession.sessionPreset = AVCaptureSessionPreset640x480;
            break;
        case VIDEO_QUALITY_720P:
            captureSession.sessionPreset = AVCaptureSessionPreset1280x720;
            break;
        case VIDEO_QUALITY_LOWEST:
            captureSession.sessionPreset = AVCaptureSessionPresetLow;
            break;
        case VIDEO_QUALITY_QVGA:
            captureSession.sessionPreset = AVCaptureSessionPresetLow;
            break;
        case VIDEO_QUALITY_1080P:
            captureSession.sessionPreset = AVCaptureSessionPreset1920x1080;
            break;
        case VIDEO_QUALITY_HIGHEST:
            captureSession.sessionPreset = AVCaptureSessionPresetHigh;
            break;
            
        case VIDEO_QUALITY_2160P:
            captureSession.sessionPreset = AVCaptureSessionPreset3840x2160;
            break;
    }
    [device unlockForConfiguration];
}

-(void)updateFlash {
    if(device == nil) {
        return;
    }
    [device lockForConfiguration:nil];
    switch(flash) {
        case FLASH_ON:
            if([device isFlashModeSupported:AVCaptureFlashModeOn]) {
                [device setFlashMode:AVCaptureFlashModeOn];
            }
            break;
        case FLASH_OFF:
            if([device isFlashModeSupported:AVCaptureFlashModeOff]) {
                [device setFlashMode:AVCaptureFlashModeOff];
            }
            break;
        case FLASH_AUTO:
            if([device isFlashModeSupported:AVCaptureFlashModeAuto]) {
                [device setFlashMode:AVCaptureFlashModeAuto];
            }
            break;
    }
    [device unlockForConfiguration];
}

-(void)updateFocus {
    if(device == nil) {
        return;
    }
    [device lockForConfiguration:nil];
    switch(focus) {
        case FOCUS_OFF:
            [device setFocusMode:AVCaptureFocusModeLocked];
            break;
        case FOCUS_CONTINUOUS:
            [device setFocusMode:AVCaptureFocusModeContinuousAutoFocus];
            break;
        case FOCUS_TAP_WITH_MARKER:
        case FOCUS_TAP:
            [device setFocusMode:AVCaptureFocusModeContinuousAutoFocus];
            UITapGestureRecognizer *tapGR = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapToFocus:)];
            [tapGR setNumberOfTapsRequired:1];
            [tapGR setNumberOfTouchesRequired:1];
            [container addGestureRecognizer:tapGR];
            break;
    }
    [device unlockForConfiguration];
}

// converted from this answer: https://stackoverflow.com/a/17025083/756809
-(void)tapToFocus:(UITapGestureRecognizer *)singleTap {
    CGPoint touchPoint = [singleTap locationInView:container];
    CGPoint convertedPoint = [previewLayer captureDevicePointOfInterestForPoint:touchPoint];
    if([device isFocusPointOfInterestSupported] && [device isFocusModeSupported:AVCaptureFocusModeAutoFocus]){
        NSError *error = nil;
        [device lockForConfiguration:&error];
        if(!error){
            [device setFocusPointOfInterest:convertedPoint];
            [device setFocusMode:AVCaptureFocusModeAutoFocus];
            [device unlockForConfiguration];
        }
    }
}

-(void)updateZoom {
    if(device == nil) {
        return;
    }
    [device lockForConfiguration:nil];
    device.videoZoomFactor = MAX(1.0, MIN(zoom, device.activeFormat.videoMaxZoomFactor));
    [device unlockForConfiguration];
}

-(void)lazyInitPostAuthorization {
    // iOS 10 and later code
    device = [self cameraWithPosition];
    NSError *error = nil;
    AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:device error:&error];
    captureSession = [[AVCaptureSession alloc] init];
    [captureSession addInput:input];
    if([AVCapturePhotoOutput class]) {
        if(photoOutput == nil) {
            photoOutput = [[AVCapturePhotoOutput alloc] init];
            
        }
        if ([captureSession canAddOutput:photoOutput]) {
            [captureSession addOutput:photoOutput];
        }
    }
    else {
        if(stillImageOutput == nil) {
            stillImageOutput = [[AVCaptureStillImageOutput alloc] init];
            NSDictionary *outputSettings = [[NSDictionary alloc] initWithObjectsAndKeys: AVVideoCodecJPEG, AVVideoCodecKey, nil];
            [stillImageOutput setOutputSettings:outputSettings];
        }
        if ([captureSession canAddOutput:stillImageOutput]) {
            [captureSession addOutput:stillImageOutput];
        }
        
    }
    

    previewLayer = [AVCaptureVideoPreviewLayer layerWithSession:captureSession];
    previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
    [container setLayer:previewLayer];
    [container.layer addSublayer:previewLayer];

    [self updateFlash];
    [self updateZoom];
    [self updateFocus];
    [self updateVideoQuality];

    dispatch_sync(globalQueue, ^{
        [captureSession startRunning];
    });
}

-(void)lazyInit {
    dispatch_sync(dispatch_get_main_queue(), ^{
        container = [[CameraKitView alloc] init];
        struct ThreadLocalData* d = getThreadLocalData();
        switch ( [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo] ) {
            case AVAuthorizationStatusNotDetermined:
                [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^( BOOL granted ) {
                    initialized = YES;
                    if ( ! granted ) {
                        com_codename1_camerakit_impl_CameraCallbacks_onError___java_lang_String_java_lang_String_java_lang_String(d, fromNSString(d, @"Permission denied"), fromNSString(d, @"Camera usage was denied.  To enabled Camera functionality, please grant access to the Camera in your system settings"), fromNSString(d, @"Camera Authorization was denied"));
                        authorized = NO;
                        return;
                    }
                    authorized = YES;
                    [self lazyInitPostAuthorization];
                }];
                break;
            case AVAuthorizationStatusDenied:
            case AVAuthorizationStatusRestricted:
                initialized = YES;
                authorized = NO;
                com_codename1_camerakit_impl_CameraCallbacks_onError___java_lang_String_java_lang_String_java_lang_String(d, fromNSString(d, @"Permission denied"), fromNSString(d, @"Please grant access to use the camera in your system settings"), fromNSString(d, @"Camera Authorization was denied"));
                break;
            case AVAuthorizationStatusAuthorized:
                initialized = YES;
                authorized = YES;
                [self lazyInitPostAuthorization];
                break;
            default:
                initialized = YES;
        }
    });
}

-(void*)getView{
    return container;
}

-(void)setPermissions:(int)param{
}

-(int)getFlash{
    return flash;
}

-(void)setZoom:(float)param{
    if(zoom != param) {
        zoom = param;
        dispatch_async(dispatch_get_main_queue(), ^{
            [self updateZoom];
        });
    }
}

- (void)captureOutput:(AVCaptureFileOutput *)output didFinishRecordingToOutputFileAtURL:(NSURL *)outputFileURL
      fromConnections:(NSArray<AVCaptureConnection *> *)connections
                error:(NSError *)error {
    if(capturingVideo && outputFileURL != nil) {
        NSString* url = [outputFileURL absoluteString];
        struct ThreadLocalData* d = getThreadLocalData();
        com_codename1_camerakit_impl_CameraCallbacks_onVideo___java_lang_String(d, fromNSString(d, url));
        return;
    }
    if(error != nil) {
        struct ThreadLocalData* d = getThreadLocalData();
        com_codename1_camerakit_impl_CameraCallbacks_onError___java_lang_String_java_lang_String_java_lang_String(d, nil, nil, nil);
        return;
    }
}

-(void)captureVideoFile:(NSString*)param{
    dispatch_async(dispatch_get_main_queue(), ^{
        if(movieOutput != nil) {
            [captureSession removeOutput:movieOutput];
            [movieOutput stopRecording];
            [movieOutput release];
        }
        capturingVideo = YES;
        movieOutput = [[AVCaptureMovieFileOutput alloc] init];
        [captureSession addOutput:movieOutput];
        [movieOutput startRecordingToOutputFileURL:[NSURL URLWithString:param] recordingDelegate:self];
    });
}

-(void)setFocus:(int)param{
    if(focus != param) {
        focus = param;
        dispatch_async(dispatch_get_main_queue(), ^{
            [self updateFocus];
        });
    }
}

-(void)setFlash:(int)param{
    if(flash != param) {
        flash = param;
        dispatch_async(dispatch_get_main_queue(), ^{
            [self updateFlash];
        });
    }
}

-(float)getVerticalViewingAngle{
    __block float fov = 0;
    dispatch_sync(dispatch_get_main_queue(), ^{
        fov = device.activeFormat.videoFieldOfView / 16.0 * 9;
    });
    return fov;
}

-(float)getHorizontalViewingAngle{
    __block float fov = 0;
    dispatch_sync(dispatch_get_main_queue(), ^{
        fov = device.activeFormat.videoFieldOfView;
    });
    return fov;
}

-(int)getFacing{
    return direction;
}

-(BOOL)isFacingFront{
    return direction == FACING_FRONT;
}

-(BOOL)isFacingBack{
    return direction == FACING_BACK;
}

-(void)setFacing:(int)param{
    if(direction != param) {
        direction = param;
        dispatch_async(dispatch_get_main_queue(), ^{
            [self updateDirection];
        });
    }
}

-(void)setPinchToZoom:(BOOL)param{
}

-(void)setVideoQuality:(int)param{
    if(param != videoQuality) {
        videoQuality = param;
        dispatch_async(dispatch_get_main_queue(), ^{
            [self updateVideoQuality];
        });
    }
}

-(void)setVideoBitRate:(int)param{
}

-(void)setLockVideoAspectRatio:(BOOL)param{
}

-(void)setJpegQuality:(int)param{
    jpegQuality = param;
}

-(void)setCropOutput:(BOOL)param{
}

-(int)toggleFacing{
    // not needed as I moved this to the Java layer
    return 0;
}

-(int)toggleFlash{
    // not needed as I moved this to the Java layer
    return 0;
}

-(void)captureOutput:(AVCapturePhotoOutput *)captureOutput didFinishProcessingPhotoSampleBuffer:(CMSampleBufferRef)photoSampleBuffer previewPhotoSampleBuffer:(CMSampleBufferRef)previewPhotoSampleBuffer resolvedSettings:(AVCaptureResolvedPhotoSettings *)resolvedSettings bracketSettings:(AVCaptureBracketedStillImageSettings *)bracketSettings error:(NSError *)error {
    if(error == nil && !capturingVideo) {
        NSData *imageData = [AVCapturePhotoOutput JPEGPhotoDataRepresentationForJPEGSampleBuffer:photoSampleBuffer previewPhotoSampleBuffer:previewPhotoSampleBuffer];
        UIImage *image = [UIImage imageWithData:imageData];
        if (image.imageOrientation != UIImageOrientationUp) {
            UIGraphicsBeginImageContextWithOptions(image.size, NO, image.scale);
            [image drawInRect:(CGRect){0, 0, image.size}];
            image = UIGraphicsGetImageFromCurrentImageContext();
            UIGraphicsEndImageContext();
            imageData = UIImageJPEGRepresentation(image, jpegQuality / 100.0f);
            
        }
        JAVA_OBJECT byteArray = nsDataToByteArr(imageData);
        com_codename1_camerakit_impl_CameraCallbacks_onImage___byte_1ARRAY(getThreadLocalData(), byteArray);
        return;
    }
    
    if(error) {
        struct ThreadLocalData* d = getThreadLocalData();
        com_codename1_camerakit_impl_CameraCallbacks_onError___java_lang_String_java_lang_String_java_lang_String(d, nil, nil, nil);
        return;
    }
}


-(void)captureImage{
    dispatch_async(dispatch_get_main_queue(), ^{
        // iOS 10 and later code
        capturingVideo = NO;
        if([AVCapturePhotoOutput class]) {
            AVCapturePhotoSettings* settings = [AVCapturePhotoSettings photoSettings];
            [photoOutput capturePhotoWithSettings:settings delegate:self];
        } else {
            // based on https://stackoverflow.com/a/20296861/756809
            
            AVCaptureConnection *videoConnection = nil;
            for (AVCaptureConnection *connection in stillImageOutput.connections) {
                for (AVCaptureInputPort *port in [connection inputPorts]) {
                    if ([[port mediaType] isEqual:AVMediaTypeVideo] ) {
                        videoConnection = connection;
                        break;
                    }
                }
                if (videoConnection){
                    break;
                }
            }
            [stillImageOutput captureStillImageAsynchronouslyFromConnection:videoConnection completionHandler:^(CMSampleBufferRef imageSampleBuffer, NSError *error) {
                if (error == nil) {
                    NSData *imageData = [AVCaptureStillImageOutput jpegStillImageNSDataRepresentation:imageSampleBuffer];
                    UIImage *image = [UIImage imageWithData:imageData];
                    if (image.imageOrientation != UIImageOrientationUp) {
                        UIGraphicsBeginImageContextWithOptions(image.size, NO, image.scale);
                        [image drawInRect:(CGRect){0, 0, image.size}];
                        image = UIGraphicsGetImageFromCurrentImageContext();
                        UIGraphicsEndImageContext();
                        imageData = UIImageJPEGRepresentation(image, jpegQuality / 100.0f);
                        
                    }
                    JAVA_OBJECT byteArray = nsDataToByteArr(imageData);
                    com_codename1_camerakit_impl_CameraCallbacks_onImage___byte_1ARRAY(getThreadLocalData(), byteArray);

                } else {
                    struct ThreadLocalData* d = getThreadLocalData();
                com_codename1_camerakit_impl_CameraCallbacks_onError___java_lang_String_java_lang_String_java_lang_String(d, nil, nil, nil);
                }
            }];
        }
    });
}

-(void)captureVideo{
    NSURL *furl = [NSURL fileURLWithPath:[NSTemporaryDirectory() stringByAppendingPathComponent:@"temp.mov"]];
    [self captureVideoFile:[furl absoluteString]];
}

-(void)stopVideo{
    dispatch_async(dispatch_get_main_queue(), ^{
        [movieOutput stopRecording];
        [captureSession removeOutput:movieOutput];
        [movieOutput release];
        movieOutput = nil;
    });
}

-(int)getPreviewWidth{
    return (int)previewLayer.frame.size.width;
}

-(int)getPreviewHeight{
    return (int)previewLayer.frame.size.height;
}

-(int)getCaptureWidth{
    return 0;
}

-(int)getCaptureHeight{
    return 0;
}

-(BOOL)isSupported{
    return YES;
}

-(BOOL)supportsFeature:(int)param {
    switch (param) {
        case 0: 
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
            return YES;

    }
    return NO;
}
@end
