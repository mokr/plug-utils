(ns plug-utils.debug-test
  (:require [clojure.test :refer [deftest is testing]]
            [plug-utils.debug :refer :all]))


(deftest DEBUG-print-test
  (testing "Returns input data unaltered"
    (is (= {:a "foo"} (DEBUG-print {:a "foo"})) "Maps")
    (is (= "foo" (DEBUG-print "foo")) "Strings")
    (is (= 123 (DEBUG-print 123)) "Numbers")
    (is (= {:a "foo"} (DEBUG-print "a text" {:a "foo"})) "Ignores string that is for printing only")
    (is (= {:a "foo"} (DEBUG-print #(:a %) {:a "foo"})) "Ignores fn that is for printing only")
    (is (= {:a "foo"} (DEBUG-print :a {:a "foo"})) "Ignores fn (keyword) that is for printing only")
    ))

(deftest DEBUG-print'-test
  (testing "Returns input data unaltered"
    (is (= {:a "foo"} (DEBUG-print' {:a "foo"})) "Maps")
    (is (= "foo" (DEBUG-print' "foo")) "Strings")
    (is (= 123 (DEBUG-print' 123)) "Numbers")
    (is (= {:a "foo"} (DEBUG-print' {:a "foo"} "a text")) "Ignores string that is for printing only")
    (is (= {:a "foo"} (DEBUG-print' {:a "foo"} #(:a %))) "Ignores fn that is for printing only")
    (is (= {:a "foo"} (DEBUG-print' {:a "foo"} :a)) "Ignores fn (keyword) that is for printing only")
    ))
