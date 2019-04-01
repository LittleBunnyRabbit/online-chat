#!/bin/bash
# Java Compile And Run

javac -cp . $(find . -name "*.java") -d bin
java -cp bin server.Server