(ns server.MVF_5
  (:require [clojure.string :as str]
            [server.repository :as repo]
            ; Replaces clojure's 'format' in cljs https://stackoverflow.com/a/34668677
            [goog.string :as gstring]
            [goog.string.format]))

(defn add_term
  [msg]
  ;Load blacklist
  (def blacklist (into #{} (repo/read_blacklist)))

  ; Hardcoded test
  ;(def msgt ".addterm =Chocolate")
  ;(def content (str/lower-case msgt))
  ;(println content)

  ; Get content from msg
  (def content (str/lower-case (aget msg "content")))

  ; Find term to be added from message content
  (def new_term (str/replace (re-find #"=\S+" content) #"=" ""))

  ; Check if term is not blacklist
  (if-not (contains? blacklist new_term)
    ; Term not blacklisted
    (do
      ; Add term to blacklist and sort alphabetically before writing file
      (repo/write-data (sort(conj blacklist new_term)))
      ; Reply with success message
      (.reply msg "the blacklist has been updated!"))

    ; Term already blacklisted
    (.reply msg "that word is already blacklisted.")))


(defn remove_term
  [msg]
  ;Load blacklist
  (def blacklist (into #{} (repo/read_blacklist)))

  ; Hardcoded test
  ;(def msgt ".removeterm =Chocolate")
  ;(def content (str/lower-case msgt))
  ;(println content)

  ; Get content from msg
  (def content (str/lower-case (aget msg "content")))

  ; Find term to be added from message content
  (def new_term (str/replace (re-find #"=\S+" content) #"=" ""))

  ; Check if term is not blacklist
  (if (contains? blacklist new_term)
    ; Term blacklisted
    (do
      ; Remove term from blacklist and sort alphabetically before writing file
      (repo/write-data (sort (disj blacklist new_term)))
      ; Reply with success message
      (.reply msg "the blacklist has been updated!"))

    ; Term not blacklisted
    (.reply msg "that word is not currently blacklisted.")))


(defn view_terms
  "Sends a message containing the blacklist as a reply to .viewterms"
  [msg]

  ; Load blacklist
  (def blacklist (sort(into #{} (repo/read_blacklist))))

  ; Send reply containing blacklist
  (.reply msg (gstring/format "Here is the blacklist: ```%s```" blacklist)))
