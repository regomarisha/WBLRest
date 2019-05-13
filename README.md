TestRequestResponse is used to test all valid/invalid cases for
GET, POST, DELETE methods for https://reqres.in/.

Framework is built using maven
It uses  behavior-driven development (BDD) approach for validating test cases
using TESTNG.

The test data is fed using the TestData/RestAssured.xlsx file which is read
using Apache POI and fed to the TESTNG using @Dataprovider

lApache Log4j is a  logging utility used .
Surefire Plugin is used to generates reports .

By default, report files are generated in ${basedir}/target/surefire-reports/TEST-*.xml.


TEST RUN
 Test can be run
By executing TestRequestResponse.java
        or
By  executing TestNG.xml
         or
By running maven commands eg:mvn test