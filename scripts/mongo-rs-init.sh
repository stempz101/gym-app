#!/bin/bash

until mongosh --host mongo1:27017 --eval 'quit(db.runCommand({ ping: 1 }).ok ? 0 : 2)'; do
    printf '.'
    sleep 1
done

cd /
echo '
try {
  var config = {
    "_id": "rs0",
    "version": 1,
    "members": [
      { "_id": 0, "host": "mongo1:27017" },
      { "_id": 1, "host": "mongo2:27018" },
      { "_id": 2, "host": "mongo3:27019" }
    ]
  };
  rs.initiate(config, { force: true });
  rs.status();
} catch (e) {
  rs.status().ok
}
' > /mongo-rs-config.js

sleep 10
mongosh -u my_user -p my_password --host mongo1:27017 /mongo-rs-config.js
