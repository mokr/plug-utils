(ns plug-utils.async
  "Utils for core.async"
  (:require [clojure.core.async :as async :refer [go go-loop chan <! >! timeout alts!]]))


(defn res->ch
  "Create a callback that will put the value it is called with, onto the provided channel"
  [ch]
  (fn [x]
    (println "[Poller] Putting CB value onto channel")      ;; DEBUG
    (go (>! ch x))))