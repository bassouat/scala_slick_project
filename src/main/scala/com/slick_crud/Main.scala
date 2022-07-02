package com.slick_crud

import slick.jdbc.GetResult

import java.time.LocalDate
import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


object PrivateExecutionContext {
  val executor: ExecutorService =Executors.newFixedThreadPool(4)
  implicit val ec:ExecutionContext= ExecutionContext.fromExecutorService(executor)
}
object Main {

  import slick.jdbc.PostgresProfile.api._
  import PrivateExecutionContext._

  val thePhantomMenace: Movie = Movie(1L, "The Phantom Menace", LocalDate.of(2005, 7, 12), 205)
  val theNoteBook: Movie = Movie(2L, "The Note Book", LocalDate.of(2003, 11, 1), 200)
  val theMatrix: Movie = Movie(3L, "The Matrix", LocalDate.of(2000, 9, 12), 205)
  val actorTheNoteBook: Actor=Actor(1L,"Ryan Gosling")
  val actorTheMatrix: Actor=Actor(2L,"Kanu Reeves")
  val tomHanks: Actor = Actor(3L,"Tom Hanks")
  val juliaRoberts: Actor = Actor(4L,"Julia Roberts")
  val mandyMoore: Actor = Actor(5L,"Mandy Moore")

  // Create
  def demoInsertMovie(): Unit = {
    val queryDescription = SlickTables.movieTable ++= Seq(thePhantomMenace, theNoteBook, theMatrix)
    val futureId = Connection.db.run(queryDescription)


    futureId.onComplete {
      case Success(_) => println(s"Query was successfull")
      case Failure(ex) => println(s"Query failed , reason :$ex")
    }
    Thread.sleep(10000)
  }

  //Read all movies
  def demoReadAllMovies(): Unit = {
    val resultRead = SlickTables.movieTable.result
    val resulFuture: Future[Seq[Movie]] = Connection.db.run(resultRead) //"select * from ___"
    resulFuture.onComplete {
      case Success(movies) => println(s"Fetched: ${movies.mkString(",")}")
      case Failure(ex) => println(s"Fetching failed:$ex")
    }
    Thread.sleep(10000)
  }
  //Read some movies
  def demoReadSomeMovies():Unit= {
    val resultReadSome = SlickTables.movieTable.filter(_.name.like("%Matrix%")).result;
    val resulFuture:Future[Seq[Movie]]=Connection.db.run(resultReadSome) //"select * from ___where name like "Matrix""
    resulFuture.onComplete{
      case Success(movies) => println(s"Fetched: ${movies.mkString(",")}")
      case Failure(ex) => println(s"Fetching failed:$ex")
    }

    Thread.sleep(10000)
  }

  //Update movies
  def demoUpDate():Unit={
    val queryUpdate= SlickTables.movieTable.filter(_.id===1L).update(thePhantomMenace.copy(lengthInMin = 180))
    val futureId: Future[Int] = Connection.db.run(queryUpdate)


    futureId.onComplete {
      case Success(newMovieId)=> println(s"Query was successfull,new id is $newMovieId")
      case Failure(ex) => println(s"Query failed , reason :$ex")
    }
    Thread.sleep(10000)
  }

  //Delete
  def demoDelete(): Unit ={
    Connection.db.run(SlickTables.movieTable.filter(_.name.like("%Matrix%")).delete)
    Thread.sleep(10000)
  }

  //Advanced queries
  def readMoviesByPlainQuery(): Future[Vector[Movie]] ={
    implicit val getResultMovie: GetResult[Movie]=
      GetResult(positionedResult => Movie(
        positionedResult.<<,
        positionedResult.<<,
        LocalDate.parse(positionedResult.nextString()),
        positionedResult.<<))
    val query=sql"""select * from movies."Movie"""".as[Movie]    //SQL interpolation
    Connection.db.run(query)
  }

  def multipleQueriesSingleTransaction(): Unit ={
    val insertMovie=SlickTables.movieTable += thePhantomMenace
    val insertActor = SlickTables.actorTable += mandyMoore
    val finalQuery = DBIO.seq(insertMovie,insertActor)
    Connection.db.run(finalQuery.transactionally)
  }
  def findAllActorsByMovie(movieId:Long):Future[Seq[Actor]]={
    val joinQuery = SlickTables.movieActorMappingTable.filter(_.movieId===movieId)
      .join(SlickTables.actorTable)
      .on(_.actorId===_.id)     // select* from movieActorMappingTable m join on actorTable a on m.actorId==a.id
      .map(_._2)
    Connection.db.run(joinQuery.result)
  }
  def main(args: Array[String]): Unit = {
    //demoInsertMovie();
    //demoReadAllMovies();
    /*demoReadSomeMovies()*/
    /*demoUpDate()*/
   /* demoDelete()*/
    findAllActorsByMovie(2L).onComplete{
      case Success(actors) => println(s"Actors from the notebook : ${actors}")
      case Failure(ex)=> println(s"Query failed $ex")
    }
  }
}
