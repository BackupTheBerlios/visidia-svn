# compile les algorithmes dans :
#  sources/visidia/algo/ 
#  sources/visidia/algoRMI/
#  sources/visidia/agents/
javac -sourcepath sources -d classes \
    sources/visidia/algo/*.java \
    sources/visidia/algo/synchronous/*.java \
    sources/visidia/algoRMI/*.java \
    sources/visidia/agents/*.java
