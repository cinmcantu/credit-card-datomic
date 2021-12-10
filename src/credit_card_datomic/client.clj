(ns credit-card-datomic.client
  (:require [schema.core :as s]
            [credit-card-datomic.credit-card :as c.credit-card]
            [credit-card-datomic.db :as c.db]))

(s/set-fn-validation! true)

(defn uudi [] (java.util.UUID/randomUUID))

(def Client {:client/id                    s/Uuid
             (s/optional-key :client/name) s/Str
             (s/optional-key :client/cpf)  s/Str
             (s/optional-key :client/card) [c.credit-card/Card]})

(def client-schema [{:db/ident       :client/id
                     :db/valueType   :db.type/uuid
                     :db/cardinality :db.cardinality/one
                     :db/unique      :db.unique/identity}
                    {:db/ident       :client/name
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :client/cpf
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :client/card
                     :db/valueType   :db.type/ref
                     :db/cardinality :db.cardinality/many}])

(c.db/create-schema! client-schema)

(s/defn new-client :- Client
  [name cpf]
  {:client/id   (uudi)
   :client/name name
   :client/cpf  cpf
   :client/card []})

(s/defn add-clients!
  [clients :- [Client]]
  (c.db/upsert-on-db! clients))


