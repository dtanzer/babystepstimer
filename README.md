Baby Steps Timer
================

A timer for the "baby steps" exercise/technique for doing TDD. But **actually**, this is some really bad piece of "legacy" code that you can use for refactoring exercises.

[Legacy Code Refactoring at Softwerkskammer Munich](http://www.davidtanzer.net/legacy_code_refactoring_at_softwerkskammer_munich): This blog post describes some exercises I am doing with groups of people using this code.  
Do you want me to facilitate this exercise at your conference / user group or meetup / your company? Contact me at Business@DavidTanzer.net

I love to hear your feedback on Twitter or per email. And if you use this code as a Kata, for yourself or during a meetup, please tell me - I love to hear your experiences! Also, if you have any questions, please contact me:  
Business@DavidTanzer.net  
[@dtanzer](https://twitter.com/dtanzer)

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
1. To have a baby steps timer.
2. To have some code for a refactoring screen cast.

Because of the second reason, I started with pretty ugly code. I had two prototypes, one for playing the sounds and one for drawing the timer window.
I put them together in a class and added workaround after workaround, until the timer was working.

Then I started to refactor the code, until it was in a state where I was not embarrassed anymore. It's still not perfect, but it is reasonably nice now.
I recorded my refactoring sessions, and I will create a three-part screen cast of the refactoring in the coming weeks. You can still find the original code
under the "OriginalCode" folder in this repository.

I did not use the "Baby Steps" technique during the refactoring, though. I don't think it would have improved the screen cast videos. And I think I
would not have been able to pull it off - I am just not that good at it. Yet. But if you want to try to refactor the original code using the
"Baby Steps" technique, feel free to try. And please tell me how it was: david@davidtanzer.net or on Twitter: http://twitter.com/dtanzer

Also, if you have any comments on the final code quality or the app itself, feel free to contact me.

The screen casts are not ready for publishing yet, but I will announce them on Twitter as soon as they are online. If you are interested, just follow me
and stay tuned: http://twitter.com/dtanzer

Acknowledgements
----------------

This project uses CC-Licensed sounds from freesound.org:
* Ships Bell by "acclivity" http://www.freesound.org/people/acclivity/sounds/32304/ http://www.freesound.org/people/acclivity/
* Bowl Struck by "suburban gorilla" http://www.freesound.org/people/suburban%20grilla/sounds/2166/ http://www.freesound.org/people/suburban%20grilla/

Contributors
------------

The [C# version of the original code](CSharp) was humbly contributed by Paul Rohorzka ([@paulroho](https://github.com/paulroho)).

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
