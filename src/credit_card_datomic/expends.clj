(ns credit-card-datomic.expends
  (:use clojure.pprint)
  (:require [schema.core :as s]
            [credit-card-datomic.db :as c.db]
            [datomic.api :as d]
            [credit-card-datomic.credit-card :as c.credit-card]
            [java-time :as t]))

(s/set-fn-validation! true)

(defn uuid [] (java.util.UUID/randomUUID))

(def Expend {:expend/id                        s/Uuid
             (s/optional-key :expend/category) s/Str
             (s/optional-key :expend/value)    BigDecimal
             (s/optional-key :expend/date)     java.util.Date
             (s/optional-key :expend/card)     c.credit-card/Card})


(def expend-schema [{:db/ident       :expend/id
                     :db/valueType   :db.type/uuid
                     :db/cardinality :db.cardinality/one
                     :db/unique      :db.unique/identity}
                    {:db/ident       :expend/category
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :expend/value
                     :db/valueType   :db.type/bigdec
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :expend/date
                     :db/valueType   :db.type/instant
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :expend/card
                     :db/valueType   :db.type/ref
                     :db/cardinality :db.cardinality/one}])

(c.db/create-schema! expend-schema)

(s/defn new-expend :- Expend
  [category value]
  {:expend/id       (uuid)
   :expend/category category
   :expend/value    value
   :expend/date     (t/java.util.Date.)})

(def card3 (c.credit-card/new-card "5555 4444 3333 2222" 123 "10/28"))
(c.credit-card/add-credit-cards! [card3])

(def expend1 (new-expend "food" 100.00M))
(def expend2 (new-expend "food" 60.00M))

(s/defn add-expends!
  [expends :- [Expend]]
  (c.db/upsert-on-db! expends))

(s/defn add-card-on-expend!
  [expend card]
  (c.db/upsert-on-db! [[:db/add [:expend/id (:expend/id expend)]
                        :expend/card
                        [:card/id (:card/id card)]]]))

(s/defn all-expends! []
  (c.db/all-data! :expend/id))

