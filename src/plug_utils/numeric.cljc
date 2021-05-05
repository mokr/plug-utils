(ns plug-utils.numeric
  "Helpers related to numbers. Including to/from strings")

;|-------------------------------------------------
;| FROM STRING

(defn str->int [s]
  #?(:clj  (some-> s (java.lang.Integer/parseInt))
     :cljs (some-> s (js/parseInt))))

(defn str->decimal [s]
  #?(:clj  (some-> s (Double/parseDouble))                  ;; Double instead of Float to keep precision of longitude/latitude
     :cljs (some-> s (js/parseFloat))))                     ;; JS Float keep precision for longitude/latitude


;|-------------------------------------------------
;| CALCULATIONS

(defn num-diff
  "Calculate the absolute value of the difference between two numbers"
  [a b]
  {:pre  [(number? a) (number? b)]
   :post [(number? %)]}
  (if (> a b)
    (- a b)
    (- b a)))