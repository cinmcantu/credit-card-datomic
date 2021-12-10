(ns credit-card-datomic.credit-card
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(defn uudi [] (java.util.UUID/randomUUID))

(def Card {:card/id                               s/Uuid
           (s/optional-key :card/number)          s/Str
           (s/optional-key :card/cvv)             Long
           (s/optional-key :card/expiration-date) s/Str})

(s/defn new-card :- Card
  [number cvv exp-date]
  {:card/id              (uudi)
   :card/number          number
   :card/cvv             cvv
   :card/expiration-date exp-date})














