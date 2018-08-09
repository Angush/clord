(ns server.main
  (:require ["discord.js" :as discord]
            [server.discord-handler :refer [handle-command]]
            [server.environment :refer [env-vars]]
            [server.MVF_1_v2 :refer [get_msg_info]]))

; Discord configuration
(def token (get env-vars "DISCORD_TOKEN"))
(def discord-client (new discord/Client))

; Discord client event listeners
(.on discord-client
     "ready"
     (partial println "Discord client connected."))

(.on discord-client
     "message"
     handle-command)

(.on discord-client
     "message"
     get_msg_info)

; App lifecycle events
(defn reload! []
      (do
        (println "Code updated!")))

(defn main! []
      (do
        (println "App loaded!")
        (.login discord-client token)))
