(ns credit-card-datomic.db
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/credit-card")

(defn open-db  []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn delete-db []
  (d/delete-database db-uri))

; SCHEMA CREDIT CARD
; card-id uuid
; card-number string
; Expiration Date string
; cvv number

; SCHEMA CLIENT
; client-id uuid
; cpf string
; name  string
; cards  many list


(def schema [ ; CARD
             {:db/ident       :card/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :card/number
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :card/expiration-date
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :card/cvv
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one}
             ; CLIENT
             {:db/ident       :client/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :client/name
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :client/cpf
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :client/cvv
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/many}])

(defn create-schema! [conn]
  (d/transact conn schema))

(def conn (open-db))

(create-schema! conn)

;(delete-db)






