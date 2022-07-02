
This is a scala CRUD project with Slick and also the using of concepts like join , transaction

####build.sbt
i will be using Slick with PostgreSQL and Hikari connection pool.
Also i will be using slick-pg library for advanced postgres features. 
i will be using Scala 2 version as support for Scala 3 is still in progress for Slick.
Let’s add all the necessary dependencies together in the build.sbt

####docker-compose.yml  
i use docker-compose.yml to spin up a service that encapsulates in a docker
container

####application.conf
i put all the database configuration in this file in order to connect to the database

####connection instance
i can create a connection instance using the PostgresProfile and 
previously defined configuration

####description of the project
in this project i use scala to do the CRUD(Create Read Update Delete)
operations by inserting movies,reading them,readind some,updating and deleting

####advanced queries
i used scala for queries dealing with mapping between movies and actor and also to insert both movie and actor both
by using transactionnal

####launch this project
To launch this project clone the github repository in your local,then
open it with and IDE after build the build.sbt and create your posgreSQL database
by using the docker and application.conf' datas