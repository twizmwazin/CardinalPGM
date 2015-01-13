CardinalPGM
===========

This is the next PGM clone.

##In Development
This plugin is in early alpha. I make no guarantees as to what will and what will not work. Features are in development, and many are unfinished or broken. At this point in time, this plugin is not ready for general use. However, if you are a developer or you just want to try to help find bugs, please feel free to try this out at your own risk.

Feel free to submit pull requests and/or issues to help further development. If you find an issue, please submit an issue so a developer can resolve it as soon as possible. If an issue is found with a dependency, submit your issue to the dependency's repository.

##Compiling
To compile and run the latest development version of CardinalPGM, you will need [JDK7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) and [Maven](http://maven.apache.org/). On most Linux distributions, you can find both packages in your default repositories. To compile, simply run `mvn clean install`. The plugin should be found in `target/`.

##Permissions

- cardinal.version (Gets your CardinalPGM version)
- cardinal.match.cancel (Cancels map start countdown)
- cardinal.match.cycle (Cycles to the next map)
- cardinal.match.setnext (Sets the next map)
- cardinal.match.start (Starts a match)
- cardinal.match.end  (Ends a match)
- cardinal.match.join (Joins a team)
- cardinal.match.leave (Leaves a team)
- cardinal.map (Shows information about current map)
- cardinal.map.list (Lists information about all maps currently loaded)
- cardinal.next (Shows information about next map)
- cardinal.rotation (Shows information about the rotation)