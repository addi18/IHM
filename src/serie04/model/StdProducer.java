package serie04.model;

import java.util.Random;

import serie04.util.Formatter;

public class StdProducer extends AbstractActor {
	//ATTRIBUTS
	private int pNum = 0;
	private int maxVal;
	private Random randVal;
	private Formatter form;
	
	//CONSTRUCTOR
	StdProducer(int maxIterNb, int maxV, Box box) {
		super(maxIterNb, box);
		
		Formatter.resetTime();
		maxVal = maxV;
		++pNum;
		form = new Formatter("P" + pNum);
		randVal = new Random();
	}
	
	//REQUETES
	@Override
	protected boolean canUseBox() {
		return getBox().isEmpty();
	}
	
	@Override
	public int number() {
		return pNum;
	}

	@Override
	public Formatter getFormatter() {
		return form;
	}
	
	//COMMANDE
	@Override
	protected void useBox() {
		int v = randVal.nextInt(maxVal);
		getBox().fill(v);
		fireSentenceSaid("box <-- " + v);
	}
}
