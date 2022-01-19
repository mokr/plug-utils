(ns plug-utils.url)


(defn open-link-in-new-tab [url]
  (js/window.open url "_blank" "noreferrer"))