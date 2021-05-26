(ns plug-utils.func
  "Utils for creating functions or altering an existing functions behaviour"
  (:require [clojure.core.async :as async :refer [go go-loop chan <! >! timeout alts!]]))


(defn- debounce
  "Add debounce mechanism to a core.async channel

  From here:
  https://gist.github.com/scttnlsn/9744501"
  [in ms]
  {:pre  [(integer? ms)]
   :post []}
  (let [out (chan)]
    (go-loop [last-val nil]
      (let [val   (if (nil? last-val) (<! in) last-val)
            timer (timeout ms)
            [new-val ch] (alts! [in timer])]
        (condp = ch
          timer (do (>! out val) (recur nil))
          in (if new-val (recur new-val)))))
    out))


;;TODO: Handle multi-arg / variadic afun
(defn fn->debounced-fn
  "Returns a debounced version of the provided function"
  [afun ms]
  {:pre  [(fn? afun) (integer? ms)]
   :post [(fn? %)]}
  (let [in  (chan)
        out (debounce in ms)]
    (go-loop [val (<! out)]
      (afun val)
      (recur (<! out)))
    (fn [x]
      (go (>! in x)))))


