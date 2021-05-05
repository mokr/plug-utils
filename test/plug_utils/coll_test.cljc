(ns plug-utils.coll-test
  (:require [clojure.test :refer [deftest is testing]]
            [plug-utils.coll :refer :all]))


(deftest nil-if-empty-test
  (testing "Empty returns nil"
    (is (nil? (nil-if-empty [])) "Empty vector should return nil")
    (is (nil? (nil-if-empty "")) "Empty vector should return nil"))

  (testing "Non empty return identity"
    (is (= [:a] (nil-if-empty [:a])) "Non empty vector should be untouched")
    (is (= "a" (nil-if-empty "a")) "Non empty string should be untouched")))


(deftest comma-separated-test
  (testing "Basics"
    (is (= "a, b" (comma-separated ["a" "b"])) "Should return string with comma and whitespace between elements")
    (is (= "a, b, c" (comma-separated '(\a \b \c))))))