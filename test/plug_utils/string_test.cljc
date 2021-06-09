(ns plug-utils.string-test
  (:require [clojure.test :refer [deftest is testing]])
  (:require [plug-utils.string :refer [gen-uuid]]))

(deftest gen-uuid-test
  (let [a-uuid (gen-uuid)]
    (testing "Basics"
      (is (string? a-uuid) "Should return a string")
      (is (= a-uuid (re-find #"^\w+-\w+-\w+-\w+-\w+$" a-uuid)) "Has groups separated by dashes"))))
