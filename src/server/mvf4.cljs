(ns server.mvf4)

; TODO
; Find the user and format the rap-sheet to
; match this structure
(defn get-user-rapsheet
  [user-id]
  {:warning 3 :kick 3 :temp-ban 3 :perm-ban 0})

(def punishment-config {
                        :warning  3
                        :kick     3
                        :temp-ban 3
                        :perm-ban 1
                        })

(def punishment-levels [:warning :kick :temp-ban :perm-ban])

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
   :warning  (fn [msg-obj] (.reply msg-obj "You have been warned"))
   :kick     (fn [msg-obj] (.reply msg-obj "You have been kicked"))
   :temp-ban (fn [msg-obj] (.reply msg-obj "You have been banned temporarily"))
   :perm-ban (fn [msg-obj] (.reply msg-obj "You have been banned permanently"))
   })

(defn
  punish
  [msg-obj]
  (let [rap-sheet (get-user-rapsheet "id")
        punishment-k (find-punishment rap-sheet punishment-config)
        punishment (get punishments punishment-k)]
    (do
      (println "update rap sheet value " punishment-k)
      (punishment msg-obj))))
