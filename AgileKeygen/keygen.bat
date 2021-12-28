@echo off
setlocal
set CLASSPATH=./keygen.jar;./crypto.jar;%CLASSPATH%
java com.agile.keygenerator.KeyGenerator
@echo on