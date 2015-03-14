CardinalPGM [![Build Status](https://travis-ci.org/twizmwazin/CardinalPGM.svg?branch=master)](https://travis-ci.org/twizmwazin/CardinalPGM)
===========

This is the next PGM clone.

##In Development
This plugin is in early alpha. I make no guarantees as to what will and what will not work. Features are in development, and many are unfinished or broken. At this point in time, this plugin is not ready for general use. However, if you are a developer or you just want to try to help find bugs, please feel free to try this out at your own risk.

Feel free to submit pull requests and/or issues to help further development. If you find an issue, please submit an issue so a developer can resolve it as soon as possible. If an issue is found with a dependency, submit your issue to the dependency's repository.

##Issue Reporting
Before creating an issue:
- Make sure the same issue is not already reported. Duplicate issues are not useful and will only hinder development.
- Make sure the topic of the issue is an issue with the plugin, not a missing or requested feature.
- If you cannot find a command to do the job, read the [Usage](https://github.com/twizmwazin/CardinalPGM/wiki/Using-Cardinal) page on the wiki.

When creating an issue, be careful to follow the following guidelines:
- Have a demonstration of the bug or clear instructions to produce it.
- Include the version of the plugin the issue is found on. The version can be found by typing `/cardinal -v`.
- Make sure the report uses clear and concise English.
 By following these guidelines, it will help developers to quickly and accurately resolve the issue.
- Make sure to report your log file in the [pastebin](http://pastebin.com) website, this can be found on your log folder in your server files.
 
 ##Contributing
 Before submitting a Pull Request, to increase the chances of the pull request being merged, make sure to follow the following guidelines:
- Have one commit per pull request. This makes it easier for the pull request to be reviewed and merged. If there is an issue with a pull request, use git to amend the commit rather than adding a second commit.
- Maintain a formal style of code. Check the code for perfect syntax and correct spacing and indentation.
- Make sure the code works. Pull reqests that do not work will hurt your chances of getting code merged in the future.
- Do not use the GitHub editor to edit Java files. Use a properly IDE such as [IntelliJ](https://www.jetbrains.com/idea/download/) or [Eclipse](https://eclipse.org/downloads/).
 
 ##Compiling
- To compile and run the latest development version of CardinalPGM, you will need [JDK7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) and [Apache-Maven](http://maven.apache.org/). On most Linux distributions, you can find both packages in your default repositories. To compile, simply run `mvn clean install` in your command line, in the CardinalPGM directory. The plugin should be found in `target/`, as `CardinalPGM-1.0-SNAPSHOT.jar`,For more assist installing Cardinal, please go [here](https://github.com/twizmwazin/CardinalPGM/wiki/Installing-CardinalPGM), and read our FAQ [here](https://github.com/twizmwazin/CardinalPGM/wiki/FAQ-(English)), If you are spanish and want to install Cardinal please go [here](https://github.com/twizmwazin/CardinalPGM/wiki/Instalando-CardinalPGM) and if you want to read our spanish FAQ go [here](https://github.com/twizmwazin/CardinalPGM/wiki/FAQ-(Spanish)).
