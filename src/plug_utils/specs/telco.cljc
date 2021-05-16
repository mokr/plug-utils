(ns plug-utils.specs.telco
  "Specs with just enough precision to work with norwegian phone numbers.
  It is not meant to capture all the rules of the Norwegian numbering plan
  https://www.nkom.no/telefoni-og-telefonnummer/telefonnummer-og-den-norske-nummerplan/alle-nummerserier-for-norske-telefonnumre#4sifrede_nummer_nummeropplysningstjenester"
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))


;|-------------------------------------------------
;| PREDICATES, PATTERNS & HELPERS

(def length-3? #(= 3 (count %)))
(def length-5? #(= 5 (count %)))
(def length-8? #(= 8 (count %)))
(def starts-with-0? #(str/starts-with? % "0"))
(def starts-with-1? #(str/starts-with? % "1"))
(def starts-with-8? #(str/starts-with? % "8"))
(def first-digit-indicates-mobile? #(#{\4 \9} (first %)))
(def first-digit-indicates-fixed? #(#{\2 \3 \5 \6 \7} (first %)))
(def digits-only-pattern #"^\d+$")
(def digits-only? #(re-matches digits-only-pattern %))
(def cc-pattern #"^(?:47|\+47|0047)")
(def strip-cc #(str/replace-first % cc-pattern ""))


;|-------------------------------------------------
;| SPECS

(s/def ::strip-cc (s/conformer strip-cc #(str "47" %)))     ;; Unform, the last fn, might add cc in another format than what was stripped (47 instead of +47 0047)
(s/def ::three-digit-number (s/and string? length-3? digits-only? starts-with-1?)) ;; Always starts with "1"
(s/def ::five-digit-number (s/and string? length-5? digits-only? starts-with-0?)) ;; Always start with "0"
(s/def ::eight-digit-number (s/and string? length-8? digits-only?)) ;; Can start with multiple different numbers
(s/def ::mobile-number (s/and ::eight-digit-number first-digit-indicates-mobile?))
(s/def ::fixed-number (s/and ::eight-digit-number first-digit-indicates-fixed?))

(s/def ::special-number (s/or :three-digit ::three-digit-number
                              :five-digit ::five-digit-number
                              :eight-digit (s/and ::eight-digit-number starts-with-8?)))

(s/def ::norwegian-phone-number (s/or :mobile ::mobile-number
                                      :fixed ::fixed-number
                                      :special ::special-number))

(s/def ::norwegian-phone-number-international-format (s/and ::strip-cc ::norwegian-phone-number))

(s/def ::norwegian-msisdn (s/or :national-format ::norwegian-phone-number
                                :international-format ::norwegian-phone-number-international-format))


(comment
  (s/unform ::strip-cc "41234567")
  (s/conform ::norwegian-msisdn "4741234567")
  (s/explain ::norwegian-msisdn "4741234567")
  (s/assert ::norwegian-msisdn "4741234567")
  (s/unform ::norwegian-msisdn [:international-format [:mobile "41234567"]])
  (-> (s/conform ::norwegian-msisdn "41234567") flatten)
  (s/valid? ::norwegian-msisdn "41234567")
  (s/conform ::norwegian-msisdn "01234")
  (s/conform ::norwegian-msisdn "113")
  (s/conform ::norwegian-msisdn "4701234")
  (s/conform ::norwegian-msisdn "4781234567")
  (s/conform ::norwegian-msisdn "81234567")
  (s/conform ::norwegian-msisdn "4721234567")
  (s/conform ::norwegian-msisdn "21234567")
  (s/conform ::mobile-number "41234567")
  (s/conform ::mobile-number "41234567")
  (s/conform ::mobile-number "01234")
  (s/conform ::mobile-number "4741234567")
  (s/valid? ::mobile-number "41234567")
  (s/valid? ::mobile-number "4741234567")
  (s/conform ::five-digit-number "01234")
  (s/explain ::five-digit-number "01234")
  )


