#!/bin/bash
#renaming script for testing automated sprite selection for units
for dir in ./*/
do
    cd $dir
    mv ./*0.png default.png
    mv ./*3.png selected.png
    cd ..
done

