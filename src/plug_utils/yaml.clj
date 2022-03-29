(ns plug-utils.yaml
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [taoensso.timbre :as log])
  (:import (flatland.ordered.map OrderedMap)))              ;; Lib is a dependency of clj-yaml.



;|-------------------------------------------------
;| YAML BASED CONFIG FILES

(defn- underscore->hyphen
  "Transform underscore to hyphen to be more idiomatic with regard to clj(s) naming"
  [k]
  (if (keyword? k)
    (-> k name (str/replace "_" "-") keyword)
    k))


(defn- to-regular-map
  "Transform a map of nested OrdredMaps to a regular hash-maps."
  [x]
  (if (instance? OrderedMap x)
    (reduce-kv (fn [acc k v]
                 (assoc acc (underscore->hyphen k) (to-regular-map v)))
               {}
               x)
    x))


(defn config-from-yml-file
  "Include config from YAML file.
   Typically, to include a yml-file generated by Ansible and symlinked into project.

   Returns an EDN map or nil.
   Keys in map will have underscore (_) stransformed to hyphen (-).
   Example: \"my_key:\" (yaml) -> \":my-key\" (edn) "
  [file]
  (if-not (.exists (io/file file))
    (log/error (format "Unable to load YAML from %s (file does not exist). Did you forget to symlink?" file))
    (do (log/info (format "Loading config from YAML file %s" file))
        (-> file slurp yaml/parse-string to-regular-map))))
