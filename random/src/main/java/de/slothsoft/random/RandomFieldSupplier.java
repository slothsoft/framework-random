package de.slothsoft.random;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import de.slothsoft.random.types.BigDecimalRandomField;
import de.slothsoft.random.types.BigIntegerRandomField;
import de.slothsoft.random.types.BirthdayRandomField;
import de.slothsoft.random.types.BooleanRandomField;
import de.slothsoft.random.types.DateRandomField;
import de.slothsoft.random.types.DoubleRandomField;
import de.slothsoft.random.types.FirstNameRandomField;
import de.slothsoft.random.types.FloatRandomField;
import de.slothsoft.random.types.IntegerRandomField;
import de.slothsoft.random.types.LongRandomField;
import de.slothsoft.random.types.ShortRandomField;
import de.slothsoft.random.types.StreetRandomField;
import de.slothsoft.random.types.StringFromListRandomField;

/**
 * Interface for storing the display names, that are in fact ids, of the random fields
 * that can be accessed.
 *
 * @author Steffi Schulz
 */
public abstract class RandomFieldSupplier {

	private static List<RandomFieldSupplier> suppliers;

	public static RandomFieldSupplier findSupplierByField(String fieldName, Class<?> fieldClass) {
		final String name = fieldName.toLowerCase();
		for (final RandomFieldSupplier supplier : getAllSuppliers()) {
			if (supplier.canSupply(name, fieldClass)) return supplier;
		}
		return null;
	}

	public static List<RandomFieldSupplier> getAllSuppliers() {
		if (suppliers == null) {
			suppliers = new ArrayList<>();

			final String[] streets = readFile(RandomFieldSupplier.class.getResourceAsStream("text/street-names.txt"));
			suppliers.add(forSynonymeList("synonyms/street-names.txt", () -> new StreetRandomField(streets)));

			suppliers.add(forSynonymeList("synonyms/last-names.txt", createStringListSupplier("text/last-names.txt")));
			suppliers.add(forSynonymeList("synonyms/city-names.txt", createStringListSupplier("text/city-names.txt")));
			suppliers.add(forSynonymeList("synonyms/first-names.txt", FirstNameRandomField::new));
			suppliers.add(forSynonymeList("synonyms/birthdays.txt", BirthdayRandomField::new));

			suppliers.add(forFieldClass(Date.class, DateRandomField::new));

			suppliers.add(forFieldClass(Short.class, ShortRandomField::new));
			suppliers.add(forFieldClass(short.class, () -> new ShortRandomField().primitive(true)));
			suppliers.add(forFieldClass(Integer.class, IntegerRandomField::new));
			suppliers.add(forFieldClass(int.class, () -> new IntegerRandomField().primitive(true)));
			suppliers.add(forFieldClass(Long.class, LongRandomField::new));
			suppliers.add(forFieldClass(long.class, () -> new LongRandomField().primitive(true)));
			suppliers.add(forFieldClass(Double.class, DoubleRandomField::new));
			suppliers.add(forFieldClass(double.class, () -> new DoubleRandomField().primitive(true)));
			suppliers.add(forFieldClass(Float.class, FloatRandomField::new));
			suppliers.add(forFieldClass(float.class, () -> new FloatRandomField().primitive(true)));
			suppliers.add(forFieldClass(BigInteger.class, BigIntegerRandomField::new));
			suppliers.add(forFieldClass(BigDecimal.class, BigDecimalRandomField::new));
			suppliers.add(forFieldClass(Boolean.class, BooleanRandomField::new));
			suppliers.add(forFieldClass(boolean.class, () -> new BooleanRandomField().primitive(true)));
		}
		return suppliers;
	}

	static RandomFieldSupplier forFieldClass(Class<?> wantedFieldClass, Supplier<RandomField<?>> supplier) {
		return new RandomFieldSupplier(supplier) {

			@Override
			public boolean canSupply(String fieldName, Class<?> fieldClass) {
				return wantedFieldClass == fieldClass;
			}
		};
	}

	static RandomFieldSupplier forSynonymeList(String fileName, Supplier<RandomField<?>> supplier) {
		final List<String> synonyms = Arrays.asList(readFile(RandomFieldSupplier.class.getResourceAsStream(fileName)));
		return new RandomFieldSupplier(supplier) {

			@Override
			public boolean canSupply(String fieldName, Class<?> fieldClass) {
				return synonyms.contains(fieldName.toLowerCase());
			}
		};
	}

	private static Supplier<RandomField<?>> createStringListSupplier(String fileName) {
		final String[] strings = readFile(RandomFieldSupplier.class.getResourceAsStream(fileName));
		return () -> new StringFromListRandomField(strings);
	}

	static String[] readFile(InputStream inputStream) {
		return new BufferedReader(new InputStreamReader(inputStream)).lines().parallel().toArray(String[]::new);
	}

	private final Supplier<RandomField<?>> supplier;

	protected RandomFieldSupplier(Supplier<RandomField<?>> supplier) {
		this.supplier = supplier;
	}

	/**
	 * Returns if this random field should be used to render the field name.
	 *
	 * @param fieldName - name of the field
	 * @param fieldClass - class of the field
	 * @return a boolean - returns true if a {@link RandomField} can be supplied
	 */

	public abstract boolean canSupply(String fieldName, Class<?> fieldClass);

	public RandomField<?> createRandomField() {
		return this.supplier.get();
	}

}