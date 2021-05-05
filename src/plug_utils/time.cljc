(ns plug-utils.time)


(defn time-now-local-str
  "Current local time as string
  Ex: \"2021-02-17 08:07:35.173\""
  []
  {:post [(re-matches #"\d\d\d\d-\d\d-\d\d \d\d:\d\d:\d\d\.\d\d\d" %)]}
  #?(:clj  (-> (java.time.LocalDateTime/now)
               (str)                                        ;; To string
               (str/replace "T" " ")                        ;; Cleanup
               (subs 0 23))                                 ;; Reduce millis precision to match JS
     :cljs (-> (js/Date.)                                   ;; New date object
               (.getTimezoneOffset)                         ;; Timezone diff in minutes
               (* 60000)                                    ;; To millis
               (->> (- (js/Date.now)))                      ;; Epoch millis minus TZ offset
               (js/Date.)                                   ;; Create new date object from the millis
               (.toISOString)                               ;; Convert to ISO string
               (.slice 0 -1)                                ;; Remove trailing "Z"
               (.replace "T" " "))))                        ;; Cleanup


(defn hours->milliseconds [n]
  (* n 3.6e6))