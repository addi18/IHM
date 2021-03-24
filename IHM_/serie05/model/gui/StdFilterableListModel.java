package serie05.model.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataListener;

import serie05.model.filters.Filter;
import serie05.model.filters.Filterable;

@SuppressWarnings({ "serial", "rawtypes" })
public class StdFilterableListModel<E extends Filterable<V>,V> extends AbstractListModel 
	implements FilterableListModel<E, V> {

	private List<E> elmts;
	private List<E> filteredElmts;
	private Filter<E, V> filter;
	
	public StdFilterableListModel() {
		super();
		elmts = new ArrayList<E>();	
		filteredElmts = new ArrayList<E>();
	}
	
	@Override
	public void addListDataListener(ListDataListener arg0) {
		super.addListDataListener(arg0);
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		super.removeListDataListener(arg0);
	}

	@Override
	public Filter<E, V> getFilter() {
		return filter;
	}

	@Override
	public E getElementAt(int i) {
		assert i >= 0 && i < getSize();
		return filteredElmts.get(i);
	}

	@Override
	public int getSize() {
		return filteredElmts.size();
	}

	@Override
	public E getUnfilteredElementAt(int i) {
		assert i >= 0 && i < getUnfilteredSize();
		return elmts.get(i);
	}

	@Override
	public int getUnfilteredSize() {
		return elmts.size();
	}

	@Override
	public void addElement(E element) {
		assert element != null;
		elmts.add(element);
		//filteredElmts = filter.filter(elmts);
		super.fireContentsChanged(this, 0, getUnfilteredSize());

	}

	@Override
	public void setFilter(Filter<E, V> filter) {
		assert filter != null;
		this.filter = filter;
		filter.addValueChangeListener(new CustomPCL());
		filteredElmts = filter.filter(elmts);
	}

	@Override
	public void setElements(Collection<E> c) {
		assert c != null;
		elmts = new ArrayList<E>(c);
		//filteredElmts = filter.filter(elmts);
		super.fireContentsChanged(this, 0, getUnfilteredSize());
	}

	private class CustomPCL implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			filteredElmts = filter.filter(elmts);
		}	
	}
}
