package com.github.mtrovo.parser;

import java.io.InputStream;
import java.net.URL;

public interface ResourceParser {
    public ParserTaskResultBean parse(InputStream is) throws ParseException;
    
    public void setBase(URL base);
    public URL getBase();
}
