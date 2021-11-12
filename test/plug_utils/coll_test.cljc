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
    (is (= "a, b, c" (comma-separated '(\a \b \c)))))
  (testing "Handles other typical 'containers'"
    (is (= "a, b" (comma-separated '("a" "b"))))
    (is (= "a, b" (comma-separated "ab")))
    (is (= "a, b" (comma-separated #{"a" "b"})))
    (is (= "[\"a\" \"b\"], [\"c\" \"d\"]" (comma-separated {"a" "b" "c" "d"}))))
  (testing "With unit"
    (is (= "1m, 2m, 3m" (comma-separated "m" [1 2 3])))))


(deftest pick-by-mask-test
  (testing "Arity two with default pred (keep truthy)"
    (is (= [:b :d] (pick-by-mask [:a :b :c :d] [false 1 nil "foo"])) "Return entries that have a corresponding truthy val in mask")
    (is (= [:b] (pick-by-mask [:a :b :c] [false 0 nil])) "Treats 0 (zero) as truthy")
    (is (= [:b] (pick-by-mask [:a :b :c :d] [false 1 nil])) "Only treats as many entries as both colls have in common"))

  (testing "Custom nil? pred"
    (is (= [:a :c] (pick-by-mask [:a :b :c] [nil "foo" nil] nil?)) "Picks entries that have a nil value in mask")))
