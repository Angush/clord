(ns server.MVF_5
  (:require [clojure.string :as str]
            [server.repository :as repo]
            ; Replaces clojure's 'format' in cljs https://stackoverflow.com/a/34668677
            [goog.string :as gstring]
            [goog.string.format]))

(defn add_term
  "When called with '.addterm <term>' this function will add the given <term> to
   the blacklist if it's not currently blacklisted"
  [bot msg]

  ; Load blacklist
  (def blacklist (into #{} (repo/read_blacklist)))

  ; Get content from msg
  (def content (re-find #" \S+" (str/lower-case (aget msg "content"))))

  (if (some? content)
    ; If command is properly formatted
    (do
      ; Strip new term down to word only
      (def new_term (str/replace content #" " ""))

      ; Check if term is not blacklist
      (if-not (contains? blacklist new_term)
        ; Term not blacklisted
        (do
          ; Add term to blacklist and sort alphabetically before writing file
          (repo/write-blacklist (sort(conj blacklist new_term)))

          ; Reply with success message
          (.reply msg "the blacklist has been updated!"))

        ; Term already blacklisted
        (.reply msg "that word is already blacklisted.")))

    ; Send error if not properly formatted
    (.reply msg "Incorrect command format. Command should be '.addterm <term>'")))

(defn remove_term
  "When called with '.removeterm <term>' this function will remove the given <term>
  from the blacklist if it's currently blacklisted"
  [bot msg]

  ;Load blacklist
  (def blacklist (into #{} (repo/read_blacklist)))

  ; Get content from msg
  (def content (re-find #" \S+" (str/lower-case (aget msg "content"))))

  (if (some? content)
    ; If command is properly formatted
    (do
      ; Strip old term down to word only
      (def old_term (str/replace content #" " ""))

      ; Check if term is blacklist
      (if (contains? blacklist old_term)
        ; Term blacklisted
        (do
          ; Remove term from blacklist and sort alphabetically before writing file
          (repo/write-blacklist (sort (disj blacklist old_term)))
          ; Reply with success message
          (.reply msg "the blacklist has been updated!"))
        ; Term not blacklisted
        (.reply msg "that word is not currently blacklisted.")))

    ; Send error if not properly formatted
    (.reply msg "Incorrect command format. Command should be '.removeterm <term>'")))

(defn view_terms
  "Sends a message containing the blacklist as a reply to .viewterms"
  [bot msg]

  ; Load blacklist
  (def blacklist (sort(into #{} (repo/read_blacklist))))

  ; Send reply containing blacklist
  (.reply msg (gstring/format "Here is the blacklist: ```%s```" blacklist)))
