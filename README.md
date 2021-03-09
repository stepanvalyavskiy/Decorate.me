<img src="src/main/resources/readme/pluginIcon.svg" height="100px"  alt="pluginIcon"/> <img style="float: right;" src="src/main/resources/readme/Decoratman.svg" height="100px"  alt="Decoratman"/>

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org) 
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![Build Status](https://circleci.com/gh/stepanvalyavskiy/Decorate.me.svg?style=svg)](https://circleci.com/gh/stepanvalyavskiy/Decorate.me)
[![Hits-of-Code](https://hitsofcode.com/github/stepanvalyavskiy/decorate.me)](https://hitsofcode.com/view/github/stepanvalyavskiy/decorate.me)
[![JetBrains IntelliJ Plugins](https://img.shields.io/badge/jetbrains%20plugins%20repository-v1.1-blue)](https://plugins.jetbrains.com/plugin/14706-elegant-ide)

**Decorate.me** is an auto-completion Intellij IDEA 
[19.3+](https://plugins.jetbrains.com/plugin/14706-elegant-ide/versions) 
[plugin](https://plugins.jetbrains.com/plugin/14706-elegant-ide) 
suggesting you a list of all available [decorators](https://en.wikipedia.org/wiki/Decorator_pattern) for your Java object.
You have to watch this [two-minutes video](https://youtu.be/ZPHrfJN6f9Q) first!

Features:

  * Suggests a list of all available decorators (by type)
  * Wraps the object in a decorator
  * Quick Documentation (Ctrl+Q)
  * Quick Definition (Ctrl-Shift-I)

Here is how it works:

<img height="300px" src="src/main/resources/readme/preview.gif" alt="demo"/>

## How to Use

Install using IDE built-in plugin system on Windows:
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Manage Plugin Repositiries...</kbd> > <kbd>Add "https://plugins.jetbrains.com/plugins/alpha/list"</kbd> > <kbd>Ok</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Elegant IDE"</kbd> > <kbd>Install Plugin</kbd>

Install using IDE built-in plugin system on MacOs:
  - <kbd>Preferences</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Manage Plugin Repositiries...</kbd> > <kbd>Add "https://plugins.jetbrains.com/plugins/alpha/list"</kbd> > <kbd>Ok</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Elegant IDE"</kbd> > <kbd>Install Plugin</kbd>

Install manually:
  - Download the [latest release](https://plugins.jetbrains.com/plugin/14706-elegant-ide) and install it manually using <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>

## Motivation



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

Check out and build the plugin
- <kbd>Gradle</kbd> > <kbd>Tasks</kbd> > <kbd>build</kbd> > <kbd>build</kbd>
 
*debug the plugin or see how your changes affected its behavior* in embedded idea(community edition) with the plugin on board.
- <kbd>Gradle</kbd> > <kbd>Tasks</kbd> > <kbd>intellij</kbd> > <kbd>Run or Debug "runIde"</kbd>

*build and install the plugin to your own idea*
  - <kbd>Gradle</kbd> > <kbd>Tasks</kbd> > <kbd>intellij</kbd> > <kbd>buildPlugin</kbd>
  
It generates a zip in `/build/distributions/`
After that you have to install it manually in your IDE.
  - <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd> > <kbd>Upload that zip</kbd>
  
