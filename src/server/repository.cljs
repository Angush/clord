(ns server.repository
  (:require ["fs" :as fs]))

(def filename "src/db.json")
(def blacklist "src/blacklist.json")

(defn read-db
  []
  (js->clj (.parse js/JSON (fs/readFileSync filename "utf8")) :keywordize-keys true))

(defn read_blacklist
  []
  (js->clj (.parse js/JSON (fs/readFileSync blacklist "utf8")) :keywordize-keys true))

(def write-blacklist
  "Write data to the DB file. This function assumes next-state contains
  all the data or the full db"
  (comp
    #(fs/writeFileSync blacklist %)
    #(.stringify js/JSON %)
    clj->js))
	
(def write-data
  "Write data to the DB file. This function assumes next-state contains
  all the data or the full db"
  (comp
    #(fs/writeFileSync filename %)
    #(.stringify js/JSON %)
    clj->js))

(defn get-user-rap-by-id
  "Find user's raphseet by id. If the user is not found
  the function returns nil."
  [id]
  (let [rapsheet (:rapsheet (read-db))]
    (get rapsheet (keyword id))))

(defn update-user-rapsheet
  "Update user rapsheet"
  [id key val]
  (let [db (read-db)
        rapsheet (:rapsheet db)
        user (get rapsheet (keyword id))
        updated-user (assoc user key val)
        updated-sheet (assoc rapsheet (keyword id) updated-user)
        updated-db (assoc db :rapsheet updated-sheet)]
    (write-data updated-db)))