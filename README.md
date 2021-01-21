This is simple project of creating appointment with doctor based on MySQL database. To run the application, it is required to enter 
login and password to MySQL database in application.properties and in application-test.properties for test purposes.
The database will be created by the App.

BUILDING
To build the application use following command in project director using command line:

mvn clean package

RUNNING
After building the application run following command to start it:

java -jar target/doctors-appointment-2.4.1.jar