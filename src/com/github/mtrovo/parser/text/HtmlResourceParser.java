package com.github.mtrovo.parser.text;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.github.mtrovo.parser.ParseException;
import com.github.mtrovo.parser.ParserTaskResultBean;


public class HtmlResourceParser extends TextResourceParser {

    private static final Set<String> IGNORED_PREFIX_HREF_SET = new HashSet<String>();
    private static final List<Pattern> patterns = new ArrayList<Pattern>();
    static {
        patterns.add(Pattern.compile("href=['\"]?(.+?)['\"\\s>]", Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
        patterns.add(Pattern.compile("src=['\"]?(.+?)['\"\\s>]", Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
        patterns.add(Pattern.compile("url\\(['\"]?(.+?)['\"]?\\)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
        
        IGNORED_PREFIX_HREF_SET.add("javascript:");
        IGNORED_PREFIX_HREF_SET.add("https:");
        IGNORED_PREFIX_HREF_SET.add("about:");
    }
    
    private static final Logger log = Logger.getLogger(HtmlResourceParser.class);

    @Override
    public ParserTaskResultBean parse(Reader reader) throws ParseException {
        ParserTaskResultBean bean = new ParserTaskResultBean();
        bean.setOutgoingLinks(new LinkedList<URL>());
        
        StringBuilder content = null;
        try {
            content = getStreamAsString(reader);
        } catch (IOException e) {
            throw new ParseException(e);
        }
        
        for (Pattern ptrn : patterns) {
            Matcher matcher = ptrn.matcher(content);
            while(matcher.find()){
                try {
                    String found = matcher.group(1);
                    log.info("match found: " + found);
                    if (null == found || !isValid(found)) continue;
                    bean.getOutgoingLinks().add(new URL(base, found));
                } catch (MalformedURLException e) {
                    throw new ParseException(e);
                }
            }
        }
        
        return bean;
    }



    private boolean isValid(String found) {
        for (String prefix : IGNORED_PREFIX_HREF_SET) {
            if(found.startsWith(prefix)){
                return false;
            }
        }
        return true;
    }

}
