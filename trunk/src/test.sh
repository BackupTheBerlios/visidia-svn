echo "# ce script pemet de lancer une interface graphique pour lancer"
echo "# des serveurs de noeuds sur les machines distantes dans le cadre"
echo "# de la version distribuee de visidia."
echo "# ATTENTION : cette fonctionnalite est en cours de developpement"
echo "#             ne pas utiliser sans connaissance de cause !"

echo "# Voulez vous continuer (o/n) : "
read continue
if [ "${continue}" == "o" ]
then	
	cd classes 
	java  visidia.gui.presentation.userInterfaceEdition.LocalNodeLauncher
fi
