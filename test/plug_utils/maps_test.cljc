(ns plug-utils.maps-test
  (:require [clojure.test :refer [deftest is testing]]
            [plug-utils.maps :refer :all]))


(deftest different?-test
  (testing "Different values"
    (is (different? :a {:a 1} {:a 2}) "True when values of key are equal"))

  (testing "Equal values"
    (is (not (different? :a {:a 1} {:a 1})) "False when values differ")))


(deftest merge-in-test
  (testing "Merges in map returned from function"
    (is (= {:a 1 :b 2 :c 3}
           (merge-in {:a 1 :b 2} (constantly {:c 3}))) "Merges in")
    (is (= {:a 1 :b 3} (merge-in {:a 1 :b 2} (constantly {:b 3}))) "Overwrites existing keyss")))


(deftest maybe-set-test
  (testing "Sets field if value is truthy"
    (is (= {:a 1 :b 2} (maybe-set {:a 1} :b 2))) "Should set field")

  (testing "Removes field if value isn't truthy"
    (is (= {:a 1} (maybe-set {:a 1} :b nil)))
    (is (= {:a 1} (maybe-set {:a 1 :b "exists"} :b nil)))))


(deftest some-updates-test
  (testing "Basics"
    (is (= {:a 1 :b 2} (some-updates {:a 1} :b (constantly 2))) "Assigns value returned from fn if it's truthy")
    ;(is (= {:a 1 :b 1} (some-updates {:a 1} :b (constantly nil))) "Keeps existing if fn returns nil") ;;TODO: Should behaviour be changed to reflect test?
    ))

(deftest remove-keys-with-empty-strings-test
  (testing "Basics"
    (is (= {:a 1 :c nil :d [] :e [:x] :f '()}
           (remove-keys-with-empty-strings {:a 1 :b "" :c nil :d [] :e [:x] :f '()}))
        "Leaves nil and empty non-strings")
    ))
