# Prober
A simple monitoring prober that tracks the uptime and response codes of a web server by
periodically issuing HTTP GET requests, and writes it out to a file.

Runs until interrupted (CTRL-C)

## To compile
```
javac Prober.java
```

## To run
```
java Prober <url> <file>
```
