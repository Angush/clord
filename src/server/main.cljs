(ns server.main
  (:require ["discord.js" :as discord]
            [server.discord-handler :refer [handle-command]]
            [server.environment :refer [env-vars]]
            [server.message-flagging :refer [check-reaction]]
            [server.environment :refer [validate-env-vars]]
            [server.MVF_1_v2 :refer [mvf_1]]))

; Discord configuration
(def token (get env-vars "DISCORD_TOKEN"))
(def discord-client (new discord/Client))

; Discord client event listeners
(.on discord-client
     "ready"
     (partial println "Ready to go!"))

(.on discord-client
     "message"
     (partial handle-command discord-client))

(.on discord-client
     "messageReactionAdd"
     (partial check-reaction discord discord-client))

(.on discord-client
     "error"
     (partial println (str "Discord API error occurred. ")))

; App lifecycle events
(defn reload! []
      (do
        (println "Code updated!")))

(defn successful-login []
  (println "Login succeeded. Validating data...")
  (let [user (aget discord-client "user")
        valid (validate-env-vars discord-client user)]
    (if (true? valid)
      (let [tag (aget user "tag")]
        (println (str "Everything looks OK! Initialized as " tag ".")))
      (do
        (println "Fix this and run me again. Shutting down...")
        (js/process.exit)))))

(defn failed-login
  [err]
  (println (str "Login failed. ERROR:\n" err "\n\nNOTE: If this is about incorrect login details, you may have set up your .env file incorrectly. Ensure the file is named '.env' exactly, and not something else like '.env.skell', or I won't be able to load properly. If that isn't the issue, ensure my .env file contains a valid token for a Discord bot app."))
  (js/process.exit))

(defn main! []
      (do
        (println "App loaded!")
        (let [login-promise (.login discord-client token)]
          (.then login-promise successful-login failed-login))))