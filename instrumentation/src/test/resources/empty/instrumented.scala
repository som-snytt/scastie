import _root_.com.olegych.scastie.api.runtime._
class Playground { private val instrumentationMap$ = _root_.scala.collection.mutable.Map.empty[_root_.com.olegych.scastie.api.Position, _root_.com.olegych.scastie.api.Render];def instrumentations$ = instrumentationMap$.toList.map{ case (pos, r) => _root_.com.olegych.scastie.api.Instrumentation(pos, r) };
 
}
object Main {
  {val _ = Html}
  val playground = new Playground
  def main(args: Array[String]): Unit = {
    scala.Predef.println("\n" + _root_.com.olegych.scastie.api.runtime.Runtime.write(playground.instrumentations$))
  }
}