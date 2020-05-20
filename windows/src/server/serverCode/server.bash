#!/bin/bash
# Java Compile And Run

javac *.java -d bin
java -cp ./bin/ Server
