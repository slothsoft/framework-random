package de.slothsoft.random.types;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.random.RandomFactory;

public abstract class AbstractNumberRandomFieldTest<N extends Number> extends AbstractRandomFieldTest {

	protected AbstractNumberRandomField<N> numberField;

	private final N seven;
	private final N ninetyTwo;

	public AbstractNumberRandomFieldTest(Object pojo, N seven, N ninetyTwo) {
		super(pojo);
		this.seven = seven;
		this.ninetyTwo = ninetyTwo;
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.numberField = createRandomField();
	}

	@Override
	protected abstract AbstractNumberRandomField<N> createRandomField();

	@Override
	@Test
	public void testConstructorEmptyMap() throws Exception {
		final RandomFactory<?> randomFactory = new RandomFactory<>(() -> this.pojo, new HashMap<>());

		Assert.assertEquals(null, randomFactory.findRandomField(this.property));
	}

	@Test
	public void testRandomFactoryStartValue() throws Exception {
		final RandomFactory<?> randomFactory = new RandomFactory<>(() -> this.pojo, new HashMap<>());
		randomFactory.addRandomField(this.property, this.numberField);

		Assert.assertSame(this.numberField, randomFactory.findRandomField(this.property));

		this.numberField.setStartValue(this.ninetyTwo);

		for (int i = 0; i < 100; i++) {
			final Object createdPojo = randomFactory.createSingle();
			final Number value = (Number) getPropertyValue(createdPojo);
			Assert.assertNotNull(value);
			Assert.assertTrue("Value is too small: " + value, value.doubleValue() >= this.ninetyTwo.doubleValue());
		}
	}

	@Test
	public void testStartValue() throws Exception {
		this.numberField.setStartValue(this.ninetyTwo);

		for (int i = 0; i < 100; i++) {
			final N value = this.numberField.nextValue();
			Assert.assertNotNull(value);
			Assert.assertTrue("Value is too small: " + value, value.doubleValue() >= this.ninetyTwo.doubleValue());
		}
	}

	@Test
	public void testRandomFactoryEndValue() throws Exception {
		final RandomFactory<?> randomFactory = new RandomFactory<>(() -> this.pojo, new HashMap<>());
		randomFactory.addRandomField(this.property, this.numberField);

		Assert.assertSame(this.numberField, randomFactory.findRandomField(this.property));

		this.numberField.setEndValue(this.ninetyTwo);

		for (int i = 0; i < 100; i++) {
			final Object createdPojo = randomFactory.createSingle();
			final Number value = (Number) getPropertyValue(createdPojo);
			Assert.assertNotNull(value);
			Assert.assertTrue("Value is too big: " + value, value.doubleValue() < this.ninetyTwo.doubleValue());
		}
	}

	@Test
	public void testEndValue() throws Exception {
		this.numberField.setEndValue(this.ninetyTwo);

		for (int i = 0; i < 100; i++) {
			final N value = this.numberField.nextValue();
			Assert.assertNotNull(value);
			Assert.assertTrue("Value is too big: " + value, value.doubleValue() < this.ninetyTwo.doubleValue());
		}
	}

	@Test
	public void testRandomFactoryStartValueAndEndValue() throws Exception {
		final RandomFactory<?> randomFactory = new RandomFactory<>(() -> this.pojo, new HashMap<>());
		randomFactory.addRandomField(this.property, this.numberField);

		Assert.assertSame(this.numberField, randomFactory.findRandomField(this.property));

		this.numberField.setStartValue(this.seven);
		this.numberField.setEndValue(this.ninetyTwo);

		for (int i = 0; i < 100; i++) {
			final Object createdPojo = randomFactory.createSingle();
			final Number value = (Number) getPropertyValue(createdPojo);
			Assert.assertTrue("Value is too small: " + value, value.doubleValue() >= this.seven.doubleValue());
			Assert.assertTrue("Value is too big: " + value, value.doubleValue() < this.ninetyTwo.doubleValue());
		}
	}

