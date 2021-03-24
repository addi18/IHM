package serie04;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import serie04.model.ProdConsModel;
import serie04.model.StdProdConsModel;

public class ProdCons {

    // CONSTANTES

    private static final int APPLI_WIDTH = 800;
    private static final int APPLI_HEIGHT = 600;
    private static final int FONT_SIZE = 12;
    private static final String SEPARATION_LINE =
        "--------------------------------------\n";

    // ATTRIBUTS

    private JButton starter;
    private JTextArea whiteBoard;
    private JFrame frame;
    private ProdConsModel model;

    public ProdCons(int prod, int cons, int iter) {
        createModel(prod, cons, iter);
        createView();
        placeComponents();
        createController();
    }

    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createModel(int prod, int cons, int iter) {
        // si le modèle était long à construire, il faudrait le faire sur un
        // autre thread que EDT...
        model = new StdProdConsModel(prod, cons, iter);
    }

    private void createView() {
        frame = new JFrame("Producteurs & Consommateurs");
        starter = new JButton("Démarrer");
        whiteBoard = new JTextArea();
        whiteBoard.setFont(new Font("Monospaced", Font.PLAIN, FONT_SIZE));
        whiteBoard.setEditable(false);
    }

    private void placeComponents() {
        JPanel p = new JPanel(); {
            p.add(starter);
        }
        frame.add(p, BorderLayout.NORTH);

        JScrollPane j = new JScrollPane(whiteBoard); {
            j.setPreferredSize(new Dimension(APPLI_WIDTH, APPLI_HEIGHT));
        }
        frame.add(j, BorderLayout.CENTER);
    }

    private void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        starter.addActionListener(new ActionListener() {
			//Observer Running qqpart...
			@Override
			public void actionPerformed(ActionEvent arg0) {
				whiteBoard.append(SEPARATION_LINE);
				model.start();
			}
		});
        
        model.addPropertyChangeListener(ProdConsModel.SENTENCE_NAME, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				whiteBoard.append((String) arg0.getNewValue());
			}
		});
        
        model.addPropertyChangeListener(ProdConsModel.FROZEN_NAME, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {

				Boolean frozen = (Boolean)arg0.getNewValue();

				if (frozen == true) {
					model.stop();
				}
			}
		});
        
        model.addPropertyChangeListener(ProdConsModel.RUNNING_NAME, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				Boolean running = (Boolean)arg0.getNewValue();
				if(running == true) {
					starter.setEnabled(false);
				} else {
					starter.setEnabled(true);
				}
				
			}
		});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProdCons(3, 2, 10).display();
            }
        });
    }
}
