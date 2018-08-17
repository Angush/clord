# clord

## Dependencies
- *shadow-cljs*: This application uses shadow-cljs to run. [Go to Docs.](http://shadow-cljs.org/)
- *Node & NPM*: You need node and NPM running. (LTS version recommended) [Go to Docs](https://nodejs.org/en/)
- *Discord 2FA*: Clord makes use of permissions that may require the bot owner have 2FA enabled on their Discord account. [Go to Docs](https://discordapp.com/developers/docs/topics/oauth2#twofactor-authentication-requirement)

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
## Adding Clord to your server
In order to add Clord to your server, copy/paste the link below, and replace <CLIENT_ID> with the client ID of your application.
The client ID can be found in the "Genreal Information" tab of your Discord application.

```https://discordapp.com/oauth2/authorize?&client_id=<CLIENT_ID>&scope=bot&permissions=1073753094```

## Credits

The blacklist is a heavily modified version of the list found [here](https://github.com/words/profanities). We've shortened it to fit within Discord's 2,000 characters per message limit.
