(ns plug-utils.maps)

;|-------------------------------------------------
;| COMPARE

(defn different?
  "Predicate that is truthy if value of key 'k' is different in the two maps"
  [k m1 m2]
  (not=
    (get m1 k)
    (get m2 k)))


;|-------------------------------------------------
;| Helpers working on collection of values

(def max-value (comp (partial reduce max) (partial filter number?)))
(def first-truthy (partial some identity))


;|-------------------------------------------------
;| Convenience for single maps

;(defn from-key
;  "Define a key 'k' whose value will be extracted from map 'm' and passed to function 'afun'."
;  [k]
;  (fn [afun]
;    (fn [m]
;      (afun (get m k)))))


;(defn with
;  "Given a key 'k' and a function 'afun', return a function that will
;  take a map 'm' and run 'afun' on the value of key 'k' from this map"
;  [k afun]
;  (fn [m]
;    (afun (get m k))))


;|-------------------------------------------------
;| Produce maps

(defn mapify
  "Turns collection of value vectors (coll of colls) into maps with given keys.
  Think 'zipmap' over multiple value vectors.
  Example use case: Turning split csv lines into maps."
  [key-names value-vectors]
  {:pre  [(sequential? key-names) (sequential? value-vectors) (every? sequential? value-vectors)]
   :post [(sequential? %) (every? map? %)]}
  (map (partial zipmap key-names) value-vectors))


;|-------------------------------------------------
;| Process/augment a map


(defn merge-in
  "Pass map to function and merge its return value back into map"
  [m & fns]
  {:pre  [(map? m) (every? fn? fns)]
   :post [(map? %)]}
  (reduce (fn [acc f]
            (merge acc (f acc)))
          m
          fns))


(defn maybe-set
  "Set field 'k' if value 'v' is truthy, remove it if not"
  [m k v]
  (if v
    (assoc m k v)
    (dissoc m k)))


(defn updates
  "Takes a map and pairs of keys/function pairs. Like: [m k1 fn1 k2 fn2 ...]
  For each k/fn pair, assign m a key 'k' with value returned by calling fnX with current map (m').

  Note: Order matters. Later key assignment will see updated keys from previous steps. This is the reason why
  k-fn-pairs are partitioned instead of being destructured directly into a map as that would not preserve order."
  [m & k-fn-pairs]
  {:pre  [(map? m) (even? (count k-fn-pairs)) (every? ifn? (take-nth 2 (rest k-fn-pairs)))]
   :post [(map? %)]}
  (->> (partition 2 k-fn-pairs)
       (reduce (fn [m' [k afun]]
                 (assoc! m' k (afun m')))
               (transient m))
       (persistent!)))


(defn some-updates
  "Updates map with given keys if their computed value is non nil.
  Processed in sequence, so later steps see the effect of previous ones.

  Takes a map followed by an even number of key/function pairs. The function will be called with the
  current map and if the return value is non-nil, it will be assigned to the map under the provided key."
  [m & k-fn-pairs]
  {:pre  [(map? m) (even? (count k-fn-pairs)) (every? ifn? (take-nth 2 (rest k-fn-pairs)))]
   :post [(map? %)]}
  (->> (partition 2 k-fn-pairs)
       (reduce (fn [m' [k afun]]
                 (let [val (afun m')]
                   (if-not (nil? val)
                     (assoc! m' k val)
                     m')))
               (transient m))
       (persistent!)))


;; Helper
(defn reduce-values-with
  "Generic "
  [reducing-fn & ks]
  {:pre [(fn? reducing-fn) (sequential? ks)]}
  (fn
    [m]
    (reduce
      reducing-fn
      (map m ks))))


(defn remove-keys-with-empty-strings
  "Remove keys with an empty string as value"
  [m]
  {:pre  [(map? m)]
   :post [(map? %)]}
  (into (empty m)
        (remove #(and (-> % second string?)
                      (-> % second empty?)))
        m))


;|-------------------------------------------------
;| Utility functions to use with "updates" function

(def sum-keys (partial reduce-values-with +))               ; TODO: Should it handle missing keys, non-numerig values?

;(defn sum-keys
;  [& keys-to-take-value-from]
;  {:post [(number? %)]}
;  (apply reduce-values-with + keys-to-take-value-from))


(def concat-keys (partial reduce-values-with str))


;|-------------------------------------------------
;| Process collection of maps

(defn max-for-key
  "Selects the max value for a given key from is values in a collection of maps"
  [k]
  (fn [maps]
    {:pre [(every? map? maps)]}
    (->> maps
         (map #(get % k))
         (max-value))))


;|-------------------------------------------------
;| Work on multiple keys in a collection of maps
;| (Decide a value for a map key based on it's values in a collection of maps)

(defn pick-val-using
  "Create map with defined fields and assign each fields value based on feeding
  the picker-fn with all values collected for this key in all provided entities.
  Returned map has a field if picker-fn returned a truthy value for it."
  [picker-fn fields-to-treat entities]
  {:pre  [(fn? picker-fn) (sequential? fields-to-treat) (sequential? entities) (every? map? entities)]
   :post [(map? %)]}
  (persistent!
    (reduce
      (fn [res field]
        (if-let [picked-value (picker-fn (map #(get % field) entities))]
          (assoc! res field picked-value)
          res))
      (transient {})
      fields-to-treat)))


(defn pick-first-truthy-value
  "Takes a collection of fields/keys and a collection of maps.
   Outputs a map where each field is assigned the first avaiable value for this field from provided maps.
   Example:
    IN: [:a :b] [{:a 1 :c 3} {:a 2 :b 2}]
    OUT: {:a 1 :b 2}"
  [fields-to-treat entities]
  (pick-val-using first-truthy fields-to-treat entities))


(defn pick-max-val
  "Takes a collection of fields/keys and a collection of maps.
   Outputs a map where each field is assigned the first avaiable value for this field from provided maps.
   Example:
    IN: [:a :b :d] [{:a 2 :c 3 :d nil} {:a 1 :b 2} {:a 3 :b 2 :d nil}]
    OUT: {:a 3 :b 3}"
  [fields-to-treat entities]
  (pick-val-using max-value fields-to-treat entities))