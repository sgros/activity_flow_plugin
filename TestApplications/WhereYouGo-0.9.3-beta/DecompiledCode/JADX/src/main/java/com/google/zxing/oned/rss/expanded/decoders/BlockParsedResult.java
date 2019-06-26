package com.google.zxing.oned.rss.expanded.decoders;

final class BlockParsedResult {
    private final DecodedInformation decodedInformation;
    private final boolean finished;

    BlockParsedResult(boolean finished) {
        this(null, finished);
    }

    BlockParsedResult(DecodedInformation information, boolean finished) {
        this.finished = finished;
        this.decodedInformation = information;
    }

    /* Access modifiers changed, original: 0000 */
    public DecodedInformation getDecodedInformation() {
        return this.decodedInformation;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isFinished() {
        return this.finished;
    }
}
