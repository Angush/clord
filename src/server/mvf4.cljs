(ns server.mvf4
  (:require [server.repository :as repo]))

(def punishment-levels [:warning :kick :perm-ban])

(defn get-user-rapsheet
  [user-id]
  (let [user (repo/get-user-rap-by-id user-id)]
    (reduce
      #(assoc %1 %2 (or (%2 user) 0))
      {}
      punishment-levels)))

(def punishment-config {
                        :warning  3
                        :kick 3
                        :perm-ban 1
                        })

(defn find-punishment
  ([rap-sheet config]
   (find-punishment 0 rap-sheet config))
  ([level rap-sheet config]
   (let [level-k (get punishment-levels level)]
     (if (< (level-k rap-sheet) (level-k config))
       level-k
       (recur (inc level) rap-sheet config)))))

(defn ban
  [msg-obj]
  (let [guild (aget msg-obj "guild")
        user-id (aget msg-obj "author" "id")]
    (.ban guild user-id)))

(defn kick
  [msg-obj]
  (let [guild (aget msg-obj "guild")
        user-id (aget msg-obj "author" "id")]
    (.kick guild user-id )))

(def punishments
  {
   :warning  (fn [msg-obj] (.reply msg-obj " wash your mouth out with soap! We don't use that kind of language here."))
   :kick kick
   :perm-ban ban
   })

(defn
  punish
  [bot-obj msg-obj]
  (let [user-id (aget msg-obj "author" "id")
        rap-sheet (get-user-rapsheet user-id)
        punishment-k (find-punishment rap-sheet punishment-config)
        punishment (get punishments punishment-k)]
    (do
      (repo/update-user-rapsheet user-id punishment-k (inc (punishment-k rap-sheet)))
      (punishment msg-obj))))
