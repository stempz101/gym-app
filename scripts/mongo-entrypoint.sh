#!/bin/bash

chmod 400 /etc/mongodb/pki/keyfile
chown 999:999 /etc/mongodb/pki/keyfile
exec docker-entrypoint.sh $@
