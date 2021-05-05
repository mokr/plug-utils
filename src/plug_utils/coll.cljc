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
  {:pre  [(sequential? xs)]
   :post [(string? %)]}
  (str/join ", " xs))