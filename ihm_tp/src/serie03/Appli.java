package serie03;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Appli {

    // ATTRIBUTS

    private static final int MAX_MOVES_PER_SEC = 10;
    private static final int WIDTH = 419;
    private static final int HEIGHT = 211;

    private JFrame frame;
    private Map<BKey, JButton> buttons;
    private JSlider slider;
    private GraphicAnimator anim;
    private Animable tested;

    // CONSTRUCTEURS

    public Appli() {
        createView();
        placeComponents();
        createController();
    }

    // COMMANDES

    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // initialisation de la vue en fonction de l'état du modèle
        slider.setMaximum(anim.getMaxSpeed());
        slider.setValue(anim.getSpeed());
        updateButtons();
    }

    // OUTILS

    private void createView() {
        frame = new JFrame();
    /* <-- deux barres obliques = SimpleComponent, une seule = MobileComponent
        tested = new SimpleComponent(WIDTH, HEIGHT);
        anim = new GraphicAnimator(
                tested,
                MAX_MOVES_PER_SEC);
    /*/
        final int ray = 10;
        final int dx = 1;
        final int dy = 1;
        tested = new MobileComponent(WIDTH, HEIGHT, ray);
        ((AnimableComponent) tested).setDiscShift(dx, dy);
        anim = new GraphicAnimator(
                tested,
                MAX_MOVES_PER_SEC * MAX_MOVES_PER_SEC);
    //*/
        slider = new JSlider();
        slider.setMajorTickSpacing(MAX_MOVES_PER_SEC / 2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        buttons = new EnumMap<BKey, JButton>(BKey.class);
        for (BKey k : BKey.values()) {
            buttons.put(k, new JButton(k.label));
        }
    }

    private void placeComponents() {
        JPanel p = new JPanel(); {
            for (BKey k : BKey.values()) {
                p.add(buttons.get(k));
            }
        }
        frame.add(p, BorderLayout.NORTH);

        p = new JPanel(); {
            p.add((Component) tested);
        }
        frame.add(p, BorderLayout.CENTER);

        frame.add(slider, BorderLayout.SOUTH);
    }

    private void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pilotage de l'animation
        buttons.get(BKey.START).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anim.startAnimation();
            }
        });
        buttons.get(BKey.PAUSE).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anim.pauseAnimation();
            }
        });
        buttons.get(BKey.RESUME).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anim.resumeAnimation();
            }
        });
        buttons.get(BKey.TERMINATE).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anim.stopAnimation();
            }
        });

        // Modification de la vitesse de l'animation
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                anim.setSpeed(slider.getValue());
            }
        });

        // Observation de l'animation
        anim.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateButtons();
            }
        });
    }

    private void updateButtons() {
        for (BKey k : BKey.values()) {
            buttons.get(k).setEnabled(k.enabledValue(anim));
        }
    }

    // POINT D'ENTREE

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Appli().display();
            }
        });
    }

    // TYPES IMBRIQUES

    private enum BKey {
        START("Start") {
            @Override boolean enabledValue(GraphicAnimator anim) {
                return !anim.isAnimationStarted();

            }
        },
        PAUSE("Pause") {
            @Override boolean enabledValue(GraphicAnimator anim) {
                return anim.isAnimationRunning() && !anim.isAnimationPaused();
            }
        },
        RESUME("Resume") {
            @Override boolean enabledValue(GraphicAnimator anim) {
                return anim.isAnimationRunning() && anim.isAnimationPaused();
            }
        },
        TERMINATE("Terminate") {
            @Override boolean enabledValue(GraphicAnimator anim) {
                return anim.isAnimationRunning();
            }
        };

        private String label;

        BKey(String lbl) {
            label = lbl;
        }

        abstract boolean enabledValue(GraphicAnimator anim);
    }
}
