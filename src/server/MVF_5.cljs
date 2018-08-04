(ns server.MVF_5
  (:require [clojure.string :as str]
            [server.repository :as repo]
          ))


(defn add_term
  []
  ;Load blacklist, _x added to avoid warning. I'll probably change this later
  (def blacklist_a (repo/read_blacklist))
      (println (type blacklist_a))
      (println "Adding to blacklist"))


(defn remove_term
  []
  (def blacklist_b (repo/read_blacklist))
      (println (type blacklist_b))
      (println "Removing from blacklist"))


(defn view_terms
  []
  (def blacklist_c (repo/read_blacklist))
  (println (type blacklist_c))
  (println "Dumping blacklist"))
