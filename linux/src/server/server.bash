#!/bin/bash
# Java Compile And Run

javac -cp ./serverCode/ $(find . -name "*.java") -d bin
java -cp bin server.Server