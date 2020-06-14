package org.NauhWuun.jio.kernel;

import java.util.*;

public class CircularBuffer<E> implements Collection, Queue 
{
    private final BufferObservable observable;
    private final Object[] buffer;
    private final int lower;
    private final int upper;
    private final int capacity;
    private int start;
    private int end;
    private int count;

    public CircularBuffer(int capacity) {
        observable 		= new BufferObservable(this);
        buffer 			= new Object[capacity];

        lower 			= 0;
        upper 			= capacity;
        this.capacity 	= capacity;
    }

    public CircularBuffer(E[] initial) {
        this(initial, true);
    }

    public CircularBuffer(E[] initial, boolean copy) {
        this(initial, 0, initial.length, copy);
    }

    public CircularBuffer(E[] initial, int offset, int count, boolean copy) {
        observable = new BufferObservable(this);

        if (copy) {
            buffer = new Object[count];
            System.arraycopy(initial, offset, buffer, 0, count);

			offset = 0;
        } else {
            buffer = initial;
        }

        this.count = count;
        lower 	   = offset;
        upper 	   = offset + count;
        capacity   = count;
        start 	   = lower;
        end 	   = lower;
    }

    @Override
    public boolean add(Object o) {
        if (count == capacity) {
            throw new IllegalStateException("buffer is full");
        }

        count++;
        buffer[end] = o;
        end++;

        if (end == upper) {
            end = lower;
        }

        return true;
    }

    @Override
    public boolean addAll(Collection c) {
        Iterator i = c.iterator();

        while (i.hasNext()) {
            add(i.next());
        }

        return true;
    }

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public void clear() {
        start 	= lower;
        end 	= lower;
        count 	= 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsAll(Collection c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drain(CircularBuffer sink) {
        if (count == 0) {
            throw new IllegalStateException("buffer is empty");
        }

        move(sink, count);
    }

    public void drain(E[] sink, int offset) {
        if (count == 0) {
            throw new IllegalStateException("buffer is empty");
        }

        if (sink.length - offset < count) {
            throw new IllegalArgumentException("destination too small");
        }

        if (end <= start) {
            // Buffer wraps around, must make two calls to arraycopy().
            int leading = upper - start;

            System.arraycopy(buffer, start, sink, offset, leading);
            System.arraycopy(buffer, lower, sink, offset + leading, end - lower);
        } else {
            // Buffer is in one contiguous region.
            System.arraycopy(buffer, start, sink, offset, end - start);
        }

        start 	= lower;
        end 	= lower;
        count 	= 0;

        observable.setAndNotify();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object element() {
        if (count == 0) {
            throw new NoSuchElementException("buffer is empty");
        }

        return (E) buffer[start];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == capacity;
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean offer(Object o) {
        if (count == capacity) {
            return false;
        }

        count++;

        buffer[end] = o;
        end++;

        if (end == upper) {
            end = lower;
        }

        return true;
    }

    public void move(CircularBuffer sink, int n) {
        if (count < n) {
            throw new IllegalStateException("source has too few items");
        }

        if (sink.upper - sink.lower - sink.count < n) {
            throw new IllegalArgumentException("sink has insufficient space");
        }

        int tocopy = n;

        while (tocopy > 0) {
            int desired = Math.min(tocopy, Math.max(end - start, upper - start));
            int willfit = sink.start <= sink.end ? sink.upper - sink.end : sink.start - sink.end;
            int copied  = Math.min(desired, willfit);

			System.arraycopy(buffer, start, sink.buffer, sink.end, copied);
            sink.end += copied;

            if (sink.end >= sink.upper) {
                sink.end -= (sink.upper - sink.lower);
            }

            start += copied;

            if (start >= upper) {
                start -= capacity;
            }

            tocopy -= copied;
        }

        sink.count += n;
        count -= n;

        if (count == 0) {
            observable.setAndNotify();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return count == 0 ? null : (E) buffer[start];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (count == 0) {
            return null;
        }

        count--;

        Object o = buffer[start];
        start++;

        if (start == upper) {
            start = lower;
        }

        if (count == 0) {
            observable.setAndNotify();
        }

        return (E) o;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove() {
        if (count == 0) {
            throw new NoSuchElementException("buffer is empty");
        }
        count--;

        Object o = buffer[start];
        start++;

        if (start == upper) {
            start = lower;
        }

        if (count == 0) {
            observable.setAndNotify();
        }

        return (E) o;
    }

    public void removeObserver(Observer o) {
        observable.deleteObserver(o);
    }

    public int remaining() {
        return capacity - count;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported on circular buffer.");
    }

    @Override
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException("Not supported on circular buffer.");
    }

    @Override
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException("Not supported on circular buffer.");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Use drain() instead.");
    }

    @Override
    public Object[] toArray(Object[] a) {
        throw new UnsupportedOperationException("Use drain() instead.");
    }

    @Override
    public int size() {
        return count;
    }

    static class BufferObservable extends Observable 
    {
        private CircularBuffer buffer;

        public BufferObservable(CircularBuffer buffer) {
            this.buffer = buffer;
        }

        public void setAndNotify() {
            setChanged();
            notifyObservers(buffer);
        }
    }
}