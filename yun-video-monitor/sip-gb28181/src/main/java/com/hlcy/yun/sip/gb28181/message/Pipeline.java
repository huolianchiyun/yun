package com.hlcy.yun.sip.gb28181.message;

import javax.sip.message.Message;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A list of {@link }s which handles or intercepts request events and response operations of a
 * {@link Message}.  {@link Pipeline} implements an advanced form of the
 */
public interface Pipeline<T> extends Iterable<Map.Entry<String, T>>{
    /**
     * Inserts a {@link T} at the first position of this pipeline.
     *
     * @param name    the name of the handler to insert first
     * @param handler the handler to insert first
     * @throws IllegalArgumentException if there's an entry with the same name already in the pipeline
     * @throws NullPointerException     if the specified handler is {@code null}
     */
    Pipeline addFirst(String name, T handler);

    /**
     * Appends a {@link T} at the last position of this pipeline.
     *
     * @param name    the name of the handler to append
     * @param handler the handler to append
     * @throws IllegalArgumentException if there's an entry with the same name already in the pipeline
     * @throws NullPointerException     if the specified handler is {@code null}
     */
    Pipeline addLast(String name, T handler);

    /**
     * Inserts a {@link T} before an existing handler of this
     * pipeline.
     *
     * @param baseName the name of the existing handler
     * @param name     the name of the handler to insert before
     * @param handler  the handler to insert before
     * @throws NoSuchElementException   if there's no such entry with the specified {@code baseName}
     * @throws IllegalArgumentException if there's an entry with the same name already in the pipeline
     * @throws NullPointerException     if the specified baseName or handler is {@code null}
     */
    Pipeline addBefore(String baseName, String name, T handler);

    /**
     * Inserts a {@link T} after an existing handler of this
     * pipeline.
     *
     * @param baseName the name of the existing handler
     * @param name     the name of the handler to insert after
     * @param handler  the handler to insert after
     * @throws NoSuchElementException   if there's no such entry with the specified {@code baseName}
     * @throws IllegalArgumentException if there's an entry with the same name already in the pipeline
     * @throws NullPointerException     if the specified baseName or handler is {@code null}
     */
    Pipeline addAfter(String baseName, String name, T handler);

    /**
     * Removes the {@link T} with the specified name from this pipeline.
     *
     * @param name the name under which the {@link T} was stored.
     * @return the removed handler
     * @throws NoSuchElementException if there's no such handler with the specified name in this pipeline
     * @throws NullPointerException   if the specified name is {@code null}
     */
    T remove(String name);

    /**
     * Returns the first {@link T} in this pipeline.
     *
     * @return the first handler.  {@code null} if this pipeline is empty.
     */
    T first();

    /**
     * Returns the last {@link T} in this pipeline.
     *
     * @return the last handler.  {@code null} if this pipeline is empty.
     */
    T last();

    /**
     * Returns the {@link T} with the specified name in this
     * pipeline.
     *
     * @return the handler with the specified name.
     * {@code null} if there's no such handler in this pipeline.
     */
    T get(String name);

    /**
     * Returns the {@link List} of the handler names.
     */
    List<String> names();

    /**
     * Converts this pipeline into an ordered {@link Map} whose keys are
     * handler names and whose values are handlers.
     */
    Map<String, T> toMap();
}
