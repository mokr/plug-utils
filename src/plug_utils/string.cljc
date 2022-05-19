(ns plug-utils.string
  (:require [plug-utils.time :as ut]
            [clojure.string :as str]))


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


(defn gen-order-id
  "Generate an order ID with memorable parts"
  []
  (let [animal    ["ant" "ape" "bat" "bee" "cat" "cow" "cub" "dog" "eel" "elk" "emu" "fly" "fox" "hen" "hog" "koi" "owl" "pig" "yak"]
        adjective ["any" "big" "bad" "coy" "dry" "fat" "fit" "fun" "fly" "hot" "hip" "lax" "old" "odd" "pro" "pet" "red" "sad" "shy" "tan" "wet"]
        timestamp (ut/time-now-local-str)
        date      (-> timestamp (subs 5 10) (str/split #"-") reverse (->> (str/join "-")))
        hhmm      (-> timestamp (subs 11 16) (str/replace ":" "-"))]
    (str (rand-nth adjective) "-" (rand-nth animal) "-" date "-" hhmm)))


(def order-id-regex #"^\w\w\w-\w\w\w-\d\d-\d\d-\d\d-\d\d$") ;; E.g. "pet-owl-21/10-10:27" - For use in e.g. specs
