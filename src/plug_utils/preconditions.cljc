(ns plug-utils.preconditions
  (:require [clojure.spec.alpha :as s]))



;|-------------------------------------------------
;| SPEC

(defn valid?
  "Helper for use in pre conditions.
  Prints an explanation to console/terminal if spec fails"
  [spec val]
  (or (s/valid? spec val)
      (let [explanation (s/explain-str spec val)]
        #?(:clj  (do (clojure.pprint/pprint val)            ;; So that we get some context for the offending field/value
                     (println explanation))
           :cljs (js/console.error explanation)))))