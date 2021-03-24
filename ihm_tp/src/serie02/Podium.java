package serie02;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Composant graphique donnant une vue de PodiumModel.
 * Le paramètre de type du podium est un type implantant Drawable.
 * Un podium possède un modèle de type PodiumModel<E> qui est une propriété
 *  liée accessible en lecture-écriture.
 * @inv <pre>
 *     getModel() != null </pre>
 */
@SuppressWarnings("serial")
public class Podium<E extends Drawable> extends JComponent {

    // ATTRIBUTS

    private static final Color NO_ANIMAL = Color.LIGHT_GRAY;
    private static final Color PODIUM_COLOR = Color.BLACK;
    private static final Color NO_PODIUM_COLOR = Color.WHITE;
    private static final int BASE_HEIGHT = 5;
    private static final int PODIUM_HEIGHT = 2 * BASE_HEIGHT;
    private static final int LEG_WIDTH = 7;
    private static final int MARGIN = 2;

    private PodiumModel<E> model;

    // CONSTRUCTEURS

    /**
     * Un podium de modèle pm.
     * @pre <pre>
     *     pm != null </pre>
     * @post <pre>
     *     getModel() == pm </pre>
     */
    public Podium(PodiumModel<E> pm) {
        Contract.checkCondition(pm != null);

        setModel(pm);
        setPreferredSize(new Dimension(
        		Drawable.ELEM_WIDTH + 2 * MARGIN,
        		pm.capacity() * Drawable.ELEM_HEIGHT + PODIUM_HEIGHT)
        		);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }

    // REQUETES

    /**
     * Le modèle de ce Podium.
     */
    public PodiumModel<E> getModel() {
        return model;
    }

    // COMMANDES

    /**
     * Fixe un nouveau modèle pour ce Podium.
     * @pre <pre>
     *     m != null </pre>
     * @post <pre>
     *     getModel() == m </pre>
     */
    public void setModel(PodiumModel<E> m) {
        Contract.checkCondition(m != null);
        
        model = m;
        model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				repaint();
			}
        });
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int capacity = model.capacity();
        int size = model.size();
        int x = (getWidth() - Drawable.ELEM_WIDTH) / 2;
        int y = getHeight() - PODIUM_HEIGHT;
        // rectangle noir du bas
        // largeur = largeur d'un animal
        // hauteur = hauteur totale du podium, pieds compris
        g.setColor(PODIUM_COLOR);
        g.fillRect(x, y, Drawable.ELEM_WIDTH, PODIUM_HEIGHT);
        // rectangle blanc du bas
        // largeur = largeur d'un animal - largeur des pieds du podium
        // hauteur = moitié de la hauteur totale du podium
        g.setColor(NO_PODIUM_COLOR);
        g.fillRect(
            x + LEG_WIDTH,
            y + PODIUM_HEIGHT - BASE_HEIGHT,
            Drawable.ELEM_WIDTH - 2 * LEG_WIDTH,
            BASE_HEIGHT
        );
        // les animaux
        for (int i = 0; i < size; i++) {
            y -= Drawable.ELEM_HEIGHT;
            model.elementAt(i).draw(g, x, y);
        }
        // le vide au-dessus des animaux
        g.setColor(NO_ANIMAL);
        int h = Drawable.ELEM_HEIGHT * (capacity - size);
        g.fillRect(x, y - h, Drawable.ELEM_WIDTH, h);
    }
}
