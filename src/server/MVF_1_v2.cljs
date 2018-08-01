(ns server.MVF_1_v2
  (:require [clojure.string :as str]
            [clojure.set :as set]
    )
  )


(defn mvf_1
  [channel_id msg_id content author]

  ; TODO:
  ; Read blacklist from file and convert to set

  ; Load blacklist, convert to set
  ;(def read_blacklist (slurp "../blacklist.json"))

  ; Debug print
  ;(println (type read_blacklist))

  ; Hardcoded until I know how to properly read json from file
  (def blacklist (into #{} (list "bitch" "cunt" "fuck" "shit")))

  ; Get lowercase of message - can probably do this in get_msg_info
  (def lower_content (str/lower-case content))

  ; Split message into individual words, convert to set
  (def msg_words (into #{}(str/split lower_content #"\s")))

  ; Debug prints
  ;(println (type blacklist) blacklist)
  ;(println (type msg_words) msg_words)

  ; Test var + debug print
  ; (def has_bad_words (empty?(set/intersection blacklist msg_words)) )
  ; (println has_bad_words)

  ; Get result of intersection of blacklist and msg words sets
  (if-not (empty?(set/intersection blacklist msg_words))
    ; TODO
    ; Remove message

    ; TODO
    ;Post warning message

    (println
      (format "Hey @%s! Wash your mouth out with soap! We don't use that kind of language here."
              author)
      )

    ; TODO
    ; Call auto punishment mvf

    )
  )


(defn get_msg_info
  [msg]

  (def channel_id (aget msg "channel"))
  (def msg_id (aget msg "id"))
  (def content (aget msg "content"))
  (def author (aget msg "author"))

  ; Debug prints
  ;(println (type channel_id) channel_id)
  ;(println (type msg_id) msg_id)
  ;(println (type content) content)
  ;(println (type author) author)

  (mvf_1 channel_id msg_id content author)
  )


; Run mvf_1 directly for testing
(println "-----")
(mvf_1 10 20 "This message contains shit" "Bob")
(println "-----")
(mvf_1 10 20 "This message contains words" "Bob")
(println "-----")


; TODO Bot permissions:
; 'MANAGE_MESSAGES'
; 'SEND_MESSAGES'
