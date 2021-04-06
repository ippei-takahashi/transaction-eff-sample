package eff

import org.atnos.eff.|=

import dbio.MyDBIO

object DBIOTypes {

  // 使っているライブラリのものに置き換える
  type DBIO[A] = MyDBIO[A]

  case class DBIORead[A](value: DBIO[A])
  case class DBIOWrite[A](value: DBIO[A])

  type _dbior[R] = DBIORead |= R
  type _dbiow[R] = DBIOWrite |= R
}
