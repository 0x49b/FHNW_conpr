package par

import java.util.concurrent.Executors
import java.util.concurrent.Callable
import scala.collection.parallel.CollectionConverters._
import java.util.concurrent.Future
import java.util.concurrent.ExecutorService
import scala.reflect.ClassTag

object ParMap extends App {
  def myParMap[A, B](l: List[A], f: A => B): List[B] = {
    val ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    val futures: List[Future[B]] = l.map(a => ex.submit(() => f(a)))
    val result: List[B] = futures.map(f => f.get)
    ex.shutdown()
    result
  }
   
  def theirParMap[A,B](l: List[A], f: A => B): List[B] =
    l.par.map(f).toList
   
     
  myParMap(List(1,2,3), (i: Int) => i + 1)
   
  theirParMap(List(1,2,3),  (i: Int) => i + 1)


  // Lösung von Silvan Laube 
  def parMap[A,B](l: List[A], f:A => B, pool: ExecutorService = Executors.newCachedThreadPool()) : List[B] = l match {
    case Nil => Nil
    case x :: xs =>
      val future = pool.submit(() => f(x))
      future.get() :: parMap(xs, f, pool) 
  }
  
    def parMap2[A, B](l: List[A], f: A => B)(implicit tag: ClassTag[B]): List[B] = {
     val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
     val iterator : Iterator[A] = l.iterator;
     var arrBuilder = Array.newBuilder[B]
     while(iterator.hasNext){
       var next = iterator.next;
       arrBuilder += executorService.submit(() => f(next)).get();
     }
     executorService.shutdown()
     arrBuilder.result().toList
  }

}