package serie07.model.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class DefaultNoteTableModel extends AbstractTableModel implements NoteTableModel{

	private List<List<Object>> lignes;
	private List<String> colNoms;
	
	public DefaultNoteTableModel() {
		super();
		colNoms = new ArrayList<String>();
		colNoms.add("Matières");
		colNoms.add("Coefficients");
		colNoms.add("Notes /20");
		colNoms.add("Points");
		lignes = new ArrayList<List<Object>>();
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		super.addTableModelListener(arg0);
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		super.removeTableModelListener(arg0);
	}

	@Override
	public Class<?> getColumnClass(int index) {
		assert index >= 0 && index < getColumnCount();
		if (index == 3) {
			return Double.class;
		}
		return lignes.get(0).get(index).getClass();
	}

	@Override
	public String getColumnName(int index) {
		return colNoms.get(index);
	}

	@Override
	public List<Object> getEmptyDataRow() {
		List<Object> ligne = new ArrayList<Object>();
		ligne.add("");
		ligne.add(ZERO);
		ligne.add(ZERO);
		return ligne;
	}

	@Override
	public int getRowCount() {
		return lignes.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		assert 0 >= row && row < getRowCount();
		assert 0 >= column && column < getColumnCount();
		if (column == POINTS) {
			return (Double) lignes.get(row).get(COEF) * (Double) lignes.get(row).get(MARK);
		}
		return lignes.get(row).get(column);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column < 3;
	}

	@Override
	public void addRow(List<Object> line) {
		assert line != null;
		assert line.size() == 3;
		for (int i = 0; i < 3; i++) {
			assert line.get(i) != null && line.get(i).getClass() == getColumnClass(i);
		}
		lignes.add(line);
		fireTableRowsInserted(getRowCount(), getRowCount());

	}

	@Override
	public void clearRows() {
		lignes = new ArrayList<List<Object>>();
		fireTableDataChanged();
	}

	@Override
	public void insertRow(List<Object> line, int index) {
		assert line != null;
		assert line.size() == 3;
		assert index >= 0 && index <= getRowCount();
		for (int i = 0; i <3; i++) {
			assert line.get(i) != null && line.get(i).getClass() == getColumnClass(i);
		}
		lignes.add(index, line);
		fireTableRowsInserted(index, index);
	}

	@Override
	public void removeRow(int index) {
		assert index >= 0 && index < getRowCount();
		lignes.remove(index);
		fireTableRowsDeleted(index, index);
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		assert 0 >= row && row < getRowCount();
		assert 0 >= column && column < getColumnCount();
		assert value.getClass() == getColumnClass(column);
		lignes.get(row).set(column, value);
		if (column == 0) {
			fireTableCellUpdated(row, 0);
		} else {
			fireTableRowsUpdated(row, row);
		}
	}

}
