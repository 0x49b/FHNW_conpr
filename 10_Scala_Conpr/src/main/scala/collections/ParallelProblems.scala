package collections

import scala.collection.parallel.CollectionConverters._

object ParallelProblems {
  def main(args: Array[String]): Unit = {
    println("Undefined processing order")
    (1 to 10).par.foreach(i => println(Thread.currentThread.toString + s"$i"))
    
    println("Leading to non-deterministic results")
    println( (1 to 1000).par.reduce((a,b) => a - b))
    println( (1 to 1000).par.reduce((a,b) => a - b))
  }
}