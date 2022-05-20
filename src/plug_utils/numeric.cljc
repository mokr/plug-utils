(ns plug-utils.numeric
  "Helpers related to numbers. Including to/from strings")

;|-------------------------------------------------
;| FROM STRING

(defn str->int
  "Converts a string/char to int,
  or returns unaltered if already a number"
  [s]
  #?(:clj  (cond
             (number? s) s
             :else (some-> s (str) (java.lang.Integer/parseInt)))
     :cljs (cond
             (number? s) s
             :else (some-> s (str) (js/parseInt)))))


(defn str->decimal
  "Converts a string to decimal,
  or returns unaltered if already a number"
  [s]
  #?(:clj  (cond
             (number? s) s
             :else (some-> s (Double/parseDouble)))         ;; Double instead of Float to keep precision of longitude/latitude
     :cljs (cond
             (number? s) s
             :else (some-> s (js/parseFloat)))))            ;; JS Float keep precision for longitude/latitude


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

(defn digit-sum
  "Takes a number and sums its digits.
  Examples: 15 => 6, 23 => 5, 7 => 7
  https://en.wikipedia.org/wiki/Digit_sum"
  [x]
  (->> x (str) (map str->int) (apply +)))                   ;; NOTE: Running on all digits as the single digit ones won't be affected anyway.
