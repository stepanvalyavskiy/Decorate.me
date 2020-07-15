# Prehistory:
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

Modern IDEs as Intellij IDEA have completions for methods.
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
in utility classes only because IDE gives me suggestions and completes methods.
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
Welcome the "Elegant IDE" Intellij IDEA plugin that makes your IDE elegant.

If you still find the problem a bit abstract and are looking for a 
more concrete example, that's [it](https://youtu.be/LPLqLaSwSsI?t=6739).

Loose translation: coming soon.

# How the plugin solve this probleb?

Watch this self-explanatory [video](https://youtu.be/ZPHrfJN6f9Q) at first.

It's easy to find and choose method, which you need,
when they are packed in one utility class.
You only have to type dot, and you instantly see all methods.
Also, you can have a look at its documentation or definition in a same tab.
Isn't it beautiful?  Sure It is! But what does then our class look like?
At least it's just huge, not to mention other issues.
It seems like a trade off - the uglier the class, the easier it is for us 
to work with it in the development environment.
This is no longer the case. Let's have a look at object-oriented Java framework Cactoos.
Consider its interface Text. It's only can be converted to string by contract.
There is the Text package which contains all implementations of Text interface.
They all are quite small. If you want to do some operation with your text,
you should use decorators instead of methods inside this class. 
Now with this plugin you will not see the difference. 
Just type dot after a constructor,then Ctrl+Shift+Space, and you will get all decorators suggestions 
with its documentation.
Choose one and press enter. That's it!

Plugin will wrap your object in decorator and perform imports.
If you are not sure which decorator you need,
use Ctrl+Q for Quick Documentation & Ctrl+Shift+I for Quick Definition.
Look how is it easy with TeeInputStream, that was a problem before.