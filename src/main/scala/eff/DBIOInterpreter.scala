package eff

import org.atnos.eff.interpret.translate
import org.atnos.eff.syntax.all._
import org.atnos.eff.{ Eff, Member, Translate }

import dbio.{ AddUser, FindUserById }
import eff.DBIOTypes.{ DBIORead, DBIOWrite }
import model.User

import scala.collection.mutable

trait DBIOInterpreter extends Serializable {

  def run[R, U, A](
    effect: Eff[R, A]
  )(
    implicit m: Member.Aux[DBIORead, R, U]
  ): Eff[U, A]

  def runTransaction[R, U, A](
    effect: Eff[R, A]
  )(
    implicit m: Member.Aux[DBIOWrite, R, U]
  ): Eff[U, A]
}

// インタプリタはMyDBIO用の適当なものなので、よしなに書き換える
object DBIOInterpreterImpl extends DBIOInterpreter {
  
  var values: mutable.Map[String, User] = mutable.Map()

  def run[R, U, A](
    effect: Eff[R, A]
  )(
    implicit m: Member.Aux[DBIORead, R, U]
  ): Eff[U, A] =
    translate(effect)(new Translate[DBIORead, U] {
      def apply[X](kv: DBIORead[X]): Eff[U, X] = {
        kv match {
          case DBIORead(FindUserById(id)) =>
           values.get(id).asInstanceOf[X].pureEff[U]
        }
      }
    })

  def runTransaction[R, U, A](
    effect: Eff[R, A]
  )(
    implicit m: Member.Aux[DBIOWrite, R, U]
  ): Eff[U, A] =
    translate(effect)(new Translate[DBIOWrite, U] {
      def apply[X](kv: DBIOWrite[X]): Eff[U, X] = {
        kv match {
          case DBIOWrite(AddUser(user)) =>
            (values(user.id) = user).asInstanceOf[X].pureEff[U]
          case DBIOWrite(FindUserById(id)) =>
            values.get(id).asInstanceOf[X].pureEff[U]
        }
      }
    })
}
