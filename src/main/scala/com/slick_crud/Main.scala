package com.slick_crud

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

  val thePhantomMenace: Movie = Movie(3L,"The Phantom Menace",LocalDate.of(2005,7,12),205)
  val theNoteBook: Movie = Movie(1L,"The Note Book",LocalDate.of(2003,11,1),200)
  val theMatrix: Movie = Movie(2L,"The Matrix",LocalDate.of(2000,9,12),205)
  // Create
  def demoInsertMovie(): Unit={
    val queryDescription = SlickTables.movieTable ++= Seq(thePhantomMenace,theNoteBook,theMatrix)
    val futureId = Connection.db.run(queryDescription)


    futureId.onComplete {
      case Success(_)=> println(s"Query was successfull")
      case Failure(ex) => println(s"Query failed , reason :$ex")
    }
    Thread.sleep(10000)
  }


  def main(args: Array[String]): Unit = {
    demoInsertMovie();


  }

}
