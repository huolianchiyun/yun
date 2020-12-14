package com.hlcy.yun.sip.message;

import javax.sip.message.Message;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A list of {@link }s which handles or intercepts request events and response operations of a
 * {@link Message}.  {@link ResponsePipeline} implements an advanced form of the
 */
public interface ResponsePipeline extends ResponseInvoker, Iterable<Map.Entry<String, ResponseHandler>> {
    /**
     * Inserts a {@link ResponseHandler} at the first position of this pipeline.
     *
     * @param name    the name of the handler to insert first
     * @param handler the handler to insert first
     * @throws IllegalArgumentException if there's an entry with the same name already in the pipeline
     * @throws NullPointerException     if the specified handler is {@code null}
     */
    ResponsePipeline addFirst(String name, ResponseHandler handler);

    /**
     * Appends a {@link ResponseHandler} at the last position of this pipeline.
     *
     * @param name    the name of the handler to append
     * @param handler the handler to append
     * @throws IllegalArgumentException if there's an entry with the same name already in the pipeline
     * @throws NullPointerException     if the specified handler is {@code null}
     */
    ResponsePipeline addLast(String name, ResponseHandler handler);

    /**
     * Inserts a {@link ResponseHandler} before an existing handler of this
     * pipeline.
     *
     * @param baseName the name of the existing handler
     * @param name     the name of the handler to insert before
     * @param handler  the handler to insert before
     * @throws NoSuchElementException   if there's no such entry with the specified {@code baseName}
     * @throws IllegalArgumentException if there's an entry with the same name already in the pipeline
     * @throws NullPointerException     if the specified baseName or handler is {@code null}
     */
    ResponsePipeline addBefore(String baseName, String name, ResponseHandler handler);

    /**
     * Inserts a {@link ResponseHandler} after an existing handler of this
     * pipeline.
     *
     * @param baseName the name of the existing handler
     * @param name     the name of the handler to insert after
     * @param handler  the handler to insert after
     * @throws NoSuchElementException   if there's no such entry with the specified {@code baseName}
     * @throws IllegalArgumentException if there's an entry with the same name already in the pipeline
     * @throws NullPointerException     if the specified baseName or handler is {@code null}
     */
    ResponsePipeline addAfter(String baseName, String name, ResponseHandler handler);

    /**
     * Removes the {@link ResponseHandler} with the specified name from this pipeline.
     *
     * @param name the name under which the {@link ResponseHandler} was stored.
     * @return the removed handler
     * @throws NoSuchElementException if there's no such handler with the specified name in this pipeline
     * @throws NullPointerException   if the specified name is {@code null}
     */
    ResponseHandler remove(String name);

    /**
     * Returns the first {@link ResponseHandler} in this pipeline.
     *
     * @return the first handler.  {@code null} if this pipeline is empty.
     */
    ResponseHandler first();

    /**
     * Returns the last {@link ResponseHandler} in this pipeline.
     *
     * @return the last handler.  {@code null} if this pipeline is empty.
     */
    ResponseHandler last();

    /**
     * Returns the {@link ResponseHandler} with the specified name in this
     * pipeline.
     *
     * @return the handler with the specified name.
     * {@code null} if there's no such handler in this pipeline.
     */
    ResponseHandler get(String name);

    /**
     * Returns the {@link List} of the handler names.
     */
    List<String> names();

    /**
     * Converts this pipeline into an ordered {@link java.util.Map} whose keys are
     * handler names and whose values are handlers.
     */
    Map<String, ResponseHandler> toMap();
}
