/*
 *  Money_test.java
 *  
 *  @since June 19, 2018
 *  @author Xinmeng Zhang
 */
package edu.northeastern.cs_5004;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.FixMethodOrder;  
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runners.MethodSorters;

import edu.northeastern.cs_5004.Money.MismatchedCurrencyException;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

/**
 * This class performs unit tests for the Money class.
 */
public class Money_test {
	/**
	 * Test construction Money in different currencies and getCurrency getAmount
	 * Add new tests to construct money in different currencies
	 */
	@Test
	public void test_0010_Money() {
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale); //if not given Locale.country, default is US
		// default currency instance
		final Currency currency = Currency.getInstance(locale);
		assertEquals(currency.getDisplayName(), "US Dollar");
		// test constructing another instance of currency
		final Currency currency1 = Currency.getInstance(Locale.JAPAN);
		assertEquals(currency1.getDisplayName(), "Japanese Yen");
		
		// construct instance using double with more decimal places than currency
		Money money1 = new Money(10.1051); // Lacale.getDefault will get US
		// us dollar defaultfraction is 2
		assertEquals(10.11, money1.getAmount().doubleValue(), 0.0);
		assertEquals(currency, money1.getCurrency());
		
		// construct instance with a specified currency janpanese yen
		Money money2 = new Money(10.1051, currency1);
		// japanese yen defaultfraction is 0
		assertEquals(10.0, money2.getAmount().doubleValue(), 0.0);
		assertEquals(currency1, money2.getCurrency());

		// check string conversion with default and UK locale
		// special handling for 1-char currency symbol
		assertEquals("$10.11", money1.toString());
		//for the US Dollar, the symbol is "$" if the default locale is the US
		//while for other locales it may be US$
		assertEquals("US$ 10.11", money1.toString(Locale.UK));

		// construct instance using BigDecimal with unscaled long value and int scale,
		// with more decimal places than currency
		Money money3 = new Money(BigDecimal.valueOf(-101051, 4));  // -10.1051
		assertEquals(-10.11, money3.getAmount().doubleValue(), 0.0);
		assertEquals(currency, money3.getCurrency());
		
		// check string conversion with default and UK locale
		// special handling for 1-char currency symbol and negative number
		assertEquals("-$10.11", money3.toString());
		assertEquals("US$ -10.11", money3.toString(Locale.UK));

		// test handling of invalid doubles 
		try {
			new Money(Double.NaN);
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (NumberFormatException ex) {
			// add an output here to test the exception is caught!
			System.out.printf("NumberFormatException caught: %s\n", ex.getMessage());
		}
		
		try {
			new Money(Double.POSITIVE_INFINITY);
			System.out.println("This will never be excuted");
			fail();
		} catch (NumberFormatException ex) {
			System.out.printf("NumberFormatException caught: %s\n", ex.getMessage());
		}
	}
	
	/**
	 * Test compareTo and equals operations.
	 * Add new comparisions between different currencies which would campare CurrencyCode
	 */
	@Test
	public void test_0020_compareTo() {
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale);
		
		// test using same currency
		Money money1 = new Money(10.1051);
		Money money2 = new Money(-10.1051);

		// test comparison operators 
		assertTrue(money1.compareTo(money2) > 0);
		assertTrue(money2.compareTo(money1) < 0);
		assertTrue(money2.compareTo(money2) == 0);
		
		// test using different currencies
		final Currency currency1 = Currency.getInstance(Locale.JAPAN);
		Money money3 = new Money(10.1051, currency1);
		
		// test comparison operators 
		assertEquals("USD", money1.getCurrency().getCurrencyCode());
		assertEquals("JPY", money3.getCurrency().getCurrencyCode());
		assertTrue(money1.compareTo(money3) > 0);
		assertTrue(money3.compareTo(money1) < 0);
		assertTrue(money3.compareTo(money3) == 0);
		
