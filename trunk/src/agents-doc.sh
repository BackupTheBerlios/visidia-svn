#! /bin/bash

rm -rf agents-doc/*
cd sources
javadoc \
    -d ../agents-doc \
    -use \
    -windowtitle 'ViSiDiA documentation for agents' \
    -encoding utf8 \
    visidia.simulation.agents \
    visidia.simulation.agents.stats \
    visidia.agents \
    visidia.agents.agentsmover \
    visidia.agents.agentchooser \
    visidia.agents.agentreport \
    visidia.tools.agents