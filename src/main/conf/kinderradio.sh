#!/bin/sh
cd `dirname $0`
rm -f *.lck
ionice -c 1 -n 0 java -Djava.util.logging.config.file="logging.properties" -jar kinderradio-*.jar
