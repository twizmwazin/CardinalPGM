CardinalPGM [![Build Status](https://travis-ci.org/twizmwazin/CardinalPGM.svg?branch=master)](https://travis-ci.org/twizmwazin/CardinalPGM)
===========

This is the next PGM clone.

Downloads can be found on [ci.twizmwaz.in](http://ci.twizmwaz.in/job/Cardinal/). It is imperative that users stay on the latest version, so please check for updates regularly. 

Feel free to submit pull requests and/or issues to help further development. If you find an issue, please submit an issue so a developer can resolve it as soon as possible. If an issue is found with a dependency, submit your issue to the dependency's repository.

##Installing
A full instalation guide can be found [here](https://github.com/twizmwazin/CardinalPGM/wiki/Easy-Installation-&-Map-Management-Guide)

##Issue Reporting
Before creating an issue:
- Make sure the same issue is not already reported. Duplicate issues are not useful and will only hinder development.
- **Make sure the topic of the issue is an issue with the plugin, not a missing or requested feature.**
- If you cannot find a command to do the job, read the [Usage](https://github.com/twizmwazin/CardinalPGM/wiki/Using-Cardinal) page on the wiki.

When creating an issue, be careful to follow the following guidelines:
- Have a demonstration of the bug or clear instructions to produce it.
- Include the version of the plugin the issue is found on. The version can be found by typing `/cardinal -v`, or you can use the commit version.
- Make sure the report uses clear and concise English.
By following these guidelines, it will help developers to quickly and accurately resolve the issue.

##Contributing
Before submitting a Pull Request, to increase the chances of the pull request being merged, make sure to follow the following guidelines:
- Use a formal commit message. Commit messages should be in the present tense. For example, "Add xxxxx, closes #123."
- Have one commit per pull request. This makes it easier for the pull request to be reviewed and merged. If there is an issue with a pull request, use git to amend the commit rather than adding a second commit.
- Maintain a formal style of code. Check the code for perfect syntax and correct spacing and indentation.
- Make sure the code works. Pull requests that do not work will hurt your chances of getting code merged in the future.
- Do not use the GitHub editor to edit Java files. Use a proper IDE such as IntelliJ or Eclipse.

##Compiling
To compile and run the latest development version of CardinalPGM, you will need [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and [Apache-Maven](http://maven.apache.org/). On most Linux distributions, you can find both packages in your default repositories. To compile, simply run `mvn clean install` in your command line, in the CardinalPGM directory. The plugin should be found in `target/`, as `CardinalPGM-1.0-SNAPSHOT.jar`.
