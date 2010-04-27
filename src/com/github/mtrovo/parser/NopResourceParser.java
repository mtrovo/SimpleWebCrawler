package com.github.mtrovo.parser;

import java.io.InputStream;

public class NopResourceParser extends BaseResourceParser {

    public ParserTaskResultBean parse(InputStream is) throws ParseException {
        return new ParserTaskResultBean();
    }

}
