(ns credit-card-datomic.credit-card
  (:use clojure.pprint)
  (:require [schema.core :as s]
            [credit-card-datomic.db :as c.db]
            [datomic.api :as d]))

(s/set-fn-validation! true)

(defn uuid [] (java.util.UUID/randomUUID))

(def Card {:card/id                               s/Uuid
           (s/optional-key :card/number)          s/Str
           (s/optional-key :card/cvv)             Long
           (s/optional-key :card/expiration-date) s/Str})

(def card-schema [{:db/ident       :card/id
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
                   :db/cardinality :db.cardinality/one}])

(c.db/create-schema! card-schema)

(s/defn new-card :- Card
  [number cvv exp-date]
  {:card/id              (uuid)
   :card/number          number
   :card/cvv             cvv
   :card/expiration-date exp-date})

(s/defn add-credit-cards!
  [cards :- [Card]]
  (c.db/upsert-on-db! cards))

(s/defn all-cards []
  (c.db/all-data! :card/id))




















