package com.github.mtrovo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.github.mtrovo.parser.ParserTaskResultBean;
import com.github.mtrovo.parser.ResourceParser;
import com.github.mtrovo.parser.ResourceParserFactory;

public class ResourceFetcher implements Callable<Queue<URL>> {

    private static final Queue<URL> EMPTY_QUEUE = new LinkedList<URL>();

    private static final int MAX_ATTEMPTS = 10;

    private URL resource = null;

    public ResourceFetcher(URL resource) {
        this.resource = resource;
    }

    public URL getResource() {
        return resource;
    }

    public void setResource(URL resource) {
        this.resource = resource;
    }

    public Queue<URL> call() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) resource.openConnection();
        String contentType = conn.getContentType();
        String contentEncoding = conn.getContentEncoding();

        ResourceParser resourceParser = ResourceParserFactory.getInstance()
                .createParserForContentType(contentType);
        
        if(null == resourceParser)
            return EMPTY_QUEUE;
        
        resourceParser.setBase(resource);

        int nattempts = 0;
        while (HttpURLConnection.HTTP_PROXY_AUTH == conn.getResponseCode()
                && nattempts++ < MAX_ATTEMPTS) {
            Thread.sleep((long) (500 * Math.random()));
            conn = (HttpURLConnection) resource.openConnection();
        }

        if (conn.getResponseCode() == HttpURLConnection.HTTP_PROXY_AUTH
                || HttpURLConnection.HTTP_FORBIDDEN == conn.getResponseCode()) return EMPTY_QUEUE;

        Logger.getLogger(this.getClass()).info(getInfoDesc(conn));

        ParserTaskResultBean resultBean = resourceParser.parse(conn
                .getInputStream());

        return resultBean.getOutgoingLinks();
    }

    private String getInfoDesc(HttpURLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Response Code: ");
        sb.append(conn.getResponseCode());
        sb.append(" / ");
        sb.append("Content Type: ");
        sb.append(conn.getContentType());
        sb.append(" / ");
        sb.append("Content Encoding: ");
        sb.append(conn.getContentEncoding());
        sb.append(" / ");
        sb.append("URL: ");
        sb.append(conn.getURL().toString());

        return sb.toString();
    }
}
