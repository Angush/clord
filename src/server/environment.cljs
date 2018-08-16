(ns server.environment
  (:require ["dotenv" :as dotenv]))

(.load dotenv)
(defn -js->clj+
      "For cases when built-in js->clj doesn't work. Source: https://stackoverflow.com/a/32583549/4839573"
      [x]
      (into {} (for [k (js-keys x)]
                    [k (aget x k)])))

(defn env
      "Returns current env vars as a Clojure map."
      []
      (-js->clj+ (.-env js/process)))

(def env-vars (env))

(defn validate-env-vars
  "Ensures the bot has the information it needs to operate correctly."
  [bot, user]
  (let [bot-guilds (aget bot "guilds")
        guild-count (aget bot-guilds "size")]
    (if (zero? guild-count)
      (do 
        (println (str "You need to add me to a server before you can initialize me! Visit this link: https://discordapp.com/oauth2/authorize/?permissions=125958&scope=bot&client_id=" (aget user "id") " to invite me. Make sure you don't disable any of those permissions, or I won't function correctly."))
        false)
    (if-not (= guild-count 1)
      (do
        (println "I seem to be in multiple servers, but I don't support multi-server operation. Please kick me from all servers except the one in which you most need me.")
        false)
      (let [guild (.first bot-guilds)
            guild-name (aget guild "name")
            guild-roles (aget guild "roles")
            mod-role-id (get env-vars "MOD_ROLE_ID")
            has-mod-role (.has guild-roles mod-role-id)
            guild-channels (aget guild "channels")
            log-channel-id (get env-vars "LOG_CHANNEL_ID")
            has-log-channel (.has guild-channels log-channel-id)]
        (if (false? has-mod-role)
          (do
            (println (str "The mod role you specified in my .env file (if you DID specify one) does not seem to exist on the '" guild-name "' server!"))
            false)
        (if (false? has-log-channel)
          (do 
            (println (str "The log channel you specified in my .env file (if you DID specify one) does not seem to exist on the '" guild-name "' server!"))
            false)
          true)))))))