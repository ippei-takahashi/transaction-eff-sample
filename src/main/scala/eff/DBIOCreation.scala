package eff

import org.atnos.eff.{ Eff, MemberIn }

import cats.~>
import eff.DBIOTypes.{ DBIO, DBIORead, DBIOWrite, _dbior, _dbiow }

trait DBIOCreation {

  final def fromDBIORead[R: _dbior, A](dbio: DBIO[A]): Eff[R, A] = {
    Eff.send[DBIORead, R, A](DBIORead(dbio))
  }

  final def fromDBIOWrite[R: _dbiow, A](dbio: DBIO[A]): Eff[R, A] = {
    Eff.send[DBIOWrite, R, A](DBIOWrite(dbio))
  }

  implicit def fromDBIOReadToWrite: DBIORead ~> DBIOWrite =
    new (DBIORead ~> DBIOWrite) {
      override def apply[X](x: DBIORead[X]): DBIOWrite[X] =
        DBIOWrite(x.value)
    }

  implicit def deriveDBIOMember[R](
    implicit member: MemberIn[DBIOWrite, R]
  ): MemberIn[DBIORead, R] = member.transform[DBIORead]
}
