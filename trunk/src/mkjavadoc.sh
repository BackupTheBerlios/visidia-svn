rm -rf javadoc/
mkdir javadoc/
cd sources/
javadoc -encoding utf8  -d ../javadoc/ \
    -use \
    -windowtitle 'ViSiDiA documentation by javadoc' \
    visidia.gui visidia.gui.donnees visidia.gui.donnees.conteneurs \
    visidia.gui.donnees.conteneurs.monde visidia.gui.presentation \
    visidia.gui.presentation.boite visidia.gui.presentation.factory \
    visidia.gui.presentation.userInterfaceEdition \
    visidia.gui.presentation.userInterfaceEdition.undo \
    visidia.gui.presentation.userInterfaceSimulation \
    visidia.gui.metier visidia.gui.metier.inputOutput \
    visidia.gui.metier.simulation visidia.simulation visidia.assert \
    visidia.gml visidia.network visidia.misc visidia.graph visidia.tools \
    visidia.algoRMI visidia.algo visidia.algo2 \
    visidia.tools.agents visidia.simulation.agents \
    visidia.agents \
    visidia.agents.agentsmover \
    visidia.agents.agentchooser \
    visidia.tools.agents 
cd ../
