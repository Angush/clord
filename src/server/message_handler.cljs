(ns server.message-handler
  (:require [server.MVF_1_v2 :refer [mvf_1]]
            [server.mvf3 :refer [rapsheet]]
            [server.mvf4 :refer [punish]]
            [server.MVF_5 :refer [add_term]]
            [server.MVF_5 :refer [remove_term]]
            [server.MVF_5 :refer [view_terms]]
            [server.permissions :refer [check-perms]]
            [server.permissions :refer [incorrect-perms]]))

(def commands
  [{:command ".rapsheet"
    :exec    rapsheet}
   {:command ".addterm"
    :exec    add_term}
   {:command ".removeterm"
    :exec    remove_term}
   {:command ".viewterms"
    :exec    view_terms}])

(defn find-command
  [str]
  (->>
   (filter #(= (:command %) (.toLowerCase (first (.split str " ")))) commands)
   first))

(defn handle-command
  "This function checks the message and when it matches a
  command it executes a function provided"
  [bot, msg]
  (let [msg-content (aget msg "content")
        command (find-command msg-content)
        channel (aget msg "channel")
        channel-type (aget channel "type")]
    (if (= channel-type "text")
      (if (nil? command)
        (mvf_1 msg bot)
        (if (or (is-self-rapsheet msg-content) (check-perms msg))
          ((:exec command) msg)
          (incorrect-perms msg)))
      ; could add handling here to say "Commands do not work in DMs" ?
)))

(defn is-self-rapsheet
  "Returns whether command is '.rapsheet me' to bypass permission check."
  [msg-content]
  (let [split-content (.split (.toLowerCase msg-content) " ")]
    (if (= (alength split-content) 2)
      (if (and (= (get split-content 0) ".rapsheet") (= (get split-content 1) "me"))
        true
        false)
      false)))