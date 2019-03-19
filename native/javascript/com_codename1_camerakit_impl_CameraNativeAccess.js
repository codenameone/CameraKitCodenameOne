(function(exports){
    
function hasGetUserMedia() {
  return !!(navigator.mediaDevices && navigator.mediaDevices.getUserMedia);
}

function updateStream(o, callback) {
    
    if (o.stream && o.constraints && o.constraints.video) {
        try {
            o.stream.getVideoTracks()[0].applyConstraints(o.constraints.video).then(function(){
                callback.complete(null);
            }).catch(function(reason){
                callback.error(new Error(reason));
            });
            
        } catch (e) {
            callback.error(e);
        }
    }
}

function updateAdvanced(o) {
    getVideoConstraints(o).advanced = [];
    if (o.flashMode === 3) {
        getVideoConstraints(o).advanced.push({torch:true});
    }
}

function getConstraints(o) {
    o.constraints = o.constraints || {};
    return o.constraints;
}

function getVideoConstraints(o) {
    var cnst = getConstraints(o);
    cnst.video = cnst.video || {};
    return cnst.video;
}

function getStream(o, callback) {
    try {
        if (o.stream) {
            o.stream.getTracks().forEach(function(track) {
              track.stop();
            });
        }
    
      
    
      navigator.mediaDevices.getUserMedia(o.constraints).
        then(function(stream) {
            o.stream = stream;
            o.el.srcObject = stream;
            callback.complete(stream);
        }).catch(function(error) {
            callback.error(error);
        });
    } catch (e) {
        callback.error(e);
    }
}
var defaultCaptureWidth = 640;
var defaultCaptureHeight = 480;
var o = {
    jpegQuality: 0.7
};

    o.start_ = function(callback) {
        if (!hasGetUserMedia()) {
            callback.error(new Error("This browser does not support user media."));
            return;
        }
        this.started = true;
        this.el = document.createElement('video');
        this.el.setAttribute('autoplay', '');
        this.el.setAttribute("playsinline", true);
        this.el.setAttribute("controls", true);
        var self = this;
        setTimeout(function() {
            self.el.removeAttribute("controls");
        });
        this.el.muted = true;
        this.constraints = {
            audio: true,
            video: {width: 640, height: 480, facingMode:'environment'}
        };
        getStream(this, {
            complete: function(stream) {
                
                callback.complete(null);
            },
            error: function(error) {
                callback.error(error);
            }
            
        });
        
    };

    o.stop_ = function(callback) {
        if (!hasGetUserMedia()) {
            callback.error(new Error("This browser does not support user media."));
            return;
        }
        if (this.stream) {
            this.stream.getTracks().forEach(function (track) {
                track.stop();
            });
        }
        callback.complete(null);
    };

    o.setCropOutput__boolean = function(param1, callback) {
        console.log("Crop Output not supported on this platform");
        callback.complete(null);
    };

    o.getPreviewHeight_ = function(callback) {
        callback.complete(this.el.videoWidth);
    };
    
    o.getPreviewWidth_ = function(callback) {
        callback.complete(this.el.videoHeight);
    };

    o.getFlash_ = function(callback) {
        callback.complete(this.flashMode || 0);
    };

    o.getVerticalViewingAngle_ = function(callback) {
        callback.complete(0);
    };

    o.isFacingBack_ = function(callback) {
        callback.complete(getVideoConstraints(this).facingMode === 'environment');
    };

    o.getHorizontalViewingAngle_ = function(callback) {
        callback.complete(null);
    };

    /*
     * public static final int FLASH_OFF = 0;
    public static final int FLASH_ON = 1;
    public static final int FLASH_AUTO = 2;
    public static final int FLASH_TORCH = 3;
     */
    o.setFlash__int = function(param1, callback) {
        this.flashMode = param1;
        updateAdvanced(this);
        updateStream(this, {
            complete: function() {
                callback.complete(null);
            },
            error: function(error) {
                callback.error(error);
            }
        });
        
    };
    
    /*
     *
    CaptureImage=0
    CaptureVideo=1,
    Zoom=2,
    Focus=3,
    Flash=4,
    Crop=5,
    HorizontalViewingAngle=6,
    VerticalViewingAngle=7,
    ToggleFacing=8
     */
    o.supportsFeature__int = function(param1, callback) {
        try {
            var constraints = navigator.mediaDevices.getSupportedConstraints();
            switch (param1) {
                case 0:
                    callback.complete(true);
                    return;
                case 1:
                    callback.complete('MediaRecorder' in window);
                    return;
                case 8:
                    callback.complete('facingMode' in constraints);
                    return;
    
            }
            callback.complete(false);
        } catch (e) {
            callback.error(e);
        }
    };

    

    o.isStarted_ = function(callback) {
        callback.complete(this.isStarted);
    };

    o.setMethod__int = function(param1, callback) {
        this.constraints = this.constraints || {};
        
        callback.complete(null);
    };

    o.getView_ = function(callback) {
        callback.complete(this.el);
    };

    o.setPermissions__int = function(param1, callback) {
        callback.complete(null);
    };

/**
 * public static final int VIDEO_QUALITY_480P = 0;
    public static final int VIDEO_QUALITY_720P = 1;
    public static final int VIDEO_QUALITY_1080P = 2;
    public static final int VIDEO_QUALITY_2160P = 3;
    public static final int VIDEO_QUALITY_HIGHEST = 4;
    public static final int VIDEO_QUALITY_LOWEST = 5;
    public static final int VIDEO_QUALITY_QVGA = 6;
 */
    o.setVideoQuality__int = function(param1, callback) {
        var size = {};
        switch (param1) {
            case 0:
                size.width=640;
                size.height=480;
                break;
            case 1:
                size.width=1280;
                size.height=720;
                break;
            case 2:
                size.width=1920;
                size.height=1080;
                break;
            case 3:
                size.width=3840;
                size.height=2160;
                break;
            case 4:
                size.width={ideal:4096};
                size.height={ideal:2160};
                break;
            case 5:
                size.width=320;
                size.height=240;
                break;
            case 6:
                size.width=320;
                size.height=240;
                break;
                        
        }
        getVideoConstraints(this).width = size.width;
        getVideoConstraints(this).height = size.height;
        updateStream(this, {
            complete: function() {
                callback.complete();
            },
            error: function(error) {
                callback.error(error);
            }
        });

    };

    

    /*
     * public static final int FACING_BACK = 0;
    public static final int FACING_FRONT = 1;
     */
    o.setFacing__int = function(param1, callback) {
        getVideoConstraints(this).facingMode = param1 == 1 ? 'user' : 'environment';
        getStream(this, {
            complete: function() {
                callback.complete(null);
            },
            error: function(error) {
                callback.error(error);
            }
        });
    };

    o.setLockVideoAspectRatio__boolean = function(param1, callback) {
        console.log("Lock Video Aspect Ratio not supported on this platform");
        callback.complete(null);
    };

    o.setZoom__float = function(param1, callback) {
        getVideoConstraints(this).zoom = param1;
        updateStream(this, {
            complete: function() {
                callback.complete(null);
            },
            error: function(error) {
                callback.error(error);
            }
        });
    };

    o.captureVideo_ = function(callback) {
        try {
            this.mediaRecorder = new MediaRecorder(this.stream);
            this.recordedChunks = [];
            var self = this;
            self.stopped = false;
            self.shouldStop = false;
            this.mediaRecorder.addEventListener('dataavailable', function (e) {
                if (e.data.size > 0) {
                    self.recordedChunks.push(e.data);
                }

                //if (self.shouldStop === true && self.stopped === false) {
                //    self.mediaRecorder.stop();
                //    self.stopped = true;
                //}
                self.stopped = true;
            });
            
            var fireOnVideo = this.$GLOBAL$.com_codename1_camerakit_impl_CameraCallbacks.onVideo__java_lang_String$async;
            this.mediaRecorder.addEventListener('stop', function () {
                var blob = new Blob(self.recordedChunks);
                self.recordedChunks = null;
                self.mediaRecorder = null;
                self.shouldStop = false;
                var savePath = self.videoFile;
                self.videoFile = null;
                
                window.saveBlobToFile(blob, savePath || '', {
                    complete : function(path) {
                        fireOnVideo(path);
                    },
                    error : function(msg) {
                        console.log('Failed to save blob to file.', msg);
                    }
                });
                
            });
            console.log("Media recorder starting");
            this.mediaRecorder.start();
            callback.complete(null);
        } catch (e){
            console.log(e);
            callback.error(e);
        }
    };

    o.captureVideoFile__java_lang_String = function(param1, callback) {
        this.videoFile = param1;
        this.captureVideo_(callback);
    };

    o.setVideoBitRate__int = function(param1, callback) {
        console.log("Set video bitrate is not supported on this platform");
        callback.complete(null);
    };

    o.toggleFacing_ = function(callback) {
        callback.complete(0);
    };

    o.getCaptureHeight_ = function(callback) {
        callback.complete(this.el.videoHeight);
    };

    o.isFacingFront_ = function(callback) {
        callback.complete(getVideoConstraints(this).facingMode === 'user');
    };

    o.setFocus__int = function(param1, callback) {
        console.log("setFocus is not supported on this platform");
        callback.complete(null);
    };

    o.captureImage_ = function(callback) {
        try {
            var canvas = document.createElement('canvas');
            
            canvas.setAttribute('width', this.el.videoWidth);
            canvas.setAttribute('height', this.el.videoHeight);
            var ctx = canvas.getContext('2d');
            ctx.drawImage(this.el, 0, 0, canvas.width, canvas.height);
            var fireOnImage = this.$GLOBAL$.com_codename1_camerakit_impl_CameraCallbacks.onImage__byte_1ARRAY$async;
            canvas.toBlob(function(blob){
                var reader = new FileReader();
                reader.onload = function(event) {
                    var buf = event.target.result;
                    var arr = new Uint8Array(buf);
                    fireOnImage(arr);
                };
                reader.readAsArrayBuffer(blob);
            }, 'image/jpeg', this.jpegQuality || 0.7);
            //var imageData = ctx.createImageData(canvas.width, canvas.height);
            //
            //var bytes = new Uint8Array(imageData.data);
            //fireOnImage(imageData.data);
            //setTimeout(function(){fireOnImage(bytes)}, 1);
            callback.complete(null);
        } catch (e) {
            callback.error(e);
        }
    };

    o.setJpegQuality__int = function(param1, callback) {
        this.jpegQuality = param1/100.0;
        callback.complete(null);
    };

    o.getFacing_ = function(callback) {
        callback.complete(getVideoConstraints(this).facingMode === 'environment' ? 0 : 1);
    };

    o.getCaptureWidth_ = function(callback) {
        callback.complete(this.el.videoWidth);
    };

    o.setPinchToZoom__boolean = function(param1, callback) {
        console.log("Pinch zoom is not supported on this platform");
        callback.complete(null);
    };

    o.toggleFlash_ = function(callback) {
        callback.complete(0);
    };

    o.stopVideo_ = function(callback) {
        this.shouldStop = true;
        if (this.mediaRecorder) {
            this.mediaRecorder.stop();
        }
        callback.complete(null);
    };

    o.isSupported_ = function(callback) {
        var supported = ('mediaDevices' in navigator && 'getUserMedia' in navigator.mediaDevices);
        callback.complete(supported);
    };

exports.com_codename1_camerakit_impl_CameraNativeAccess= o;

})(cn1_get_native_interfaces());
