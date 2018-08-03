(ns server.MVF_1_v2
  (:require [clojure.string :as str]
            [clojure.set :as set]
            ["fs" :as fs]
            ))

; I canabalised this from repository.cljs, I might move this to repository later.
(defn read_blacklist
  []
  (def filename "src/blacklist.json")
  (js->clj (.parse js/JSON (fs/readFileSync filename "utf8")) :keywordize-keys true))


(defn mvf_1
  [msg channel_id msg_id content author]

  ; Load blacklist and convert to set
  (def blacklist (into #{} (read_blacklist)))

  ; Get lowercase of message - can probably do this in get_msg_info
  (def lower_content (str/lower-case content))

  ; Split message into individual words, convert to set
  (def msg_words (into #{}(str/split lower_content #"\s")))

  ; Debug prints
  ;(println (type blacklist) blacklist)
  ;(println (type msg_words) msg_words)

  ; Test var + debug print
  ;(def has_bad_words (empty?(set/intersection blacklist msg_words)) )
  ;(println has_bad_words)

  ; Get result of intersection of blacklist and msg words sets
  (if-not (empty?(set/intersection blacklist msg_words))
    (println "The message has bad words")

    ;Post warning message
    ;(.reply msg " wash your mouth out with soap! We don't use that kind of language here.")

    ; TODO
    ; Remove message

    ; TODO
    ; Call auto punishment
    ))


(defn get_msg_info
  "This function accepts msg object from discord and gets the message ID, channel ID,
   author, and message content. It then passes this info to mvf_1"

  [msg]

  (def channel_id (aget msg "channel"))
  (def msg_id (aget msg "id"))
  (def content (aget msg "content"))
  (def author (aget msg "author"))

  (mvf_1 msg channel_id msg_id content author))
