val cars = List("green car", "blue car", "red car")
val pens = List("red pen", "blue pen", "green pen")

// does a cross-join
for(c <- cars; p <- pens) {
	println(f"$c comes with a $p")
}