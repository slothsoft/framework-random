package de.slothsoft.random.types;

public class ShortRandomFieldPrimitiveTest extends AbstractNumberRandomFieldTest<Short> {

	public static class Pojo {

		private short value;

		public short getValue() {
			return this.value;
		}

		public void setValue(short value) {
			this.value = value;
		}

	}

	public ShortRandomFieldPrimitiveTest() {
		super(new ShortRandomFieldPrimitiveTest.Pojo(), Short.valueOf((short) 7), Short.valueOf((short) 92));
	}

	@Override
	protected AbstractNumberRandomField<Short> createRandomField() {
		return new ShortRandomField();
	}
}
