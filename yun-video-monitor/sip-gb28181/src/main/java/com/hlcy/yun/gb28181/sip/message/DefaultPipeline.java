package com.hlcy.yun.gb28181.sip.message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default {@link Pipeline} implementation.
 */
public class DefaultPipeline<T extends MessageHandler<E>, E> implements Pipeline<T>, MessageInvoker<E> {
    private Map<String, T> handlerMap = new ConcurrentHashMap<>();
    private T head;
    private T tail;

    @Override
    public Pipeline addFirst(String name, T handler) {
        handler.setPipeline(this);
        if (this.head == null){
            this.head = this.tail = handler;
            return this;
        }
        handler.prev = null;
        handler.next = head;
        head.prev = handler;
        head = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public Pipeline addLast(String name, T handler) {
        handler.setPipeline(this);
        if (this.tail == null){
            this.head = this.tail = handler;
            return this;
        }
        handler.next = null;
        handler.prev = tail;
        this.tail.next = handler;
        this.tail = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public Pipeline addBefore(String baseName, String name, T handler) {
        handler.setPipeline(this);
        final T baseHandler = handlerMap.get(baseName);
        baseHandler.prev.next = handler;
        handler.prev = baseHandler.prev;
        handler.next = baseHandler;
        baseHandler.prev = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public Pipeline addAfter(String baseName, String name, T handler) {
        handler.setPipeline(this);
        final T baseHandler = handlerMap.get(baseName);
        baseHandler.next.prev = handler;
        handler.next = baseHandler.next;
        handler.prev = baseHandler;
        baseHandler.next = handler;
        handlerMap.put(name, handler);
        return this;
    }

    @Override
    public T remove(String name) {
        final T deletedHandler = handlerMap.remove(name);
        deletedHandler.prev.next = deletedHandler.next;
        deletedHandler.next.prev = deletedHandler.prev;
        return deletedHandler;
    }

    @Override
    public T first() {
        return head;
    }

    @Override
    public T last() {
        return tail;
    }

    @Override
    public T get(String name) {
        return handlerMap.get(name);
    }

    @Override
    public List<String> names() {
        return new ArrayList<>(handlerMap.keySet());
    }

    @Override
    public Map<String, T> toMap() {
        return handlerMap;
    }

    @Override
    public void processMessage(E event) {
        head.handle(event);
    }

    @Override
    public Iterator<Map.Entry<String, T>> iterator() {
        return handlerMap.entrySet().iterator();
    }
}
