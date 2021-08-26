(ns plug-utils.dom)


(defn target-value
  "Extract target-value from DOM event"
  [e]
  (some-> e .-target .-value))
