package chapter7

import java.util.concurrent.{ExecutorService, Future, Callable}
import scala.concurrent.duration.TimeUnit

object Par {
  type Par[A] = ExecutorService => Future[A]

  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true
    def get(timeout: Long, units: TimeUnit) = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false
  }

  def fork[A](a: => Par[A]): Par[A] =
    es => es.submit(new Callable[A] {
      def call = a(es).get
    })

  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  def map[A,B](pa: Par[A])(f: A => B): Par[B] =
    map2(pa, unit(()))((a,_) => f(a))

  def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C): Par[C] = 
    (es: ExecutorService) => {
      val af = a(es)
      val bf = b(es)
      UnitFuture(f(af.get, bf.get)) 
    }

  def asyncF[A,B](f: A => B): A => Par[B] = {
    a => lazyUnit(f(a))
  }

  def sequence[A](ps: List[Par[A]]): Par[List[A]] = {
    ps.foldLeft(unit[List[A]](List.empty)) ( (v,s) => {
      map2(v,s)(_ :+ _)
    })
  }

  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = {
    as.foldLeft(unit[List[A]](List.empty)) ( (s,v) => {
      val job = lazyUnit[(A, Boolean)]((v, f(v)))
      map2(s,job)( (la, tu) => if(tu._2) la :+ tu._1 else la)
    })
  }

  def choiceN[A](n: Par[Int])(choices: List[Par[A]]): Par[A] = {
    join(map(n)(choices(_)))
  }

  def join[A](as: Par[Par[A]]): Par[A] = {
    // can this be done non-blocking with current types? Don't think so
    null
  }
}