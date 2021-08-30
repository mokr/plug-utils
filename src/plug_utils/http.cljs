(ns plug-utils.http)


(defn redirect
  "Frontend redirect to provided url, or to the login screen if no url given."
  ([] (redirect "/login"))
  ([url] (set! (.-location js/document) url)))
