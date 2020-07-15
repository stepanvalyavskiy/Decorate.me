<img height="408" width="800" src="src/main/resources/readme/preview.gif" alt="demo">

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org) 
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![Build Status](https://circleci.com/gh/stepanvalyavskiy/Decorate.me.svg?style=svg)](https://circleci.com/gh/stepanvalyavskiy/Decorate.me)
[![Hits-of-Code](https://hitsofcode.com/github/stepanvalyavskiy/decorate.me)](https://hitsofcode.com/view/github/stepanvalyavskiy/decorate.me)

[![JetBrains IntelliJ Plugins](https://img.shields.io/badge/jetbrains%20plugins%20repository-v1.1-blue)](https://plugins.jetbrains.com/plugin/14706-elegant-ide)

**Elegant IDE** is an [Intellij IDEA plugin](https://plugins.jetbrains.com/plugin/14706-elegant-ide) for decorators completion.
It looks for decorators and provide them in completion list,
if smart completion is called.
By choosing such a completion,
it wraps original expression in decorators constructor.
Quick Documentation and Quick Definition are also available in completion list.

**Motivation.** 
We like decorators and want to conveniently work with them.<br/>
See more detailed answer [here](README_Motivation.md) or watch this two minute [video](https://youtu.be/ZPHrfJN6f9Q)
 
**How to use**
- Using IDE built-in plugin system on Windows:
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Manage Plugin Repositiries...</kbd> > <kbd>Add "https://plugins.jetbrains.com/plugins/alpha/list"</kbd> > <kbd>Ok</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Elegant IDE"</kbd> > <kbd>Install Plugin</kbd>
- Using IDE built-in plugin system on MacOs:
  - <kbd>Preferences</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Manage Plugin Repositiries...</kbd> > <kbd>Add "https://plugins.jetbrains.com/plugins/alpha/list"</kbd> > <kbd>Ok</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Elegant IDE"</kbd> > <kbd>Install Plugin</kbd>
- Manually:
  - Download the [latest release](https://plugins.jetbrains.com/plugin/14706-elegant-ide) and install it manually using <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>

**How to start developing**

*Prerequisites*
 1. `JDK 11`
 2. `Gradle 6.3+` 
 3. `Lombok plugin`
 4. `Enabled annotation proccessing`
  
Check out and build the plugin
- <kbd>Gradle</kbd> > <kbd>Tasks</kbd> > <kbd>build</kbd> > <kbd>build</kbd>
 
*debug the plugin or see how your changes affected its behavior* in embedded idea(community edition) with the plugin on board.
- <kbd>Gradle</kbd> > <kbd>Tasks</kbd> > <kbd>intellij</kbd> > <kbd>Run or Debug "runIde"</kbd>

*install the plugin with your changes to your own idea*
  - <kbd>Gradle</kbd> > <kbd>Tasks</kbd> > <kbd>intellij</kbd> > <kbd>buildPlugin</kbd>
  
It generates a zip in `/build/distributions/`
After that you have to install it manually in your IDE.
  - <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd> > <kbd>Upload that zip</kbd>
  

##How it works:
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

then Decorator's constructor is a decorator for ElegantObject 
and for all classes that implement the Decorable interface.
So now if you type point after any object implementing the Decorable interface,
and call smart completion (Ctrl+Shift+Space)
Decorator's constructor will be in completion list.

![example](src/main/resources/readme/example.PNG?raw=true "example")

##Contribution
You can contribute by forking the repo and sending a pull request. 
We will review your changes and apply them to the master branch.

You are also welcome if you want to help me with CircleCI.
Pleas contact me at Telegram @clueqva.

