package serie05.gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import serie05.model.filters.Filterable;
import serie05.model.gui.MarkableFilterableListModel;

public class ItalicRenderer<E extends Filterable<V>, V> implements ListCellRenderer {
	
	private Font italic;
	private Font origin;
	private DefaultListCellRenderer delegate;
	private MarkableFilterableListModel<E, V> model;
	
	public ItalicRenderer(MarkableFilterableListModel<E, V> model) {
		delegate = new DefaultListCellRenderer();
		this.model = model;
		origin = delegate.getFont();
		italic = new Font(origin.getName(), Font.ITALIC, origin.getSize());
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component result = (delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus));
		boolean m = model.isMarked((E) value);
		if (model.isMarked((E) value)) {
			result.setFont(italic);
		} else {
			result.setFont(origin);
		}
		if (m && isSelected) {
			result.setFont(italic);

		}
		return result;
	}

}
