* Copy war.libs.all to war.libs if you want to embed ZK libraries
  into your Web application.
* Copy war.libs.minimal to war.libs if you installed ZK libraires
  into the shared directory of the Web server.

# Run  test with testcafe

# Install test cafe
npm i -g testcafe

# run all tests
## start test server
cd test/addon/ckeztest
mvn clean jetty:run

## run tests with testcafe
testcafe chrome ./test/

## run manual visual tests
open test page
http://localhost:8080/ckeztest/test2/

open each test and check for errors
