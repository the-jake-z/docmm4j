#!/bin/sh
mvn package
docker build .
