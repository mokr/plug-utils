(ns plug-utils.log
  "Mostly wrappers for js/console.___ logging"
  (:require [cljs.pprint :refer [pprint]]))


(def info
  "Use native js/console to log. Enables browser console to show source file and have it clickable"
  js/console.info)

(def warn
  "Use native js/console to warn. Enables browser console to show source file and have it clickable"
  js/console.warn)

(def error
  "Use native js/console to show error. Enables browser console to show source file and have it clickable"
  js/console.error)

(defn pplog
      "Use native js/console to log. Enables browser console to show source file and have it clickable.
      Taks one arg that is pretty printed"
      [msg]
      (js/console.info (with-out-str (pprint msg))))
