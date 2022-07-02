package com.slick_crud

import java.time.LocalDate

//it is a case class used in the mapping with the Slick table
case class Movie(id: Long, name: String, releaseDate: LocalDate, lengthInMin: Int)

object SlickTables {

  import slick.jdbc.PostgresProfile.api._

  class MovieTable(tag: Tag) extends Table[Movie](tag, Some("movies")/*<- schema name */,"Movie"){
    def id = column[Long]("movie_id",O.PrimaryKey,O.AutoInc)
    def name = column[String]("name")
    def releaseDate= column[LocalDate]("release_date")
    def lengthInMin= column[Int]("length_in_min")

    // mapping function to the case class
    override def * = (id, name, releaseDate, lengthInMin) <> (Movie.tupled,Movie.unapply)
  }
  //"API entry point"
  lazy val movieTable = TableQuery[MovieTable]
}
