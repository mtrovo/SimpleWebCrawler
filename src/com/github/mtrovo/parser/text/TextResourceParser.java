package com.github.mtrovo.parser.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.github.mtrovo.parser.BaseResourceParser;
import com.github.mtrovo.parser.ParseException;
import com.github.mtrovo.parser.ParserTaskResultBean;


public abstract class TextResourceParser extends BaseResourceParser {
    
    public final ParserTaskResultBean parse(InputStream is) throws ParseException {
        return parse(new InputStreamReader(is));
    }

    public abstract ParserTaskResultBean parse(Reader reader) throws ParseException;

    protected static StringBuilder getStreamAsString(Reader reader)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buff = new char[1 << 10];
        int read = 0;
        while (-1 != (read = reader.read(buff))) {
            sb.append(buff, 0, read);
        }

        return sb;
    }
}
