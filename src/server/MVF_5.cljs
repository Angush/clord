(ns server.MVF_5
  (:require [clojure.string :as str]
            ["fs" :as fs]
          ))

; I canabalised this from repository.cljs, I might move this to repository later.
(defn read_blacklist
  []
  (def filename "src/blacklist.json")
  (js->clj (.parse js/JSON (fs/readFileSync filename "utf8")) :keywordize-keys true))


(defn add_term
  []
  ;Load blacklist, _x added to avoid warning. I'll probably change this later
  (def blacklist_a (read_blacklist))
  (println "Adding to blacklist"))


(defn remove_term
  []
  (def blacklist_b (read_blacklist))
  (println "Removing from blacklist"))


(defn view_terms
  []
  (def blacklist_c (read_blacklist))
  (println "Dumping blacklist"))
