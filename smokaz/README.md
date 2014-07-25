# Smokaz
At the table sits a dealer and 3 smokers. Each smoker requires a cigarette, a match, and some striking paper so they can shmoke. But they each 
have only 2 of the ingredients (creating each possible combination - no 2 have the same items).

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

You can also sbt "test" but the sleeps might not operate successfully on your machine (akka timefactor can be used - pull requests welcome)

## Sample output
```
INFO akka.event.slf4j.Slf4jLogger - Slf4jLogger started
INFO NeedPaperSmoker - I neeeeed some paper. Feed my lungs with tar
INFO NeedACigSmoker - I neeeeed a cigarette. Feed my lungs with tar
INFO NeedAMatchSmoker - I neeeeed a match. Feed my lungs with tar
INFO Dealer - Placed Paper on table
INFO NeedPaperSmoker - I am now smoking leave me alone
INFO Dealer - Placed Cigarette on table
INFO NeedACigSmoker - I am now smoking leave me alone
INFO Dealer - Placed Match on table
INFO NeedAMatchSmoker - I am now smoking leave me alone
INFO NeedACigSmoker - Finished smoking. Feed me more tar
INFO Dealer - Placed Cigarette on table
INFO NeedACigSmoker - I am now smoking leave me alone
INFO NeedACigSmoker - Finished smoking. Feed me more tar
INFO Dealer - Placed Cigarette on table
INFO NeedACigSmoker - I am now smoking leave me alone
INFO NeedPaperSmoker - Finished smoking. Feed me more tar
INFO NeedAMatchSmoker - Finished smoking. Feed me more tar
INFO Dealer - Placed Paper on table
INFO NeedPaperSmoker - I am now smoking leave me alone
INFO Dealer - Placed Match on table
INFO NeedAMatchSmoker - I am now smoking leave me alone
```