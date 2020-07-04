
public abstract class AbstractStack<E> implements BDDStack<E>
{
	protected int maxSize;
	protected int size;
	
	public AbstractStack(int maxSize) throws IllegalArgumentException
	{
		if(maxSize <=0 )
			throw new IllegalArgumentException("Size of stack must be greater than zero");
		
		this.maxSize = maxSize;
		this.size = 0;
	}

	public boolean isEmpty()
	{
		return (size == 0);
	}
	
	public int capacity()
	{
		return maxSize;
	}
	public boolean isFull() {
		return (size == maxSize);
	}
	public int depth() {
		return size;
	}
	
	public abstract void flip();
	public abstract BDDStack<E> copy();
	public abstract BDDStack<E> newInstance();
	public abstract E pop() throws IllegalStateException;
	public abstract void push(E element) throws NullPointerException, IllegalStateException;
}
