#import <Foundation/Foundation.h>
#import "CameraKitView.h"

@implementation CameraKitView

-(void)layoutSubviews {
    innerLayer.frame = self.bounds;
}

-(void)setLayer:(AVCaptureVideoPreviewLayer*)l {
    innerLayer = l;
}

@end
