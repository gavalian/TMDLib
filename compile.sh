#!/bin/sh

cd jfoam ; mvn install; cd -
cp jfoam/target/jfoam-2.0-SNAPSHOT.jar lib/