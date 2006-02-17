# creation des souches pour les objets RMI : version distribuee 

cd classes
rmic -d . visidia.network.NodeTry
rmic -d . visidia.network.NodeServerImpl
rmic -d . visidia.network.Simulator_Rmi
rmic -d . visidia.network.VisidiaRegistryImpl
