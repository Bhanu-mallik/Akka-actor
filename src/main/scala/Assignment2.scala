import Assignment1.{Error, Info, SimpleActorLogger}
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.dispatch.sysmsg.Suspend

object Assignment2 extends App{

  class SupervisorAct extends Actor {

    override val supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: RuntimeException => Resume
      case _: Exception => Restart
    }

    override def receive: Receive = {
      case props: Props =>
        val childRef = context.actorOf(props)
        sender() ! childRef
    }




  }

  val system = ActorSystem("Supervisior")
  val supervisorActor = system.actorOf(Props[SupervisorAct])
  val loggerActor = system.actorOf(Props[SimpleActorLogger])

  loggerActor ! Error("error message")
  loggerActor ! Info("Hey")
  loggerActor ! Info("Mallik")
  loggerActor ! Info("Bhanu")
  loggerActor ! Error("error message 2")
  loggerActor ! Info("Bro")

}
