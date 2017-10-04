# Hints

## Test framework

In order to write tests we require some kind of constructs to express
the desired behavior. And we should be notified whenever the applications
behavior is different.

If you already have a favorite test framework and know how to set it
up within cmake, then just use that.

### Plain assert

One of the easiest to use frameworks is part of the C++ standard
library. You'll find it in `#include <cassert>` and it provides one
single test macro `assert`.

It has a few drawbacks, but it's a good option to get started fast.

### DIY

Another option consists of writing your own simple assert function
that throws an exception.

```cpp
class AssertionFailed : public std::exception {};

void assert_that(bool condition) {
  if(not condition) {
    throw AssertionFailed{};
  }
}
```

By calling your test functions in loop with a `try`-`catch` for each
test case, you can print _passed_ or _failed_ for each test case.

### Of-the-shelf

Popular options include [Catch](https://github.com/philsquared/Catch)
which provides a simple single header include.

Or [GoogleTest](https://github.com/google/googletest) which has
a lot of functionality.

## Write a first test

A high level system test requires two things. Interacting with the
application by _clicking_ on _start_, _stop_ etc. And accessing
the text within the application to check its state.

### Accessing the widget

Either can be achieved by having access to the `QTextBrowser` widget
variable `timerWidget`.
But we can not just change the variable into a public member variable
because of how Qt works. As a workaround replace `timerWidget` with a
`std::unique_ptr` and initialize and reset at the right places to
control the widgets lifetime.

### Test helpers

Interacting with the application:

```cpp
void click(BabystepsTimer const & testee, QString const & command) {
  emit testee._timerWidget->anchorClicked(QString{"command://" + command});
}
```

Reading its state:

```cpp
std::string ui_text(BabystepsTimer const & testee) {
  return testee._timerWidget->toPlainText().toStdString();
}
```

## Test and Qt loop threads

The application is started with the `exec()` method which will block
until execution finishes.
In order to interact with the application from the test we need a
separate thread for either the application or the test:

```cpp
void BabystepsTimer_is_set_to_two_minutes_when_created) {
  BabystepsTimer testee;

  std::thread test_thread{[&testee] {
    std::this_thread::sleep_for(std::chrono::milliseconds(200));

    assert_that(ui_text(testee).find("02:00") != std::string::npos);

    click(testee, "quit");
  }};

  testee.exec(0, nullptr);
  test_thread.join();
}
```

## Application exit

You may be wondering why the test exits even before the first test
has finished.

There is a _hard_ `exit(0)` when you want to close the application.
Replace it with `application.quit();` to stop the application without
exiting the test executable.



