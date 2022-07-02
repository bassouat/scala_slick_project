package com.slick_crud

import slick.jdbc.PostgresProfile.api._

case object Connection{
  val db=Database.forConfig("postgres")
}
