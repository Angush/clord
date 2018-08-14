(ns server.mvf3
  (:require ["fs" :as fs]
            [cognitect.transit :as transit]
            [server.repository :refer [filename]]
            [server.error :as error]))

(defn rapsheet [msg]
  (let [users (.-users (.-client msg))
        rapsheets (get (transit/read (transit/reader :json) (fs/readFileSync filename "utf8")) "rapsheet")
        msg_author (.-author msg)
        msg_author_id (.-id msg_author)
        msg_cmd (first (.split (.-content msg) " "))
        msg_content (clojure.string/join " " (rest (.split (.-content msg) " ")))
        msg_content_id (clojure.string/join "" (rest (rest (reverse (rest (reverse msg_content))))))
        msg_user #(.get users msg_content_id)
        msg_user_id #(.-id (msg_user))
        msg_user_rapsheet #(get rapsheets %)
        get_rapsheet (fn [u i] (comment u=user, i=id) (str "Here is " u "'s current rapsheet:\n" (clojure.string/join "\n" (map #(str "\t- " (get (msg_user_rapsheet i) %) " " % "s") (keys (msg_user_rapsheet i))))))
        ret_clean #(str % " has no rap sheet. They're squeaky-clean!")]
    (.send (.-channel msg)
      (cond
        ; `.rapsheet me`
        (= msg_content "me") (if (contains? rapsheets msg_author_id) (get_rapsheet msg_author msg_author_id) (ret_clean msg_author))
        ; @user is not mod
        ;(TODO) "Only Moderators can look at people's rap sheets."
        ; `.rapsheet`
        (empty? msg_content) (error/msg (str "`" msg_cmd "` requires @user."))
        ; `.rapsheet not a user`
        (not (.includes (.array users) (msg_user))) (error/msg (str "`" msg_content "` is not a @user! `" msg_cmd "` requires @user; try using tab-completion."))
        ; `.rapsheet @bot`
        (.-bot (msg_user)) (ret_clean (msg_user)) ; "ERR: @user cannot be a bot."
        ; no rapsheet
        (not (contains? rapsheets (msg_user_id))) (ret_clean (msg_user))
        :else (get_rapsheet (msg_user) (msg_user_id))))))
