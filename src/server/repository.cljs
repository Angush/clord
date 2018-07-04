(ns server.repository
  (:require ["fs" :as fs]))

(def filename "../src/db.json")
(def json-file (fs/readFileSync filename "utf8"))
(def db (js->clj (.parse js/JSON json-file) :keywordize-keys true))

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
      (:users db))

(defn add-user
      []
      (write-data {:users [{:name "Daniel"} {:name "Angus"}]}))
