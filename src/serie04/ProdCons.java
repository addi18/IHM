package serie04;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import serie04.event.SentenceEvent;
import serie04.event.SentenceListener;
import serie04.model.AbstractActor;
import serie04.model.Actor;
import serie04.model.ProdConsModel;
import serie04.model.StdProdConsModel;
import serie04.util.Formatter;

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
	private Actor[]actors;

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
		actors = new AbstractActor[model.consumersNb() + model.producersNb()];
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
			@Override
			public void actionPerformed(ActionEvent e) {	 
				Formatter.resetTime();
				starter.setEnabled(false);

				whiteBoard.append(SEPARATION_LINE);
				for (int i = 0; i < model.producersNb(); i++) {
					actors[i] = model.producer(i);
				}
				for (int i = 0; i < model.consumersNb(); i++) {
					actors[i] = model.consumer(i);
				}
				for (Actor act : actors) {
					if (act != null) {
						final Actor actor = act;
						act.addSentenceListener(new SentenceListener() {
							@Override
							public void sentenceSaid(final SentenceEvent e) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										whiteBoard.append(
											((AbstractActor) 
											actor).getFormatter().
										format(e.getSentence()));
									}
								});
							} 
						});
					}
				}
				model.start();
				starter.setEnabled(true);
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
