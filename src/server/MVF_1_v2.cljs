(ns server.MVF_1_v2
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [server.environment :refer [env-vars]]
            [server.repository :as repo]
            [server.mvf4 :as mvf4]))

(def bot_id (get env-vars "BOT_CLIENT_ID"))

(defn mvf_1
  [msg content]

  ; Load blacklist and convert to set
  (def blacklist (into #{} (repo/read_blacklist)))

  ; Split message into individual words, convert to set
  (def msg_words (into #{}(str/split content #"\s")))

  ; Get result of intersection of blacklist and msg words sets
  (if-not (empty?(set/intersection blacklist msg_words))
    (do
      ; Remove message
      (.delete msg)
      ; Call auto punishment module
      (mvf4/punish msg))))


(defn get_msg_info
  "This function accepts msg object from discord and gets the message ID, channel ID,
   author, and message content. It then passes this info to mvf_1"

  [msg]

  (def content (str/lower-case (aget msg "content")))

  ; Check message content if the author isn't Clord
  (def result (re-matches (re-pattern bot_id) (aget msg "author" "id")))

  (if-not (= result bot_id)
    (mvf_1 msg content)))
