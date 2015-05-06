package se.liu.ida.albpe868.tddd78.javaproject;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;

/**
 * ArrayIterator class for iterator design pattern
 */
public class ArrayIterator implements Iterator<AbstractGameObject> {
    private AbstractGameObject[] array;
    private int index;

    public ArrayIterator(AbstractGameObject[] array) {
        this.array = array;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < array.length;
    }

    @Override
    public AbstractGameObject next() {
        if (index > array.length) {
            LogHandler.log(Level.SEVERE, "ArrayIterator next error", null);
            throw new NoSuchElementException("Problem with ArrayIterator next method");
        }
        AbstractGameObject gameObject = array[index];
        index++;
        return gameObject;
    }
}
