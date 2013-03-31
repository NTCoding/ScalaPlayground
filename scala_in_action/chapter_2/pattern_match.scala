def sayMyName (name: String) = name match {
	case "Nick" => println("Yo Nick")	
	case "Carole" => println("Hi Carole")
	case "Egg" => println("Love you Egg")
	case _ => println("Gday fella")
}

def giveMeAKiss (name: String) = { 
	val nicePeople = List("Mum", "Nan", "Auntie")
	name match {
		case nicePerson if nicePeople contains nicePerson => println("Kissy kissy")
		case _ => println("get lost")
	}
}