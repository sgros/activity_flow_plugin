// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import java.io.IOException;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import java.util.ArrayList;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.ParsableByteArray;

final class VorbisReader extends StreamReader
{
    private VorbisUtil.CommentHeader commentHeader;
    private int previousPacketBlockSize;
    private boolean seenFirstAudioPacket;
    private VorbisUtil.VorbisIdHeader vorbisIdHeader;
    private VorbisSetup vorbisSetup;
    
    static void appendNumberOfSamples(final ParsableByteArray parsableByteArray, final long n) {
        parsableByteArray.setLimit(parsableByteArray.limit() + 4);
        parsableByteArray.data[parsableByteArray.limit() - 4] = (byte)(n & 0xFFL);
        parsableByteArray.data[parsableByteArray.limit() - 3] = (byte)(n >>> 8 & 0xFFL);
        parsableByteArray.data[parsableByteArray.limit() - 2] = (byte)(n >>> 16 & 0xFFL);
        parsableByteArray.data[parsableByteArray.limit() - 1] = (byte)(n >>> 24 & 0xFFL);
    }
    
    private static int decodeBlockSize(final byte b, final VorbisSetup vorbisSetup) {
        int n;
        if (!vorbisSetup.modes[readBits(b, vorbisSetup.iLogModes, 1)].blockFlag) {
            n = vorbisSetup.idHeader.blockSize0;
        }
        else {
            n = vorbisSetup.idHeader.blockSize1;
        }
        return n;
    }
    
    static int readBits(final byte b, final int n, final int n2) {
        return b >> n2 & 255 >>> 8 - n;
    }
    
    public static boolean verifyBitstreamType(final ParsableByteArray parsableByteArray) {
        try {
            return VorbisUtil.verifyVorbisHeaderCapturePattern(1, parsableByteArray, true);
        }
        catch (ParserException ex) {
            return false;
        }
    }
    
    @Override
    protected void onSeekEnd(final long n) {
        super.onSeekEnd(n);
        int blockSize0 = 0;
        this.seenFirstAudioPacket = (n != 0L);
        final VorbisUtil.VorbisIdHeader vorbisIdHeader = this.vorbisIdHeader;
        if (vorbisIdHeader != null) {
            blockSize0 = vorbisIdHeader.blockSize0;
        }
        this.previousPacketBlockSize = blockSize0;
    }
    
    @Override
    protected long preparePayload(final ParsableByteArray parsableByteArray) {
        final byte[] data = parsableByteArray.data;
        int n = 0;
        if ((data[0] & 0x1) == 0x1) {
            return -1L;
        }
        final int decodeBlockSize = decodeBlockSize(data[0], this.vorbisSetup);
        if (this.seenFirstAudioPacket) {
            n = (this.previousPacketBlockSize + decodeBlockSize) / 4;
        }
        final long n2 = n;
        appendNumberOfSamples(parsableByteArray, n2);
        this.seenFirstAudioPacket = true;
        this.previousPacketBlockSize = decodeBlockSize;
        return n2;
    }
    
    @Override
    protected boolean readHeaders(final ParsableByteArray parsableByteArray, final long n, final SetupData setupData) throws IOException, InterruptedException {
        if (this.vorbisSetup != null) {
            return false;
        }
        this.vorbisSetup = this.readSetupHeaders(parsableByteArray);
        if (this.vorbisSetup == null) {
            return true;
        }
        final ArrayList<byte[]> list = new ArrayList<byte[]>();
        list.add(this.vorbisSetup.idHeader.data);
        list.add(this.vorbisSetup.setupHeaderData);
        final VorbisUtil.VorbisIdHeader idHeader = this.vorbisSetup.idHeader;
        setupData.format = Format.createAudioSampleFormat(null, "audio/vorbis", null, idHeader.bitrateNominal, -1, idHeader.channels, (int)idHeader.sampleRate, list, null, 0, null);
        return true;
    }
    
    VorbisSetup readSetupHeaders(final ParsableByteArray parsableByteArray) throws IOException {
        if (this.vorbisIdHeader == null) {
            this.vorbisIdHeader = VorbisUtil.readVorbisIdentificationHeader(parsableByteArray);
            return null;
        }
        if (this.commentHeader == null) {
            this.commentHeader = VorbisUtil.readVorbisCommentHeader(parsableByteArray);
            return null;
        }
        final byte[] array = new byte[parsableByteArray.limit()];
        System.arraycopy(parsableByteArray.data, 0, array, 0, parsableByteArray.limit());
        final VorbisUtil.Mode[] vorbisModes = VorbisUtil.readVorbisModes(parsableByteArray, this.vorbisIdHeader.channels);
        return new VorbisSetup(this.vorbisIdHeader, this.commentHeader, array, vorbisModes, VorbisUtil.iLog(vorbisModes.length - 1));
    }
    
    @Override
    protected void reset(final boolean b) {
        super.reset(b);
        if (b) {
            this.vorbisSetup = null;
            this.vorbisIdHeader = null;
            this.commentHeader = null;
        }
        this.previousPacketBlockSize = 0;
        this.seenFirstAudioPacket = false;
    }
    
    static final class VorbisSetup
    {
        public final VorbisUtil.CommentHeader commentHeader;
        public final int iLogModes;
        public final VorbisUtil.VorbisIdHeader idHeader;
        public final VorbisUtil.Mode[] modes;
        public final byte[] setupHeaderData;
        
        public VorbisSetup(final VorbisUtil.VorbisIdHeader idHeader, final VorbisUtil.CommentHeader commentHeader, final byte[] setupHeaderData, final VorbisUtil.Mode[] modes, final int iLogModes) {
            this.idHeader = idHeader;
            this.commentHeader = commentHeader;
            this.setupHeaderData = setupHeaderData;
            this.modes = modes;
            this.iLogModes = iLogModes;
        }
    }
}
