#!/bin/sh

rm -rf target
mvn clean install package -DskipTests

nameId="dispatcher"

containerId=`docker ps -a -q --filter "name=$nameId"`


if [ ! -z "$containerId" ]
then 
    echo "Deteniendo contenedor $nameId $containerId"
	docker stop $containerId
fi

if [ ! -z "$containerId" ]
then 	
	echo "Eliminando contenedor $nameId $containerId"
	docker rm $containerId
fi

echo "Construyendo imagen $nameId"
docker build -t $nameId .

#echo "Instanciando contenidor $nameId"
#docker run -d --name $nameId -p 9333:9333 --net=host $nameId
