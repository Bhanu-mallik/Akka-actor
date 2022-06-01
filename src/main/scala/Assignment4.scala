import Assignment3.BankAccount._
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import Assignment3._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
implicit val to: Timeout = new akka.util.Timeout(2 seconds)
import scala.util.{Failure, Success}


class Assignment4 extends TestKit(ActorSystem("Assignment4"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  // setup
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }



  "A Person actor" should {
    import Person._

    "have 10000 after depositing 10000 " in {

      val account = system.actorOf(Props[BankAccount], "b1")
      val person = system.actorOf(Props[Person], "a1")

      person ! bankAccount(account)

      person ! deposit(10000)
      Thread.sleep(500)

      val reply = person ? Statement

      reply onComplete {
        case Success(value) => assert(value == "Your balance is 10000")
        case Failure(exception) => fail("Exception thrown")
      }
    }



      "have 4500 after depositing 10000 and withdrawing 5500 " in {

        val account = system.actorOf(Props[BankAccount], "b2")
        val person = system.actorOf(Props[Person], "a2")

        person ! bankAccount(account)

        person ! deposit(10000)
        person ! withdraw(5500)
        Thread.sleep(500)


        val reply = person ? Statement

        reply onComplete {
          case Success(value) => assert( value == "Your balance is 4500" )
          case Failure(exception) => fail("Exception thrown")
        }

    }
  }

}
