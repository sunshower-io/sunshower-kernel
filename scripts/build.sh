#!/bin/bash


source /scripts/set-version.sh

mvn clean install deploy -f bom/pom.xml 
#mvn versions:set -DnewVersion=${VERSION} -f bom/pom.xml
gradle clean build pTML publish \
-PmavenRepositoryUrl=${MVN_REPO_URL} \
-PmavenRepositoryUsername=${MVN_REPO_USERNAME} \
-PmavenRepositoryPassword=${MVN_REPO_PASSWORD} \
-Pversion=1.0.0-SNAPSHOT
    