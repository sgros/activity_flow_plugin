// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.nio.ShortBuffer;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Assertions;

final class Sonic
{
    private final int channelCount;
    private final short[] downSampleBuffer;
    private short[] inputBuffer;
    private int inputFrameCount;
    private final int inputSampleRateHz;
    private int maxDiff;
    private final int maxPeriod;
    private final int maxRequiredFrameCount;
    private int minDiff;
    private final int minPeriod;
    private int newRatePosition;
    private int oldRatePosition;
    private short[] outputBuffer;
    private int outputFrameCount;
    private final float pitch;
    private short[] pitchBuffer;
    private int pitchFrameCount;
    private int prevMinDiff;
    private int prevPeriod;
    private final float rate;
    private int remainingInputToCopyFrameCount;
    private final float speed;
    
    public Sonic(int maxRequiredFrameCount, final int channelCount, final float speed, final float pitch, final int n) {
        this.inputSampleRateHz = maxRequiredFrameCount;
        this.channelCount = channelCount;
        this.speed = speed;
        this.pitch = pitch;
        this.rate = maxRequiredFrameCount / (float)n;
        this.minPeriod = maxRequiredFrameCount / 400;
        this.maxPeriod = maxRequiredFrameCount / 65;
        this.maxRequiredFrameCount = this.maxPeriod * 2;
        maxRequiredFrameCount = this.maxRequiredFrameCount;
        this.downSampleBuffer = new short[maxRequiredFrameCount];
        this.inputBuffer = new short[maxRequiredFrameCount * channelCount];
        this.outputBuffer = new short[maxRequiredFrameCount * channelCount];
        this.pitchBuffer = new short[maxRequiredFrameCount * channelCount];
    }
    
