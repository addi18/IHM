package serie05.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import serie05.model.filters.Filter;
import serie05.model.filters.Filterable;
import serie05.model.gui.FilterableListModel;
import serie05.model.gui.MarkableFilterableListModel;
import serie05.util.Translator;

@SuppressWarnings("serial")
public class FilterablePane<E extends Filterable<V>, V> extends JPanel {
	
	private JTextField filterText;
	private JList filterableList;
	private Translator<V> translator;
	private JComboBox filterTypes;
	ItalicRenderer<E, V> renderer;
	
	public FilterablePane (Translator<V> vt) {
		super();
		assert vt != null;
		createView(vt);
		placeComponents();
		createController();
	}
	
    // REQUETES
    public JComboBox getCombo() { 
    	return filterTypes;
    }
    
    public JTextField getTextField() { 
    	return filterText;
    }
    
    public JList getList() { 
    	return filterableList;
    }
    
 
    // COMMANDES
    /**
     * Remplace les éléments du modèle de getList() par ceux de c.
     * @pre
     *     c != null
     * @post
     *     Soit m ::= getList().getModel()
     *     m.getSize() == c.size()
     *     forall i:c.contains(m.getElementAt(i))
     */
    public void setElements(Collection<E> c) { 
    	assert c != null;
    	((FilterableListModel<E, V>)filterableList.getModel()).setElements(c);
    }
    /**
     * Ajoute un élément à la fin du modèle de getList().
     * @pre
     *     element != null
     * @post
     *     Soit m ::= getList().getModel()
     *     m.getSize() == old m.getSize() + 1 
     *     m.getElementAt(m.getSize() - 1).equals(element)
     */
    public void addElement(E element) {
    	assert element != null;
    	((FilterableListModel<E, V>)filterableList.getModel()).addElement(element);
    }
    /**
     * Change le modèle de getCombo() par un nouveau modèle basé sur les
     *  éléments de filters.
     * @pre
     *     filters != null
     *     forall f:filters : f != null
     * @post
     *     Soit m ::= getCombo().getModel()
     *     m.getSize() == filters.size()
     *     forall i:m.getElementAt(i).equals(filters.get(i))
     */
    public void setFilters(List<Filter<E, V>> filters) {
    	assert filters != null;
    	for (Filter<E,V> f : filters) {
    		assert f != null;
    		filterTypes.addItem(f);
    	}
    }
    
    //OUTILS
    
    private void createView(Translator<V> vt) {
    	filterText = new JTextField(40);
    	MarkableFilterableListModel<E, V> model = new MarkableFilterableListModel<E, V>();
		filterableList = new JList<E>(model);
		translator = vt;
		filterTypes = new JComboBox<E>();
		renderer = new ItalicRenderer<E, V>(model);
		filterableList.setCellRenderer(renderer);
    }
    
    private void placeComponents() {
    	this.setLayout(new BorderLayout());
    	JPanel p = new JPanel(new BorderLayout()); {
    		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Recherche"));
    		p.add(filterTypes, BorderLayout.WEST);
    		p.add(filterText, BorderLayout.CENTER);
    	}
    	this.add(p, BorderLayout.NORTH);
		JScrollPane q = new JScrollPane(); {
			q.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Résultat"));
			q.setViewportView(filterableList);
		}
    	
    	this.add(q, BorderLayout.CENTER);
    }
    
    private void createController() {
		filterTypes.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				Filter<E,V> f = (Filter<E,V>)arg0.getItem();
				V v = translator.translateToValue(filterText.getText());
				f.setValue(v);
				((FilterableListModel<E, V>)filterableList.getModel()).setFilter(f);
			}
		});
		
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				act(arg0);
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				act(arg0);
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}
			
			private void act(DocumentEvent arg0) {
				Filter<E, V> f = ((FilterableListModel<E, V>)filterableList.getModel()).getFilter();
				String s = filterText.getText();
				V v = translator.translateToValue(s);
				f.setValue(v);
				System.out.println("value = " + s);
			}
		});
		
		filterableList.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {

			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {

			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {

			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {

			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2) {
					int index = filterableList.locationToIndex(arg0.getPoint());
					MarkableFilterableListModel<E, V> model = (MarkableFilterableListModel<E, V>)filterableList.getModel();
					E elmt = (E)model.getElementAt(index);
					model.toggleMark(elmt);
				}
			}
		});
    }
}
