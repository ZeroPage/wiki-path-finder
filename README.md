# wiki-path-finder [![Build Status](https://travis-ci.org/ZeroPage/wiki-path-finder.svg?branch=master)](https://travis-ci.org/ZeroPage/wiki-path-finder)

This project is renewal of project with same name.
The objective of this project is: 
 - To find the shortest path between two documents
 - To improve performance and architecture from recent project

This project is also the result of term project for Design Pattern class, CAUCSE, 2015.

#Pre-requisites
- Language: Java8
- Build management: maven4
- External libraries
  - junit (4.12): Testing Library
  - mediawiki-japi (0.0.11): Wikipedia API Java Library
  - sqlite-jdbc (3.8.11.2): java-sqlite connectivity Library

#How-to-use
You can select target language(EN, KO, JP) to be searched in Wikipedia, then type in two document titles, each for "from" and "to" parameters.

The program will look for shortest link path between two keywords, and show you the path starting from "from" parameter.

#License
- [MIT License](https://opensource.org/licenses/MIT)
