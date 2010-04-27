package com.github.mtrovo.parser;

import java.net.URL;
import java.util.Queue;

public class ParserTaskResultBean {
    private Queue<URL> outgoingLinks;

    public Queue<URL> getOutgoingLinks() {
        return outgoingLinks;
    }

    public void setOutgoingLinks(Queue<URL> outgoingLinks) {
        this.outgoingLinks = outgoingLinks;
    }
    
}
