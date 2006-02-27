#! /bin/sh
javac -sourcepath sources -d classes sources/visidia/Main.java \
    sources/visidia/agents/*.java \
    sources/visidia/agents/agentsmover/*.java
