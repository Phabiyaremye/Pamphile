import java.util.ArrayList;
import java.util.Iterator;


public class ListStack<E> extends AbstractStack<E> {
	
	private ArrayList<E> stack;

	public ListStack(int maxSize) throws IllegalArgumentException {
		super(maxSize);
		stack = new ArrayList<>(maxSize);
	}


	public E top() {
		return stack.get(size-1);
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
		
		stack.add(element);
		size++;
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
		
		E element = stack.get(size - 1);
        stack.remove(size - 1);
		--size;

        return element;
	}

	@Override
	public void clear() {
		size = 0 ;	// Make stack size 0, in effect clearing it.
		stack.clear();
	}

	@Override
	public BDDStack<E> newInstance() {
		return new ListStack<E>(maxSize);
	}

	@Override
	public void flip() {
		ArrayList<E> tmp = new ArrayList<>(maxSize);
		for (int i=0; i < size; i++) {
			tmp.add(stack.get(size - i - 1));
		}
		stack = tmp;    
	}

	@Override
	public BDDStack<E> copy() {
		return this;
	}


	@Override
	public Iterator<E> iterator() {
		return stack.iterator();
	}
}
