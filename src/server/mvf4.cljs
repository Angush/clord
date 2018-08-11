(ns server.mvf4
  (:require [server.repository :as repo]))

(def punishment-levels [:warning :kick :temp-ban :perm-ban])

(defn get-user-rapsheet
  [user-id]
  (let [user (repo/get-user-rap-by-id user-id)]
    (reduce
      #(assoc %1 %2 (or (%2 user) 0))
      {}
      punishment-levels)))

(def punishment-config {
                        :warning  3
                        :kick     3
                        :temp-ban 3
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

; TODO
; Write discord actions
(def punishments
  {
   :warning  (fn [msg-obj] (.send (aget msg-obj "author") "You have been warned"))
   :kick     (fn [msg-obj] (.send (aget msg-obj "author") "You have been kicked"))
   :temp-ban (fn [msg-obj] (.send (aget msg-obj "author") "You have been banned temporarily"))
   :perm-ban (fn [msg-obj] (.send (aget msg-obj "author") "You have been banned permanently"))
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
