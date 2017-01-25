# QuarSk

QuarSk is a new cutting-edge addon for Skript !

## Features :

### References :

References are a new way to simplify your code and make it more readable !

####Explanation :
Using any modification (add, set, remove) on a reference will affect what the reference was set to.
And displaying the reference will make it update itself. References start with `@`.

**Example :**
```yml
link @reference to metadata "sth" of last spawned pig

send "%@reference%"
#will show "<none>" because the metadata isn't set

set @reference to "stuff"
#will be the same as
set metadata "sth" of last spawned pig  to "stuff"

send "%@reference%"
#will send "stuff"

#to unlink a reference :
unlink @reference
```

**Syntaxes :**
Everything is in the example, but I thought I'd put the actual syntaxes.
**Link/unlink :**
```
link @<.+> to %object%
unlink @<.+>
```
**Accessing a reference :**
```
@<.+>
```
The parts between < and > is regex. But you don't need to know that.
**NOTE :**
You can't use spaces in a reference's name. As for naming references, I recommend using [lowerCamelCase](http://wiki.c2.com/?LowerCamelCase).


###Orientation :

There is also an easy-to-use orienting effect that allows you to make any entity face a defined location !

**Syntaxes :**
```
orient %entity% (towards|away from) %location%
make %entity% (face|look [at]) ([towards]|away from) %location%
force %entity% to (look [at]|face) ([towards]|away from) %location%
```

###Potion stuff

I'm glad to present you Potion Control, the new QuarSk 1.1 feature ! It adds 2 effects, 1 condition, 8 expressions and 1 type to this addon ! If you have any suggestion, suggest it anywhere !

**Syntaxes :**
```java
//Effects :
apply [potion] [effect[s] [of]] %potioneffects% to %livingentities%

milk %livingentities% //Removes all potion effects from an entity

//Condition :
[entity] %livingentity% (has [got]|has( not|n't) [got]) [(the|a)] %potioneffecttype% [potion] effect

//Expressions :
[[potion] effect [(with|by)]] %potioneffecttype% for %timespan% with [a] [tier [of]] %number% [particles %-boolean%[ with ambient [effect] %-boolean%[ and [particle] colo[u]r[ed] %-color%]]]]] //Returns a "potioneffect" type, which is used quite a lot

[(normal|splash|linger[ing])] potion (of|by|with|from) [effect[s]] %potioneffects%

[(all|every|each)] [active] [potion] effects (on|in) %livingentities%
[(every|all|each) of] %livingentities%['s] [active] [potion] effect[s]

[(all|every|each)] [potion] effect[s] (on|of) %itemstack% //The item can only be a potion
[(all|every|each) of] %itemstack%['s] [potion] effect[s]

//A "potioneffect" is made of a potion effect type (speed, strength...), duration and other parameters. This only returns the potion effect type 
potion[ ]effect[[ ]type][s] of %potioneffect% 
%potioneffect%['s] potion[ ]effect[[ ]type][s]

//Cannot be set because Spigot doesn't allow it
(duration|length) of [potion] effect[s] %potioneffect%
[potion] effect[s] %potioneffect%['s] (duration|length)

//Same here
(tier|level|amplifier|power) of [potion] [effect] %potioneffect%
[potion] [effect] %potioneffect%['s] (tier|amplifier|level|power)
```
Hope you enjoy !
