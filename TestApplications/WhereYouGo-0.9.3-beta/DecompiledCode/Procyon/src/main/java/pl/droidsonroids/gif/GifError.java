// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import java.util.Locale;
import android.support.annotation.NonNull;

public enum GifError
{
    CLOSE_FAILED(110, "Failed to close given input"), 
    DATA_TOO_BIG(108, "Number of pixels bigger than width * height"), 
    EOF_TOO_SOON(113, "Image EOF detected before image complete"), 
    IMAGE_DEFECT(112, "Image is defective, decoding aborted"), 
    IMG_NOT_CONFINED(1003, "Image size exceeds screen size"), 
    INVALID_BYTE_BUFFER(1005, "Invalid and/or indirect byte buffer specified"), 
    @Deprecated
    INVALID_IMG_DIMS(1002, "Invalid image size, dimensions must be positive"), 
    INVALID_SCR_DIMS(1001, "Invalid screen size, dimensions must be positive"), 
    NOT_ENOUGH_MEM(109, "Failed to allocate required memory"), 
    NOT_GIF_FILE(103, "Data is not in GIF format"), 
    NOT_READABLE(111, "Given file was not opened for read"), 
    NO_COLOR_MAP(106, "Neither global nor local color map found"), 
    NO_ERROR(0, "No error"), 
    NO_FRAMES(1000, "No frames found, at least one frame required"), 
    NO_IMAG_DSCR(105, "No image descriptor detected"), 
    NO_SCRN_DSCR(104, "No screen descriptor detected"), 
    OPEN_FAILED(101, "Failed to open given input"), 
    READ_FAILED(102, "Failed to read from given input"), 
    REWIND_FAILED(1004, "Input source rewind failed, animation stopped"), 
    UNKNOWN(-1, "Unknown error"), 
    WRONG_RECORD(107, "Wrong record type detected");
    
    @NonNull
    public final String description;
    int errorCode;
    
    private GifError(final int errorCode, final String description) {
        this.errorCode = errorCode;
        this.description = description;
    }
    
    static GifError fromCode(final int errorCode) {
        for (final GifError unknown : values()) {
            if (unknown.errorCode == errorCode) {
                return unknown;
            }
        }
        GifError unknown = GifError.UNKNOWN;
        unknown.errorCode = errorCode;
        return unknown;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    String getFormattedDescription() {
        return String.format(Locale.ENGLISH, "GifError %d: %s", this.errorCode, this.description);
    }
}
