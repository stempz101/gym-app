#!/bin/bash

# Use to create MongoDB keyfile
SCRIPT_DIR=$(dirname "$(realpath "$0")")
PARENT_DIR=$(dirname "$SCRIPT_DIR")

mkdir -p $PARENT_DIR/config
openssl rand -base64 756 > $PARENT_DIR/config/mongo_keyfile
chmod 400 $PARENT_DIR/config/mongo_keyfile
