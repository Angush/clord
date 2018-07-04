(ns server.main
  (:require [server.repository :as repository]))

(defn reload! []
      (do
        (println "Code updated!")
        (println (repository/add-user))))

(defn main! []
      (do
        (println "App loaded!")
        (println (repository/get-users))))