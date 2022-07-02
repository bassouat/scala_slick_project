
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