(ns plug-utils.string-test
  (:require [clojure.test :refer [deftest is testing]])
  (:require [plug-utils.string :refer [gen-uuid]]))

(deftest gen-uuid-test
  (let [a-uuid (gen-uuid)]
    (testing "Basics"
      (is (uuid? a-uuid) "Should return a UUID"))
    (testing "Format of UUID"
      (is (= (str a-uuid)
             (re-find #"^\w+-\w+-\w+-\w+-\w+$" (str a-uuid)))
          "Has groups separated by dashes"))))
