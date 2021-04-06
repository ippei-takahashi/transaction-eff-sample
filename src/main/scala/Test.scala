import org.atnos.eff.Fx

import dbio.{ AddUser, FindUserById }
import model.User
import org.atnos.eff.syntax.all._

import eff.DBIOTypes.{ DBIORead, DBIOWrite }
import eff.DBIOEffect._
import eff.{ DBIOInterpreter, DBIOInterpreterImpl }

object Test {

  implicit val dbioInterpreter: DBIOInterpreter = DBIOInterpreterImpl

  def runReadSample: Option[User] = ({
    type R = Fx.fx1[DBIORead]
    for {
      userOption <- fromDBIORead[R, Option[User]](FindUserById("hoge"))
    } yield userOption
  }).runDBIO.run

  def runReadWriteSample: Option[User] = ({
    type R = Fx.fx1[DBIOWrite]
    for {
      _ <- fromDBIOWrite(AddUser(User("hoge", "hoge fuga", 20)))
      userOption <- fromDBIORead[R, Option[User]](FindUserById("hoge")) // 同じfor内でread/writeを合成してwriteになっている
    } yield userOption
  }).runTransactionDBIO.run
}
