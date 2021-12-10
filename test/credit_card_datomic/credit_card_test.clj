(ns credit-card-datomic.credit-card-test
  (:require [clojure.test :refer :all]
            [credit-card-datomic.credit-card :refer :all])
  (:import (clojure.lang ExceptionInfo)))

(deftest new-card-test
  (testing "Schema created successfully"
    (is (= {:card/number          "5555 5555 5555 555"
            :card/cvv             123
            :card/expiration-date "10/29"}
          (dissoc (new-card "5555 5555 5555 555"
                            123
                            "10/29") :card/id)))
    (is (not (nil? (:card/id (new-card "5555 5555 5555 555"
                                       123
                                       "10/29"))))))
  (testing "Schema not create"
    (is (thrown? ExceptionInfo (new-card 5555 123 "10/29")))
    (is (thrown? ExceptionInfo (new-card "5555 5555 5555 555" "123" "10/29")))
    (is (thrown? ExceptionInfo (new-card "5555 5555 5555 555" 123 10)))))
