#!/bin/bash
# Java Compile And Run

javac *.java -d bin
read -p 'Server Ip: ' serverip
java -cp ./bin/ Client $serverip
