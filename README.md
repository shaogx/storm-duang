# Example Storm Topologies

Learn to use Storm!

---

Table of Contents

* <a href="#getting-started">Getting started</a>
* <a href="#maven">Using storm-duang with Maven</a>

---


<a name="getting-started"></a>

# Getting started

## Prerequisites

First, you need `java` and `git` installed and in your user's `PATH`.

Next, make sure you have the storm-duang code available on your machine.  Git/GitHub beginners may want to use the
following command to download the latest storm-duang code and change to the new directory that contains the downloaded
code.

    $ git clone git://github.com/apache/storm.git && cd storm
    $ git clone https://github.com/shaogx/storm-duang && cd storm-duang

## storm-duang overview

storm-duang contains a variety of examples of using Storm.  If this is your first time working with Storm, check out
these topologies first:

1. [DuangTopology](src/jvm/storm/duang/DuangTopology.java):  Basic topology written in all Java.

If you want to learn more about how Storm works, please head over to the
[Storm project page](http://storm.apache.org).


<a name="maven"></a>

# Using storm-duang with Maven

## Install Maven

Install [Maven](http://maven.apache.org/) (preferably version 3.x) by following
the [Maven installation instructions](http://maven.apache.org/download.cgi).


## Build and install Storm jars locally

If you are using the latest development version of Storm, e.g. by having cloned the Storm git repository,
then you must first perform a local build of Storm itself.  Otherwise you will run into Maven errors such as
"Could not resolve dependencies for project `org.apache.storm:storm-duang:1.0.0`".

    # Must be run from the top-level directory of the Storm code repository
    $ mvn clean install -DskipTests=true

This command will build Storm locally and install its jar files to your user's `$HOME/.m2/repository/`.  When you run
the Maven command to build and run storm-duang (see below), Maven will then be able to find the corresponding version
of Storm in this local Maven repository at `$HOME/.m2/repository`.


## Running topologies with Maven

> Note: All following examples require that you run `cd examples/storm-duang` beforehand.

storm-duang topologies can be run with the maven-exec-plugin. For example, to
compile and run `DuangTopology` in local mode, use the command:

    $ mvn compile exec:java -Dstorm.topology=storm.duang.DuangTopology

## Packaging storm-duang for use on a Storm cluster

You can package a jar suitable for submitting to a Storm cluster with the command:

    $ mvn package

This will package your code and all the non-Storm dependencies into a single "uberjar" (or "fat jar") at the path
`target/storm-duang-1.0.0-jar-with-dependencies.jar`.

Example filename of the uberjar:

    >>> target/storm-duang-1.0.0-jar-with-dependencies.jar

You can submit (run) a topology contained in this uberjar to Storm via the `storm` CLI tool:

    $ storm jar storm-duang-1.0.0-jar-with-dependencies.jar storm.duang.DuangTopology storm-duang


-- may behave differently, e.g. by always submitting to a remote cluster (i.e. hardcoded in a way that you, as a user,
cannot change without modifying the topology code), or by requiring a customized configuration file that the topology
code will parse prior submitting the topology to Storm.  Similarly, further options such as the name of the topology may
be user-configurable or be hardcoded into the topology code.  So make sure you understand how the topology of your
choice is set up and configured!

