package com.davemorrissey.labs.subscaleview.decoder;

public interface DecoderFactory {
   Object make() throws IllegalAccessException, InstantiationException;
}