    private void adjustRate(final float n, int n2) {
        if (this.outputFrameCount == n2) {
            return;
        }
        int inputSampleRateHz;
        int n3;
        for (inputSampleRateHz = this.inputSampleRateHz, n3 = (int)(inputSampleRateHz / n); n3 > 16384 || inputSampleRateHz > 16384; n3 /= 2, inputSampleRateHz /= 2) {}
        this.moveNewSamplesToPitchBuffer(n2);
        n2 = 0;
        int pitchFrameCount;
        while (true) {
            pitchFrameCount = this.pitchFrameCount;
            boolean b = true;
            if (n2 >= pitchFrameCount - 1) {
                break;
            }
            int oldRatePosition;
            int newRatePosition;
            while (true) {
                oldRatePosition = this.oldRatePosition;
                newRatePosition = this.newRatePosition;
                if ((oldRatePosition + 1) * n3 <= newRatePosition * inputSampleRateHz) {
                    break;
                }
                this.outputBuffer = this.ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, 1);
                int n4 = 0;
                while (true) {
                    final int channelCount = this.channelCount;
                    if (n4 >= channelCount) {
                        break;
                    }
                    this.outputBuffer[this.outputFrameCount * channelCount + n4] = this.interpolate(this.pitchBuffer, channelCount * n2 + n4, inputSampleRateHz, n3);
                    ++n4;
                }
                ++this.newRatePosition;
                ++this.outputFrameCount;
            }
            this.oldRatePosition = oldRatePosition + 1;
            if (this.oldRatePosition == inputSampleRateHz) {
                this.oldRatePosition = 0;
                if (newRatePosition != n3) {
                    b = false;
                }
                Assertions.checkState(b);
                this.newRatePosition = 0;
            }
            ++n2;
        }
        this.removePitchFrames(pitchFrameCount - 1);
    }
    
    private void changeSpeed(final float n) {
        final int inputFrameCount = this.inputFrameCount;
        if (inputFrameCount < this.maxRequiredFrameCount) {
            return;
        }
        int n2 = 0;
        int n4;
        do {
            int n3;
            if (this.remainingInputToCopyFrameCount > 0) {
                n3 = this.copyInputToOutput(n2);
            }
            else {
                final int pitchPeriod = this.findPitchPeriod(this.inputBuffer, n2);
                if (n > 1.0) {
                    n3 = pitchPeriod + this.skipPitchPeriod(this.inputBuffer, n2, n, pitchPeriod);
                }
                else {
                    n3 = this.insertPitchPeriod(this.inputBuffer, n2, n, pitchPeriod);
                }
            }
            n4 = (n2 += n3);
        } while (this.maxRequiredFrameCount + n4 <= inputFrameCount);
        this.removeProcessedInputFrames(n4);
    }
    
    private int copyInputToOutput(final int n) {
        final int min = Math.min(this.maxRequiredFrameCount, this.remainingInputToCopyFrameCount);
        this.copyToOutput(this.inputBuffer, n, min);
        this.remainingInputToCopyFrameCount -= min;
        return min;
    }
    
    private void copyToOutput(final short[] array, final int n, final int n2) {
        this.outputBuffer = this.ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, n2);
        final int channelCount = this.channelCount;
        System.arraycopy(array, n * channelCount, this.outputBuffer, this.outputFrameCount * channelCount, channelCount * n2);
        this.outputFrameCount += n2;
    }
    
    private void downSampleInput(final short[] array, final int n, int i) {
        final int n2 = this.maxRequiredFrameCount / i;
        final int channelCount = this.channelCount;
        final int n3 = i * channelCount;
        short n4;
        int n5;
        for (i = 0; i < n2; ++i) {
            n4 = 0;
            n5 = 0;
            while (n4 < n3) {
                n5 += array[i * n3 + n * channelCount + n4];
                ++n4;
            }
            this.downSampleBuffer[i] = (short)(n5 / n3);
        }
    }
    
    private short[] ensureSpaceForAdditionalFrames(final short[] original, final int n, final int n2) {
        final int length = original.length;
        final int channelCount = this.channelCount;
        final int n3 = length / channelCount;
        if (n + n2 <= n3) {
            return original;
        }
        return Arrays.copyOf(original, (n3 * 3 / 2 + n2) * channelCount);
    }
    
    private int findPitchPeriod(final short[] array, int prevPeriod) {
        final int inputSampleRateHz = this.inputSampleRateHz;
        int n;
        if (inputSampleRateHz > 4000) {
            n = inputSampleRateHz / 4000;
        }
        else {
            n = 1;
        }
        if (this.channelCount == 1 && n == 1) {
            prevPeriod = this.findPitchPeriodInRange(array, prevPeriod, this.minPeriod, this.maxPeriod);
        }
        else {
            this.downSampleInput(array, prevPeriod, n);
            final int pitchPeriodInRange = this.findPitchPeriodInRange(this.downSampleBuffer, 0, this.minPeriod / n, this.maxPeriod / n);
            if (n != 1) {
                final int n2 = pitchPeriodInRange * n;
                final int n3 = n * 4;
                int n4 = n2 - n3;
                final int n5 = n2 + n3;
                final int minPeriod = this.minPeriod;
                if (n4 < minPeriod) {
                    n4 = minPeriod;
                }
                final int maxPeriod = this.maxPeriod;
                int n6;
                if ((n6 = n5) > maxPeriod) {
                    n6 = maxPeriod;
                }
                if (this.channelCount == 1) {
                    prevPeriod = this.findPitchPeriodInRange(array, prevPeriod, n4, n6);
                }
                else {
                    this.downSampleInput(array, prevPeriod, 1);
                    prevPeriod = this.findPitchPeriodInRange(this.downSampleBuffer, 0, n4, n6);
                }
            }
            else {
                prevPeriod = pitchPeriodInRange;
            }
        }
        int prevPeriod2;
        if (this.previousPeriodBetter(this.minDiff, this.maxDiff)) {
            prevPeriod2 = this.prevPeriod;
        }
        else {
            prevPeriod2 = prevPeriod;
        }
        this.prevMinDiff = this.minDiff;
        this.prevPeriod = prevPeriod;
        return prevPeriod2;
    }
    
    private int findPitchPeriodInRange(final short[] array, int n, int i, final int n2) {
        final int n3 = n * this.channelCount;
        int n4 = 1;
        int n5 = 0;
        int n6 = 0;
        int n7 = 255;
        while (i <= n2) {
            int j = 0;
            n = 0;
            while (j < i) {
                n += Math.abs(array[n3 + j] - array[n3 + i + j]);
                ++j;
            }
            int n8 = n4;
            int n9 = n5;
            if (n * n5 < n4 * i) {
                n9 = i;
                n8 = n;
            }
            int n10 = n6;
            int n11 = n7;
            if (n * n7 > n6 * i) {
                n11 = i;
                n10 = n;
            }
            ++i;
            n4 = n8;
            n5 = n9;
            n6 = n10;
            n7 = n11;
        }
        this.minDiff = n4 / n5;
        this.maxDiff = n6 / n7;
        return n5;
    }
    
    private int insertPitchPeriod(final short[] array, final int n, final float n2, final int n3) {
        int n4;
        if (n2 < 0.5f) {
            n4 = (int)(n3 * n2 / (1.0f - n2));
        }
        else {
            this.remainingInputToCopyFrameCount = (int)(n3 * (2.0f * n2 - 1.0f) / (1.0f - n2));
            n4 = n3;
        }
        final short[] outputBuffer = this.outputBuffer;
        final int outputFrameCount = this.outputFrameCount;
        final int n5 = n3 + n4;
        this.outputBuffer = this.ensureSpaceForAdditionalFrames(outputBuffer, outputFrameCount, n5);
        final int channelCount = this.channelCount;
        System.arraycopy(array, n * channelCount, this.outputBuffer, this.outputFrameCount * channelCount, channelCount * n3);
        overlapAdd(n4, this.channelCount, this.outputBuffer, this.outputFrameCount + n3, array, n + n3, array, n);
        this.outputFrameCount += n5;
        return n4;
    }
    
    private short interpolate(final short[] array, int n, int n2, int n3) {
        final short n4 = array[n];
        n = array[n + this.channelCount];
        final int newRatePosition = this.newRatePosition;
        final int oldRatePosition = this.oldRatePosition;
        final int n5 = (oldRatePosition + 1) * n3;
        n2 = n5 - newRatePosition * n2;
        n3 = n5 - oldRatePosition * n3;
        return (short)((n4 * n2 + (n3 - n2) * n) / n3);
    }
    
    private void moveNewSamplesToPitchBuffer(final int outputFrameCount) {
        final int n = this.outputFrameCount - outputFrameCount;
        this.pitchBuffer = this.ensureSpaceForAdditionalFrames(this.pitchBuffer, this.pitchFrameCount, n);
        final short[] outputBuffer = this.outputBuffer;
        final int channelCount = this.channelCount;
        System.arraycopy(outputBuffer, outputFrameCount * channelCount, this.pitchBuffer, this.pitchFrameCount * channelCount, channelCount * n);
        this.outputFrameCount = outputFrameCount;
        this.pitchFrameCount += n;
    }
    
    private static void overlapAdd(final int n, final int n2, final short[] array, final int n3, final short[] array2, final int n4, final short[] array3, final int n5) {
        for (int i = 0; i < n2; ++i) {
            int n6 = n4 * n2 + i;
            int n7 = n5 * n2 + i;
            int n8 = n3 * n2 + i;
            for (short n9 = 0; n9 < n; ++n9) {
                array[n8] = (short)((array2[n6] * (n - n9) + array3[n7] * n9) / n);
                n8 += n2;
                n6 += n2;
                n7 += n2;
            }
        }
    }
    
    private boolean previousPeriodBetter(final int n, final int n2) {
        return n != 0 && this.prevPeriod != 0 && n2 <= n * 3 && n * 2 > this.prevMinDiff * 3;
    }
    
    private void processStreamInput() {
        final int outputFrameCount = this.outputFrameCount;
        final float speed = this.speed;
        final float pitch = this.pitch;
        final float n = speed / pitch;
        final float n2 = this.rate * pitch;
        final double n3 = n;
        if (n3 <= 1.00001 && n3 >= 0.99999) {
            this.copyToOutput(this.inputBuffer, 0, this.inputFrameCount);
            this.inputFrameCount = 0;
        }
        else {
            this.changeSpeed(n);
        }
        if (n2 != 1.0f) {
            this.adjustRate(n2, outputFrameCount);
        }
    }
    
    private void removePitchFrames(final int n) {
        if (n == 0) {
            return;
        }
        final short[] pitchBuffer = this.pitchBuffer;
        final int channelCount = this.channelCount;
        System.arraycopy(pitchBuffer, n * channelCount, pitchBuffer, 0, (this.pitchFrameCount - n) * channelCount);
        this.pitchFrameCount -= n;
    }
    
    private void removeProcessedInputFrames(final int n) {
        final int inputFrameCount = this.inputFrameCount - n;
        final short[] inputBuffer = this.inputBuffer;
        final int channelCount = this.channelCount;
        System.arraycopy(inputBuffer, n * channelCount, inputBuffer, 0, channelCount * inputFrameCount);
        this.inputFrameCount = inputFrameCount;
    }
    
    private int skipPitchPeriod(final short[] array, final int n, final float n2, final int n3) {
        int n4;
        if (n2 >= 2.0f) {
            n4 = (int)(n3 / (n2 - 1.0f));
        }
        else {
            this.remainingInputToCopyFrameCount = (int)(n3 * (2.0f - n2) / (n2 - 1.0f));
            n4 = n3;
        }
        this.outputBuffer = this.ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, n4);
        overlapAdd(n4, this.channelCount, this.outputBuffer, this.outputFrameCount, array, n, array, n + n3);
        this.outputFrameCount += n4;
        return n4;
    }
    
    public void flush() {
        this.inputFrameCount = 0;
        this.outputFrameCount = 0;
        this.pitchFrameCount = 0;
        this.oldRatePosition = 0;
        this.newRatePosition = 0;
        this.remainingInputToCopyFrameCount = 0;
        this.prevPeriod = 0;
        this.prevMinDiff = 0;
        this.minDiff = 0;
        this.maxDiff = 0;
    }
    
    public int getFramesAvailable() {
        return this.outputFrameCount;
    }
    
    public void getOutput(final ShortBuffer shortBuffer) {
        final int min = Math.min(shortBuffer.remaining() / this.channelCount, this.outputFrameCount);
        shortBuffer.put(this.outputBuffer, 0, this.channelCount * min);
        this.outputFrameCount -= min;
        final short[] outputBuffer = this.outputBuffer;
        final int channelCount = this.channelCount;
        System.arraycopy(outputBuffer, min * channelCount, outputBuffer, 0, this.outputFrameCount * channelCount);
    }
    
    public void queueEndOfStream() {
        final int inputFrameCount = this.inputFrameCount;
        final float speed = this.speed;
        final float pitch = this.pitch;
        final int outputFrameCount = this.outputFrameCount + (int)((inputFrameCount / (speed / pitch) + this.pitchFrameCount) / (this.rate * pitch) + 0.5f);
        this.inputBuffer = this.ensureSpaceForAdditionalFrames(this.inputBuffer, inputFrameCount, this.maxRequiredFrameCount * 2 + inputFrameCount);
        int n = 0;
        int maxRequiredFrameCount;
        while (true) {
            maxRequiredFrameCount = this.maxRequiredFrameCount;
            final int channelCount = this.channelCount;
            if (n >= maxRequiredFrameCount * 2 * channelCount) {
                break;
            }
            this.inputBuffer[channelCount * inputFrameCount + n] = 0;
            ++n;
        }
        this.inputFrameCount += maxRequiredFrameCount * 2;
        this.processStreamInput();
        if (this.outputFrameCount > outputFrameCount) {
            this.outputFrameCount = outputFrameCount;
        }
        this.inputFrameCount = 0;
        this.remainingInputToCopyFrameCount = 0;
        this.pitchFrameCount = 0;
    }
    
    public void queueInput(final ShortBuffer shortBuffer) {
        final int remaining = shortBuffer.remaining();
        final int channelCount = this.channelCount;
        final int n = remaining / channelCount;
        shortBuffer.get(this.inputBuffer = this.ensureSpaceForAdditionalFrames(this.inputBuffer, this.inputFrameCount, n), this.inputFrameCount * this.channelCount, channelCount * n * 2 / 2);
        this.inputFrameCount += n;
        this.processStreamInput();
    }
}
