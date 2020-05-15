# Prehistory:
I watched [the speech](https://youtu.be/75U9eefFYoU) and was struck by the beauty of an elegant object-oriented code in comparison with the procedural one.
One of the main ideas is that classes should not have many methods, there should be many classes with a minimal set of methods.
The proposed approach involves using composed decorators(instead of methods) to configure the Object. 
[See more](https://www.elegantobjects.org/) about Elegant Objects approach.

The Maintainability of such design is undoubtedly several times higher.
However, code maintainability consists not only of its design, but also of tools that allow you to quickly and conveniently work with it.

Modern IDEs as Intellij IDEA have completions for methods. 
Developers should only type a dot to get completions in a few milliseconds. 
Completions list contains all methods with their arguments and return type.
What about composed decorators? 
If I want to decorate my Objects, to provide new functionality, instead of adding new methods? Will IDEA complete me? 
Not Really.
 
So an absolutely reasonable [question](https://youtu.be/75U9eefFYoU?t=2498) was asked.

My loose translation:
 
\- What simplifies maintainability here if IDEs have completions with methods ant their descriptions, but if I use small classes, I should learn all existing decorators?

\- Very good question. IDEs are to blame for the convenience of working in a procedural programming style. Probably after a while IDEs will make some new completions.

I am attracted to this approach, but I absolutely do not want to keep in mind all the existing classes. 
So after this question and answer to it the idea to make the Intellij IDEA plugin "Decorate.me" was born.

# How it works:
Axiom: If two classes implement same Interface, and the second one has at least one constructor, which has this Interface as argument, 
then the second class is a decorator of the first, and this constructor is a decorating constructor.

This plugin finds all such decorating constructor and provide them in completion list.
When choosing such a completion, the original expression will be wrapped with the selected constructor.

It's only a Proof of Concept. I'm still reading IDEA documentation and Elegant Objects vol. 1. 
Many other features are coming... 

# [Screencast Example](https://www.youtube.com/watch?v=dQw4w9WgXcQ)



