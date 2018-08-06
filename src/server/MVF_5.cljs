(ns server.MVF_5
  (:require [clojure.string :as str]
            [server.repository :as repo]
            ; Replaces clojure's 'format' in cljs https://stackoverflow.com/a/34668677
            [goog.string :as gstring]
            [goog.string.format]
  ))


(defn add_term
  [msg]
  ;Load blacklist, _x added to avoid warning. I'll probably change this later
  (def blacklist_a (repo/read_blacklist))
  (println (type msg))
  (println "Adding to blacklist"))


(defn remove_term
  []
  (def blacklist_b (repo/read_blacklist))
  (println (type blacklist_b))
  (println "Removing from blacklist"))


(defn view_terms
  "Sends a message containing the blacklist as a reply to .viewterms"
  [msg]
  (def blacklist_c (repo/read_blacklist))
  (.reply msg (gstring/format "Here is the blacklist: ```%s```" blacklist_c)))
