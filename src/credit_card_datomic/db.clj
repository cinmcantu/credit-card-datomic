(ns credit-card-datomic.db
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/credit-card")

(defn open-db  []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn delete-db []
  (d/delete-database db-uri))

(def conn (open-db))

(defn create-schema! [schema]
  (d/transact conn schema))

(defn upsert-on-db!
  [data]
  (d/transact conn data))

(defn all-data!
  ([sent-key]
   (all-data! sent-key (d/db conn)))
  ([sent-key db]
   (d/q '[:find [(pull ?entity [*]) ...]
          :in $ ?key
          :where [?entity ?key]] db sent-key)))

(defn one-item!
  ([wanted-key wanted-data]
   (one-item! wanted-key wanted-data (d/db conn)))
  ([wanted-key wanted-data db]
   (d/pull db '[*] [wanted-key wanted-data])))

(defn many-itens!
  ([wanted-key wanted-data]
   (many-itens! wanted-key wanted-data (d/db conn)))
  ([wanted-key wanted-data db]
   (d/q '[:find [(pull ?entity [*]) ...]
          :in $ ?key ?data
          :where [?entity ?key ?data]]
        db wanted-key wanted-data)))
; (delete-db)






