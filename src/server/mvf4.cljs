(ns server.mvf4
  (:require [server.repository :as repo]
            [server.environment :refer [env-vars]]
            ["discord.js" :as discord]))

(def punishment-levels [:warning :kick :perm-ban])

(defn get-user-rapsheet
  [user-id]
  (let [user (repo/get-user-rap-by-id user-id)]
    (reduce
      #(assoc %1 %2 (or (%2 user) 0))
      {}
      punishment-levels)))

(defn load-thresholds 
  [type]
    (let [env-string (get env-vars type)
          number (js/parseInt env-string)]
      (if (integer? number)
          number
          3)))

(def punishment-config {
                        :warning (load-thresholds "WARN_THRESHOLD")
                        :kick (load-thresholds "KICK_THRESHOLD")
                        :perm-ban 1
                        })

(defn find-punishment
  ([rap-sheet config]
   (find-punishment 0 rap-sheet config))
  ([level rap-sheet config]
   (let [level-k (get punishment-levels level)]
     (if (= level-k :perm-ban)
      :perm-ban
      (if (< (level-k rap-sheet) (level-k config))
        level-k
        (recur (inc level) rap-sheet config))))))

(defn punishment-success
  [msg-obj, member, punishment-verb, reason, performed-by]
  (let [user-id (aget member "id")
        rap-sheet (get-user-rapsheet user-id)
        punishment-type (cond (= punishment-verb "warned") :warning (= punishment-verb "kicked") :kick (= punishment-verb "permanently banned") :perm-ban)
        channel (.get (aget msg-obj "guild" "channels") (get env-vars "LOG_CHANNEL_ID"))
        embed (new discord/RichEmbed)
        reason-type (if (nil? performed-by) "Reason:" "Reason/Mod message:")]
    (.setDescription embed reason)
    (.setColor embed "#d10000")
    (if (nil? performed-by)
      (do 
        (.send channel (str member " (" (aget member "displayName") ") has been __" punishment-verb "__ automatically. " reason-type) embed)
        (repo/update-user-rapsheet user-id punishment-type (inc (punishment-type rap-sheet))))
      (do 
        (.send channel (str member " (" (aget member "displayName") ") has been " punishment-verb " by " performed-by ". " reason-type) embed)
        (repo/update-user-rapsheet user-id punishment-type (inc (punishment-type rap-sheet)))))))

(defn ban
  [msg-obj, member, reason, performed-by]
  (let [promise (.ban member (str "Automated punishment. " reason))]
    (.then promise (partial punishment-success msg-obj member "permanently banned" reason performed-by))))

(defn kick
  [msg-obj, member, reason, performed-by]
  (let [promise (.kick member (str "Automated punishment. " reason))]
    (.then promise (partial punishment-success msg-obj member "kicked" reason performed-by))))

(defn warn
  [msg-obj, member, reason, performed-by]
  (let [channel (aget msg-obj "channel")]
    (if (nil? performed-by)
      (let [promise (.send channel (str "Wash your mouth out with soap, " member "! We don't use that kind of language here. This is an automated warning, because you " (.toLowerCase reason)))]
        (.then promise (partial punishment-success msg-obj member "warned" reason performed-by)))
      (let [promise (.send channel (str "This is a warning - " reason))]
        (.then promise (partial punishment-success msg-obj member "warned" reason performed-by))))))

(def punishments
  {
   :warning warn
   :kick kick
   :perm-ban ban
   })

(defn punish
  [msg-obj, reason]
  (let [user-id (aget msg-obj "author" "id")
        rap-sheet (get-user-rapsheet user-id)
        punishment-k (find-punishment rap-sheet punishment-config)
        punishment (get punishments punishment-k)
        member (aget msg-obj "member")]
    (punishment msg-obj member reason nil)))
