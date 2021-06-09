(ns plug-utils.string)


(defn gen-uuid
  "Generate a UUID"
  []
  #?(:clj  (.toString (java.util.UUID/randomUUID))
     :cljs (random-uuid)))
