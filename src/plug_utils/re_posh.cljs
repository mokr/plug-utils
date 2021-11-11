(ns plug-utils.re-posh
  "Helpers to e.g. clean up re-posh query and pull using re-frame signals as input")

;; NOTE: A single input signal can be a collection itself, so we can't simply assume a coll means multi-arg


(defn single-input-query
  "---------------------------
  SINGLE signal/input version
  ---------------------------
  re-posh helper for using re-frame input signal as input to datascript query.

  To be used with re-posh/reg-sub to compile a handler that constructs a single
  arg query map.

  USAGE:
  (re-posh/reg-sub
  ::the-answer
  :<- [:some-input-signal]
  (single-input-query q/some-query-taking-one-input))"
  [query]
  (fn [signal]
    {:type      :query
     :query     query
     :variables [signal]}))


(defn multi-input-query
  "--------------------------
  MULTI signal/input version
  --------------------------
  re-posh helper for using re-frame input signals as input to datascript query.

  To be used with re-posh/reg-sub to compile a handler that constructs a query
  map with multiple variables.

  Note: Number of input signals must correspond to the number of inputs the query needs.

  USAGE:
  (re-posh/reg-sub
  ::the-answer
  :<- [:some-input-signal1]
  :<- [:some-input-signal2]
  (multi-input-query q/some-query-taking-two-inputs)"
  [query]
  (fn [signals]                                             ;; signals = [signal signal ...]
    {:type      :query
     :query     query
     :variables signals}))


(defn single-input-pull-one-with-pattern
  "---------------------------
  SINGLE signal/input version
  ---------------------------
  re-posh helper for using re-frame input signal for
  pulling data from Datascript with given pattern

  USAGE:
  (re-posh/reg-sub
    ::hydrated-entity
    :<- [::some-eid]
    (single-input-pull-one-with-pattern p/essential-keys)"
  [pattern]
  (fn [signal]
    {:type    :pull
     :pattern pattern
     :id      signal}))


(defn single-input-pull-many-with-pattern
  "---------------------------
  SINGLE signal/input version
  ---------------------------
  re-posh helper for using re-frame input signal for
  pull-many from Datascript with given pattern

  USAGE:
  (re-posh/reg-sub
    ::hydrated-entities
    :<- [::coll-of-eids]
    (single-input-pull-many-with-pattern p/essential-keys)"
  [pattern]
  (fn [signal]
    {:type    :pull-many
     :pattern pattern
     :ids     signal}))


;;TODO: Verify functionality
(defn multi-input-pull-many-with-pattern
  "--------------------------
  MULTI signal/input version
  --------------------------
  re-posh helper for using multiple re-frame input signals for
  pull-many Datascript with given pattern using re-posh

  USAGE:
  (re-posh/reg-sub
    ::selected-order-selected-cell
    :<- [::some-eid]
    :<- [::some-other-eid]
    (multi-input-pull-many-with-pattern p/essential-keys)"
  [pattern]
  (fn [signals]
    {:type    :pull-may
     :pattern pattern
     :ids     signals}))