# compile les algorithmes dans :
#  sources/visidia/algo/ 
#  sources/visidia/algoRMI/
#  sources/visidia/agents/
javac -encoding utf8 -sourcepath sources -d classes \
    sources/visidia/algo/*.java \
    sources/visidia/algo/synchronous/*.java \
    sources/visidia/algoRMI/*.java \
    sources/visidia/agents/*.java \
    sources/visidia/agents/agentsmover/*.java \
    sources/visidia/agents/agentchooser/*.java \
    sources/visidia/agents/agentreport/*.java

