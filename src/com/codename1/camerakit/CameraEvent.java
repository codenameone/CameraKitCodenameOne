/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.camerakit;

/**
 *
 * @author shai
 */
public class CameraEvent {
    private String type;
    private String message;
    private String exceptionMessage;
    private byte[] jpeg;
    private String file;
    
    public CameraEvent(String type, String message, String exceptionMessage) {
        this.type = type;
        this.message = message;
        this.exceptionMessage = exceptionMessage;
    }
    
    public CameraEvent(byte[] jpeg) {
        this.jpeg = jpeg;
    }
    
    public CameraEvent(String file) {
        this.file = file;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the exceptionMessage
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * @return the jpeg
     */
    public byte[] getJpeg() {
        return jpeg;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    
}
