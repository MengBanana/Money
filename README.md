# Money
This version of Money creates instances that use the Currency for the default Locale. The constructors that include a Currency are 
included but have only package visibility to facilitate testing. Your assignent is to full enable Money operations in multiple currencies. 
There are several parts to this assignment.

Make the constructors that enable creating Money in multiple currencies public.
Operations between currencies are not supported. Create a new nested static class MismatchedCurrencyException that can be thrown 
in every Money operation on two or more currencies. Be surprised to learn that the base for this exception will be RuntimeException. 
This is one of those rare "exceptions" to the general rule that all developer-created exceptions should be checked exceptions. 
Money is a lot like Integer, Double, or BigDecimal where it is also considered burdensome to declare and catch every ArithmeticException 
or NumberFormatException that can be thrown. So enjoy this rare opportunity to work with an unchecked exception! 
(Do add @throws to methods that can throw the exception, however.)
Add a new method Money.asCurrency(Currency aCurrency, double exchangeRate) that converts an instance to one for the specified Currency 
by multiplying the instance amount by the given exchange rate, and returns the new instance with that amount and currency.Enhance the 
unit tests in Money_test to create and work with Money in multiple currencies, and to ensure that MismatchedCurrencyException is thrown 
where appropriate. Be sure to add a unit test for the new asCurrency() currency conversion method.
