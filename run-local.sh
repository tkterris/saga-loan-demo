#!/bin/bash

echo  "You must have a local instance of postgresql running AND"
echo  "you must have the sagademo database / schema provisioned"
echo  "see saga-loan-demo/loan-demo-model/src/main/resources/sql/postgresql/*.sql"


sleep 5

echo running lra-coordinator

# might be 50000
podman run --cidfile lra-coordinator.cid --detach -p 8080:8080 quay.io/abryson/lra-coordinator-quarkus-jvm:latest

#echo compiling project
#mvn clean package

echo running loan model service
java -Dserver.port=8081 -jar loan-demo-model/target/*.jar > loan-model.log 2>&1 &
echo "$!" > loan-model.pid

echo running loan service
java -Dserver.port=8082 -jar loan-service/target/*.jar > loan.log 2>&1 &
echo "$!" > loan.pid

echo running applicant service
java -Dserver.port=8083 -jar applicant-service/target/*.jar > applicant.log 2>&1 &
echo "$!" > applicant.pid

echo running create-loan / saga application
java -Dserver.port=8084 -jar create-loan/target/*.jar > create-loan.log 2>&1 &
echo "$!" > create-loan.pid
