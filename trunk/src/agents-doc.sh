#! /bin/bash

rm -rf agents-doc/*
cd sources
javadoc \
    -d ../agents-doc \
    -use \
    -windowtitle 'ViSiDiA documentation for agents' \
    visidia.simulation.agents \
    visidia.agents \
    visidia.tools.agents 