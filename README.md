# clord

## Dependencies
- *shadow-cljs*: This application uses shadow-cljs to run. [Go to Docs.](http://shadow-cljs.org/)
- *Node & NPM*: You need node and NPM running. (LTS version recommended) [Go to Docs](https://nodejs.org/en/)

## Environment Variables
- *DISCORD_TOKEN*: This is the token we use to connect to the discord app. [Go to docs](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token)
- *BOT_CLIENT_ID*: Required so clord ignores messages that it sends when checking the message content.

## Commands
### Install
Open a terminal window and run the following command:
```bash
npm i
```

Then copy the .env.skell and fill in the env variables

### Dev
Before developing make sure you have a .env file on the root fo the project with all the environment variables set. 
Open a terminal window and run the following command:
```bash
shadow-cljs watch app
```

After the watcher is running, go to another terminal window and run:
```bash
npm run start
```
## Credits

The blacklist is a heavily modified version of the list found [here](https://github.com/words/profanities). We've shortened it to fit within Discord's 2,000 characters per message limit.
