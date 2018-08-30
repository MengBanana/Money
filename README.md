# Money
This version of Money creates instances that use the Currency for the default Locale. The constructors that include a Currency are 
included but have only package visibility to facilitate testing. Enable Money operations in multiple currencies. 

The constructors that enable creating Money in multiple currencies should be public.

Operations between currencies are not supported. A nested static class MismatchedCurrencyException that can be thrown 
in every Money operation on two or more currencies. The base for this exception will be RuntimeException. This is one of those rare "exceptions" to the general rule that all developer-created exceptions should be checked exceptions. 

Money.asCurrency(Currency aCurrency, double exchangeRate) that converts an instance to one for the specified Currency 
by multiplying the instance amount by the given exchange rate, and returns the new instance with that amount and currency.

