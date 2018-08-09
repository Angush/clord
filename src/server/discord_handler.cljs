(ns server.discord-handler
  (:require [server.mvf4 :refer [punish]]
            [server.MVF_5 :refer [add_term]]
            [server.MVF_5 :refer [remove_term]]
            [server.MVF_5 :refer [view_terms]]))

(def commands
  [
   {
    :command "ping"                                         ; This string is the command to match
    :exec    punish                                         ; The use case is the function to execute
    }
   {
    :command "hello"
    :exec    (fn [msg-obj] (.reply msg-obj "world"))
    }
   {
    :command ".addterm"
    :exec    (fn [msg-obj] (add_term msg-obj))
    }
   {
    :command ".removeterm"
    :exec    (fn [msg-obj] (remove_term msg-obj))
    }
   {
    :command ".viewterms"
    :exec    (fn [msg-obj] (view_terms msg-obj))
    }
   ]
  )

(defn find-command
  [str]
  (->>
    (filter #(= (:command %) (.toLowerCase (first (.split str " ")))) commands)
    first))

(defn handle-command
  "This function checks the message and when it matches a
  command it executes a function provided"
  [msg]
  (let [msg-content (aget msg "content")
        command (find-command msg-content)]
       (if (nil? command)
         msg
         ((:exec command) msg))))