#!/usr/bin/env bash
mvn clean install && docker build . -t cache-movies-manager && docker run --rm --network isolatedNetwork --name cache-movies-manager cache-movies-manager
