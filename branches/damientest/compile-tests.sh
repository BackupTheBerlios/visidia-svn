#! /bin/sh

./compile.sh
javac -sourcepath sources -d classes sources/visidia/tests/tools/*.java

