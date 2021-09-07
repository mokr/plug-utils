(ns plug-utils.string)


;;TODO: Move this somewhere else as it is not a string
(defn gen-uuid
  "Generate a UUID"
  []
  #?(:clj  (java.util.UUID/randomUUID)
     :cljs (random-uuid)))


(defn gen-uuid-str
  "Generate a UUID"
  []
  #?(:clj  (.toString (java.util.UUID/randomUUID))
     :cljs (str (random-uuid))))
