# Smokaz
At the table sits a dealer and n smokers. Each smoker requires a cigarette, a match, and some rolling paper so they can schmoke. But they each
have an unlimited supply of only 1 of the ingredients.

Fortunately the dealer will put 1 smoking item on the table, which will may then rightfully be claimed by only the smoker who needs that item.

Once the smoker has claimed the item from the table they will then smoke a cigarette.

- The dealer can only put 1 item on the table at a time

- The dealer can not put an item on the table that is for a smoker who is currently smoking a cigarette

## Implementation
This implementation uses actors and events to form a state-machine like, declarative-like model expressing the rules of the game. Communication occurs using
Akka's event bus. This allows all entities involved in the game to be completely decoupled from each other - and without any shared state. 
Apparently that's good for concurrency and parallelism - and no threads or locks necessary].

It's not intended to be production quality - lots of magics strings and untested code blah blah blah.

## Running the application
Just fire up sbt and "run" the thing. It will continue forever so you have to kill it yourself (pull requests welcome)

You can also sbt "test". You may need to adjust the time factor in /src/test/resource/application.conf if some tests fail

## Sample output
```
akka://Smokaz/user/table - Table now available
akka://Smokaz/user/hasPaperSmoker - I am eagerly watching the table mr dealer. Please feed my cravings; I need: Cigarette, Match
akka://Smokaz/user/hasCigsSmoker - I am eagerly watching the table mr dealer. Please feed my cravings; I need: Match, Paper
akka://Smokaz/user/hasMatchesSmoker - I am eagerly watching the table mr dealer. Please feed my cravings; I need: Cigarette, Paper
akka://Smokaz/user/dealer - About to start dealing to the smokaz
akka://Smokaz/user/dealer - Placed Match on table
akka://Smokaz/user/table - Match now on table
akka://Smokaz/user/table - akka://Smokaz/user/hasCigsSmoker got the Match off the table
akka://Smokaz/user/dealer - Placed Match on table
akka://Smokaz/user/table - Match now on table
akka://Smokaz/user/table - akka://Smokaz/user/hasPaperSmoker got the Match off the table
akka://Smokaz/user/dealer - Placed Paper on table
akka://Smokaz/user/table - Paper now on table
akka://Smokaz/user/table - akka://Smokaz/user/hasMatchesSmoker got the Paper off the table
akka://Smokaz/user/dealer - Placed Cigarette on table
akka://Smokaz/user/table - Cigarette now on table
akka://Smokaz/user/table - akka://Smokaz/user/hasMatchesSmoker got the Cigarette off the table

```