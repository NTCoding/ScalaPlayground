
object PartialFunctions extends App {

    def tellAFunnyJoke: PartialFunction[String, String] = {
        case "cheese" => "Cheesey joke"
    }

    def tellAWeirdJoke: PartialFunction[String, String] = {
        case "weird" => "Weird joke"
    }

    def yoMammaJoke: PartialFunction[String, String] = {
        case _ => "Yo mamma....."
    }

    val jokeTeller = tellAFunnyJoke orElse tellAWeirdJoke orElse yoMammaJoke

    println(jokeTeller("blah blah"))
    println(jokeTeller("weird"))
    println(jokeTeller("cheese"))
}
