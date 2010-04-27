package com.github.mtrovo;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

public class Main {

    public static void main(String[] args) throws Exception {
        //setProxyProperties();

        ExecutorService threadPool = Executors.newFixedThreadPool(8);
        Queue<URL> toVisit = new LinkedList<URL>();
        Set<URL> visited = new HashSet<URL>();
        Queue<Future<Queue<URL>>> futures = new LinkedList<Future<Queue<URL>>>();

        toVisit.offer(new URL("http://www.uol.com.br"));

        boolean hasMore = false;
        do {
            while (!toVisit.isEmpty()) {
                URL url = toVisit.poll();
                if (visited.add(url)) {
                    futures.add(threadPool.submit(new ResourceFetcher(url)));
                }
            }

            hasMore = false;
            for (Iterator<Future<Queue<URL>>> it = futures.iterator(); it
                    .hasNext();) {
                Future<Queue<URL>> future = it.next();
                if (!future.isDone()) {
                    hasMore = true;
                    continue;
                }
                
                Queue<URL> queue = null;
                try {
                    queue = future.get();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                
                if (null != queue) {
                    Logger.getLogger(Main.class).info(queue);
                    toVisit.addAll(queue);
                }
            }
        } while (hasMore || !toVisit.isEmpty());

        if (!threadPool.isTerminated()) {
            threadPool.shutdown();
        }
    }

    private static void setProxyProperties() {
        Properties props = new Properties(System.getProperties());
        props.put("http.proxySet", "true");
        props.put("http.proxyHost", "192.168.0.19");
        props.put("http.proxyPort", "3128");
        Properties newprops = new Properties(props);
        System.setProperties(newprops);
    }
}
