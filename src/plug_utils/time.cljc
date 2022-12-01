(ns plug-utils.time
  (:require [clojure.string :as str]))


;|-------------------------------------------------
;| EPOC

;;TODO: UTC vs Local time. What is returned in different situation? How does locale affect result?
(defn epoch-millis-now
  "Current time in epoch millis"
  []
  #?(:clj  (System/currentTimeMillis)
     :cljs (.now js/Date)))


(defn epoch-millis->inst
  "Convert from epoch with millis to inst"
  [^Long epoch-millis]
  #?(:clj  (java.util.Date. epoch-millis)
     :cljs (js/Date. epoch-millis)))


;|-------------------------------------------------
;| INSTANTS

(defn instant-now
  "Current time/instant (UTC)"
  []
  #?(:clj  (java.util.Date.)
     :cljs (js/Date.)))


(defn inst->str
  "Turn an inst into 'yyyy.MM.dd HH:mm:ss' string
  or 'yyyy.MM.dd HH:mm:ss.SSS' if :with-millis? true is passed as opts

  opts:
  - :millis true  -- To get millis precision
  - :timezone     -- (Java only) \"CET\", \"UTC\", \"GMT\", ... "
  [inst & {:keys [millis? timezone]}]
  (when inst
    #?(:clj  (let [pattern     (if millis? "yyyy.MM.dd HH:mm:ss.SSS" "yyyy.MM.dd HH:mm:ss") ;; Choose pattern
                   *formatted* (new java.text.SimpleDateFormat pattern)] ;; Creat formatter

               (when timezone                               ;; Which timezone to display the time for (inst is just millis since "epoch" without time zone info)
                 (.setTimeZone *formatted* (java.util.TimeZone/getTimeZone timezone)))

               (.format *formatted* inst))                  ;; Format inst with formatter
       :cljs (-> inst
                 (.getTimezoneOffset)                       ;; Timezone (TZ) diff in minutes
                 (* 60000)                                  ;; Convert diff To millis
                 (->> (- (.getTime inst)))                  ;; Inst in epoch millis minus TZ offset
                 (js/Date.)                                 ;; Create new date object from the millis
                 (.toISOString)                             ;; Convert to ISO string
                 (.slice 0 -1)                              ;; Remove trailing "Z"
                 (cond-> (not millis?) (.slice 0 -4))       ;; Remove millis if not desired
                 (.replaceAll "-" ".")                      ;; Have '.' separator in date part
                 (.replace "T" " ")))))                     ;; Cleanup


;|-------------------------------------------------
;| DIRECTLY AS STRING

;;TODO: Rewrite to be alias for time-now-local-str-with-millis (after behaviour has been verified=)
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


(defn time-now-local-str-with-millis
  "Current time in format 'yyyy.MM.dd HH.mm.ss.SSS'
  Local time including milli seconds"
  []
  (-> (instant-now)
      (inst->str :with-millis? true)))


(defn time-now-local-str-without-millis
  "Current time in format 'yyyy.MM.dd HH.mm.ss'
  Local time without milli seconds"
  []
  (-> (instant-now)
      (inst->str :with-millis? false)))


;|-------------------------------------------------
;| CONVERSIONS

(defn hours->milliseconds [n]
  (* n 3.6e6))


;|-------------------------------------------------
;| RESOURCES

;; https://stackoverflow.com/questions/14757553/how-to-convert-a-clojure-java-date-to-simpler-form
;; https://docs.oracle.com/javase/tutorial/datetime/iso/index.html