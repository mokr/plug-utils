(ns plug-utils.telco
  "Utils for working with phone numbers in Norway.
  Look to Google's libphonenumber for full featured tools"
  (:require [clojure.string :as str]
            [plug-utils.preconditions :refer [valid?]]
            [plug-utils.specs.telco :as spec]
            [clojure.spec.alpha :as s]))


;; ---------------------------------------------------------------------------
;; IMSI, MCC/MNC
;;
;;   Resources:
;;     - http://mcc-mnc.com


(defn imsi->operator
  "Returns name of operator for some MCC+MNC combinations, else nil"
  [imsi]
  {:pre  [(string? imsi)]
   :post [(or (string? %) (nil? %))]}
  (case (subs imsi 0 5)
    "24201" "Telenor"
    "24202" "Telia"
    "24214" "Ice"
    "24206" "Ice"
    nil))


(defn imsi->country
  "Return two letter country code of some MNCs, else nil"
  [imsi]
  {:pre  [(string? imsi)]
   :post [(or (string? %) (nil? %))]}
  (case (subs imsi 0 3)
    "242" "NO"
    "240" "SE"
    "238" "DK"
    nil))

;|-------------------------------------------------
;|



;(defn norwegian-8-digit-international-format? [msisdn]
;  (and (string? msisdn)
;       (= 10 (count msisdn))
;       (str/starts-with? msisdn "47")))
;
;
;(defn norwegian-5-digit-international-format?
;  "Is it a 5 digit short number prefixed with country code (47)"
;  [msisdn]
;  (and (= 7 (count msisdn))
;       (str/starts-with? msisdn "47")))                     ;; TODO: Consider extending to 0047 and +47
;
;
;(defn norwegian-mobile-range-msisdn?
;  "Is this phone number (international format) a mobile number?"
;  [[_ _ digit :as msisdn]]
;  ;{:pre [(valid? ::spec/norwegian-cc-8-digit-msisdn msisdn)]}
;  (boolean
;    (and (norwegian-8-digit-international-format? msisdn)
;         (#{\4 \9} digit))))
;
;
;(defn norwegian-0047-prefix?
;  "Does MSISDN start with a 0047?"
;  [msisdn]
;  (some-> msisdn (str/starts-with? "0047")))


(defn pretty-phone-number
  "Format a phone number string into the usual format (without country code) used in Norway when number is Norwegian.
  Note: Touches Norwegian MSISDNs only."
  [msisdn]
  (let [[_ variant :as conformed] (some->> msisdn (s/conform ::spec/norwegian-msisdn) (flatten)) ;; => Eg: (:national-format :mobile "41234567")
        [d1 d2 d3 d4 d5 d6 d7 d8 :as normalized] (last conformed)]
    (case variant
      :mobile (str d1 d2 d3 " " d4 d5 " " d6 d7 d8)
      :fixed (str d1 d2 " " d3 d4 " " d5 d6 " " d7 d8)
      (or normalized msisdn))))

(comment
  (pretty-phone-number' "004741234567")
  (pretty-phone-number' "+4741234567")
  (pretty-phone-number' "4741234567")
  (pretty-phone-number' "41234567")
  (pretty-phone-number' "+46123456789")
  (pretty-phone-number' "179")
  (pretty-phone-number' "21234567")
  (pretty-phone-number' "4721234567")
  (pretty-phone-number' "4701234")
  (pretty-phone-number' "01234")
  (pretty-phone-number' "113")
  )

(comment
  (pretty-phone-number "4741234567")
  (pretty-phone-number "41234567")
  )

(def phone-number-patterns
  [#"\d{8,}"
   #"47\d{5,}"                                              ;; Five digits with 47 prefix
   #"47\d{3,}"
   #"\D(0\d{4,4})"                                          ;; Keep 5-digit short numbers as is
   #"\d{7,7}"])                                             ;; When MSISDN is missing a digit.
