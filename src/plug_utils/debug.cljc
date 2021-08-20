(ns plug-utils.debug
  "Some debug functionality that can be used from both Clojure and Clojurescript")


(def log-text
  "Log something that should just be a text"
  #?(:clj  println
     :cljs js/console.info))                                ; Using native js/console enables browser console to show source file and have it clickable


(def log-pretty
  "Log a data a structure"
  #?(:clj  clojure.pprint/pprint
     :cljs js/console.info))


(defn DEBUG-print
  "Transparently logs a piece of data.
  Optionally takes a descriptive text and/or a function to process the data with before presentation."
  ([data] (DEBUG-print "" identity data))
  ([text-or-fn data]
   (if (string? text-or-fn)
     (DEBUG-print text-or-fn identity data)
     (DEBUG-print "" text-or-fn data))
   data)
  ([text afun data]
   (log-text (str text ":"))
   (log-pretty (afun data))
   data))


(defn DEBUG-print'
  "Transparently logs a piece of data.
  Takes a datum and an optional descriptive text"
  ([data] (DEBUG-print "" data))
  ([data text-or-fn]
   (if (string? text-or-fn)
     (DEBUG-print text-or-fn identity data)
     (DEBUG-print "" text-or-fn data))
   data)
  ([data text afun]
   (DEBUG-print text afun data)
   data))
