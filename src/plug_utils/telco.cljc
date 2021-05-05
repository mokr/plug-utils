(ns plug-utils.telco)


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