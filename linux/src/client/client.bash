#!/bin/bash
# Java Compile And Run

javac -classpath ./clientCode/ $(find . -name "*.java") -d bin
read -p 'Server Ip: ' serverip
java -cp bin client.Client $serverip
