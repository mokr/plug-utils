(ns plug-utils.re-frame
  "Re-frame and Reagent related utils")


(def <sub (comp deref re-frame.core/subscribe))
(def >evt re-frame.core/dispatch)


