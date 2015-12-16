#!/bin/sh

cd jfoam ; mvn3 install; cd -
cp jfoam/target/jfoam-2.0-SNAPSHOT.jar lib/