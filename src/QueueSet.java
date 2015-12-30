import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class QueueSet<T> {
	private Set<T> set = new HashSet<T>();
	private Queue<T> queue = new ArrayDeque<T>();
	
	public QueueSet()
	{		
	}
	
	public void add(Collection<T> collection)
	{
		for(T t : collection)
			add(t);
	}
	
	public boolean add(T elem)
	{
		if(set.contains(elem))
			return false;
		
		set.add(elem);
		queue.add(elem);
		return true;
	}
	
	public T peek()
	{
		return queue.peek();
	}
	
	public T pull()
	{
		T t = queue.poll();
		if(t != null)
			set.remove(t);
		return t;
	}
	
	public boolean isEmpty()
	{
		return set.isEmpty();
	}

}
