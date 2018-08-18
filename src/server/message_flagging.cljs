(ns server.message-flagging
  (:require [server.environment :refer [env-vars]]))

(defn create-url-list
  [attachments, array, index]
  (let [attachment (get attachments index)]
    (if (nil? attachment)
      array
      (let [url (aget attachment "url")] 
        (.push array url)
        (recur attachments array (inc index))))))

(defn attachments-handler
  "Creates embed with attachments field (if applicable)."
  [discord, msg]
  (let [embed (new discord/RichEmbed)
        msg-attachments (aget msg "attachments")
        attachment-count (aget msg-attachments "size")]
    (if (zero? attachment-count)
      embed
      (do
        (let [urls (create-url-list (.array msg-attachments) (clj->js []) 0)]
          (.addField embed "ATTACHMENTS" (clojure.string/join "\n" urls)))))))

(defn post-message 
  "Creates and posts the alert to Moderators."
  [discord, bot, msg, user]
  (let [guild (aget msg "guild")
        guild-channels (aget guild "channels")
        channel-id (get env-vars "LOG_CHANNEL_ID")]
    (if (.has guild-channels channel-id) (do
      (let [channel (.get guild-channels channel-id)
            embed (attachments-handler discord msg)
            attachments (aget msg "attachments")
            member (aget msg "member")
            msg-content (aget msg "content")
            msg-author (aget msg "author")
            msg-channel (aget msg "channel")
            msg-created-at (aget msg "createdAt")
            member-color (aget member "displayHexColor")
            member-tag (aget msg-author "tag")
            member-avatar (aget msg-author "displayAvatarURL")
            mod-role-id (get env-vars "MOD_ROLE_ID")
            mod-role (.get (aget guild "roles") mod-role-id)]
        (.setTitle embed "FLAGGED MESSAGE")
        (.setTimestamp embed msg-created-at)
        (.setColor embed member-color)
        (.setFooter embed member-tag member-avatar)
        (.setDescription embed msg-content)
        (.send channel (str "**ALERT:** " user " flagged a message by " member " in " msg-channel "! " mod-role) embed)))
      (println (str "Channel with ID '" channel-id "' not found, so I cannot post the alert. Please add a correct channel ID to my .env file and reboot me.")))))

(defn handle-reaction
  "Handles logic to respond to a valid reaction."
  [discord, bot, reaction, user]
  (let [msg (aget reaction "message")
        user-id (aget user "id")
        msg-author (aget msg "author")
        msg-author-id (aget msg-author "id")
        author-is-bot (aget msg-author "bot")]
    (if-not (= msg-author-id user-id) (do
      (if-not (= author-is-bot true) (do
        (.remove reaction user-id)
        (post-message discord bot msg user)))))))

(defn check-reaction
  "Checks whether the reaction is the designated flag."
  [discord, bot, reaction, user]
  (let [emoji-obj (aget reaction "emoji")
        emoji (aget emoji-obj "name")
        msg (aget reaction "message")
        channel (aget msg "channel")
        channel-type (aget channel "type")]
    (if (= channel-type "text") (do
      (if (= emoji "🏴") (do
        (handle-reaction discord bot reaction user)))))))