	@Test
	public void testStartValueAndEndValue() throws Exception {
		this.numberField.setStartValue(this.seven);
		this.numberField.setEndValue(this.ninetyTwo);

		for (int i = 0; i < 100; i++) {
			final N value = this.numberField.nextValue();
			Assert.assertNotNull(value);
			Assert.assertTrue("Value is too small: " + value, value.doubleValue() >= this.seven.doubleValue());
			Assert.assertTrue("Value is too big: " + value, value.doubleValue() < this.ninetyTwo.doubleValue());
		}
	}

	@Test
	public void testRandomFactoryStartValueAndEndValueSwitched() throws Exception {
		final RandomFactory<?> randomFactory = new RandomFactory<>(() -> this.pojo, new HashMap<>());
		randomFactory.addRandomField(this.property, this.numberField);

		Assert.assertSame(this.numberField, randomFactory.findRandomField(this.property));

		this.numberField.setStartValue(this.ninetyTwo);
		this.numberField.setEndValue(this.seven);

		for (int i = 0; i < 100; i++) {
			final Object createdPojo = randomFactory.createSingle();
			final Number value = (Number) getPropertyValue(createdPojo);
			Assert.assertTrue("Value is too small: " + value, value.doubleValue() >= this.seven.doubleValue());
			Assert.assertTrue("Value is too big: " + value, value.doubleValue() < this.ninetyTwo.doubleValue());
		}
	}

	@Test
	public void testStartValueAndEndValueSwitched() throws Exception {
		this.numberField.setStartValue(this.ninetyTwo);
		this.numberField.setEndValue(this.seven);

		for (int i = 0; i < 100; i++) {
			final N value = this.numberField.nextValue();
			Assert.assertNotNull(value);
			Assert.assertTrue("Value is too small: " + value, value.doubleValue() >= this.seven.doubleValue());
			Assert.assertTrue("Value is too big: " + value, value.doubleValue() < this.ninetyTwo.doubleValue());
		}
	}
	@Test
	public void testRandomFactoryStartValueEqualsEndValue() throws Exception {
		final RandomFactory<?> randomFactory = new RandomFactory<>(() -> this.pojo, new HashMap<>());
		randomFactory.addRandomField(this.property, this.numberField);

		Assert.assertSame(this.numberField, randomFactory.findRandomField(this.property));

		this.numberField.setStartValue(this.ninetyTwo);
		this.numberField.setEndValue(this.ninetyTwo);

		for (int i = 0; i < 100; i++) {
			final Object createdPojo = randomFactory.createSingle();
			final Number value = (Number) getPropertyValue(createdPojo);
			Assert.assertEquals(value.doubleValue(), this.ninetyTwo.doubleValue(), 0.001);
		}
	}

	@Test
	public void testStartValueEqualsEndValue() throws Exception {
		this.numberField.setStartValue(this.seven);
		this.numberField.setEndValue(this.seven);

		for (int i = 0; i < 100; i++) {
			final N value = this.numberField.nextValue();
			Assert.assertNotNull(value);
			Assert.assertEquals(value.doubleValue(), this.seven.doubleValue(), 0.001);
		}
	}

	@Test
	public void testSetEndValue() throws Exception {
		this.numberField.setEndValue(this.seven);
		Assert.assertEquals(this.seven, this.numberField.getEndValue());

		this.numberField.endValue(this.ninetyTwo);
		Assert.assertEquals(this.ninetyTwo, this.numberField.getEndValue());
	}

	@Test
	public void testSetStartValue() throws Exception {
		this.numberField.setStartValue(this.seven);
		Assert.assertEquals(this.seven, this.numberField.getStartValue());

		this.numberField.startValue(this.ninetyTwo);
		Assert.assertEquals(this.ninetyTwo, this.numberField.getStartValue());
	}

}
