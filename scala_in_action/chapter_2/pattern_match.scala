def sayMyName (name: String) = name match {
	case "Nick" => println("Yo Nick")	
	case "Carole" => println("Hi Carole")
	case "Egg" => println("Love you Egg")
	case _ => println("Gday fella")
}