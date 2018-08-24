(ns server.permissions
  (:require [server.environment :refer [env-vars]]))

(defn check-perms
  "Returns true if user has the designated Mods role. Returns false if they don't, UNLESS the command is '.rapsheet me', in which case, returns true, as this does not require Mod access."
  [msg, specific-member]
  (let [guild-owner-id (aget msg "guild" "owner" "id")
        mod-role-id (get env-vars "MOD_ROLE_ID")
        member (if (nil? specific-member) (aget msg "member") specific-member)
        member-id (aget member "id")
        member-roles (aget member "roles")
        member-has-mod-role (.has member-roles mod-role-id)]
    (if (or member-has-mod-role (= guild-owner-id member-id))
      true
      false)))

(defn incorrect-perms
  "Informs user they do not have the requisite permissions to use the given command."
  ([msg]
    (.reply msg "only people with the Mod role can use this command."))
  ([msg, errtext]
    (.reply msg (str "only people with the Mod role can use this command. " errtext))))