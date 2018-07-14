(ns server.repository
  (:require ["fs" :as fs]))

(def filename "../src/db.json")

(defn read-db
      []
      (js->clj (.parse js/JSON (fs/readFileSync filename "utf8")) :keywordize-keys true))

(defn write-data
      "Write data to the DB file. This function assumes next-state contains
      all the data or the full db"
      [next-state]
      (->>
        next-state
        (clj->js)
        #(.stringify js/JSON %)
        (partial fs/writeFileSync filename)))

(defn get-users
      []
      (:users (read-db)))

(defn add-user
      []
      (write-data {:users [{:name "Daniel"} {:name "Angus"}]}))
