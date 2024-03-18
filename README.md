<img src="src/main/resources/readme/pluginIcon.svg" height="100px"  alt="pluginIcon"/> <img style="float: right;" src="src/main/resources/readme/Decoratman.svg" height="100px"  alt="Decoratman"/>

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org) 
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

![Build Status](https://github.com/stepanvalyavskiy/Decorate.me/workflows/CI/badge.svg)
[![Hits-of-Code](https://hitsofcode.com/github/stepanvalyavskiy/decorate.me)](https://hitsofcode.com/view/github/stepanvalyavskiy/decorate.me)
[![JetBrains IntelliJ Plugins](https://img.shields.io/badge/jetbrains%20plugins%20repository-v1.3-blue)](https://plugins.jetbrains.com/plugin/14706-decorate-me/versions/stable/118855)

**Decorate.me** is an auto-completion Intellij IDEA 
20.1+ 
[plugin](https://plugins.jetbrains.com/plugin/14706-elegant-ide) 
suggesting you a list of all available [decorators](https://en.wikipedia.org/wiki/Decorator_pattern) for your Java object.
You have to watch this [two-minutes video](https://youtu.be/ZPHrfJN6f9Q) first!

Features:

  * Suggests a list of all available decorators (by type)
  * Wraps the object in a decorator
  * Quick Documentation (Ctrl+Q)
  * Quick Definition (Ctrl-Shift-I)

Here is how it works:

<img height="408" src="src/main/resources/readme/preview.gif" alt="demo"/>

## How to Use

Install using IDEA built-in plugin system:
  - <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Decorate.me"</kbd> > <kbd>Install</kbd>

Install manually:
  - Download the [latest release](https://plugins.jetbrains.com/plugin/14706-elegant-ide) and install it manually using <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>

## Motivation

I watched [the speech](https://youtu.be/75U9eefFYoU) 
and was struck by the beauty of an elegant object-
oriented code in comparison with the procedural one.
One of the main ideas is that classes should not have many methods,
there should be many classes with a minimal set of methods.
The proposed approach involves using composed decorators
(instead of methods) to configure the Object. 
[See more](https://www.elegantobjects.org/) about Elegant Objects approach.

The Maintainability of such design is undoubtedly several times higher.
However, code maintainability consists not only of its design, 
but also of tools that allow you to quickly and conveniently work with it.

Modern IDEs as IntelliJ IDEA have completions for methods.
Developers should only type a dot to get completions in a few milliseconds. 
Completions list contains all methods with their arguments and return type.
What about composed decorators? 
Will IDEA complete me, if I want to provide new functionality 
by decorating my Objects, instead of adding new methods?
No, it isn't.
 
So an absolutely reasonable [question](https://youtu.be/75U9eefFYoU?t=2498) was asked.

Loose translation:
 
> What simplifies maintainability here if IDEs have completions
with methods and their descriptions, 
but if I use small classes, I should learn all existing decorators?

> Very good question. IDEs are to blame
for the convenience of working in a procedural programming style. 
Probably after a while IDEs will make some new completions.

This guy is absolutely right. So we have to deal with it!
I still don't want to pack dozens of methods 
in utility classes only because IDE gives me suggestions and completions.
On the other hand, Developers, including myself, 
are so spoiled by good development environments(IDEs)
that they don’t want to give up such powerful features.
Sure, No one wants to manually search for methods that can be called.
Not to mention decorators, which, unlike methods,
can be scattered on the project in a more chaotic order.
However, there is no place for Tools-Driven Development.
Don't let the IDE decide how your code looks! RESIST!
I offer our weapon in the struggle for Elegant Objects.
With it, we can elegantly design classes and at the same time
use all the conveniences of modern tools.

If you still find the problem a bit abstract and are looking for a 
more concrete example, that's [it](https://youtu.be/LPLqLaSwSsI?t=6739)(in Russian).

It's easy to find and choose method, which you need,
when they are packed in one utility class.
You only have to type dot, and you instantly see all methods.
Also, you can have a look at its documentation or definition in a same tab.
Isn't it beautiful?  Sure It is! 
But what does then our class looks like?
At least it's just huge, not to mention other issues.
It seems like a trade off - the uglier the class, the easier it is for us 
to work with it in the development environment.
This is no longer the case. 
Let's have a look at object-oriented Java framework [Cactoos](https://github.com/yegor256/cactoos).
Consider its interface [Text](https://github.com/yegor256/cactoos/blob/master/src/main/java/org/cactoos/Text.java).
It's only can be converted to string by contract.
There is the Text [package](https://github.com/yegor256/cactoos/tree/master/src/main/java/org/cactoos/text)
which contains all implementations of Text interface.
They all are quite small. 
If you want to do some operation with your text,
you should use decorators instead of methods inside this class. 
Here is `Decorate.me` Intellij IDEA plugin tha eliminates the tooling difference between using methods and decorators.  
Just type dot after a constructor, then `Ctrl+Shift+Space`, and you will get all decorators suggestions.
Choose one and press enter. That's it!

## How It Works
 
Axiom: If two classes implement same interface or abstract class, 
and the second one has at least one constructor,
which has this interface or abstract class as argument, 
then the second class is a decorator of the first,
and this constructor is a decorating constructor.

This plugin finds all such decorating constructors 
and provide them in completion list. 
When choosing such a completion, the original expression will be wrapped
with the selected constructor.

If you have the same interface

![explanation](src/main/resources/readme/explanation.PNG?raw=true "explanation")

then `Decorator`'s constructor is a decorator for `ElegantObject` 
and for all classes that implement the `Decorable` interface.
So now if you type point after any object implementing the `Decorable` interface,
and call smart completion (Ctrl+Shift+Space)
`Decorator`'s constructor will be in completion list.

![example](src/main/resources/readme/example.PNG?raw=true "example")

## How to Contribute

You can contribute by forking the repo and sending a pull request. You will need these tools
to be installed locally:

 1. JDK 11
 2. Gradle 6.3+
 3. Lombok plugin
 4. Enabled annotation proccessing
  
Before sending a pull request, make sure all tests passed, and you covered your changes with new ones.
Intellij IDEA instance required for plugin tests, so I can't add them to pipeline on circleci. 

Therefore, you need to run them locally.
For that you need to declare an absolute path to your local IntelliJ IDEA in `build.gradle` and run `gradle test`. 
See [JB docs](https://jetbrains.org/intellij/sdk/docs/tutorials/writing_tests_for_plugins/tests_prerequisites.html?search=test).

Then we will review your changes and apply them to the master branch.

### Tips
 
- build the plugin

    `gradle build build`
 
- run embedded community edition idea with the plugin on board.
 
    `gradle intellij runIde`

- build and install the plugin to your own idea

    `gradle intellij buildPlugin`
  
    It generates a zip in `./build/distributions/`
    After that you have to install it manually in your IDE.

    <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd> > <kbd>Upload that zip</kbd>
  
