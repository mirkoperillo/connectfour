Project developed several years ago for an university exam.

connectfour is a Java Swing application to play Connect Four game.

You can play
* player vs computer
* player vs player (on local machine or on the net)

Application developed using jdk4, tested successfully with oracle jdk 7

Computer player uses alpha-beta pruning algorithm to search best action to play.


## Build and run application

To build application move to connectfour root folder then

 ```
	mkdir dist
	javac -d dist/ src/connectfour/*.java && cp src/connectfour/*.gif dist/connectfour && cp cfg.txt dist
	jar cvfe dist/connectfour.jar connectfour/CFApp -C dist dist/connectfour/*
 ```

To run application

```
	cd dist
	java -jar connectfour.jar
```

## License

Project under GPL v3 license
