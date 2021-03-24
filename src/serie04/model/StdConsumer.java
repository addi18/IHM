package serie04.model;

import serie04.util.Formatter;

public class StdConsumer extends AbstractActor {
	//ATTRIBUTS
	private int number = 0;
	private Formatter form;
	
	//CONSTRUCTEUR
	StdConsumer(int maxIter, Box box) {
		super(maxIter, box);
		
		++number;
		Formatter.resetTime();
		form = new Formatter("C" + number);
	}
	
	//REQUETES
	@Override
	protected boolean canUseBox() {
		return !getBox().isEmpty();
	}
	@Override
	public int number() {
		return number;
	}

	@Override
	public Formatter getFormatter() {
		return form;
	}
	
	//COMMANDE
	@Override
	protected void useBox() {
//		Box box = getBox();
//		fireSentenceSaid("box <-- " + box.getValue());
//		box.dump();
		fireSentenceSaid("box --> " + getBox().getValue());
		getBox().dump();
	}
}
