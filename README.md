# MoneyTransferSpark

This is the main project, which while building provides a single jar, that can be used to run the rest API.
This is just a router and uses https://github.com/agrsachin81/MoneyTransferService as the main core java API

By Default uses port "1010"

To run integration tests  "mvn clean verify -P integration-test"
 

To run unit tests  "mvn clean test -P dev"

On maven installs creates a jar named "MoneyTransferSpark-<version>-jar-with-dependencies.jar", which can run as a standalone jar.

Account CRUD
   To create Account POST /account   {"name":John Doe,mobileNumber: "89898080"} 
    TODO: check duplicate name and mobileNumber

   To get latest account status   GET  /account/accountId

   To fetch all the accounts   GET /account
    (TODO: Limit number of results)

   To edit an account use PUT /account/accountId  (WIP)

   To delete an account use DELETE /account/accountId

Transfer FUNDS
   To Transfer funds  use POST /account/accountId/transact

   To get transactions of an account  GET  /account/accountId/transact
     TODO: Limit number of results

   To get status of a transaction  GET  /account/accountId/transact/transctionid



