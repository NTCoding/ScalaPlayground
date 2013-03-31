val people = List("Jimmy", "Fred", "Carole")
val pets = List("Rover", "Rex", "Tabby")

val result = for{ p <- people; pet <- pets } yield f"$p has pet $pet"
for(r <- result) println(r)