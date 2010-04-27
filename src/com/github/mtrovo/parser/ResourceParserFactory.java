package com.github.mtrovo.parser;

import java.util.HashMap;
import java.util.Map;

import com.github.mtrovo.parser.text.HtmlResourceParser;


public class ResourceParserFactory {
    private static ResourceParserFactory _instance = null;

    public static ResourceParserFactory getInstance() {
        if (null == _instance) _instance = new ResourceParserFactory();
        return _instance;
    }

    private Map<String, ResourceParser> resourceParserMap;
    private ResourceParserFactory() {
        this.resourceParserMap = new HashMap<String, ResourceParser>();
        this.resourceParserMap.put("text/html", new HtmlResourceParser());
        this.resourceParserMap.put("text/xhtml", new HtmlResourceParser());
    }


    public ResourceParser createParserForContentType(String contentType) {
        String ct = null;
        if(null == contentType) ct = "text/html";
        else if(contentType.indexOf(';') >= 0) 
            ct = contentType.substring(0, contentType.indexOf(';'));
        else ct = contentType;
        
        return resourceParserMap.get(ct);
    }
}
