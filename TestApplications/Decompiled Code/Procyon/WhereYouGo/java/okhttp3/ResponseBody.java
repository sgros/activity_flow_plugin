// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import okio.Buffer;
import okio.BufferedSource;
import okhttp3.internal.Util;
import java.nio.charset.Charset;
import java.io.Reader;
import java.io.Closeable;

public abstract class ResponseBody implements Closeable
{
    private Reader reader;
    
    private Charset charset() {
        final MediaType contentType = this.contentType();
        Charset charset;
        if (contentType != null) {
            charset = contentType.charset(Util.UTF_8);
        }
        else {
            charset = Util.UTF_8;
        }
        return charset;
    }
    
    public static ResponseBody create(final MediaType mediaType, final long n, final BufferedSource bufferedSource) {
        if (bufferedSource == null) {
            throw new NullPointerException("source == null");
        }
        return new ResponseBody() {
            @Override
            public long contentLength() {
                return n;
            }
            
            @Override
            public MediaType contentType() {
                return mediaType;
            }
            
            @Override
            public BufferedSource source() {
                return bufferedSource;
            }
        };
    }
    
    public static ResponseBody create(final MediaType obj, final String s) {
        Charset charset = Util.UTF_8;
        MediaType parse = obj;
        if (obj != null) {
            charset = obj.charset();
            parse = obj;
            if (charset == null) {
                charset = Util.UTF_8;
                parse = MediaType.parse(obj + "; charset=utf-8");
            }
        }
        final Buffer writeString = new Buffer().writeString(s, charset);
        return create(parse, writeString.size(), writeString);
    }
    
    public static ResponseBody create(final MediaType mediaType, final byte[] array) {
        return create(mediaType, array.length, new Buffer().write(array));
    }
    
    public final InputStream byteStream() {
        return this.source().inputStream();
    }
    
    public final byte[] bytes() throws IOException {
        final long contentLength = this.contentLength();
        if (contentLength > 2147483647L) {
            throw new IOException("Cannot buffer entire body for content length: " + contentLength);
        }
        final BufferedSource source = this.source();
        try {
            final byte[] byteArray = source.readByteArray();
            Util.closeQuietly(source);
            if (contentLength != -1L && contentLength != byteArray.length) {
                throw new IOException("Content-Length (" + contentLength + ") and stream length (" + byteArray.length + ") disagree");
            }
        }
        finally {
            Util.closeQuietly(source);
        }
        return;
    }
    
    public final Reader charStream() {
        Reader reader = this.reader;
        if (reader == null) {
            reader = new BomAwareReader(this.source(), this.charset());
            this.reader = reader;
        }
        return reader;
    }
    
    @Override
    public void close() {
        Util.closeQuietly(this.source());
    }
    
    public abstract long contentLength();
    
    public abstract MediaType contentType();
    
    public abstract BufferedSource source();
    
    public final String string() throws IOException {
        final BufferedSource source = this.source();
        try {
            return source.readString(Util.bomAwareCharset(source, this.charset()));
        }
        finally {
            Util.closeQuietly(source);
        }
    }
    
    static final class BomAwareReader extends Reader
    {
        private final Charset charset;
        private boolean closed;
        private Reader delegate;
        private final BufferedSource source;
        
        BomAwareReader(final BufferedSource source, final Charset charset) {
            this.source = source;
            this.charset = charset;
        }
        
        @Override
        public void close() throws IOException {
            this.closed = true;
            if (this.delegate != null) {
                this.delegate.close();
            }
            else {
                this.source.close();
            }
        }
        
        @Override
        public int read(final char[] array, final int n, final int n2) throws IOException {
            if (this.closed) {
                throw new IOException("Stream closed");
            }
            Reader delegate;
            if ((delegate = this.delegate) == null) {
                delegate = new InputStreamReader(this.source.inputStream(), Util.bomAwareCharset(this.source, this.charset));
                this.delegate = delegate;
            }
            return delegate.read(array, n, n2);
        }
    }
}
