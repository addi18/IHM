package serie08;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public enum Gender {
    MALE(
            "fils",
            new Color(200, 210, 255),
            new Color(99, 130, 191)
    ),
    FEMALE(
            "fille",
            new Color(255, 175, 175),
            new Color(191, 88, 88)
    );

    // les icones proviennent de http://www.fatcow.com/free-icons/
    private static final ImageIcon[] ICONS = new ImageIcon[] {
        createImageIcon("/serie08/images/utilisateur.png"),
        createImageIcon("/serie08/images/utilisatrice.png")
    };

    // ATTRIBUTS

    private String desc;
    private Color background;
    private Color border;

    // CONSTRUCTEURS

    Gender(String d, Color bk, Color bd) {
        desc = d;
        background = bk;
        border = bd;
    }

    // REQUETES

    public String getDesc() {
        return desc;
    }

    public Color getBackgroundSelectionColor() {
        return background;
    }

    public Color getBorderSelectionColor() {
        return border;
    }

    public ImageIcon getImage() {
        return ICONS[ordinal()];
    }

    // OUTILS

    private static ImageIcon createImageIcon(String path) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        java.net.URL imgURL = Gender.class.getResource(path);
        if (imgURL == null) {
            System.err.println("Aucune ressource de nom " + path);
            return null;
        }
        return new ImageIcon(tk.getImage(imgURL));
    }
}
