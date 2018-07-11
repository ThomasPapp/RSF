package rsf.game;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Indexer<E> implements Iterable<E> {

	private final int minIndex;
	private final Object[] arr;

	private int size = 0;
	private int highest;

	public Indexer(int minIndex, int capacity) {
		this.minIndex = highest = minIndex;
		arr = new Object[capacity];
	}

	public Indexer(int capacity) {
		this(0, capacity);
	}

	@SuppressWarnings("unchecked")
	public E get(int index) {
		return (E) arr[index];
	}

	@SuppressWarnings("unchecked")
	public E set(int index, E element) {
		Object previous = arr[index];
		arr[index] = element;
		if (previous == null && element != null) {
			size++;
			if (highest < index)
				highest = index;
		} else if (previous != null && element == null) {
			size--;
			if (highest == index)
				highest--;
		}
		return (E) previous;
	}

	public int add(E element) {
		int index = nextIndex();
		set(index, element);
		return index;
	}

	public void remove(E element) {
		for (int i = minIndex; i <= highest; i++) {
			if (element == arr[i]) {
				set(i, null);
				return;
			}
		}
	}

	public boolean contains(E element) {
		if (element == null)
			return false;

		for (E e : this) {
			if (e == element) return true;
		}

		return false;
	}

	public int size() {
		return size;
	}

	public boolean isFull() {
		return size >= arr.length;
	}

	public int nextIndex() {
		for (int i = minIndex; i < arr.length; i++) {
			if (null == arr[i])
				return i;
		}
		throw new IllegalStateException("Out of indices!");
	}

	@Override
	public Iterator<E> iterator() {
		IndexerIterator it = new IndexerIterator();
		it.pointer = minIndex;
		return it;
	}

	public Stream<E> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	public final class IndexerIterator implements Iterator<E> {

		private int pointer;

		@Override
		public boolean hasNext() {
			return size > 0 && pointer <= highest;
		}

		@Override
		@SuppressWarnings("unchecked")
		public E next() {
			Object o = arr[pointer++];
			if (o == null && hasNext())
				return next();
			return (E) o;
		}

		@Override
		public void remove() {
			set(pointer, null);
		}

	}

}