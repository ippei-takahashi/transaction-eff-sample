package dbio

import model.User

trait MyDBIO[A]

case class AddUser(user: User) extends MyDBIO[Unit]

case class FindUserById(id: String) extends MyDBIO[Option[User]]
