(ns plug-utils.string-test
  (:require [clojure.test :refer [deftest is testing]])
  (:require [plug-utils.string :as $ :refer [gen-uuid gen-order-id]]))

(deftest gen-uuid-test
  (let [a-uuid (gen-uuid)]
    (testing "Basics"
      (is (uuid? a-uuid) "Should return a UUID"))
    (testing "Format of UUID"
      (is (= (str a-uuid)
             (re-find #"^\w+-\w+-\w+-\w+-\w+$" (str a-uuid)))
          "Has groups separated by dashes"))))


(deftest gen-order-id-test
  (let [order-id (gen-order-id)]
    (testing "Matches pattern"
      (is (re-matches $/order-id-regex order-id)))))
