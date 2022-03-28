(ns plug-utils.numeric-test
  (:require [clojure.test :refer [deftest is testing]]
            [plug-utils.numeric :refer :all]))


(deftest digit-sum-test
  (testing "Sums digits"
    (is (= 5 (digit-sum 23)))))