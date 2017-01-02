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

**NOTE :**
You can't use spaces in a reference's name. As for naming references, I recommend using [lowerCamelCase](http://wiki.c2.com/?LowerCamelCase).



