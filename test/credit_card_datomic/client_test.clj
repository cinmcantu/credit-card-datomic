(ns credit-card-datomic.client-test
  (:require [clojure.test :refer :all]
            [credit-card-datomic.client :refer :all])
  (:import (clojure.lang ExceptionInfo)))

(deftest new-client-test
  (testing "Should add new client successfully"
    (is (= {:client/name "Foo"
            :client/cpf  "400.111.222-30"
            :client/card []}
           (dissoc (new-client "Foo" "400.111.222-30") :client/id)))
    (is (not (nil? (:client/id (new-client "Foo"
                                           "400.111.222-30"))))))
  (testing "Schema not create"
    (is (thrown? ExceptionInfo (new-client 111 "400.111.222-30")))
    (is (thrown? ExceptionInfo (new-client "Foo" 40011122230)))))
