package types

class TypeConstraints {

  class Base {
    def imBase() { println("yeahh") }
  }
  class Sub extends Base {
    def subDoYourShit() { println("this is gonna blow!") }
  }
  class Jimmy extends Sub

  class Lower_aka_subtype_bounds {
    // BaseType must be a supertype of sub - so methods on sub might not be available on BaseType
    type BaseType >: Sub

    def makeANoise(p: BaseType) = { println("No methods available because interface of subtypes unknown") }
  }

  // Magic is on the line below - we specify the base type here
  val tc = new Lower_aka_subtype_bounds { type BaseType = Base }
  tc.makeANoise(new Base)



  class Thing
  class BirdWithFeathers extends Thing {
    def cluckAroo() { println("cluckaroo") }
  }
  class Turkey extends BirdWithFeathers

  class Upper_aka_supertype_bounds {
    // Clucky is a subtype of bird with feathers so must have it's methods
    type Clucky <: BirdWithFeathers

    def makeANoise(chick: Clucky) = chick.cluckAroo() // methods available because a subtype
  }

  val uc = new Upper_aka_supertype_bounds { type Clucky = BirdWithFeathers }
  uc.makeANoise(new Turkey)


}





