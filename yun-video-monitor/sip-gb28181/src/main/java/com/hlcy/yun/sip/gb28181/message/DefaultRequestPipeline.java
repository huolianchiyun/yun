package com.hlcy.yun.sip.gb28181.message;

import javax.sip.RequestEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default {@link RequestPipeline} implementation.
 */
public class DefaultRequestPipeline implements RequestPipeline {
    private Map<String, RequestHandler> handlerMap = new ConcurrentHashMap<>();
    private RequestHandler head;
    private RequestHandler tail;

    @Override
    public RequestPipeline addFirst(String name, RequestHandler handler) {
        if (this.head == null) {
            this.head = this.tail = handler;
        }
        handler.prev = null;
        handler.next = head;
        head.prev = handler;
        head = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public RequestPipeline addLast(String name, RequestHandler handler) {
        if (this.tail == null) {
            this.head = this.tail = handler;
        }
        handler.next = null;
        handler.prev = tail;
        this.tail.next = handler;
        this.tail = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public RequestPipeline addBefore(String baseName, String name, RequestHandler handler) {
        final RequestHandler baseHandler = handlerMap.get(baseName);
        baseHandler.prev.next = handler;
        handler.prev = baseHandler.prev;
        handler.next = baseHandler;
        baseHandler.prev = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public RequestPipeline addAfter(String baseName, String name, RequestHandler handler) {
        final RequestHandler baseHandler = handlerMap.get(baseName);
        baseHandler.next.prev = handler;
        handler.next = baseHandler.next;
        handler.prev = baseHandler;
        baseHandler.next = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public RequestHandler remove(String name) {
        final RequestHandler deletedHandler = handlerMap.remove(name);
        deletedHandler.prev.next = deletedHandler.next;
        deletedHandler.next.prev = deletedHandler.prev;
        return deletedHandler;
    }

    @Override
    public RequestHandler first() {
        return head;
    }

    @Override
    public RequestHandler last() {
        return tail;
    }

    @Override
    public RequestHandler get(String name) {
        return handlerMap.get(name);
    }

    @Override
    public List<String> names() {
        return new ArrayList<>(handlerMap.keySet());
    }

    @Override
    public Map<String, RequestHandler> toMap() {
        return handlerMap;
    }

    @Override
    public void processRequest(RequestEvent event) {
        head.handle(event);
    }

    @Override
    public Iterator<Map.Entry<String, RequestHandler>> iterator() {
        return handlerMap.entrySet().iterator();
    }

}
