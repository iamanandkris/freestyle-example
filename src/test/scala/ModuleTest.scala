import cats.arrow.FunctionK
import cats.implicits._
import freestyle._
import freestyle.implicits._
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by abdhesh on 19/04/17.
  */
class ModuleTest extends WordSpec with Matchers {

  "the @module annotation" should {

    import algebras._
    import modules._
    import freestyle.implicits._
    import interps._

    "[simple] create a companion with a `T` type alias" in {
      type T[A] = M1.Op[A]
    }

    "[onion] create a companion with a `T` type alias" in {
      type T[A] = O1.Op[A]
    }

    "[simple] provide instances through it's companion `apply`" in {
      "M1[M1.Op]" should compile
    }

    "[onion] provide instances through it's companion `apply`" in {
      "O1[O1.Op]" should compile
    }

    "[simple] implicit summoning" in {
      "implicitly[M1[M1.Op]]" should compile
    }

    "[onion] allow implicit sumoning" in {
      "implicitly[O1[O1.Op]]" should compile
    }

    "[simple] autowire implementations of it's contained smart constructors" in {
      val m1 = M1[M1.Op]
      "(m1.sctors1: SCtors1[M1.Op])" should compile
      "(m1.sctors2: SCtors2[M1.Op])" should compile
    }

    "[onion] autowire implementations of it's contained smart constructors" in {
      val o1 = O1[O1.Op]
      "(o1.m1.sctors1: SCtors1[O1.Op])" should compile
      "(o1.m1.sctors2: SCtors2[O1.Op])" should compile
      "(o1.m2.sctors3: SCtors3[O1.Op])" should compile
      "(o1.m2.sctors4: SCtors4[O1.Op])" should compile
    }

    "[simple] allow composition of it's contained algebras" in {
      val m1 = M1[M1.Op]
      val result = for {
        a <- m1.sctors1.x(1)
        b <- m1.sctors1.y(1)
        c <- m1.sctors2.i(1)
        d <- m1.sctors2.j(1)
      } yield a + b + c + d
      "(result: FreeS[M1.Op, Int])" should compile
    }

    "[onion] allow composition of it's contained algebras" in {
      val o1 = O1[O1.Op]
      val result = for {
        a <- o1.m1.sctors1.x(1)
        b <- o1.m1.sctors1.y(1)
        c <- o1.m1.sctors2.i(1)
        d <- o1.m1.sctors2.j(1)
        e <- o1.m2.sctors3.o(1)
        f <- o1.m2.sctors3.p(1)
        g <- o1.m2.sctors4.k(1)
        h <- o1.m2.sctors4.m(1)
      } yield a + b + c + d + e + f + g + h
      "(result: FreeS[O1.Op, Int])" should compile
    }

    "[simple] find a FunctionK[Module.Op, ?] providing there is existing ones for it's smart constructors" in {
      import freestyle.implicits._
      import interps.{optionHandler1, optionHandler2}
      "implicitly[FunctionK[M1.Op, Option]]" should compile
    }

    "[onion] find a FunctionK[Module.Op, ?] providing there is existing ones for it's smart constructors" in {
      "implicitly[FunctionK[O1.Op, Option]]" should compile
    }

    "[simple] reuse program interpretation in diferent runtimes" in {
      val m1 = M1[M1.Op]
      val program = for {
        a <- m1.sctors1.x(1)
        b <- m1.sctors1.y(1)
        c <- m1.sctors2.i(1)
        d <- m1.sctors2.j(1)
      } yield a + b + c + d
      program.interpret[Option] shouldBe Option(4)
      program.interpret[List] shouldBe List(4)
    }

    "[onion] reuse program interpretation in diferent runtimes" in {
      val o1 = O1[O1.Op]
      val program = for {
        a <- o1.m1.sctors1.x(1)
        b <- o1.m1.sctors1.y(1)
        c <- o1.m1.sctors2.i(1)
        d <- o1.m1.sctors2.j(1)
        e <- o1.m2.sctors3.o(1)
        f <- o1.m2.sctors3.p(1)
        g <- o1.m2.sctors4.k(1)
        h <- o1.m2.sctors4.m(1)
      } yield a + b + c + d + e + f + g + h

      program.interpret[Option] shouldBe Option(8)
      program.interpret[List] shouldBe List(8)
    }

    "pass through concrete members to implementations" in {
      val o2 = O2[O2.Op]
      o2.x shouldBe 1
      o2.y shouldBe 2
    }

    "allow modules with just concrete members unrelated to freestyle's concerns" in {
      val o3 = O3[O3.Op]
      o3.x shouldBe 1
      o3.y shouldBe 2
    }

  }

}
