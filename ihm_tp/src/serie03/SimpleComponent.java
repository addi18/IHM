package serie03;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JLabel;

public class SimpleComponent extends JLabel implements Animable {

    // ATTRIBUTS

    private final Random r;

    // CONSTRUCTEURS

    public SimpleComponent(int w, int h) {
        super(" ");
        setOpaque(true);
        setPreferredSize(new Dimension(w, h));
        r = new Random();
    }

    // COMMANDE

    @Override
    public void animate() {
        setBackground(new Color(r.nextInt()));
    }
}
