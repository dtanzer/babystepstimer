Baby Steps Timer
================

A timer for the "baby steps" exercise/technique for doing TDD. But **actually**, this is some really bad piece of "legacy" code that you can use for refactoring exercises.

[Legacy Code Refactoring at Softwerkskammer Munich](http://www.davidtanzer.net/legacy_code_refactoring_at_softwerkskammer_munich): This blog post describes some exercises I am doing with groups of people using this code.  
Do you want me to facilitate this exercise at your conference / user group or meetup / your company? Contact me at Business@DavidTanzer.net

I love to hear your feedback on Twitter or per email. And if you use this code as a Kata, for yourself or during a meetup, please tell me - I love to hear your experiences! Also, if you have any questions, please contact me:  
Business@DavidTanzer.net  
[@dtanzer](https://twitter.com/dtanzer)

Contributors
------------

The [C# version of the original code](CSharp) was humbly contributed by Paul Rohorzka ([@paulroho](https://github.com/paulroho)).

Want to contribute a language or a new variant/twist to the existing code? Just open a pull request. Here are some characteristics of the original code that you may (or may not) want to preserve:

* **Single Responsibility Violations** Almost every part of the code violates the single responsibility principle (i.e. does more than one thing).
* **High Coupling** When you read or refactor the code, you'll see that almost every little bit of it depends on other, seemingly unrelated parts of the code (with different mechanisms).
* **Low Cohesion** Unrelated things happen close to each other in the code.
* **Low Level Constructs** The code does not "protect" itself from low level APIs. For example, it makes multiple calls to "System.currentTimeMillis" and uses threading constructs directly. This makes testing harder.
* **Reasonable Names** Things are mostly reasonably well named. I did not want to write _unreadable_ code: I wanted to create code that is hard to change (because of its structure / interdependencies), even though it is fairly easy to understand.
* **Complicated UI** This code renders HTML within a Swing application. You usually would not do it exactly like that in a "real" Java application (But I have really seen HTML inside a JPane in a real project to render a small part of the UI). On the other hand, this adds some fun string manipulating code...

How it works
------------

This is a little timer app you can use for the "Taking baby steps" TDD exercise: http://blog.adrianbolboaca.ro/2013/01/the-history-of-taking-baby-steps/

* You can move the timer window around with your mouse.
* Once you start the timer, the window stays always on top.
* When the timer counts down to zero, it becomes red because you did not take a baby step. Throw away everything you wrote in the two minutes before.
* When you finish your red/green/refactoring cycle before the timer counts down to zero, commit your changes and press "Reset" in the timer window.

Refactoring Screen Cast
-----------------------

I created this project for two reasons:
* To have a baby steps timer.
* To have some code for a refactoring screen cast (that was actually the real reason).

Because of the second reason, I started with pretty ugly code. I had two prototypes, one for playing the sounds and one for drawing the timer window.

I put them together in a class and added workaround after workaround, until the timer was working. I tried to make sure to make the code as hard to test as possible, while I still wanted it to be easy to understand.

But then I did not finish the screen cast, even though I started recording it. But I am still using this code to practice refactoring myself and to facilitate legacy code sessions at user group meetings and during trainings.

Acknowledgements
----------------

This project uses CC-Licensed sounds from freesound.org:
* Ships Bell by "acclivity" http://www.freesound.org/people/acclivity/sounds/32304/ http://www.freesound.org/people/acclivity/
* Bowl Struck by "suburban gorilla" http://www.freesound.org/people/suburban%20grilla/sounds/2166/ http://www.freesound.org/people/suburban%20grilla/

License
-------

Copyright 2013 David Tanzer (business@davidtanzer.net, @dtanzer)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
