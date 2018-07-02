# clord

## Dependencies
- *shadow-cljs*: This application uses shadow-cljs to run. [Do to Docs.](http://shadow-cljs.org/)
- *Node & NPM*: You need node and NPM running. (LTS version recommended) [Go to Docs](https://nodejs.org/en/)

## Commands
### Install
Open a terminal window and run the following command:
```bash
npm i
```

### Dev
Open a terminal window and run the following command:
```bash
shadow-cljs watch app
```

After the watcher is running, go to another terminal window and run:
```bash
node target/main.js
```
