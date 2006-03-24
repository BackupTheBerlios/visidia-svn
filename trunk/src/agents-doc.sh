#! /bin/bash

rm -rf agents-doc/*
cd sources
javadoc \
    -d ../agents-doc \
    -use \
    -windowtitle 'ViSiDiA documentation for agents' \
    visidia.simulation.agents \
    visidia.agents \
    visidia.agents.agentsmover \
    visidia.agents.agentchooser \
    visidia.tools.agents 