// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.video;

import android.media.MediaFormat;
import android.media.MediaCodec$BufferInfo;
import java.util.ArrayList;
import com.googlecode.mp4parser.util.Matrix;
import java.io.File;

public class Mp4Movie
{
    private File cacheFile;
    private int height;
    private Matrix matrix;
    private ArrayList<Track> tracks;
    private int width;
    
    public Mp4Movie() {
        this.matrix = Matrix.ROTATE_0;
        this.tracks = new ArrayList<Track>();
    }
    
    public void addSample(final int index, final long n, final MediaCodec$BufferInfo mediaCodec$BufferInfo) {
        if (index >= 0) {
            if (index < this.tracks.size()) {
                this.tracks.get(index).addSample(n, mediaCodec$BufferInfo);
            }
        }
    }
    
    public int addTrack(final MediaFormat mediaFormat, final boolean b) {
        final ArrayList<Track> tracks = this.tracks;
        tracks.add(new Track(tracks.size(), mediaFormat, b));
        return this.tracks.size() - 1;
    }
    
    public File getCacheFile() {
        return this.cacheFile;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public Matrix getMatrix() {
        return this.matrix;
    }
    
    public ArrayList<Track> getTracks() {
        return this.tracks;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setCacheFile(final File cacheFile) {
        this.cacheFile = cacheFile;
    }
    
    public void setRotation(final int n) {
        if (n == 0) {
            this.matrix = Matrix.ROTATE_0;
        }
        else if (n == 90) {
            this.matrix = Matrix.ROTATE_90;
        }
        else if (n == 180) {
            this.matrix = Matrix.ROTATE_180;
        }
        else if (n == 270) {
            this.matrix = Matrix.ROTATE_270;
        }
    }
    
    public void setSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
}
