(ns credit-card-datomic.client
  (:require [schema.core :as s]
            [credit-card-datomic.credit-card :as c.credit-card]
            [credit-card-datomic.db :as c.db]))

(s/set-fn-validation! true)

(defn uuid [] (java.util.UUID/randomUUID))

(def Client {:client/id                    s/Uuid
             (s/optional-key :client/name) s/Str
             (s/optional-key :client/cpf)  s/Str
             (s/optional-key :client/test)  [s/Str]
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
  {:client/id   (uuid)
   :client/name name
   :client/cpf  cpf
   :client/test []
   :client/card []})

(s/defn add-clients!
  [clients :- [Client]]
  (c.db/upsert-on-db! clients))

(def client1 (new-client "Client1" "000000000-00"))
(def client2 (new-client "Client2" "111111111-11"))
(def client3 (new-client "Client3" "222222222-22"))
(def card1 (c.credit-card/new-card "5555 4444 3333 2222" 123 "10/28"))
(c.credit-card/add-credit-cards! [card1])

(add-clients! [client1 client2 client3])

(s/defn add-card-on-client!
  [client card]
  (c.db/upsert-on-db! [[:db/add [:client/id (:client/id client)]
                                :client/card
                                [:card/id (:card/id card)]]]))

(s/defn all-clients! []
  (c.db/all-data! :client/id))




