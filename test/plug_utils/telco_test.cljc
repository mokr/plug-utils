(ns plug-utils.telco-test
  (:require [clojure.test :refer [deftest is are testing]]
            [plug-utils.telco :refer :all]))


;(deftest norwegian-8-digit-international-format?-test
;  (testing "Recognises valid numbers"
;    (is (norwegian-8-digit-international-format? "4712345678")))
;
;  (testing "Rejects too long numbers"
;    (is (not (norwegian-8-digit-international-format? "47123456780"))))
;
;  (testing "Rejects too short numbers"
;    (is (not (norwegian-8-digit-international-format? "47123")))))
;
;
;(deftest norwegian-5-digit-international-format?-test
;  (testing "Recognises valid numbers"
;    (is (norwegian-5-digit-international-format? "4701234"))))
;
;
;(deftest norwegian-mobile-range-msisdn?-test
;  (testing "Recognises valid numbers"
;    (is (norwegian-mobile-range-msisdn? "4741234567"))
;    (is (norwegian-mobile-range-msisdn? "4791234567")))
;
;  (testing "Rejects fixed numbers"
;    (is (not (norwegian-mobile-range-msisdn? "21234567")))
;    (is (not (norwegian-mobile-range-msisdn? "61234567")))))


(deftest pretty-phone-number-test
  (testing "Prettify mobile numbers"
    (is (= "412 34 567" (pretty-phone-number "41234567")))
    (is (= "912 34 567" (pretty-phone-number "91234567")))
    (is (= "412 34 567" (pretty-phone-number "4741234567")))
    (is (= "912 34 567" (pretty-phone-number "4791234567")))
    (is (= "412 34 567" (pretty-phone-number "004741234567")))
    (is (= "912 34 567" (pretty-phone-number "004791234567")))
    (is (= "412 34 567" (pretty-phone-number "+4741234567")))
    (is (= "912 34 567" (pretty-phone-number "+4791234567"))))
  (testing "Prettify fixed numbers"
    (is (= "21 23 45 67" (pretty-phone-number "21234567")))
    (is (= "21 23 45 67" (pretty-phone-number "4721234567")))
    (is (= "21 23 45 67" (pretty-phone-number "+4721234567")))
    (is (= "21 23 45 67" (pretty-phone-number "004721234567"))))
  (testing "Presets short number without country code"
    (is (= "01234" (pretty-phone-number "01234")))
    (is (= "01234" (pretty-phone-number "4701234")))
    (is (= "01234" (pretty-phone-number "+4701234")))
    (is (= "01234" (pretty-phone-number "004701234")))
    (is (= "113" (pretty-phone-number "113")))
    )
  (testing "Leaves other numbers untouched (like international ones)"
    (is (= "1-800-275-2273" (pretty-phone-number "1-800-275-2273"))) ;; 1-800-275-2273 =  Apple support US
    (is (= "+46123456789" (pretty-phone-number "+46123456789")))) ;; fictitious Swedish number

  )



