[![Build Status](https://img.shields.io/travis/damianszczepanik/cucumber-sandwich/master.svg?style=flat)](https://travis-ci.org/damianszczepanik/cucumber-sandwich)
[![Maven Central](https://img.shields.io/maven-central/v/net.masterthought/cucumber-sandwich.svg)](http://search.maven.org/#search|gav|1|g%3A%22net.masterthought%22%20AND%20a%3A%22cucumber-sandwich%22)
[![Maven Dependencies](https://www.versioneye.com/user/projects/55cf4cc715ff9b0022000039/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55cf4cc715ff9b0022000039?child=summary)

# Generate Pretty HTML Reports For Cucumber On The Fly

This project generates pretty cucumber html reports on the fly. It monitors your cucumber json report directory for change and then publishes a new report if your report json files change.

## Background

I wanted a way of quickly seeing my cucumber reports results when running locally where we didn't have maven available so the maven mojo could not be used.

## Install

1. download this project
2. mvn clean install

You should see a file like this: cucumber-sandwich-0.0.1-SNAPSHOT-jar-with-dependencies.jar
rename this file to cucumber-sandwich.jar

## Use

Use the cucumber-sandwich.jar like this:

    java -jar cucumber-sandwich.jar -f path/to/the/folder/containing/cucumber.json -o /path/to/generate/html/reports/into

It's probably best to stick that in a .bat or .sh script for easy running. Also note there is no checking done to verify if the json files are cucumber ones and it picks up any json files in the input directory. So make sure you generate your cucumber.json files in a directory without other json files.

You can also add an option -n flag to just run once instead of listening for changes:

     java -jar cucumner-sandwich.jar -n -f path/to/the/folder/containing/json -o path/to/folder/to/generate/reports/into

An example with real paths would be:

     java -jar cucumber-sandwich.jar -f /home/kings/cucumber/json -o /home/kings/cucumber/reports -n



You can specify where the cucumber.json goes in your runner e.g.

     import cucumber.junit.Cucumber;
     import org.junit.runner.RunWith;

     @RunWith(Cucumber.class)
     @Cucumber.Options(format = {"json:path/to/cucumber.json"})
     public class SomeTest {
     }


## Develop

Interested in contributing to the cucumber-reporting? Just contact me or send a pull request.
