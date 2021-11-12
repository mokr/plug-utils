(ns plug-utils.coll
  "Utils for collections"
  (:require [clojure.string :as str]))



(defn nil-if-empty
  "Turn empty collection into nil.
  Useful to ensure some->> short circuits"
  [xs]
  (if (empty? xs)
    nil
    xs))


(defn comma-separated
  "Turn a collection into a comma separated string (optionally with a unit).

  Arities:
  [xs]        :: [1 2 3]      => \"1, 2, 3\"
  [unit, xs]  :: \"km\" [1 2 3] => \"1km, 2km, 3km\""
  ([xs]
   {:pre  [(or (seqable? xs) (nil? xs))]
    :post [(string? %)]}
   (str/join ", " xs))
  ([unit xs]
   {:pre  [(string? unit) (or (seqable? xs) (nil? xs))]
    :post [(string? %)]}
   (->> xs
        (map #(str % unit))
        (str/join ", "))))


(defn pick-by-mask
  "Keep entries from first coll using second coll as a mask.
  Optionally takes a pred that gets to decide what is considered truthy in mask"
  ([xs mask]
   {:pre [(sequential? xs) (sequential? mask)]}
   (pick-by-mask xs mask identity))
  ([xs mask pred]
   {:pre [(sequential? xs) (sequential? mask) (fn? pred)]}
   (->>
     (map (fn [x mask-entry]
            (when (pred mask-entry)
              x))
          xs mask)
     (remove nil?))))


(comment
  (comma-separated "km" [1 2 3])
  (comma-separated 200 [1 2 3])
  (comma-separated [1 2 3])

  )