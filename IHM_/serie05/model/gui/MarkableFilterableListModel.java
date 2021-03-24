package serie05.model.gui;

import java.util.HashSet;
import java.util.Set;

import serie05.model.filters.Filterable;

@SuppressWarnings("serial")
public class MarkableFilterableListModel<E extends Filterable<V>, V> extends StdFilterableListModel<E, V> {
	
	private Set<E> marked;
	
	public MarkableFilterableListModel() {
		super();
		marked = new HashSet<E>();
	}
	
	public void toggleMark(E element) {
		if(isMarked(element)) {
			marked.remove(element);
			System.out.println("rm");
		} else {
			marked.add(element);
			System.out.println("add");
		}
	}
	
	public boolean isMarked(E element) {
		assert element != null;
		return marked.contains(element);
	}
}
