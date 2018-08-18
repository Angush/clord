(ns server.message-handler
  (:require [server.MVF_1_v2 :refer [mvf_1]]
            [server.mvf3 :refer [rapsheet]]
            [server.MVF_5 :refer [add_term]]
            [server.MVF_5 :refer [remove_term]]
            [server.MVF_5 :refer [view_terms]]
            [server.permissions :refer [check-perms]]
            [server.permissions :refer [incorrect-perms]]
            [server.punish-on-command :refer [warn-user]]
            [server.punish-on-command :refer [kick-user]]
            [server.punish-on-command :refer [ban-user]]))

(defn help
  [msg]
  (if (check-perms msg nil)
    (.reply msg "here are all of my commands:\n`.rapsheet me` - view your own rapsheet (list of punishments I have performed on this user)\n`.rapsheet @user` - view another user's rapsheet (requires a ping).\n`.viewterms` - view the list of blacklisted terms.\n`.addterm word` - add the given word to the blacklist.\n`.removeterm word` - remove the given word from the blacklist.\n`.warn` - manually warn a user (requires a ping; can also take a reason/message)\n`.kick` - manually kick a user (requires a ping; can also take a reason/message)\n`.ban` - manually ban a user (requires a ping; can also take a reason/message)\n\nThat's everything!")
    (.reply msg "the only command of mine you're able to use is:\n`.rapsheet me` - view your own rapsheet (list of punishments I have performed on this user)")))

(def commands
  [{:command ".help"
    :exec    help
    :modonly false}
   {:command ".rapsheet me"
    :exec    rapsheet
    :modonly false}
   {:command ".rapsheet"
    :exec    rapsheet}
   {:command ".addterm"
    :exec    add_term}
   {:command ".removeterm"
    :exec    remove_term}
   {:command ".viewterms"
    :exec    view_terms}
   {:command ".warn"
    :exec    warn-user}
   {:command ".kick"
    :exec    kick-user}
   {:command ".ban"
    :exec    ban-user}])

(defn find-command
  ([content]
    (find-command content 0))
  ([content, index]
    (let [cmd (get commands index)]
      (if-not (nil? cmd)
        (if (.startsWith content (:command cmd))
          cmd
          (recur content (inc index)))))))

(defn is-modonly
  [cmd]
  (let [modonly (:modonly cmd)]
    (if (or (nil? modonly) (true? modonly))
      true
      false)))

(defn handle-command
  "This function checks the message and when it matches a
  command it executes a function provided"
  [bot, msg]
  (let [msg-content (.toLowerCase (aget msg "content"))
        command (find-command msg-content)
        channel (aget msg "channel")
        channel-type (aget channel "type")]
    (if (= channel-type "text")
      (if (nil? command)
        (mvf_1 msg bot)
        (if-not (is-modonly command)
          ((:exec command) msg)
          (if (check-perms msg nil)
            ((:exec command) msg)
            (incorrect-perms msg))))
      ; could add handling here to say "Commands do not work in DMs" ?
)))