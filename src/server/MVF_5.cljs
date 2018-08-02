(ns server.MVF_5
  (:require [clojure.string :as str]

            )
  )


(defn add_term
      [term]
      (println "Adding " term " to blacklist"))


(defn remove_term
      [term]
      (println "Removing " term " from blacklist"))


(defn view_terms
      []
      (println "Sending you a PM containing the blacklist"))