		// test equals given that values are scaled to currency
		Money money4 = new Money(BigDecimal.valueOf(1, -2));
		Money money5 = new Money(BigDecimal.valueOf(100, 0));
		assertEquals(money4,money4);
		assertTrue(money5.compareTo(money4) == 0);
		

	}

	/**
	 * Test add operations.
	 * Add new tests to throw exceptions when adding different currencies
	 */
	@Test
	public void test_0030_add() {
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale);
		
		Money money1 = new Money(10.1051);
		Money money2 = new Money(-10.1051);

		// test add operations on Money
		Money money3;
		
		// test addition of number with its negative
		money3 = money1.add(money2);
		assertEquals(0.0, money3.getAmount().doubleValue(), 0.0);
		assertEquals(money1.getCurrency(), money3.getCurrency());
		
		// test addition of number with twice its negative
		money3 = money1.add(money2, money2);
		assertEquals(-10.11, money2.getAmount().doubleValue(), 0.0);
		assertEquals(money1.getCurrency(), money3.getCurrency());
		
		// test addition of 0.1 nine times itself
		money3 = new Money(0.1);
		money3 = money3.add(money3,money3,money3,money3,money3,money3,money3,money3,money3);
		assertEquals(1.0, money3.getAmount().doubleValue(), 0.0);
		
		// test handling of adding two different currencies
		final Currency currency1 = Currency.getInstance(Locale.JAPAN);
		Money money4 = new Money(-10.1051, currency1);
		try {
			money1.add(money4);	
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (MismatchedCurrencyException ex) {
			// add an output here to test the exception is caught!
			System.out.printf("MismatchedCurrencyException caught: %s\n", ex.getMessage());
		}
	}

	/**
	 * Test subtract operations.
	 * Add new tests to throw exceptions when subtracting different currencies
	 */
	@Test
	public void test_0040_subtract() {
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale);
		//final Currency currency = Currency.getInstance(locale);
		
		Money money1 = new Money(10.1051);
		Money money2 = new Money(-10.1051);

		// test subtract operations on Money
		Money money3;
		
		// test subtraction
		money3 = money1.subtract(money2);
		assertEquals(20.22, money3.getAmount().doubleValue(), 0.0);
		assertEquals(money1.getCurrency(), money3.getCurrency());
		
		// test handling of subtracting two different currencies
		final Currency currency1 = Currency.getInstance(Locale.JAPAN);
		Money money4 = new Money(-10.1051, currency1);
		try {
			money1.subtract(money4);	
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (MismatchedCurrencyException ex) {
			// add an output here to test the exception is caught!
			System.out.printf("MismatchedCurrencyException caught: %s\n", ex.getMessage());
		}
	}

	/**
	 * Test multiply operations.
	 * Add new tests to throw exceptions when multiplying different currencies
	 */
	@Test
	public void test_0050_multiply() {
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale);
		
		Money money1 = new Money(10.1051);
		Money money2 = new Money(-10.1051);

		// test multiply operations on Money
		Money money3;
		
		// test multiplication of number and its negative
		money3 = money1.multiply(money2);
		assertEquals(-102.21, money3.getAmount().doubleValue(), 0.0);
		assertEquals(money1.getCurrency(), money3.getCurrency());
		
		// test multiplication by long
		money3 = money1.multiply(-1);
		assertEquals(money2, money3);
		
		money3 = money2.multiply(-1);
		assertEquals(money1, money3);

		// test multiplication by double
		money3 = money1.multiply(-1.00);
		assertEquals(money2, money3);
		
		money3 = money2.multiply(-1.0);
		assertEquals(money1, money3);

		// test multiplication by double that has more decimal places that currency
		// -- rounding only happens after multiplication
		money3 = money1.multiply(1.005);  
		assertEquals(10.16, money3.getAmount().doubleValue(), 0.0);
		assertEquals(money1.getCurrency(), money3.getCurrency());

		// test multiplication by invalid double
		try {
			money2.multiply(Double.NaN);
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (NumberFormatException ex) {
			// add an output here to test the exception is caught!
			System.out.printf("NumberFormatException caught: %s\n", ex.getMessage());
		}
		
		// test handling of multiplying two different currencies
		final Currency currency1 = Currency.getInstance(Locale.JAPAN);
		Money money4 = new Money(-10.1051, currency1);
		try {
			money1.multiply(money4);	
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (MismatchedCurrencyException ex) {
			// add an output here to test the exception is caught!
			System.out.printf("MismatchedCurrencyException caught: %s\n", ex.getMessage());
		}
	}

	/**
	 * Test divide operations.
	 * Add new tests to throw exceptions when dividing different currencies
	 */
	@Test
	public void test_0060_divide() {		
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale);
		
		Money money1 = new Money(10.1051);
		Money money2 = new Money(-10.1051);

		// test divide operations on Money
		Money money3;
		
		// test division of number and its negative
		money3 = money1.divide(money2);
		assertEquals(-1.0, money3.getAmount().doubleValue(), 0.0);
		assertEquals(money1.getCurrency(), money3.getCurrency());

		// test division by long
		money3 = money1.divide(-1);
		assertEquals(money2, money3);
		
		money3 = money2.divide(-1);
		assertEquals(money1, money3);

		// test division by double
		money3 = money1.divide(-1.0);
		assertEquals(money2, money3);
		
		money3 = money2.divide(-1.0);
		assertEquals(money1, money3);

		// test division by invalid zero
		try {
			money1.divide(new Money(0.0));
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (ArithmeticException ex) {
			System.out.printf("ArithmeticException caught: %s\n", ex.getMessage());
		}
		
		// test division by invalid double
		try {
			money2.divide(Double.NaN);
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (NumberFormatException ex) {
			System.out.printf("NumberFormatException caught: %s\n", ex.getMessage());
		}
		
		// test handling of dividing two different currencies
		final Currency currency1 = Currency.getInstance(Locale.JAPAN);
		Money money4 = new Money(-10.1051, currency1);
		try {
			money1.divide(money4);	
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (MismatchedCurrencyException ex) {
			// add an output here to test the exception is caught!
			System.out.printf("MismatchedCurrencyException caught: %s\n", ex.getMessage());
		}
	}

	/**
	 * Test remainder operations.
	 * Add new tests to throw exceptions when calculating remainder on different currencies
	 */
	@Test
	public void test_0070_remainder() {
		
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale);
		
		Money money1 = new Money(10.1051);
		Money money2 = new Money(-10.1051);

		// test remainder operations on Money
		Money money3;
		
		// test remainder of number with its negative
		money3 = money1.remainder(money2);
		assertEquals(0.0, money3.getAmount().doubleValue(), 0.0);
		assertEquals(money1.getCurrency(), money3.getCurrency());
		
		// test remainder of number with 1
		money3 = money1.remainder(new Money(BigDecimal.ONE));
		assertEquals(0.11, money3.getAmount().doubleValue(), 0.0);
		assertEquals(money1.getCurrency(), money3.getCurrency());
		
		// test handling of get remainder of two different currencies
		final Currency currency1 = Currency.getInstance(Locale.JAPAN);
		Money money4 = new Money(-10.1051, currency1);
		try {
			money1.remainder(money4);	
			// the following 2 lines will never be excuted if there is exception
			System.out.println("This will never be excuted");
			fail();
		} catch (MismatchedCurrencyException ex) {
			// add an output here to test the exception is caught!
			System.out.printf("MismatchedCurrencyException caught: %s\n", ex.getMessage());
		}
	}

	/**
	 * Test negate and abs operations.
	 */
	@Test
	public void test_0080_abs() {
		
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale);
		
		Money money1 = new Money(10.1051);
		Money money2 = new Money(-10.1051);

		// test negate and abs operations on Money
		Money money3;
		
		// test negation
		money3 = money1.negate();
		assertEquals(money2, money3);
		
		money3 = money2.negate();
		assertEquals(money1, money3);

		// test abs
		money3 = money1.abs();
		assertEquals(money1, money3);
		
		money3 = money2.abs();
		assertEquals(money1, money3);

	}

	/**
	 * Test asCurrency function
	 */
	@Test
	public void test_0090_asCurrency() {
		// set locale and currency for testing purposes
		final Locale locale = Locale.US;
		Locale.setDefault(locale);
		
		// construct two instance of currency
		final Currency currency = Currency.getInstance(locale);
		assertEquals(currency.getDisplayName(), "US Dollar");
		final Currency currency1 = Currency.getInstance(Locale.JAPAN);
		assertEquals(currency1.getDisplayName(), "Japanese Yen");
		
		// test convert us dollar to us dollar, same currency so no change
		Money money1 = new Money(10.1051);
		assertEquals("$10.11", money1.toString(Locale.US));
		Money money2 = money1.asCurrency(currency, 110.60);
		// us dollar defaultfraction is 2, more detais of money2 as following
		assertEquals(2, currency.getDefaultFractionDigits());
		assertEquals(10.11, money2.getAmount().doubleValue(), 0.0);
		assertEquals("$10.11", money2.toString(Locale.US));
		
		// test convert us dollar to japanes yen
		// 110.60 as exchange rate between us dollar and japanese yen on 6/21/2018
		Money money3 = money1.asCurrency(currency1, 110.60);
		// japanese yen defaultfraction is 0, more detais of money3 as following
		assertEquals(0, currency1.getDefaultFractionDigits());
		assertEquals(1118.0, money3.getAmount().doubleValue(), 0.0);
		assertEquals("￥1118", money3.toString(Locale.JAPAN));
	}
	
	/**
	 * Run the tests in this class.
	 * 
	 * @param args the program arguments
	 */
	public static void main(String[] args) {
	    Result result = JUnitCore.runClasses(Money_test.class);
	    
	    System.out.println("[Unit Test Results]");
	    System.out.println();
	    
	    if (result.getFailureCount() > 0) {
	    	System.out.println("Test failure details:");
		    for (Failure failure : result.getFailures()) {
		       System.out.println(failure.toString());
		    }
		    System.out.println();
	    }
	    
	    int passCount = result.getRunCount()-result.getFailureCount()-result.getIgnoreCount(); 
	    System.out.println("Test summary:");
	    System.out.println("* Total tests = " + result.getRunCount());
	    System.out.println("* Passed tests: " + passCount);
	    System.out.println("* Failed tests = " + result.getFailureCount());
	    System.out.println("* Inactive tests = " + result.getIgnoreCount());
	}
}
