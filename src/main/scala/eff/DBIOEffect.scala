package eff

import org.atnos.eff.{ Eff, Member }

import eff.DBIOTypes.{ DBIORead, DBIOWrite }

trait DBIOOps {

  implicit class DBIOOps[R, A](effects: Eff[R, A]) {

    def runDBIO[U](
      implicit
      interpreter: DBIOInterpreter,
      m: Member.Aux[DBIORead, R, U]
    ): Eff[U, A] = interpreter.run(effects)

    def runTransactionDBIO[U](
      implicit
      interpreter: DBIOInterpreter,
      m: Member.Aux[DBIOWrite, R, U]
    ): Eff[U, A] = interpreter.runTransaction(effects)
  }

}

object DBIOEffect extends DBIOOps with DBIOCreation
