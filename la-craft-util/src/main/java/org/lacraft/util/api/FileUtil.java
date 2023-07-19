package org.lacraft.util.api;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static Reader getReaderFromStream(InputStream initialStream) {
       return new InputStreamReader(initialStream, StandardCharsets.UTF_8);
    }
}
