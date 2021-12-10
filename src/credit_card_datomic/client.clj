(ns credit-card-datomic.client
  (:require [schema.core :as s]
            [credit-card-datomic.credit-card :as c.credit-card]))

(s/set-fn-validation! true)

(defn uudi [] (java.util.UUID/randomUUID))

(def Client {:client/id                    s/Uuid
             (s/optional-key :client/name) s/Str
             (s/optional-key :client/cpf)  s/Str
             (s/optional-key :client/card) [c.credit-card/Card]})

(s/defn new-client :- Client
  [name cpf]
  {:client/id   (uudi)
   :client/name name
   :client/cpf  cpf
   :client/card []})
