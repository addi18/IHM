package serie04.model;

import serie04.util.Formatter;

public class StdConsumer extends AbstractActor {
	
	private static int num = 1;

	public StdConsumer(int maxIter, Box box) {
		super(maxIter, box, new Formatter("C"+num));
		num++;
	}

	@Override
	protected void useBox() {
		writeMsg("box --> " + box.getValue());
		box.dump();

	}

	@Override
	protected boolean canUseBox() {
		return !box.isEmpty();
	}


}
