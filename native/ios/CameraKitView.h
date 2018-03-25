#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>


@interface CameraKitView : UIView {
    AVCaptureVideoPreviewLayer *innerLayer;
}

-(void)layoutSubviews;
-(void)setLayer:(AVCaptureVideoPreviewLayer*)l;

@end
