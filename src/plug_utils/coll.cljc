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
  "Turn a collection into a comma separated string"
  [xs]
  {:pre  [(or (seqable? xs) (nil? xs))]
   :post [(string? %)]}
  (str/join ", " xs))


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