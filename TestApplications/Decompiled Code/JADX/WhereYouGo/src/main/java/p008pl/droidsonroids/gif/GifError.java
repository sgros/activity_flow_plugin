package p008pl.droidsonroids.gif;

import android.support.annotation.NonNull;
import android.support.p000v4.view.PointerIconCompat;
import java.util.Locale;
import locus.api.objects.geocaching.GeocachingData;

/* renamed from: pl.droidsonroids.gif.GifError */
public enum GifError {
    NO_ERROR(0, "No error"),
    OPEN_FAILED(101, "Failed to open given input"),
    READ_FAILED(102, "Failed to read from given input"),
    NOT_GIF_FILE(103, "Data is not in GIF format"),
    NO_SCRN_DSCR(104, "No screen descriptor detected"),
    NO_IMAG_DSCR(GeocachingData.CACHE_SOURCE_OPENCACHING_NL, "No image descriptor detected"),
    NO_COLOR_MAP(GeocachingData.CACHE_SOURCE_OPENCACHING_PL, "Neither global nor local color map found"),
    WRONG_RECORD(GeocachingData.CACHE_SOURCE_OPENCACHING_RO, "Wrong record type detected"),
    DATA_TOO_BIG(GeocachingData.CACHE_SOURCE_OPENCACHING_UK, "Number of pixels bigger than width * height"),
    NOT_ENOUGH_MEM(109, "Failed to allocate required memory"),
    CLOSE_FAILED(110, "Failed to close given input"),
    NOT_READABLE(111, "Given file was not opened for read"),
    IMAGE_DEFECT(112, "Image is defective, decoding aborted"),
    EOF_TOO_SOON(113, "Image EOF detected before image complete"),
    NO_FRAMES(1000, "No frames found, at least one frame required"),
    INVALID_SCR_DIMS(PointerIconCompat.TYPE_CONTEXT_MENU, "Invalid screen size, dimensions must be positive"),
    INVALID_IMG_DIMS(PointerIconCompat.TYPE_HAND, "Invalid image size, dimensions must be positive"),
    IMG_NOT_CONFINED(PointerIconCompat.TYPE_HELP, "Image size exceeds screen size"),
    REWIND_FAILED(PointerIconCompat.TYPE_WAIT, "Input source rewind failed, animation stopped"),
    INVALID_BYTE_BUFFER(1005, "Invalid and/or indirect byte buffer specified"),
    UNKNOWN(-1, "Unknown error");
    
    @NonNull
    public final String description;
    int errorCode;

    private GifError(int code, @NonNull String description) {
        this.errorCode = code;
        this.description = description;
    }

    static GifError fromCode(int code) {
        for (GifError err : GifError.values()) {
            if (err.errorCode == code) {
                return err;
            }
        }
        GifError unk = UNKNOWN;
        unk.errorCode = code;
        return unk;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    /* Access modifiers changed, original: 0000 */
    public String getFormattedDescription() {
        return String.format(Locale.ENGLISH, "GifError %d: %s", new Object[]{Integer.valueOf(this.errorCode), this.description});
    }
}
