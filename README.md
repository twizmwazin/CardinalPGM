CardinalPGM [![Build Status](https://travis-ci.org/twizmwazin/CardinalPGM.svg?branch=master)](https://travis-ci.org/twizmwazin/CardinalPGM)
===========

This is the next PGM clone.

##In Development
This plugin is in early alpha. I make no guarantees as to what will and what will not work. Features are in development, and many are unfinished or broken. At this point in time, this plugin is not ready for general use. However, if you are a developer or you just want to try to help find bugs, please feel free to try this out at your own risk.

Feel free to submit pull requests and/or issues to help further development. If you find an issue, please submit an issue so a developer can resolve it as soon as possible. If an issue is found with a dependency, submit your issue to the dependency's repository.

##Issue Reporting
When creating an issue, be careful to follow the following guidelines:
- Make sure the same issue is not already reported. Duplicate issues are not useful and will only hinder development.
- Make sure the topic of the issue is an issue with the plugin, not a missing or requested feature.
- Have a demonstration of the bug or clear instructions to produce it.
- Include the version of the plugin the issue is found on. The version can be found by typing `/cardinal -v`.
- Make sure the report uses clear and concise English.
By following these guidelines, it will help developers to quickly and accurately resolve the issue.

##Compiling
To compile and run the latest development version of CardinalPGM, you will need [JDK7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) and [Maven](http://maven.apache.org/). On most Linux distributions, you can find both packages in your default repositories. To compile, simply run `mvn clean install`. The plugin should be found in `target/`.
