# Stock Hawk 

Part of the Udacity Android Developer Nanodegree. The app was created by another student and is debugged and extended by me. The files added by me can be found under [this link](https://github.com/daniellehrner/stockhawk/search?utf8=%E2%9C%93&q=Created+by+Daniel+Lehrner&type=Code).

## Required extensions
* Each stock quote on the main screen is clickable and leads to a new screen which graphs the stock’s value over time. :white_check_mark:
* Stock Hawk does not crash when a user searches for a non-existent stock. :white_check_mark:
* Stock Hawk Stocks can be displayed in a collection widget.
* Stock Hawk app has content descriptions for all buttons. :white_check_mark:
* Stock Hawk app supports layout mirroring using both the LTR attribute and the start/end tags. :white_check_mark:
* Strings are all included in the strings.xml file and untranslatable strings have a translatable tag marked to false. :white_check_mark:
* Stock Hawk displays a default text on screen when offline, to inform users that the list is empty or out of date. :white_check_mark:

## Used libraries

### App
* [Otto](https://github.com/square/otto): Event bus
* [Dagger 2](https://google.github.io/dagger/): Dependency injection
* [Butterknife](https://jakewharton.github.io/butterknife/): View injection
* [WilliamChart](https://github.com/diogobernardino/WilliamChart): Line charts
* [RxAndroid](https://github.com/ReactiveX/RxAndroid): [Reactive Programming](https://gist.github.com/staltz/868e7e9bc2a7b8c1f754#what-is-reactive-programming)

### Instrumentation Tests
* [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/): UI test framework
* [Mockito](http://mockito.org/): Mocking framework

### Unit tests
* [Robolectric](http://robolectric.org/): Unit test framework