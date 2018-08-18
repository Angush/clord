# clord

## Dependencies
- `shadow-cljs`: This application uses shadow-cljs to run. [Go to Docs.](http://shadow-cljs.org/)
- `Node & NPM`: You need node and NPM running. (LTS version recommended) [Go to Docs](https://nodejs.org/en/)
- `Discord 2FA`: Clord makes use of permissions that may require the bot owner have 2FA enabled on their Discord account. [Go to Docs](https://discordapp.com/developers/docs/topics/oauth2#twofactor-authentication-requirement)

## Environment Variables
- `DISCORD_TOKEN`: This is the token we use to connect to the discord app. [Go to docs](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token) **[REQUIRED]**
- `LOG_CHANNEL_ID`: ID of the channel Clord will post in when a message is flagged, when an automatic punishment is carried out, and when a *manual* punishment is carried out. Find it by enabling Developer Mode, then right clicking the channel and selecting "Copy ID". **[REQUIRED]**
- `MOD_ROLE_ID`: ID of the Mod role Clord will use to determine who may utilize Clord's commands (and who to ignore for automatic punishments). Find it by pinging the role in any channel and preceeding it with a backslash, eg. `\@Mods Role`, then copying the numbers from within the resulting string. **[REQUIRED]**
- `WARN_THRESHOLD`: An integer indicating how many warnings a user should have on their rapsheet before Clord moves on to automatically kicking. Defaults to 3. **[OPTIONAL]**
- `KICK_THRESHOLD`: An integer indicating how many kicks a user should have on their rapsheet before Clord moves on to automatically banning them. Defaults to 3. **[OPTIONAL]**

## Commands
### Install
Open a terminal window and run the following command:
```bash
npm i
```

Then duplicate the `.env.skell`, rename this duplicate to `.env` (*without* the `.skell`), and fill in the environment variables as specified above.

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
## Running Clord
When you run Clord using the two commands in the previous section, the bot will inform you (in the terminal) if there are any missing required environment variables, as well as providing a link to add the bot to your server, if you have not already done so.

Once the bot is up and running, you can see the list of commands by posting `.help` in your server. If you have the Mod role (or you're the server owner), Clord will show you all the available commands. If someone who *doesn't* have the Mod role (and *is not* the server owner) uses the `.help` command, Clord will only show commands they have the permissions to use (ie. `.rapsheet me`).

## Credits
The default blacklist is a heavily modified version of the list found [here](https://github.com/words/profanities). We've shortened it dramatically to fit within Discord's 2,000 characters per message limit. You may find it over-sensitive; be sure to peruse the list with `.viewterms` and delete any against which you do not want Clord to take automatic action, either by using `.removeterm` or by editing the `/src/blacklist.json` file directly.