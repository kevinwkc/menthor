package menthor.akka.cluster

import akka.actor.Actor
import Actor._
import akka.serialization.RemoteActorSerialization._

class ClusterService extends Actor {
  def receive = {
    case CreateForeman(parent) =>
      val foreman = actorOf(new Foreman(parent)).start()
      self.channel ! ForemanCreated(foreman)
    case x => println(x)
  }

  override def postStop() {
    registry.shutdownAll()
    remote.shutdown()
  }
}

object ClusterService {
  def run() {
    remote.start()
    remote.register(actorOf[ClusterService])
  }

  def main(args: Array[String]) {
    run()
  }
}
