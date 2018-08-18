(ns server.MVF_1_v2
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [server.environment :refer [env-vars]]
            [server.repository :as repo]
            [server.mvf4 :as mvf4]
            [server.permissions :refer [check-perms]]))

(defn check_msg
  [msg, content, bot]

  ; Load blacklist and convert to set
  (def blacklist (into #{} (repo/read_blacklist)))

  ; Split message into individual words, convert to set
  (def msg_words (into #{} (str/split content #"\s")))

  ; Get result of intersection of blacklist and msg words sets
  (let [used-terms (set/intersection blacklist msg_words)]
    (if-not (empty? used-terms)
      (do
        ; Remove message
        (.delete msg)
        ; Call auto punishment module
        (mvf4/punish msg (str "Used the banned term(s): " (str/join "; " used-terms) "."))))))

(defn mvf_1
  "This function accepts msg object from discord and gets the message ID, channel ID, author, and message content. It then passes this info to mvf_1"

  [msg, bot]

  (def content (str/lower-case (aget msg "content")))

  (if-not (aget msg "author" "bot")
    (if-not (check-perms msg nil)
      (check_msg msg content bot))))
