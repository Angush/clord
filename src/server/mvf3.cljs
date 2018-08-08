(ns server.mvf3
  (:require ["fs" :as fs]
            [cognitect.transit :as transit]))

(defn rapsheet [bot msg]
  (let [users (.split (str (.array (aget bot "users"))) ",")
        rapsheets (get (transit/read (transit/reader :json) (fs/readFileSync "src/db.json" "utf8")) "rapsheet")
        msg_author (aget msg "author")
        msg_author_id (aget msg "author" "id")
        msg_cmd (first (.split (aget msg "content") " "))
        msg_content (clojure.string/join " " (rest (.split (aget msg "content") " ")))
        msg_content_id (clojure.string/join "" (rest (rest (reverse (rest (reverse msg_content))))))
        msg_user #(.get (.-users bot) msg_content_id)
        msg_user_id #(aget (msg_user) "id")
        msg_user_rapsheet #(get rapsheets %)
        get_rapsheet (fn [u i] (comment u=user, i=id) (str "Here is " u "'s current rapsheet:\n" (clojure.string/join "\n" (map #(str "\t- " (get (msg_user_rapsheet i) %) " " % "s") (keys (msg_user_rapsheet i))))))
        ret_clean #(str % " has no rap sheet. They're squeaky-clean!")]
    (.send (aget msg "channel")
      (cond
        ; `.rapsheet me`
        (= msg_content "me") (if (contains? rapsheets msg_author_id) (get_rapsheet msg_author msg_author_id) (ret_clean msg_author))
        ; @user is not mod
        ;(TODO) "Only Moderators can look at people's rap sheets."
        ; `.rapsheet`
        (empty? msg_content) (str "ERR: `" msg_cmd "` requires @user.")
        ; `.rapsheet not a user`
        (not (some #(= msg_content %) users)) (str "ERR: `" msg_content "` is not a @user! `" msg_cmd "` requires @user; try using tab-completion.")
        ; `.rapsheet @bot`
        (aget (msg_user) "bot") (ret_clean (msg_user)) ; "ERR: @user cannot be a bot."
        ; no rapsheet
        (not (contains? rapsheets (msg_user_id))) (ret_clean (msg_user))
        :else (get_rapsheet (msg_user) (msg_user_id))))))
