(ns plug-utils.time-test
  (:require [clojure.test :refer [deftest is testing]]
            [plug-utils.time :refer [epoch-millis-now instant-now]]))



(deftest epoch-millis-now-test
  (testing "Basics"
    (is (pos-int? (epoch-millis-now)))
    (is (= 13 (count (str (epoch-millis-now)))))))


(deftest instant-now-test
  (testing "Basics"
    (is (inst? (instant-now)))))
