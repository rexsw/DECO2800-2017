#!/bin/bash
#renaming script for testing automated sprite selection for units
for dir in ./*/
do
    cd $dir
    mv ./*1.png default.png
    cp ../../Neutral/Astronaut_1.png selected.png
    cd ..
done
