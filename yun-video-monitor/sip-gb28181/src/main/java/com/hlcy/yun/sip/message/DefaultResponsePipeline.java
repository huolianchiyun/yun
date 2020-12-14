package com.hlcy.yun.sip.message;

import javax.sip.ResponseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default {@link ResponsePipeline} implementation.
 */
public class DefaultResponsePipeline implements ResponsePipeline {
    private Map<String, ResponseHandler> handlerMap = new ConcurrentHashMap<>();
    private ResponseHandler head;
    private ResponseHandler tail;

    @Override
    public ResponsePipeline addFirst(String name, ResponseHandler handler) {
        if (this.head == null){
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
    public ResponsePipeline addLast(String name, ResponseHandler handler) {
        if (this.tail == null){
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
    public ResponsePipeline addBefore(String baseName, String name, ResponseHandler handler) {
        final ResponseHandler baseHandler = handlerMap.get(baseName);
        baseHandler.prev.next = handler;
        handler.prev = baseHandler.prev;
        handler.next = baseHandler;
        baseHandler.prev = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public ResponsePipeline addAfter(String baseName, String name, ResponseHandler handler) {
        final ResponseHandler baseHandler = handlerMap.get(baseName);
        baseHandler.next.prev = handler;
        handler.next = baseHandler.next;
        handler.prev = baseHandler;
        baseHandler.next = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public ResponseHandler remove(String name) {
        final ResponseHandler deletedHandler = handlerMap.remove(name);
        deletedHandler.prev.next = deletedHandler.next;
        deletedHandler.next.prev = deletedHandler.prev;
        return deletedHandler;
    }

    @Override
    public ResponseHandler first() {
        return head;
    }

    @Override
    public ResponseHandler last() {
        return tail;
    }

    @Override
    public ResponseHandler get(String name) {
        return handlerMap.get(name);
    }

    @Override
    public List<String> names() {
        return new ArrayList<>(handlerMap.keySet());
    }

    @Override
    public Map<String, ResponseHandler> toMap() {
        return handlerMap;
    }

    @Override
    public void processResponse(ResponseEvent event) {
        head.handle(event);
    }

    @Override
    public Iterator<Map.Entry<String, ResponseHandler>> iterator() {
        return handlerMap.entrySet().iterator();
    }
}
