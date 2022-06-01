
import Assignment3.BankAccount.Statement
import akka.actor.{Actor, ActorRef, ActorSystem, Props}




object Assignment3 extends  App{


  val system = ActorSystem("actorCapabilitiesDemo")

  object BankAccount {
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object Statement

    case class TransactionSuccess(message: String)
    case class TransactionFailure(reason: String)
  }

  class BankAccount extends Actor {
    import BankAccount._

    var funds = 0

    override def receive: PartialFunction[Any, Unit] = {
      case Deposit(amount) =>
//        println("money depositing ...")
        if (amount < 0) sender() ! TransactionFailure("invalid deposit amount")
        else {
          funds += amount
//          println("money deposit completed")
          sender() ! TransactionSuccess(s"successfully deposited $amount")
        }
      case Withdraw(amount) =>
//        println("money withdrawing ...")
        if (amount < 0) sender() ! TransactionFailure("invalid withdraw amount")
        else if (amount > funds) sender() ! TransactionFailure("insufficient funds")
        else {
          funds -= amount
//          println("money withdraw completed")
          sender() ! TransactionSuccess(s"successfully withdrew $amount")
        }
      case Statement => sender() ! s"Your balance is $funds"
    }

    def getFunds: Int = {
      funds
    }
  }

  object Person {
    case class deposit(amount: Int)
    case class withdraw(amount: Int)

    case class bankAccount(account: ActorRef)
  }

  class Person extends Actor {

    import Person.{deposit, withdraw, bankAccount}
    import BankAccount._

    var acc: ActorRef = Option[ActorRef]
    override def receive: Receive = {

      case bankAccount(account: ActorRef) => acc = account
//      println("initialize")
      case withdraw(amount: Int) => acc ! Withdraw(amount)
//      println("withdrawing")
      case deposit(amount: Int) => acc ! Deposit(amount)
//      println("depositing")
      case Statement => acc ! Statement
//      println("balance")
      case message =>
        println(message.toString)


    }

    //def balance: Int = getFunds
  }



  val person = system.actorOf(Props[Person], "billionaire")
  val account : ActorRef = system.actorOf(Props[BankAccount], "bankAccount")
  import Person._
          person ! bankAccount(account)
          Thread.sleep(1000)

          person ! deposit(10000)
          person ! withdraw(90000)
          person ! withdraw(500)
          person ! deposit(10000)
          person ! Statement

}

