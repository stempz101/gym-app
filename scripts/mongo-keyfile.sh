#!/bin/bash

# Use to create MongoDB keyfile
mkdir -p ../config
openssl rand -base64 756 > ../config/mongo_keyfile
chmod 400 ../config/mongo_keyfile
