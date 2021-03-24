package serie04.model;

import java.util.Random;

import serie04.util.Formatter;

public class StdProducer extends AbstractActor {

	private final int maxValue;
	private Random rand;
	private static int num = 1;
	
	public StdProducer(int maxIter, int maxValue, Box box) {
		super(maxIter, box, new Formatter("P"+num));
		this.maxValue = maxValue;
		num++;
		rand = new Random();
	}

	@Override
	protected void useBox() {
		int value = rand.nextInt(maxValue);
		writeMsg("box <-- " + value);
		box.fill(value);

	}

	@Override
	protected boolean canUseBox() {
		return box.isEmpty();
	}
}
