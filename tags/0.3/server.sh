#---------------------------------------------#
#   Lancement d'un serveur de noeuds sur une  #
#   une machine haute : creation a distance   #
#   des noeuds d'un graphe                    #
#---------------------------------------------#



#set -x
#-----------------------------------------------------------#
#fonction trap system pour tuer les Registry qui ont ete    #
#lancees sur les differents port de la machine.  	    #
#n'utiliser cette fonction qu'en connaissance de cause	    #
#-----------------------------------------------------------#
#f_exit()
#{
#	code_erreur="0"
#	while [ "${code_erreur}" == "0" ]
#	do 
#	   ps | gawk 'match($0,"rmiregis"){system("kill -9 " $1); exit}'
#	   ps | grep rmiregis > /dev/null
#	   code_erreur=$?	
#	done
#}
#trap "f_exit" INT



####################################
#pour lancer un serveur à la main  #
####################################
f_run_one_LocalNode()
{
#Recuperation du localhost
loop="true"
while [  "$loop" == "true" ]
do
     echo -e "Entrer le nom de la machine locale : \c"
     read localhost
     if [ "${localhost}" == "" ]
     then 
          echo "Il faut entrer un nom"
     else
          loop="false"
     fi
done

    
#recuperation du nom du serveur
echo -e "Choisissez le nom dans la registry du Noeud Local : ("${localhost}" par defaut) \c"
read url
if [ "${url}" == "" ]
then 
    ServerUrl=""
else
    ServerUrl="-Name "${url}" "
fi 

#recuperation du port du serveur
echo -e "Choisissez le port sur lequel le serveur va etre lancer : (port quelconque par defaut) : \c"
read ServerPort
server=${localhost}" "${ServerPort}" "

#lanecement de la registry par defaut si le port n est pas specifie
echo -e "Voulez vous lancer une registry : (o/[n]) \c"
read var
if [ "${var}" == "o" ]
then
    echo -e "Entrer le port de la registry : (1099 par defaut) : \c"
    read RegistryPort
    rmiregistry ${RegistryPort} &
else
    echo -e "!!! Assurer vous qu'une registry est deja lance sur la machine : "${localhost}" !!!"
    echo -e "Entrer le port de la registry que vous voulez utiliser (1099 par defaut) : \c"
    read RegistryPort
    RegistryPort=""
fi

if [ "${RegistryPort}" == "" ]
then 
    registry=""
else
    registry="-RMIregistry "${RegistryPort}
fi

#lancement du server
loop="true"
while [  "$loop" == "true" ]
do
    echo "Vouler vous lancer un server de type NodeServer [2]"
    echo "                         ou de type NodeFactory [1]"
    echo -e "Votre Reponse : \c"
    read reponse
    if [ "${reponse}" == "2" -o "${reponse}" == "" ] 
    then 
	tagForServer="NodeServer"
        loop="false"
    else 
       if [ "${reponse}" == "1" ]
       then
           tagForServer="NodeFactory"
           loop="false"
       fi
    fi
done

echo "###########################################################"
echo ">>>Demarrage du serveur de noeud (type "${tagForServer}")" 
if [ ! "${ServerPort}" == "" ]
then 
    echo -e ">>> port : "${ServerPort} 
fi
if [ ! "${url}" == "" ]
then 
    echo -e ">>>    URL : "${url}
else 
    echo -e ">>>    URL : "${localhost} 
fi
if [ ! "${RegistryPort}" == "" ]
then 
    echo -e ">>>    port de la registry : "${RegistryPort} 
fi
echo "###########################################################"


#echo  ${server}" "${ServerUrl}" "${registry}

if [ "${tagForServer}" == "NodeServer" ]
then
    echo -e "java -Xmx1024M visidia.network.Server "${server}${ServerUrl}${registry}
    java -Xmx1024M visidia.network.Server ${server}${ServerUrl}${registry}
else 
    java -Xmx1024M visidia.network.Factory ${server}${ServerUrl}${registry}
fi
}


####################################
#pour lancer N serveur à la main  #
####################################
f_run_N_LocalNodes()
{
#Recuperation du localhost
loop="true"
while [  "$loop" == "true" ]
do
     echo -e "Entrer le nom de la machine locale : \c"
     read localhost
     if [ "${localhost}" == "" ]
     then 
          echo "Il faut entrer un nom"
     else
          loop="false"
     fi
done

    
#recuperation du nom du serveur
server=${localhost}" "

#lanecement de la registry par defaut si le port n est pas specifie
echo -e "Voulez vous lancer une registry : (o/[n]) \c"
read var
if [ "${var}" == "o" ]
then
    echo -e "Entrer le port de la registry : (1099 par defaut) : \c"
    read RegistryPort
    rmiregistry ${RegistryPort} &
else
    echo -e "!!! Assurer vous qu'une registry est deja lance sur la machine : "${localhost}" !!!"
    echo -e "Entrer le port de la registry que vous voulez utiliser (1099 par defaut) : \c"
    read RegistryPort
    RegistryPort=""
fi

if [ "${RegistryPort}" == "" ]
then 
    registry=""
else
    registry="-RMIregistry "${RegistryPort}
fi


N=$1
i=0
while [ ! "${N}" == "${i}" ]
do
  echo "###########################################################"
  echo ">>>Demarrage du serveur de noeud (type NodeServer)" 
  echo -e ">>>    URL : "${i}
  
  if [ ! "${RegistryPort}" == "" ]
      then 
        echo -e ">>>    port de la registry : "${RegistryPort} 
  fi
  


#echo  ${server}" "${ServerUrl}" "${registry}
  ServerUrl="-Name "${i}" "
  echo -e ">>>   java -Xmx1024M fr.enserb.das.network.Server "${server}${ServerUrl}${registry}
  echo -e "###########################################################"
  echo -e " "
  java -Xmx1024M fr.enserb.das.network.Server ${server}${ServerUrl}${registry} &
  
  i=`expr ${i} + 1`
done
}
#################################
# Le main                       #
#################################
if [ "$1" == "" ]
then
    f_run_one_LocalNode
else
    f_run_N_LocalNodes ${1}
fi
