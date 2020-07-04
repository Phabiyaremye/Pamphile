
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayStack<E> extends AbstractStack<E>
{

	private E[] stack;             // the set's contents

	@SuppressWarnings("unchecked")
	public ArrayStack(int maxSize) throws IllegalArgumentException {
		super(maxSize);
		
		/**
		 * It is not possible in Java to create a new array containing objects of a generic 
		 * type like E, so we have to create an Object[] array and then reinterpret cast is
		 * to E[] array
		 */
        stack = (E[])(new Object[maxSize]);
	}

	public E top() {
		return stack[size-1];
	}
	
	/**
	 * Pushes the specified element onto this stack.
	 * @param element the element to be pushed
	 * @throws NullPointerException if {@code element == null}
	 * @throws IllegalStateException if this stack is full
	 */
	@Override
	public void push(E element) throws NullPointerException, IllegalStateException {

		if (element == null) {
			throw new NullPointerException("Stack cannot store null values");
		}
		
		if (maxSize <= size) {
			throw new IllegalStateException("Stack is full");
		}
		
		this.stack[size] = element;
		size ++;
	}


	/**
	 * Removes and returns the element at the top of this stack.
	 * @return the element at the top of this stack
	 * @throws IllegalStateException if this stack is empty
	 */
	@Override
	public E pop() throws IllegalStateException {

		if(this.isEmpty()) {
			throw new IllegalStateException("Stack is empty, cannot pop value");
		}
		
		--size;
		return stack[size]; // Return Popped object + Decrement.
	}



	/**
	 * Empties this stack.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		size = 0; // Make stack size 0, in effect clearing it.
		this.stack = (E[])(new Object[maxSize]); // this is additional step to ensure that all elements in the stack get destroyed
	}
	@Override
	public BDDStack<E> newInstance() {
		return new ArrayStack<E>(maxSize);
	}
	
	@Override
	public void flip() {
		@SuppressWarnings("unchecked")
		E[] tmp = (E[])(new Object[maxSize]);
		for (int i=0; i < size; i++) {
			tmp[i] = stack[size - i - 1];
		}
		stack = tmp;
	}
	@Override
	public BDDStack<E> copy() {
		return this;
	}

	@Override
	public Iterator<E> iterator() {

		return new StackIterator();
	}
	
	private class StackIterator implements Iterator<E> {
		private int index;
		private boolean called = false;

		/**
		 * create a class for stack iterator
		 */
		public StackIterator() {
			index = size;
		}

		@Override
		public boolean hasNext() {
			return index != 0;
		}

		@Override
		public E next() {
			called = true;
			if (index == 0) {
				throw new NoSuchElementException("There are no elements remaining.");
			}
			--index;
			return stack[index];
		}

		@Override
		public void remove() {
			if (called) {
				for (int i = index; i < size; i++) {
					stack[i] = stack[i + 1];
				}
				called = false;
				size--;
			}

			else {
				throw new IllegalStateException("error removing: no element read yet");
			}
		}
	}

}
