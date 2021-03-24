package serie04;

public class Contract {
	public static void checkCondition(boolean cond) {
		if (!cond) {
			throw new AssertionError();
		}
	}
}
