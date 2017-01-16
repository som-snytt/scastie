package com.olegych.scastie
package sbt

import api._

import akka.actor.ActorSystem
import akka.testkit.{TestKit, ImplicitSender, TestProbe, TestActorRef}
import org.scalatest.{FunSuiteLike, BeforeAndAfterAll}

import scala.concurrent.duration._

class SbtActorTest() extends TestKit(ActorSystem("SbtActorTest")) with ImplicitSender
  with FunSuiteLike with BeforeAndAfterAll {
 
  val progressActor = TestProbe()
  val timeout = 10.seconds  
  val sbtActor = TestActorRef(new SbtActor(timeout))

  test("timeout") {
    val id = 0L
    val infiniteLoop = Inputs.default.copy(code = 
      """|class Playground {
         |  while(true){}
         |}""".stripMargin
    )
    sbtActor ! ((id, infiniteLoop, progressActor.ref))

    progressActor.fishForMessage(timeout + 5.seconds){
      case progress: PasteProgress => progress.timeout
    }
  }

  test("after a timeout the sbt instance is ready to be used") {
    val helloWorld = "class Playground { 1 + 1 }"
    sbtActor ! ((1L, helloWorld, progressActor.ref))

    progressActor.fishForMessage(timeout + 5.seconds){
      case progress: PasteProgress => progress.instrumentations.nonEmpty
    }
  }

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
}
