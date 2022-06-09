(ns plug-utils.specs.re-frame
  (:require [clojure.spec.alpha :as s]))


(s/def :error/source string?)
(s/def :error/action string?)
(s/def :error/message string?)
(s/def :error/raw (s/or :map map?
                        :nil nil?))

(s/def ::error-map (s/keys :opt-un [:error/source
                                    :error/action
                                    :error/message
                                    :error/raw]))
